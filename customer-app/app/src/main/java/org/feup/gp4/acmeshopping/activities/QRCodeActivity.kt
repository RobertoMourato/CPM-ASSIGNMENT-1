package org.feup.gp4.acmeshopping.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import org.feup.gp4.acmeshopping.R
import org.feup.gp4.acmeshopping.addBackButton
import org.feup.gp4.acmeshopping.entities.SQLiteDbHelper
import org.feup.gp4.acmeshopping.httpRequests.PaymentInfo
import java.util.*

class QRCodeActivity : AppCompatActivity() {
    private val paymentInfo by lazy { intent.getSerializableExtra("payment") as PaymentInfo }
    private val viewDetailsBtn by lazy { findViewById<Button>(R.id.qrCodeViewDetailsButton) }
    private val dbHelper by lazy { SQLiteDbHelper(this) }
    private val SIZE = 500
    private val ISO_SET = "ISO-8859-1"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_layout)

        val image = findViewById<ImageView>(R.id.img_code)
        val token = dbHelper.getToken(paymentInfo.date + " " + paymentInfo.time)
        val content = token?.token ?: ""

        Thread {
            encodeAsBitmap(content).also { runOnUiThread { image.setImageBitmap(it) } }
        }.start()

        viewDetailsBtn.setOnClickListener { _ -> viewDetails() }

        addBackButton(supportActionBar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home) {
            finish() //do here something what you want on clicks on the Home/Up button
        }
        return super.onOptionsItemSelected(item)
    }

    private fun viewDetails() {
        val intent = Intent(this, OrderPageActivity::class.java).apply {
            putExtra(R.string.intent_qr_code_payment.toString(), paymentInfo)
        }
        startActivity(intent)
    }

    private fun encodeAsBitmap(str: String): Bitmap? {
        val result: BitMatrix
        val hints =
            Hashtable<EncodeHintType, String>().apply { put(EncodeHintType.CHARACTER_SET, ISO_SET) }
        var width = SIZE
        val format: BarcodeFormat = BarcodeFormat.QR_CODE

        try {
            result = MultiFormatWriter().encode(str, format, width, SIZE, hints)
        } catch (exc: Exception) {
            println(exc)
            return null
        }
        val w = result.width
        val h = result.height
        val colorDark = resources.getColor(R.color.black, null)
        val colorLight = resources.getColor(R.color.white, null)
        val pixels = IntArray(w * h)
        for (line in 0 until h) {
            val offset = line * w
            for (col in 0 until w)
                pixels[offset + col] = if (result.get(col, line)) colorDark else colorLight
        }
        return Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            .apply { setPixels(pixels, 0, w, 0, 0, w, h) }
    }
}