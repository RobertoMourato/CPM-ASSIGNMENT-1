package org.feup.apm.lunchlist4

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.feup.apm.lunchlist4.crypto.generateKeyPair
import org.feup.apm.lunchlist4.httpRequests.registerUser


const val ID_EXTRA = "org.feup.cpm.acme.customer"
const val REMOTE_ADDRESS = "10.0.2.2:8080"
var currentId: Long = -1L

class MainActivity : AppCompatActivity() {
  val regName by lazy { findViewById<EditText>(R.id.reg_name) }
  val regAddress by lazy { findViewById<EditText>(R.id.reg_address) }
  val regNIF by lazy { findViewById<EditText>(R.id.reg_nif) }
  val regCardType by lazy { findViewById<EditText>(R.id.reg_card_type)}
  val regCardNumber by lazy { findViewById<EditText>(R.id.reg_card_number)}
  val regCardDate by lazy { findViewById<EditText>(R.id.reg_card_expiry_date)}


  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)
    val registerButton = findViewById<Button>(R.id.button_register);
    registerButton.setOnClickListener{
      val keyPair = generateKeyPair()
      println(regName.text)
      Thread {
          registerUser(regName.text.toString(),
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

}
