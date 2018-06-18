package com.wallet.crypto.util

import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 08/03/2018 14:21
 * @description
 */
class ExchangeCalculator private constructor() {
    private val TAG = "ExchangeCalculator"

    private val lastUpdateTimestamp: Long = 0
    var rateForChartDisplay = 1.0
        private set
    private val formatterUsd = DecimalFormat("#,###,###.##")
    private val formatterCrypt = DecimalFormat("#,###,###.####")
    private val formatterCryptExact = DecimalFormat("#,###,###.#######")


    companion object {

        val ONE_ETHER = BigDecimal("1000000000000000000")

        val instance: ExchangeCalculator by lazy { ExchangeCalculator() }

    }

}