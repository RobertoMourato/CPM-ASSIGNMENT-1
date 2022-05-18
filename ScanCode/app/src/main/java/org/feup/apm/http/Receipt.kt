package org.feup.apm.http

import org.json.JSONObject
import org.json.JSONTokener
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.annotations.SerializedName
import com.google.gson.Gson


const val REMOTE_ADDRESS = "192.168.1.85:8080"

data class Receipt(
    @SerializedName("id") val id: String,
    @SerializedName("model") val model: String,
    @SerializedName("make") val manufacturer: String,
    @SerializedName("price") val price: Float,
    @SerializedName("characteristic") val description: String,
    val quantity: Int = 0,
)

fun getReceiptByToken(token: String): Receipt? {
    val url: URL
    var urlConnection: HttpURLConnection? = null
    var receipt: Receipt? = null
    try {
        url = URL("http://$REMOTE_ADDRESS/receipts/$token")
        println("GET " + url.toExternalForm())
        urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.doInput = true
        urlConnection.setRequestProperty("Content-Type", "application/json")
        urlConnection.useCaches = false
        val responseCode = urlConnection.responseCode
        if (responseCode == 200) {
            val responseJson = JSONObject(JSONTokener(readStream(urlConnection.inputStream)))
            receipt = Gson().fromJson(responseJson.toString(), Receipt::class.java)
            println("Finish json")
        }


    } catch (e: java.lang.Exception) {
        println(e)
    } finally {
        urlConnection?.disconnect()
    }

    return receipt
}
