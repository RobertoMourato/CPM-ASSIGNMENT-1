package org.feup.gp4.acmeshopping.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.feup.gp4.acmeshopping.R
import org.feup.gp4.acmeshopping.addBackButton
import org.feup.gp4.acmeshopping.entities.SQLiteDbHelper
import org.feup.gp4.acmeshopping.httpRequests.Product

class ProductActivity : AppCompatActivity() {
    //    Buttons
    private val btnQtyIncrease by lazy { findViewById<Button>(R.id.productQtyIncrease) }
    private val btnQtyDecrease by lazy { findViewById<Button>(R.id.productQtyDecrease) }
    private val btnAddToCart by lazy { findViewById<Button>(R.id.productAddToCart) }
    private val btnCancel by lazy { findViewById<Button>(R.id.productCancel) }

    //    TextView
    private val productID by lazy { findViewById<TextView>(R.id.productID) }
    private val productModel by lazy { findViewById<TextView>(R.id.productModel) }
    private val productPrice by lazy { findViewById<TextView>(R.id.productPrice) }
    private val productManufacturer by lazy { findViewById<TextView>(R.id.productManufacturer) }
    private val productDescription by lazy { findViewById<TextView>(R.id.productDescription) }
    private val productQty by lazy { findViewById<TextView>(R.id.productQtyInv) }
    private val product by lazy { intent.getSerializableExtra(R.string.product.toString()) as Product }
    private val productQuantityToAdd by lazy { findViewById<TextView>(R.id.productQty) }

    private val table by lazy { findViewById<TableLayout>(R.id.productTable) }

    private val dbHelper by lazy { SQLiteDbHelper(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product_cart)

        btnQtyIncrease.visibility = GONE
        btnQtyDecrease.visibility = GONE
        btnAddToCart.visibility = GONE
        btnCancel.visibility = GONE
        productQuantityToAdd.visibility = GONE
        (productQty.parent as TableRow).visibility = VISIBLE

        productModel.text = product.model
        productPrice.text = "%.2f".format(product.price)
        productManufacturer.text = product.manufacturer
        productDescription.text = product.description
        productQty.text = product.quantity.toString()
        productID.text = product.id

        runOnUiThread {
            addBackButton(supportActionBar)
            addRemoveButton()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home) {
            finish() //do here something what you want on clicks on the Home/Up button
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addRemoveButton() {
        val button = layoutInflater.inflate(R.layout.deletebutton, null)
        button.setOnClickListener {
            val product = dbHelper.delete(product)
            finish()
        }

        table.addView(button)
    }

}