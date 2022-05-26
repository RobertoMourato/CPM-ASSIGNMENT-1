package services.utilities

//import org.apache.tomcat.util.codec.binary.Base64
import java.lang.Exception
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.net.URLDecoder
import java.lang.RuntimeException
import java.security.KeyFactory
import java.security.Signature
import java.util.*

class VerifySignature(
    requestURI: String?,
    clientId: String?,
    reponseTime: String?,
    publicKey: String,
    responseBody: String?,
    signatureToBeVerified: String?
) {
    var isValidSignature = false

    init {
        val content = String.format(
            "POST %s\n%s.%s.%s", requestURI, clientId, reponseTime,
            responseBody
        )
        try {
            val signature = Signature
                .getInstance("SHA256withRSA")
            val pubKey = KeyFactory.getInstance("RSA")
                .generatePublic(
                    //               F     new X509EncodedKeySpec(Base64.decodeBase64(publicKey.getBytes("UTF-8"))));
                    X509EncodedKeySpec(Base64.getDecoder().decode(publicKey.toByteArray(charset("UTF-8"))))
                )
            signature.initVerify(pubKey)
            signature.update(content.toByteArray(charset("UTF-8")))
            isValidSignature = signature.verify(
//                Base64.decodeBase64(
//                    URLDecoder.decode(
//                        signatureToBeVerified,
//                        "UTF-8"
//                    ).toByteArray(charset("UTF-8"))
                Base64.getDecoder().decode(URLDecoder.decode(signatureToBeVerified, charset("UTF-8")))
            )
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}