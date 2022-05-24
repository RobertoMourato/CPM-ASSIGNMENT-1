package org.feup.apm.lunchlist4.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class QRCodeActivity: AppCompatActivity() {
    private val paymentInfo by lazy { intent.getSerializableExtra("payment")}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

//    private val tv_value by lazy { findViewById<TextView>(R.id.tv_content) }
//    private var content = ""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_show_code)
//
//        val image = findViewById<ImageView>(R.id.img_code)
//        val type = intent.getIntExtra("type", 0)
//        val value = intent.getStringExtra("value") ?: ""
//
//        content = "Value: ${value}"
//        findViewById<TextView>(R.id.tv_title).text = if (type == 0) "Bar Code" else "QR Code"
//        tv_value.text = content
//
//        Thread {
//            encodeAsBitmap(type, value).also { runOnUiThread { image.setImageBitmap(it) } }
//        }.start()
//    }
//
//    private fun encodeAsBitmap(type: Int, str: String): Bitmap? {
//        val result: BitMatrix
//        val hints = Hashtable<EncodeHintType, String>().apply { put(EncodeHintType.CHARACTER_SET, ISO_SET) }
//        var width = SIZE
//        val format: BarcodeFormat
//
//        if (type == 0) {
//            width *= 2
//            format = BarcodeFormat.UPC_A
//        }
//        else
//            format = BarcodeFormat.QR_CODE
//
//        try {
//            result = MultiFormatWriter().encode(str, format, width, SIZE, hints)
//        }
//        catch (exc: Exception) {
//            content += "\n${exc.message}"
//            runOnUiThread { tv_value.text = content }
//            return null
//        }
//        val w = result.width
//        val h = result.height
//        val colorDark = resources.getColor(R.color.black, null)
//        val colorLight = resources.getColor(R.color.white, null)
//        val pixels = IntArray(w * h)
//        for (line in 0 until h) {
//            val offset = line * w
//            for (col in 0 until w)
//                pixels[offset + col] = if (result.get(col, line)) colorDark else colorLight
//        }
//        return Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888).apply { setPixels(pixels, 0, w, 0, 0, w, h) }
//    }
}