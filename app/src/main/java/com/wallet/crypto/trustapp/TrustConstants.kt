package com.wallet.crypto.trustapp

object TrustConstants {

    val IMPORT_REQUEST_CODE = 1001
    val EXPORT_REQUEST_CODE = 1002
    val SHARE_REQUEST_CODE = 1003

    val ETHEREUM_NETWORK_NAME = "Ethereum"
    val CLASSIC_NETWORK_NAME = "Ethereum Classic"
    val POA_NETWORK_NAME = "POA Network"
    val KOVAN_NETWORK_NAME = "Kovan (Test)"
    val ROPSTEN_NETWORK_NAME = "Ropsten (Test)"
    val RINKEBY_NETWORK_NAME = "Rinkeby (Test)"

    val ETHEREUM_TIKER = "ethereum"
    val POA_TIKER = "poa"

    val USD_SYMBOL = "$"
    val ETH_SYMBOL = "ETH"
    val POA_SYMBOL = "POA"
    val ETC_SYMBOL = "ETC"

    val GWEI_UNIT = "Gwei"

    val EXTRA_ADDRESS = "ADDRESS"
    val EXTRA_CONTRACT_ADDRESS = "CONTRACT_ADDRESS"
    val EXTRA_DECIMALS = "DECIMALS"
    val EXTRA_SYMBOL = "SYMBOL"
    val EXTRA_SENDING_TOKENS = "SENDING_TOKENS"
    val EXTRA_TO_ADDRESS = "TO_ADDRESS"
    val EXTRA_AMOUNT = "AMOUNT"
    val EXTRA_GAS_PRICE = "GAS_PRICE"
    val EXTRA_GAS_LIMIT = "GAS_LIMIT"

    val COINBASE_WIDGET_CODE = "88d6141a-ff60-536c-841c-8f830adaacfd"
    val SHAPESHIFT_KEY = "c4097b033e02163da6114fbbc1bf15155e759ddfd8352c88c55e7fef162e901a800e7eaecf836062a0c075b2b881054e0b9aa2324be7bc3694578493faf59af4"
    val CHANGELLY_REF_ID = "968d4f0f0bf9"
    val DONATION_ADDRESS = "0x9f8284ce2cf0c8ce10685f537b1fff418104a317"

    val DEFAULT_GAS_PRICE = "21000000000"
    val DEFAULT_GAS_LIMIT = "90000"
    val DEFAULT_GAS_LIMIT_FOR_TOKENS = "144000"
    val GAS_LIMIT_MIN = 21000L
    val GAS_LIMIT_MAX = 300000L
    val GAS_PRICE_MIN = 1000000000L
    val NETWORK_FEE_MAX = 20000000000000000L
    val ETHER_DECIMALS = 18

    const val DB_NAME = "db_turst_app"

    interface ErrorCode {
        companion object {

            val UNKNOWN = 1
            val CANT_GET_STORE_PASSWORD = 2
        }
    }

    interface Key {
        companion object {
            val WALLET = "wallet"
            val TRANSACTION = "transaction"
            val SHOULD_SHOW_SECURITY_WARNING = "should_show_security_warning"
        }
    }
}
