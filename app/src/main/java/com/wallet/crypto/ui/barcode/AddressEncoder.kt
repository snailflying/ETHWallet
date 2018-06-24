package com.wallet.crypto.ui.barcode

import java.io.IOException
import java.math.BigInteger

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 08/03/2018 17:54
 * @description
 */
class AddressEncoder(var address: String?) {
    var gas: String? = null
    var amount: String? = null
    var data: String? = null
    var type: Byte = 0

    constructor(address: String, amount: String) : this(address) {
        this.amount = amount
    }

    companion object {

        @Throws(IOException::class)
        fun decode(s: String): AddressEncoder {
            return if (s.startsWith("ethereum:", false))
                decodeERC(s)
            else if (s.startsWith("iban:XE", false))
                decodeICAP(s)
            else
                decodeLegacyMercury(s)
        }

        @Throws(IOException::class)
        private fun decodeERC(s: String): AddressEncoder {
            if (!s.startsWith("ethereum:", false))
                throw IOException("Invalid data format, see ERC-67 https://github.com/ethereum/EIPs/issues/67")
            val re = AddressEncoder(s.substring(9, 51))
            if (s.length == 51) return re
            val parsed = s.substring(51).split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (entry in parsed) {
                val entryArr = entry.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (entryArr.size != 2) continue
                if (entryArr[0].equals("value", ignoreCase = true)) re.amount = entryArr[1]
                if (entryArr[0].equals("gas", ignoreCase = true)) re.gas = entryArr[1]
                if (entryArr[0].equals("data", ignoreCase = true)) re.data = entryArr[1]
            }
            return re
        }

        fun encodeERC(a: AddressEncoder): String {
            var re = "ethereum:" + a.address!!
            if (a.amount != null) re += "?value=" + a.amount!!
            if (a.gas != null) re += "?gas=" + a.gas!!
            if (a.data != null) re += "?data=" + a.data!!
            return re
        }

        @Throws(IOException::class)
        private fun decodeICAP(s: String): AddressEncoder {
            if (!s.startsWith("iban:XE", false))
                throw IOException("Invalid data format, see ICAP https://github.com/ethereum/wiki/wiki/ICAP:-Inter-exchange-Client-Address-Protocol")
            // TODO: verify checksum and length
            val temp = s.substring(9)
            val index = if (temp.indexOf("?") > 0) temp.indexOf("?") else temp.length
            var address = BigInteger(temp.substring(0, index), 36).toString(16)
            while (address.length < 40)
                address = "0$address"
            val re = AddressEncoder("0x$address")
            val parsed = s.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (entry in parsed) {
                val entryArr = entry.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (entryArr.size != 2) continue
                if (entryArr[0].equals("amount", ignoreCase = true)) re.amount = entryArr[1]
                if (entryArr[0].equals("gas", ignoreCase = true)) re.gas = entryArr[1]
                if (entryArr[0].equals("data", ignoreCase = true)) re.data = entryArr[1]
            }
            return re
        }

        @Throws(IOException::class)
        private fun decodeLegacyMercury(s: String): AddressEncoder {
            if (!s.startsWith("iban:", false)) return AddressEncoder(s)
            var temp = s.substring(5)
            var amount: String? = null
            if (temp.indexOf("?") > 0) {
                if (temp.indexOf("amount=") > 0 && temp.indexOf("amount=") < temp.length)
                    amount = temp.substring(temp.indexOf("amount=") + 7)
                temp = temp.substring(0, temp.indexOf("?"))
            }
            val re = AddressEncoder(temp)
            re.amount = amount
            return re
        }
    }
}