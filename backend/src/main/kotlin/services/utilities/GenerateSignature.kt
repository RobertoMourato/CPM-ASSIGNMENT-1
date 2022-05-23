//package services.utilities
//
//import org.apache.tomcat.util.codec.binary.Base64
//import java.lang.Exception
//import java.security.PrivateKey
//import java.security.spec.PKCS8EncodedKeySpec
//import java.lang.RuntimeException
//import java.net.URLEncoder
//import java.security.KeyFactory
//import java.security.Signature
//
//class GenerateSignature(
//    requestURI: String?, clientId: String?, requestTime: String?,
//    privateKey: String, requestBody: String?
//) {
//    val signature: String? = null
//
//    init {
//        val content = String.format(
//            "POST %s\n%s.%s.%s", requestURI, clientId, requestTime,
//            requestBody
//        )
//        try {
//            val signature = Signature
//                .getInstance("SHA256withRSA")
//            val priKey = KeyFactory.getInstance("RSA").generatePrivate(
//                PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.toByteArray(charset("UTF-8"))))
//            )
//            signature.initSign(priKey)
//            signature.update(content.toByteArray(charset("UTF-8")))
//            val signed = signature.sign()
//            this.signature = URLEncoder.encode(String(Base64.encodeBase64(signed), "UTF-8"), "UTF-8")
//        } catch (e: Exception) {
//            throw RuntimeException(e)
//        }
//    }
//}