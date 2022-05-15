package org.feup.apm.lunchlist4.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.feup.apm.lunchlist4.R
import org.feup.apm.lunchlist4.crypto.generateKeyPair
import org.feup.apm.lunchlist4.crypto.getKeyPair
import org.feup.apm.lunchlist4.httpRequests.registerUser


const val ID_EXTRA = "org.feup.cpm.acme.customer"
const val REMOTE_ADDRESS = "192.168.1.85:8080"
var currentId: Long = -1L

class MainActivity : AppCompatActivity() {
  private val regName by lazy { findViewById<EditText>(R.id.reg_name) }
  private val regAddress by lazy { findViewById<EditText>(R.id.reg_address) }
  private val regNIF by lazy { findViewById<EditText>(R.id.reg_nif) }
  private val regCardType by lazy { findViewById<EditText>(R.id.reg_card_type)}
  private val regCardNumber by lazy { findViewById<EditText>(R.id.reg_card_number)}
  private val regCardDate by lazy { findViewById<EditText>(R.id.reg_card_expiry_date)}


  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val registerButton = findViewById<Button>(R.id.button_register)

    val pair = getKeyPair()
    if (pair.first != null || pair.second != null){
        startDashboard()
    }
    setContentView(R.layout.activity_register)
    registerButton?.setOnClickListener{
      val keyPair = generateKeyPair()
      println(regName.text)
      Thread {
          registerUser(this@MainActivity,
              regName.text.toString(),
              regAddress.text.toString(),
              regNIF.text.toString().toLong(),
              regCardType.text.toString(),
              regCardNumber.text.toString().toLong(),
              regCardDate.text.toString(),
              keyPair.second
          )
       }.start()
    }
  }

    private fun startDashboard(){
        val intent = Intent(this, DashboardActivity::class.java).apply {

        }
        startActivity(intent)
        finish()
    }
}
