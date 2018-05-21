package com.wallet.crypto.trustapp.entity

import com.wallet.crypto.trustapp.util.ExchangeCalculator
import java.math.BigDecimal
import java.math.BigInteger

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 08/03/2018 16:58
 * @description
 */
class WalletDisplay : Comparable<Any> {

    var name: String? = null
    var publicKey: String? = null
        get() = field!!.toLowerCase()
    var balanceNative: BigInteger? = null
        private set
    var type: Byte = 0

    val balance: Double
        get() =
            BigDecimal(balanceNative).divide(ExchangeCalculator.ONE_ETHER, 8, BigDecimal.ROUND_UP).toDouble()

    constructor(name: String, publicKey: String, balance: BigInteger, type: Byte) {
        this.name = name
        this.publicKey = publicKey
        this.balanceNative = balance
        this.type = type
    }

    constructor(name: String, publicKey: String) {
        this.name = name
        this.publicKey = publicKey
        this.balanceNative = null
        this.type = CONTACT
    }

    fun setBalance(balance: BigInteger) {
        this.balanceNative = balance
    }

    override operator fun compareTo(o: Any): Int {
        return name!!.compareTo((o as WalletDisplay).name!!)
    }

    companion object {

        val NORMAL: Byte = 0
        val WATCH_ONLY: Byte = 1
        val CONTACT: Byte = 2
    }
}