package org.feup.apm.http

import org.json.JSONObject
import org.json.JSONTokener
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.annotations.SerializedName
import com.google.gson.Gson


const val REMOTE_ADDRESS = "192.168.1.72:8080"

data class Receipt(
    val customer: Customer,
    val payment: Payment

) {
     override fun toString(): String {
        val sb = StringBuilder("Name: ").append(customer.name).append("\nAddress: ").append(customer.address)
            .append("\nNIF: ").append(customer.nif).append("\n \n")

        for (item in payment.items) {
            sb.append("    Model: ").append(item.model)
                .append("\n    Manufacturer: ").append(item.manufacturer)
                .append("\n    Price: ").append(item.price)
                .append("\n    Description: ").append(item.description)
                .append("\n    Quantity: ").append(item.quantity)
                .append("\n\n")
        }
        sb.append("Full price: ").append(payment.price)
            .append("\nDate: ").append(payment.date).append("\nTime: ").append(payment.time)
        return sb.toString()
    }
}

data class Customer(
    val name: String,
    val address: String,
    val nif: Int
)

data class Payment(
    val items: List<Item>,
    val price: Double,
    val date: String,
    val time: String
)

data class Item(
    @SerializedName("id") val id: String,
    @SerializedName("model") val model: String,
    @SerializedName("make") val manufacturer: String,
    @SerializedName("price") val price: Float,
    @SerializedName("characteristic") val description: String,
    @SerializedName("quantity") val quantity: Int
)



fun getReceiptByToken(token: String): Receipt? {
    //val url: URL
    var urlConnection: HttpURLConnection? = null
    var receipt: Receipt? = null
    try {
        val url = URL("http://$REMOTE_ADDRESS/payments/receipt")
        urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.doInput = true
        urlConnection.setRequestProperty("Content-Type", "application/json")
        urlConnection.setRequestProperty("Token", token)
        urlConnection.useCaches = false
        val responseCode = urlConnection.responseCode
        if (responseCode == 200) {
            val responseJson = JSONObject(JSONTokener(readStream(urlConnection.inputStream)))
            receipt = Gson().fromJson(responseJson.toString(), Receipt::class.java)
        }
    } catch (e: java.lang.Exception) {
        println(e)
    } finally {
        urlConnection?.disconnect()
    }
    return receipt
}
