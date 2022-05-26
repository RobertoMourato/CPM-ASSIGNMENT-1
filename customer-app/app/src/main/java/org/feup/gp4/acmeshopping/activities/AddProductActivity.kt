package org.feup.gp4.acmeshopping.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.feup.gp4.acmeshopping.R
import org.feup.gp4.acmeshopping.entities.SQLiteDbHelper
import org.feup.gp4.acmeshopping.httpRequests.Product
import org.feup.gp4.acmeshopping.httpRequests.getProductByID
import java.lang.Integer.max

class AddProductActivity : AppCompatActivity() {
    // DB helper
    private val dbHelper by lazy { SQLiteDbHelper(this) }

    //    Buttons
    private val btnQtyIncrease by lazy { findViewById<Button>(R.id.productQtyIncrease) }
    private val btnQtyDecrease by lazy { findViewById<Button>(R.id.productQtyDecrease) }
    private val btnAddToCart by lazy { findViewById<Button>(R.id.productAddToCart) }
    private val btnCancel by lazy { findViewById<Button>(R.id.productCancel) }

    //  TextView
    private val productQuantityToAdd by lazy { findViewById<TextView>(R.id.productQty) }
    private val productID by lazy { findViewById<TextView>(R.id.productID) }
    private val productModel by lazy { findViewById<TextView>(R.id.productModel) }
    private val productPrice by lazy { findViewById<TextView>(R.id.productPrice) }
    private val productManufacturer by lazy { findViewById<TextView>(R.id.productManufacturer) }
    private val productDescription by lazy { findViewById<TextView>(R.id.productDescription) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product_cart)
        val barCode =
            intent.extras?.getString(R.string.product_intent_barcode.toString()).toString()
        productID.text = barCode
        retrieveInformation()

        btnQtyIncrease.setOnClickListener { _ -> changeQty(1) }
        btnQtyDecrease.setOnClickListener { _ -> changeQty(-1) }
        btnCancel.setOnClickListener { _ -> finish() }
        btnAddToCart.setOnClickListener { _ -> addProduct() }

    }

    @SuppressLint("SetTextI18n")
    fun retrieveInformation() {
        Thread {

            val product = dbHelper.getProductById(productID.text.toString())
                ?: getProductByID(productID.text.toString())
            this.runOnUiThread {
                if (product != null) {
                    productModel.text = product.model
                    productPrice.text = "%.2f".format(product.price)
                    productManufacturer.text = product.manufacturer
                    productDescription.text = product.description
                } else finish()
            }
        }.start()
    }

    private fun changeQty(delta: Int) {
        val result = (productQuantityToAdd.text.toString().toInt() + delta)
        productQuantityToAdd.text = (max(result, 0)).toString()

    }

    private fun addProduct() {
        val success = dbHelper.insert(
            Product(
                productID.text as String,
                productModel.text as String,
                productManufacturer.text as String,
                productPrice.text.toString().toFloat(),
                productDescription.text as String,
                productQuantityToAdd.text.toString().toInt()
            )
        )
        if (success.toInt() == -1) println("Product ${productID.text} updated quantity on database")
        else println("Product ${productID.text} added to the database")
        this.runOnUiThread { finish() }
    }

    private fun printAllProducts() {
        for (product in dbHelper.getAll()) {
            println(product.toString())
        }
    }


}
