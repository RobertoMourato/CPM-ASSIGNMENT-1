package org.feup.apm.lunchlist4.httpRequests

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.feup.apm.lunchlist4.activities.REMOTE_ADDRESS
import org.json.JSONObject
import org.json.JSONTokener
import java.net.HttpURLConnection
import java.net.URL


data class Product(
    @SerializedName("id") var id: String?,
    @SerializedName("model") val model: String,
    @SerializedName("make") val manufacturer: String,
    @SerializedName("price") val price: Float,
    @SerializedName("characteristic") val description: String,
    @SerializedName("quantity")  val quantity: Int = 0,
)

fun getProductByID(productID: String): Product? {
    val url: URL
    var urlConnection: HttpURLConnection? = null
    var product: Product? = null
    try {
        url = URL("http://$REMOTE_ADDRESS/products/$productID")
        println("GET " + url.toExternalForm())
        urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.doInput = true
        urlConnection.setRequestProperty("Content-Type", "application/json")
        urlConnection.useCaches = false
        val responseCode = urlConnection.responseCode
        if (responseCode == 200) {
            val responseJson = JSONObject(JSONTokener(readStream(urlConnection.inputStream)))
            product = Gson().fromJson(responseJson.toString(), Product::class.java)
            println("Finish json")
        }


    } catch (e: java.lang.Exception) {
        println(e)
    } finally {
        urlConnection?.disconnect()
    }

    return product
}
