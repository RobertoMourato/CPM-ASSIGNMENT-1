package org.feup.apm.lunchlist4.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.feup.apm.lunchlist4.R
import org.feup.apm.lunchlist4.entities.ShopCartDbHelper
import org.feup.apm.lunchlist4.httpRequests.Product

class CartActivity : AppCompatActivity() {
    // DB helper
    private val dbHelper by lazy { ShopCartDbHelper(this) }

    //    TextViews
    private val cartTableLayout by lazy { findViewById<TableLayout>(R.id.cartTableLayout) }
    private val totalPriceView by lazy { findViewById<TextView>(R.id.cartTotalPrice)}

    // Buttons
    private val payButton by lazy { findViewById<Button>(R.id.cartPayButton) }

    private var counter = 1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val products = dbHelper.getAll()
        var totalPrice = 0.0f
        if (products.isEmpty()) payButton.visibility = View.GONE
        for (product in products){
            addRow(product)
            totalPrice += product.price * product.quantity
        }
        totalPriceView.text = "%.2f".format(totalPrice) + "€"

        payButton.setOnClickListener { _ -> pay()}
    }

    private fun pay(){
        println("Payed")
        dbHelper.clearDB()
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