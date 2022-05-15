package org.feup.apm.lunchlist4.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.feup.apm.lunchlist4.R
import org.feup.apm.lunchlist4.httpRequests.getProductByID
import java.lang.Integer.max

class AddProductActivity : AppCompatActivity() {
//    Buttons
    private val btnQtyIncrease by lazy { findViewById<Button>(R.id.productQtyIncrease) }
    private val btnQtyDecrease by lazy { findViewById<Button>(R.id.productQtyDecrease) }
    private val btnAddToCart by lazy { findViewById<Button>(R.id.productAddToCart) }
    private val btnCancel by lazy { findViewById<Button>(R.id.productCancel) }
//  TextView
    private val productQuantityToAdd by lazy { findViewById<TextView>(R.id.productQty)}
    private val productID by lazy { findViewById<TextView>(R.id.productID)}
    private val productModel by lazy { findViewById<TextView>(R.id.productModel)}
    private val productPrice by lazy { findViewById<TextView>(R.id.productPrice)}
    private val productManufacturer by lazy { findViewById<TextView>(R.id.productManufacturer )}
    private val productDescription by lazy { findViewById<TextView>(R.id.productDescription)}

    private lateinit var barCode : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product_cart)
        barCode = intent.extras?.getString(R.string.product_intent_barcode.toString()).toString()
        productID.text = barCode

        btnQtyIncrease.setOnClickListener { _ -> changeQty(1) }
        btnQtyDecrease.setOnClickListener { _ -> changeQty(-1) }
        btnCancel.setOnClickListener { _ -> finish() }

        retrieveInformation()
    }

    @SuppressLint("SetTextI18n")
    fun retrieveInformation(){
        Thread{
            val product = getProductByID(barCode)
            this.runOnUiThread{
                if(product != null){
                    productModel.text = product.model
                    productPrice.text = "%.2f".format(product.price)
                    productManufacturer.text = product.manufacturer
                    productDescription.text = product.description
                }
            }
        }.start()
    }

    private fun changeQty(delta: Int){
        val result = (productQuantityToAdd.text.toString().toInt() + delta)
        productQuantityToAdd.text = (max(result,0)).toString()

    }


}
