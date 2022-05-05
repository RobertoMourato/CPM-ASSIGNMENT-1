package services.utilities;

import org.jetbrains.annotations.NotNull;

import java.security.*;
import java.util.Base64;

public class KeysToString {
    private PublicKey publicKey;
    private String publicKeyString;
    private PrivateKey privateKey;
    private String privateKeyString;

    public KeysToString(@NotNull KeyPair keyPair){
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();

        this.publicKeyString = Base64.getEncoder().encodeToString(this.publicKey.getEncoded());
        this.privateKeyString = Base64.getEncoder().encodeToString(this.privateKey.getEncoded());
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public String getPrivateKeyString() {
        return privateKeyString;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getPublicKeyString() {
        return publicKeyString;
    }
}
