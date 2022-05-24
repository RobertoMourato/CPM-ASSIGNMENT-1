package org.feup.apm.lunchlist4.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TableLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.feup.apm.lunchlist4.R
import org.feup.apm.lunchlist4.entities.Token
import org.feup.apm.lunchlist4.httpRequests.PaymentInfo
import org.feup.apm.lunchlist4.httpRequests.getAllPastOrders
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PastOrdersActivity: AppCompatActivity() {
    private val tableLayout by lazy { findViewById<TableLayout>(R.id.pastOrderTable)}
    private var counter = 1
    private var payments : List<PaymentInfo> = emptyList()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_orders)

        Thread{
            payments = getAllPastOrders(this@PastOrdersActivity)
            payments.forEach {
                addRow(it)
            }
        }.start()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun addRow(paymentInfo: PaymentInfo){
        val inflater = layoutInflater
        val rowView = inflater.inflate(R.layout.activity_past_orders_row, null)

        val dateTextView = rowView.findViewById<TextView>(R.id.pastOrderDate)
//        dateTextView?.text = token.requestDate
        dateTextView?.text =
            LocalDate.parse(paymentInfo.date + "T" + paymentInfo.time, DateTimeFormatter.ofPattern("dd/MM/yyyyTHH:mm:ss"))
                .toString()



        rowView.setOnClickListener{
            val intent = Intent(this, QRCodeActivity::class.java).apply {
                putExtra("payment", paymentInfo)
            }
            startActivity(intent)
            finish()
        }

        tableLayout.addView(rowView,counter)
        counter++
    }
}