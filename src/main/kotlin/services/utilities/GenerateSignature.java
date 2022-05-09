package services.utilities;

import org.apache.tomcat.util.codec.binary.Base64;

import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class GenerateSignature {
    private String signature;

    public GenerateSignature(String requestURI, String clientId, String requestTime,
                                    String privateKey, String requestBody){
        String content = String.format("POST %s\n%s.%s.%s", requestURI, clientId, requestTime,
                requestBody);

        try {
            java.security.Signature signature = java.security.Signature
                    .getInstance("SHA256withRSA");

            PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes("UTF-8"))));

            signature.initSign(priKey);
            signature.update(content.getBytes("UTF-8"));

            byte[] signed = signature.sign();

            this.signature = URLEncoder.encode(new String(Base64.encodeBase64(signed), "UTF-8"), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getSignature() {
        return signature;
    }
}
