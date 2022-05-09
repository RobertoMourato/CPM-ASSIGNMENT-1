package services.utilities;

import org.jetbrains.annotations.NotNull;

import java.security.*;
import java.util.Base64;

public class GenerateKeys {
    private PublicKey publicKey;
    private String publicKeyString;
    private PrivateKey privateKey;
    private String privateKeyString;

    public GenerateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(512);
        KeyPair keyPair = generator.genKeyPair();

        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();

        this.publicKeyString = Base64.getEncoder().encodeToString(this.publicKey.getEncoded());
        this.privateKeyString = Base64.getEncoder().encodeToString(this.privateKey.getEncoded());
    }

    @Override
    public String toString() {
        return this.publicKeyString + '\n' + this.privateKeyString;
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
