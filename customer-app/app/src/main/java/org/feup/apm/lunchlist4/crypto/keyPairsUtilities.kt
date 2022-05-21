package org.feup.apm.lunchlist4.crypto

import android.app.Activity
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import org.feup.apm.lunchlist4.R
import java.security.*
import java.security.spec.X509EncodedKeySpec
import java.util.*


fun generateKeyPair(): Pair<PrivateKey, PublicKey> {
    val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
    val KEY_ALIAS = R.string.keypair_alias.toString()
    val generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ks.provider)

    val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(KEY_ALIAS,
        KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY).run {
//        setCertificateSerialNumber(BigInteger.valueOf(777))       //Serial number used for the self-signed certificate of the generated key pair, default is 1
//        setCertificateSubject(X500Principal("CN=$KEY_ALIAS"))     //Subject used for the self-signed certificate of the generated key pair, default is CN=fake
        setDigests(KeyProperties.DIGEST_SHA256)                         //Set of digests algorithms with which the key can be used
        setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1) //Set of padding schemes with which the key can be used when signing/verifying
//        setUserAuthenticationRequired(true)                             //Sets whether this key is authorized to be used only if the user has been authenticated, default false
        build()
    }
    generator.initialize(parameterSpec)
    val keyPair = generator.genKeyPair()


    return Pair(keyPair.private, keyPair.public);
}


fun getKeyPair(): Pair<PrivateKey?, PublicKey?> {
    val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    val privateKey = ks.getKey(R.string.keypair_alias.toString(),null) ?: return Pair(null, null)

    if(privateKey is PrivateKey){
        val cert = ks.getCertificate(R.string.keypair_alias.toString())

        val publicKey = cert.publicKey

        return Pair(privateKey,publicKey)
    }

    return Pair(null, null)
}

@RequiresApi(Build.VERSION_CODES.O)
fun keyToB64(key: Key): String? {
    val pk = key as PublicKey
    pk.encoded
    return Base64.getEncoder().encodeToString(key.encoded)
}
