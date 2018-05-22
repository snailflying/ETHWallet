package com.wallet.crypto.trustapp.util

import android.util.Log
import com.google.gson.Gson
import com.wallet.crypto.trustapp.entity.Account
import com.wallet.crypto.trustapp.entity.StorableAccounts
import org.json.JSONException
import org.web3j.crypto.*
import org.web3j.protocol.ObjectMapperFactory
import org.web3j.utils.Numeric
import java.io.File
import java.io.IOException

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 08/03/2018 17:06
 * @description
 */
class WalletStorage private constructor() {

    private val walletStorageName = "sp_wallets"

    private var accountsDb: MutableList<Account> = mutableListOf()


    init {
        load()
    }

    @Synchronized
    private fun add(account: Account): Boolean {
        for (i in accountsDb.indices)
            if (accountsDb[i].address.equals(account.address, true)) return false
        accountsDb.add(account)
        save()
        return true
    }

    /**
     * 获取钱包list
     * @return MutableList<Account>?
     */
    @Synchronized
    fun getAccounts(): MutableList<Account> {
        return accountsDb
    }
//
//    @Throws(CipherException::class, IOException::class, InvalidAlgorithmParameterException::class, NoSuchAlgorithmException::class, NoSuchProviderException::class)
//    fun generateWalletFileWithKey(
//            password: String, privatekey: String, useFullScrypt: Boolean): Account {
//
//        val ecKeyPair = ECKeyPair.create(Hex.decode(privatekey))
//        return generateWalletFile(password, ecKeyPair, useFullScrypt)
//    }


    fun hasAddress(address: String): Boolean {
        for (account in accountsDb) {
            if (account.address.equals(getAddress(address), true)) {
                return true
            }
        }
        return false
    }

    /**
     * 通过密钥生成钱包
     * @param password String
     * @param ecKeyPair ECKeyPair
     * @param useFullScrypt Boolean
     * @return Account
     * @throws CipherException
     * @throws IOException
     */
    @Throws(CipherException::class, IOException::class)
    fun generateWalletFile(password: String, ecKeyPair: ECKeyPair = Keys.createEcKeyPair(), useFullScrypt: Boolean = true): Account {

        val walletFile: WalletFile
        if (useFullScrypt) {
//            walletFile = Wallet.createStandard(password, ecKeyPair)
            walletFile = Wallet.create(password, ecKeyPair, N, P)
        } else {
            walletFile = Wallet.createLight(password, ecKeyPair)
        }

        val address = walletFile.address
        Log.i("WalletStorage", "File.pathSeparator:" + File.separator)
        Log.i("WalletStorage", "wallet file path:" + FileUtils.WALLET_DIR)
        val destination = File(FileUtils.WALLET_DIR, address)

        objectMapper.writeValue(destination, walletFile)
//        WalletUtils.generateWalletFile(password, ecKeyPair, destination, useFullScrypt)

        val account = Account(address, System.currentTimeMillis())
        add(account)
        return account
    }

    /**
     * 通过keystore.json文件导入钱包
     *
     * @param keystore 原json文件
     * @param pwd      json文件密码
     * @return
     */
    fun importWalletByKeystore(keystore: String, pwd: String): Account? {
        var credentials: Credentials? = null
        try {
            var walletFile: WalletFile? = null
            walletFile = objectMapper.readValue(keystore, WalletFile::class.java)

            //            WalletFile walletFile = new Gson().fromJson(keystore, WalletFile.class);
            credentials = Credentials.create(Wallet.decrypt(pwd, walletFile!!))
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("ETHWalletUtils", e.toString())
        } catch (e: CipherException) {
            Log.e("ETHWalletUtils", e.toString())
            //            ToastUtils.showToast(R.string.load_wallet_by_official_wallet_keystore_input_tip);
            e.printStackTrace()
        }

        return if (credentials != null) {
            generateWalletFile(pwd, ecKeyPair = credentials.ecKeyPair)
        } else null
    }

