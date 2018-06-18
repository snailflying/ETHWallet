package com.wallet.crypto.entity

import java.io.Serializable

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 08/03/2018 16:34
 * @description
 */
data class Account(var address: String,
                   var dateAdded: Long
//                   var privateKey: String,
//                   var publicKey: String,
//                   var mnemonic: List<String>,
//                   var mnemonicPath: String,
//                   var keystore: String
) : Serializable {


}
