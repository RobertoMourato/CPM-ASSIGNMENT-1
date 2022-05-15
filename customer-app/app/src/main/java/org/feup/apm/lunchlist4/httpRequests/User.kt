package org.feup.apm.lunchlist4.httpRequests

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import org.feup.apm.lunchlist4.MainActivity
import org.feup.apm.lunchlist4.R
import org.feup.apm.lunchlist4.REMOTE_ADDRESS
import org.feup.apm.lunchlist4.crypto.keyToB64
import org.json.JSONObject
import org.json.JSONTokener
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.PublicKey


@RequiresApi(Build.VERSION_CODES.O)
fun registerUser(activity: MainActivity,
                 name: String,
                 address: String,
                 nif: Long,
                 cardType: String,
                 cardNumber: Long,
                 cardValidity: String,
                 publicKey: PublicKey) {
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
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            with(sharedPref.edit()){
                putString(R.string.uuid_alias.toString(), responseJson.getString("uuid"))
                apply()
            }
        }
        else
            println("Code: $responseCode")

    } catch (e: java.lang.Exception) {
        println(e)
    } finally {
        urlConnection?.disconnect()
    }
}