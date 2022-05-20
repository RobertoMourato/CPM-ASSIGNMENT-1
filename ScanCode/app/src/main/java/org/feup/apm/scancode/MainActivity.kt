package org.feup.apm.scancode

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.feup.apm.http.Receipt
import org.feup.apm.http.getReceiptByToken


private const val ACTION_SCAN = "com.google.zxing.client.android.SCAN"

class MainActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    findViewById<Button>(R.id.bt_scan_qr).setOnClickListener { _ -> scan(true) }
  }

  fun scan(qrcode: Boolean) {
    try {
      val intent = Intent(ACTION_SCAN)
      intent.putExtra("SCAN_MODE", if (qrcode) "QR_CODE_MODE" else "PRODUCT_MODE")
      startActivityForResult(intent, 0)
    }
    catch (anfe: ActivityNotFoundException) {
      showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == 0) {
      if (resultCode == RESULT_OK) {
        val contents = data?.getStringExtra("SCAN_RESULT") ?: ""
        Thread {
          val receipt = getReceiptByToken(contents)
          startReceiptPrinting(receipt)
        }.start()
      }
    }
  }

  @SuppressLint("SetTextI18n")
  private fun startReceiptPrinting(receipt: Receipt?){
    val qrButton = findViewById<Button>(R.id.bt_scan_qr)
    qrButton.visibility = View.INVISIBLE
    val titleText = findViewById<TextView>(R.id.title_text)
    Handler(Looper.getMainLooper()).post(Runnable { titleText.text = "Receipt" })
    val receiptText = findViewById<TextView>(R.id.receipt)
    Handler(Looper.getMainLooper()).post(Runnable { receiptText.text = receipt.toString() })
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
}
