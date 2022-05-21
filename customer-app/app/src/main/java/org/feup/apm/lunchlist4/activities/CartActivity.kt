package org.feup.apm.lunchlist4.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.feup.apm.lunchlist4.R
import org.feup.apm.lunchlist4.entities.ShopCartDbHelper
import org.feup.apm.lunchlist4.httpRequests.Product
import org.feup.apm.lunchlist4.httpRequests.pay

class CartActivity : AppCompatActivity() {
    // DB helper
    private val dbHelper by lazy { ShopCartDbHelper(this) }

    //    TextViews
    private val cartTableLayout by lazy { findViewById<TableLayout>(R.id.cartTableLayout) }
    private val totalPriceView by lazy { findViewById<TextView>(R.id.cartTotalPrice)}

    // Buttons
    private val payButton by lazy { findViewById<Button>(R.id.cartPayButton) }

    private var counter = 1

    private lateinit var  products : List<Product>

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        products = dbHelper.getAll()
        var totalPrice = 0.0f
        if (products.isEmpty()) payButton.visibility = View.GONE
        for (product in products){
            addRow(product)
            totalPrice += product.price * product.quantity
        }
        totalPriceView.text = "%.2f".format(totalPrice) + "€"

        payButton.setOnClickListener { _ -> _pay()}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun _pay(){
        Thread(){
            pay(products, this@CartActivity)
        }.start()
    }

    @SuppressLint("SetTextI18n")
    fun addRow(product:Product){
        val inflater = layoutInflater
        val rowView = inflater.inflate(R.layout.layout_cart_row, null)

        val modelTextView = rowView.findViewById<TextView>(R.id.cartModel)
        modelTextView?.text = product.model

        val qtyTextView = rowView.findViewById<TextView>(R.id.cartQty)
        qtyTextView?.text = product.quantity.toString()

        val priceTextView = rowView.findViewById<TextView>(R.id.cartPrice)
        priceTextView?.text = "%.2f".format(product.price) + "€"

        cartTableLayout.addView(rowView, counter)
        counter++
    }

}