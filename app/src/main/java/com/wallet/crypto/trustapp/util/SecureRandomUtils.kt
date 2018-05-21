package com.wallet.crypto.trustapp.util

import org.web3j.crypto.LinuxSecureRandom

import java.security.SecureRandom

/**
 * Utility class for working with SecureRandom implementation.
 *
 *
 *
 * This is to address issues with SecureRandom on Android. For more information, refer to the
 * following [issue](https://github.com/web3j/web3j/issues/146).
 */
internal object SecureRandomUtils {

    private val SECURE_RANDOM: SecureRandom

    // Taken from BitcoinJ implementation
    // https://github.com/bitcoinj/bitcoinj/blob/3cb1f6c6c589f84fe6e1fb56bf26d94cccc85429/core/src/main/java/org/bitcoinj/core/Utils.java#L573
    private var isAndroid = -1

    val isAndroidRuntime: Boolean
        get() {
            if (isAndroid == -1) {
                val runtime = System.getProperty("java.runtime.name")
                isAndroid = if (runtime != null && runtime == "Android Runtime") 1 else 0
            }
            return isAndroid == 1
        }

    init {
        if (isAndroidRuntime) {
            LinuxSecureRandom()
        }
        SECURE_RANDOM = SecureRandom()
    }

    fun secureRandom(): SecureRandom {
        return SECURE_RANDOM
    }
}
