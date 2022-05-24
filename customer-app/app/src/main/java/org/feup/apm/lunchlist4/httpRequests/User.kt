package org.feup.apm.lunchlist4.httpRequests

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.feup.apm.lunchlist4.activities.MainActivity
import org.feup.apm.lunchlist4.R
import org.feup.apm.lunchlist4.activities.REMOTE_ADDRESS
import org.feup.apm.lunchlist4.crypto.decrypt
import org.feup.apm.lunchlist4.crypto.getKeyPair
import org.feup.apm.lunchlist4.crypto.keyToB64
import org.feup.apm.lunchlist4.entities.Token
import org.json.JSONObject
import org.json.JSONTokener
import java.io.DataOutputStream
import java.io.OutputStreamWriter
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.PublicKey
import java.security.Signature
import java.time.LocalDateTime
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
fun registerUser(
    activity: MainActivity,
    name: String,
    address: String,
    nif: Long,
    cardType: String,
    cardNumber: Long,
    cardValidity: String,
    publicKey: PublicKey
) {
    val url: URL
    var urlConnection: HttpURLConnection? = null
    try {
        url = URL("http://$REMOTE_ADDRESS/customers")
        println("POST " + url.toExternalForm())
        urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.doOutput = true
        urlConnection.doInput = true
        urlConnection.requestMethod = "POST"
        urlConnection.setRequestProperty("Content-Type", "application/json")
        urlConnection.useCaches = false
        val outputStream = DataOutputStream(urlConnection.outputStream)

        val jsonObject = JSONObject()

        jsonObject.put("publicKey", keyToB64(publicKey))
        jsonObject.put("name", name)
        jsonObject.put("address", address)
        jsonObject.put("nif", nif)
        jsonObject.put("cardType", cardType)
        jsonObject.put("cardNumber", cardNumber)
        jsonObject.put("cardValidity", cardValidity)
        jsonObject.put("address", address)

        outputStream.writeBytes(jsonObject.toString())
        outputStream.flush()
        outputStream.close()

        val responseCode = urlConnection.responseCode
        if (responseCode == 200) {
            val responseJson = JSONObject(JSONTokener(readStream(urlConnection.inputStream)))
            setUUID(activity, responseJson.getString("uuid"))
        } else
            println("Code: $responseCode")

    } catch (e: java.lang.Exception) {
        println(e)
    } finally {
        urlConnection?.disconnect()
    }
}

fun getUUID(activity: Activity): String? {
    val sharedPref =
        activity.getSharedPreferences(R.string.uuid_alias.toString(), Context.MODE_PRIVATE)
    return sharedPref.getString(R.string.uuid_alias.toString(), "")
}

fun setUUID(activity: Activity, uuid: String) {
    val sharedPref =
        activity.getSharedPreferences(R.string.uuid_alias.toString(), Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString(R.string.uuid_alias.toString(), uuid)
        apply()
    }
}

fun getSignature(body: String): ByteArray? {
    val signature = Signature.getInstance("SHA256withRSA")
    val keypair = getKeyPair()

    signature.initSign(keypair.first)
    signature.update(body.toByteArray())

    return signature.sign()
}

@RequiresApi(Build.VERSION_CODES.O)
fun pay(products: List<Product>, activity: Activity): Token? {
    data class Items(@SerializedName("items") val items: List<Product>)
    if (products.isEmpty()) return null
    products.forEach {
        it.id = null
    }

    val body = Gson().toJson(Items(products))
    val userID = getUUID(activity)
    val url: URL
    var urlConnection: HttpURLConnection? = null
    val uri = "/payments"
    var token: Token? = null
    try {
        url = URL(
            "http://$REMOTE_ADDRESS$uri"
        )
        println("POST " + url.toExternalForm())
        urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.doInput = true
        urlConnection.doOutput = true
        urlConnection.useCaches = false
        urlConnection.requestMethod = "POST"

        // Header
        val requestTime = LocalDateTime.now().toString()
        val signature = getSignature(
            String.format(
                "POST %s\n%s.%s.%s", uri, userID, requestTime, body.toString()
            )
        )
        urlConnection.addRequestProperty("Content-Type", "application/json")
        urlConnection.addRequestProperty(
            "Signature",
            URLEncoder.encode(Base64.getEncoder().encodeToString(signature), "UTF-8")
        )
        urlConnection.addRequestProperty("Client-Id", userID)
        urlConnection.addRequestProperty("Request-Time", requestTime)

        val outputStreamWriter = OutputStreamWriter(urlConnection.outputStream)
        outputStreamWriter.write(body)
        outputStreamWriter.flush()


        val responseCode = urlConnection.responseCode
        if (responseCode == 200) {
            val responseBody = JSONObject(JSONTokener(readStream(urlConnection.inputStream)))
            println("Success POST")
            token = Token(
                responseBody.getString("token"),
                requestTime
            )
        }
    } catch (e: java.lang.Exception) {
        println(e)
    } finally {
        urlConnection?.disconnect()
    }
    return token

}

data class PaymentInfo(
    @SerializedName("items") val products: List<Product>,
    @SerializedName("price") val price: Double = 0.0,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String
) : Serializable

@RequiresApi(Build.VERSION_CODES.O)
fun getAllPastOrders(
    activity: Activity
): List<PaymentInfo> {

    data class Response(
        @SerializedName("uuid") val userID: String,
        @SerializedName("payments") val payments: List<PaymentInfo>
    )

    var paymentInfo: List<PaymentInfo> = emptyList()
    val userID = getUUID(activity)
    val url: URL
    var urlConnection: HttpURLConnection? = null
    val uri = "/payments/past"
    try {
        url = URL(
            "http://$REMOTE_ADDRESS$uri"
        )
        println("GET " + url.toExternalForm())
        urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.doInput = true
        urlConnection.useCaches = false
        urlConnection.requestMethod = "GET"

        // Header
        val requestTime = LocalDateTime.now().toString()
        val signature = getSignature(
            "POST $uri\n$userID.$requestTime."
        )
        urlConnection.addRequestProperty("Content-Type", "application/json")
        urlConnection.addRequestProperty(
            "Signature",
            URLEncoder.encode(Base64.getEncoder().encodeToString(signature), "UTF-8")
        )
        urlConnection.addRequestProperty("Client-Id", userID)
        urlConnection.addRequestProperty("Request-Time", requestTime)


        val responseCode = urlConnection.responseCode
        if (responseCode == 200) {
            val responseBody =
                Gson().fromJson(readStream(urlConnection.inputStream), Response::class.java)
            setUUID(activity, decrypt(responseBody.userID))
            println("Success GET $uri")
            paymentInfo = responseBody.payments
        }
    } catch (e: java.lang.Exception) {
        println(e)
    } finally {
        urlConnection?.disconnect()
    }
    return paymentInfo
}