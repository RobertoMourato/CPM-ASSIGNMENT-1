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
import org.feup.apm.lunchlist4.crypto.getKeyPair
import org.feup.apm.lunchlist4.crypto.keyToB64
import org.json.JSONObject
import org.json.JSONTokener
import java.io.DataOutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.time.LocalDateTime
import java.util.*
import kotlin.math.sign


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
            val sharedPref = activity.getSharedPreferences(R.string.uuid_alias.toString(),Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString(R.string.uuid_alias.toString(), responseJson.getString("uuid"))
                apply()
            }
        } else
            println("Code: $responseCode")

    } catch (e: java.lang.Exception) {
        println(e)
    } finally {
        urlConnection?.disconnect()
    }
}

fun getUUID(activity: Activity): String? {
    val sharedPref = activity.getSharedPreferences(R.string.uuid_alias.toString(),Context.MODE_PRIVATE)
    return sharedPref.getString(R.string.uuid_alias.toString(), "")
}

fun getSignature(body: String): ByteArray? {
    val signature = Signature.getInstance("SHA256withRSA")
    val signatureVer = Signature.getInstance("SHA256withRSA")

    val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
    val keypair = getKeyPair()
//    val key = keyStore.getKey(R.string.keypair_alias.toString(), null)
    signature.initSign(keypair.first)
    signature.update(body.toByteArray())
    val signed = signature.sign()

    signature.initVerify(keypair.second)
    signature.update(body.toByteArray())
    val bl = signature.verify(signed)



    return signed
}

@RequiresApi(Build.VERSION_CODES.O)
fun pay(products: List<Product>, activity: Activity) {
    data class Items(@SerializedName("items") val items: List<Product>)
    if (products.isEmpty()) return

    val body = Gson().toJson(Items(products))
    val signature = getSignature(body.toString())
    val userID = getUUID(activity)
    val url: URL
    var urlConnection: HttpURLConnection? = null
    try {
        url = URL("http://$REMOTE_ADDRESS/payments")
        println("POST " + url.toExternalForm())
        urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.doInput = true
        urlConnection.doOutput = true
        urlConnection.useCaches = false
        urlConnection.requestMethod = "POST"

        // Header
        urlConnection.addRequestProperty("Content-Type", "application/json")
        urlConnection.addRequestProperty("Signature", Base64.getEncoder().encodeToString(signature))
        urlConnection.addRequestProperty("Client-Id", userID)
        urlConnection.addRequestProperty("Request-Time", LocalDateTime.now().toString())

        val outputStreamWriter = OutputStreamWriter(urlConnection.outputStream)
        outputStreamWriter.write(body)
        outputStreamWriter.flush()


        val responseCode = urlConnection.responseCode
        if(responseCode == 200){
            println("Success POST")
            println(JSONObject(JSONTokener(readStream(urlConnection.inputStream))).toString())
        }
    }catch (e: java.lang.Exception) {
        println(e)
    } finally {
        urlConnection?.disconnect()
    }

}