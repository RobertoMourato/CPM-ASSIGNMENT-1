package services.utilities;

import org.apache.tomcat.util.codec.binary.Base64;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class VerifySignature {
    private boolean validSignature;

    public VerifySignature(String requestURI, String clientId, String reponseTime,
                                   String publicKey, String responseBody,
                                   String signatureToBeVerified){
        String content = String.format("POST %s\n%s.%s.%s", requestURI, clientId, reponseTime,
                responseBody);

        try {
            Signature signature = Signature
                    .getInstance("SHA256withRSA");

            PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(
                    new X509EncodedKeySpec(Base64.decodeBase64(publicKey.getBytes("UTF-8"))));

            signature.initVerify(pubKey);
            signature.update(content.getBytes("UTF-8"));

            this.validSignature = signature.verify(Base64.decodeBase64(signatureToBeVerified.getBytes("UTF-8")));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidSignature() {
        return validSignature;
    }
}
