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
import javax.crypto.Cipher


fun generateKeyPair(): Pair<PrivateKey, PublicKey> {
    val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
    val KEY_ALIAS = R.string.keypair_alias.toString()
    val generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ks.provider)

    val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(KEY_ALIAS,
        KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY).run {
        setDigests(KeyProperties.DIGEST_SHA256)                         //Set of digests algorithms with which the key can be used
        setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1) //Set of padding schemes with which the key can be used when signing/verifying
        setKeySize(512)
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
    return Base64.getEncoder().encodeToString(key.encoded)
}

@RequiresApi(Build.VERSION_CODES.O)
fun decrypt(body: String): String {
    val kp = getKeyPair()
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    cipher.init(Cipher.DECRYPT_MODE, kp.first)
    return String(cipher.doFinal(Base64.getDecoder().decode(body)))
}
