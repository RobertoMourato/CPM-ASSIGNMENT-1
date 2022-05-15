package org.feup.apm.lunchlist4.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import org.feup.apm.lunchlist4.R

class DashboardActivity : AppCompatActivity(){
    private val btnShowCart by lazy { findViewById<Button>(R.id.dashShowCart) }
    private val btnShowPastOrders by lazy { findViewById<Button>(R.id.dashShowPastOrders) }
    private val btnScanBarCode by lazy { findViewById<Button>(R.id.dashScanBarCode) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        btnShowCart.setOnClickListener {
            println(btnShowCart.text)
        }
        btnShowPastOrders.setOnClickListener {
            println(btnShowPastOrders.text)
        }
        btnScanBarCode.setOnClickListener {
            println(btnScanBarCode.text)
        }
    }
}