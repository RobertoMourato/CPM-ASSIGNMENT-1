package org.feup.gp4.acmeshopping.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.TableLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.feup.gp4.acmeshopping.R
import org.feup.gp4.acmeshopping.addBackButton
import org.feup.gp4.acmeshopping.httpRequests.PaymentInfo
import org.feup.gp4.acmeshopping.httpRequests.getAllPastOrders
import java.util.*

class PastOrdersActivity : AppCompatActivity() {
    private val tableLayout by lazy { findViewById<TableLayout>(R.id.pastOrderTable) }
    private var counter = 1
    private var payments: List<PaymentInfo> = emptyList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_orders)

        Thread {
            payments = getAllPastOrders(this@PastOrdersActivity)

            runOnUiThread {
                payments.forEach {
                    addRow(it)
                    println(it)
                }
            }
        }.start()

        addBackButton(supportActionBar)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home) {
            finish() //do here something what you want on clicks on the Home/Up button
        }
        return super.onOptionsItemSelected(item)
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun addRow(paymentInfo: PaymentInfo) {
        val inflater = layoutInflater
        val rowView = inflater.inflate(R.layout.activity_past_orders_row, null)

        val dateTextView = rowView.findViewById<TextView>(R.id.pastOrderDate)
        dateTextView?.text = "${paymentInfo.date} ${paymentInfo.time}"

        rowView.setOnClickListener {
            val intent = Intent(this, QRCodeActivity::class.java).apply {
                putExtra("payment", paymentInfo)
            }
            startActivity(intent)
        }

        tableLayout.addView(rowView, counter)
        counter++
    }
}