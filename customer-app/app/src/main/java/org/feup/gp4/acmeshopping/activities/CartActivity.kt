package org.feup.gp4.acmeshopping.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.feup.gp4.acmeshopping.R
import org.feup.gp4.acmeshopping.addBackButton
import org.feup.gp4.acmeshopping.entities.SQLiteDbHelper
import org.feup.gp4.acmeshopping.httpRequests.PaymentInfo
import org.feup.gp4.acmeshopping.httpRequests.Product
import org.feup.gp4.acmeshopping.httpRequests.pay

class CartActivity : AppCompatActivity() {
    // DB helper
    private val dbHelper by lazy { SQLiteDbHelper(this) }

    //    TextViews
    private val cartTableLayout by lazy { findViewById<TableLayout>(R.id.cartTableLayout) }
    private val totalPriceView by lazy { findViewById<TextView>(R.id.cartTotalPrice) }
    private val cartHeader by lazy { findViewById<TableRow>(R.id.cartHeader) }
    private val totalPriceLabelView by lazy { findViewById<TextView>(R.id.cartTotalLabel) }

    // Buttons
    private val payButton by lazy { findViewById<Button>(R.id.cartPayButton) }

    private var counter = 1

    private lateinit var products: List<Product>


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        addProducts()

        payButton.setOnClickListener { _ -> _pay() }
        addBackButton(supportActionBar)
    }

    private fun addProducts(addRow: Boolean = true) {
        products = dbHelper.getAll()
        var totalPrice = 0.0f
        if (products.isEmpty()) onEmpty()

        for (product in products) {
            if (addRow)
                addRow(product)
            totalPrice += product.price * product.quantity
        }
        totalPriceView.text = "%.2f".format(totalPrice) + "€"
    }

    private fun onEmpty() {
        runOnUiThread {
            payButton.visibility = View.GONE
            totalPriceView.visibility = View.GONE
            cartHeader.visibility = View.GONE
            totalPriceLabelView.visibility = View.GONE
            val inflater = layoutInflater
            val textView = inflater.inflate(R.layout.empty_cart, null)
            cartTableLayout.addView(textView)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // Handle action bar item clicks here. The action bar will

        // automatically handle clicks on the Home/Up button, so long

        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.getItemId()
        if (id == android.R.id.home) {
            finish() //do here something what you want on clicks on the Home/Up button
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun _pay() {
        Thread() {
            val token = pay(products, this@CartActivity)
            if (token != null) {
                dbHelper.clearProducts()
                dbHelper.insert(token)
                runOnUiThread {
                    finish()
                }
            }
        }.start()
    }


    @SuppressLint("SetTextI18n")
    fun addRow(product: Product) {
        val inflater = layoutInflater
        val rowView = inflater.inflate(R.layout.layout_cart_row, null)

        val modelTextView = rowView.findViewById<TextView>(R.id.cartModel)
        modelTextView?.text = product.model

        val qtyTextView = rowView.findViewById<TextView>(R.id.cartQty)
        qtyTextView?.text = product.quantity.toString()

        val priceTextView = rowView.findViewById<TextView>(R.id.cartPrice)
        priceTextView?.text = "%.2f".format(product.price) + "€"

        val removeButton = rowView.findViewById<Button>(R.id.deleteProduct)
        removeButton.setOnClickListener {
            dbHelper.delete(product)
            (rowView.parent as ViewManager).removeView(rowView)
            addProducts(false)
            counter--
        }

        rowView.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java).apply {
                putExtra(R.string.product.toString(), product)
            }
            startActivity(intent)
        }

        cartTableLayout.addView(rowView, counter)
        counter++
    }

}