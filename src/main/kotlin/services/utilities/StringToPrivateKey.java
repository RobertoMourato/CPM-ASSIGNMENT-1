package services.utilities;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class StringToPrivateKey {
    private PrivateKey privateKey;

    public StringToPrivateKey(@NotNull String privateKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyString.getBytes("utf-8"));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        this.privateKey = fact.generatePrivate(keySpec);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
