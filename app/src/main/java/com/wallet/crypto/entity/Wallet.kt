package com.wallet.crypto.entity

import java.io.Serializable

class Wallet(address: String) : Serializable {
    val address: String = address
        get() = generateAddress(field)


    private fun generateAddress(address: String): String {
        return if (!address.startsWith("0x") && !address.startsWith("0X")) {
            "0x$address"
        } else address
    }


    fun sameAddress(address: String): Boolean {
        return this.address == address
    }

}
