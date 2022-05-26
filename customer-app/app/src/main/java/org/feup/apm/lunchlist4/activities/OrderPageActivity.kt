package org.feup.apm.lunchlist4.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.feup.apm.lunchlist4.R
import org.feup.apm.lunchlist4.addBackButton
import org.feup.apm.lunchlist4.httpRequests.PaymentInfo
import org.feup.apm.lunchlist4.httpRequests.Product

class OrderPageActivity : AppCompatActivity() {
    private val payBtn by lazy { findViewById<Button>(R.id.cartPayButton) }
    private val cartTableLayout by lazy { findViewById<TableLayout>(R.id.cartTableLayout) }
    private val paymentInfo by lazy { intent.getSerializableExtra(R.string.intent_qr_code_payment.toString()) as PaymentInfo }
    private var counter = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        payBtn.visibility = GONE
        runOnUiThread {
            paymentInfo.products.forEach {
                addRow(it)
            }
        }


        addBackButton(supportActionBar)
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

        rowView.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java).apply {
                putExtra(R.string.product.toString(), product)
            }
            startActivity(intent)
        }

        cartTableLayout.addView(rowView, counter)
        counter++
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
}