    /**
     * 通过明文私钥导入钱包
     *
     * @param privateKey
     * @param pwd
     * @return
     */
    fun importWalletByPrivateKey(privateKey: String, pwd: String): Account? {
        val credentials: Credentials? = null
        val ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey))
        return generateWalletFile(pwd, ecKeyPair)
    }


    /**
     * 导出明文私钥
     *
     * @param walletId 钱包Id
     * @param pwd      钱包密码
     * @return
     */
    fun exportPrivateKey(pwd: String, walletAddress: String): String? {
        val credentials = getWalletCredentials(pwd, walletAddress)
        val keypair: ECKeyPair
        var privateKey: String? = null
        try {
            keypair = credentials.ecKeyPair
            privateKey = Numeric.toHexStringNoPrefixZeroPadded(keypair.privateKey, Keys.PRIVATE_KEY_LENGTH_IN_HEX)
        } catch (e: CipherException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return privateKey
    }

    /**
     * 导出keystore文件
     *
     * @param walletId
     * @param pwd
     * @return
     */
    fun exportKeystore(address: String): String? {
        var keystore: String? = null
        val walletFile: WalletFile
        try {
            walletFile = objectMapper.readValue<WalletFile>(File(FileUtils.WALLET_DIR, getAddress(address)), WalletFile::class.java)
            keystore = objectMapper.writeValueAsString(walletFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return keystore
    }

    /**
     * 获取 Credentials
     * @param password String
     * @param address String
     * @return Credentials
     * @throws IOException
     * @throws JSONException
     * @throws CipherException
     */
    @Throws(IOException::class, JSONException::class, CipherException::class)
    fun getWalletCredentials(password: String, address: String): Credentials {
        return WalletUtils.loadCredentials(password, File(FileUtils.WALLET_DIR, getAddress(address)))
    }

    /**
     * 删除钱包
     * @param address String
     */
    fun deleteWallet(address: String) {
        Log.e("aaron", "delete address:" + address)
        var position = -1
        for (i in accountsDb.indices) {
            if (accountsDb[i].address.equals(getAddress(address), true)) {
                position = i
                break
            }
        }
        if (position >= 0) {
            File(FileUtils.WALLET_DIR, getAddress(address)).delete()
            accountsDb.removeAt(position)
        }
        save()
    }

    /**
     * 修改钱包密码
     * @param address
     * @param oldPassword
     * @param newPassword
     * @return
     */
    fun modifyPassword(address: String, oldPassword: String, newPassword: String): Account {
        var credentials: Credentials? = null
        var keypair: ECKeyPair? = null
        try {
            credentials = getWalletCredentials(oldPassword, address)
            keypair = credentials.ecKeyPair
            val destinationDirectory = File(FileUtils.WALLET_DIR, address)
            WalletUtils.generateWalletFile(newPassword, keypair!!, destinationDirectory, true)
        } catch (e: CipherException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val account = Account(address, System.currentTimeMillis())
        add(account)
        return account
    }


    private fun getAddress(address: String): String {
        var walletShadowed = address
        if (walletShadowed.startsWith("0x"))
            walletShadowed = walletShadowed.substring(2, walletShadowed.length)
        return walletShadowed
    }

    @Synchronized
    fun save() {
        val gson = Gson()

        SPHelper.create().put(walletStorageName, gson.toJson(StorableAccounts(accountsDb)))

    }

    @Synchronized
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun load() {
        val wallet = SPHelper.create().get(walletStorageName)
        val gson = Gson()
        val storableWallets = gson.fromJson(wallet, StorableAccounts::class.java)
        if (storableWallets != null)
            accountsDb = storableWallets.account

    }

    companion object {
        private var instance: WalletStorage? = null
        private val objectMapper = ObjectMapperFactory.getObjectMapper()

        /**
         * 随机
         */
        private val secureRandom = SecureRandomUtils.secureRandom()
        /**
         * 通用的以太坊基于bip44协议的助记词路径 （imtoken jaxx Metamask myetherwallet）
         */
        var ETH_JAXX_TYPE = "m/44'/60'/0'/0/0"
        var ETH_LEDGER_TYPE = "m/44'/60'/0'/0"
        var ETH_CUSTOM_TYPE = "m/44'/60'/1'/0/0"

        /**
         * CPU/Memory cost parameter. Must be larger than 1, a power of 2 and less than 2^(128 * r / 8).
         */
        private val N = 1 shl 9
        /**
         * Parallelization parameter. Must be a positive integer less than or equal to Integer.MAX_VALUE / (128 * r * 8).
         */
        private val P = 1

        fun getInstance(): WalletStorage {
            if (instance == null) {
                instance = WalletStorage()
            }
            return instance!!
        }
    }

}