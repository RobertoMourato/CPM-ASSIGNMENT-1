package org.feup.apm.lunchlist4.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.feup.apm.lunchlist4.R



private const val ACTION_SCAN = "com.google.zxing.client.android.SCAN"

class DashboardActivity : AppCompatActivity(){
    private val btnShowCart by lazy { findViewById<Button>(R.id.dashShowCart) }
    private val btnShowPastOrders by lazy { findViewById<Button>(R.id.dashShowPastOrders) }
    private val btnScanBarCode by lazy { findViewById<Button>(R.id.dashScanBarCode) }
    private lateinit var qrCodeActivityLauncher : ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        qrCodeActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if (it.resultCode == Activity.RESULT_OK){
                println("SCAN_RESULT" + " " + it.data?.getStringExtra("SCAN_RESULT"))
                it.data?.getStringExtra("SCAN_RESULT")?.let { it1 -> callAddProduct(it1) }
            }
        }
        btnShowCart.setOnClickListener {
            this.startActivity(Intent(this,CartActivity::class.java))
        }
        btnShowPastOrders.setOnClickListener {
            println(btnShowPastOrders.text)
        }
        btnScanBarCode.setOnClickListener {
            scan(false)
        }
    }

    fun scan(qrcode: Boolean) {
        try {
            val intent = Intent(ACTION_SCAN)
            intent.putExtra("SCAN_MODE", if (qrcode) "QR_CODE_MODE" else "PRODUCT_MODE")
            qrCodeActivityLauncher.launch(intent)
        }
        catch (anfe: ActivityNotFoundException) {
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show()
        }
    }

    private fun showDialog(act: Activity, title: CharSequence, message: CharSequence, buttonYes: CharSequence, buttonNo: CharSequence): AlertDialog {
        val downloadDialog = AlertDialog.Builder(act)
        downloadDialog.setTitle(title)
        downloadDialog.setMessage(message)
        downloadDialog.setPositiveButton(buttonYes) { _, _ ->
            val uri = Uri.parse("market://search?q=pname:com.google.zxing.client.android")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            act.startActivity(intent)
        }
        downloadDialog.setNegativeButton(buttonNo, null)
        return downloadDialog.show()
    }

    private fun callAddProduct(productBarCode : String){
        val intent = Intent(this, AddProductActivity::class.java).putExtra(R.string.product_intent_barcode.toString(), productBarCode)
        startActivity(intent)
    }

}