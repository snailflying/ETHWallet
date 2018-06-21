package com.wallet.crypto.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.wallet.crypto.entity.Wallet
import com.wallet.crypto.util.WalletStorage
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.crypto.Wallet.create
import java.math.BigDecimal
import java.math.BigInteger

class GethKeystoreAccountService(private val keyStore: WalletStorage = WalletStorage.getInstance()) {

    fun createAccount(password: String): Single<Wallet> {
        return Single.fromCallable {
            Wallet(keyStore.generateWalletFile(password).address.toLowerCase())
        }.subscribeOn(Schedulers.io())
    }

    fun importKeystore(store: String, password: String, newPassword: String): Single<Wallet> {
        return Single.fromCallable {
            val account = keyStore
                    .importWalletByKeystore(store, password, newPassword)
            Wallet(account!!.address.toLowerCase())
        }
                .subscribeOn(Schedulers.io())
    }

    fun importPrivateKey(privateKey: String, newPassword: String): Single<Wallet> {
        return Single.fromCallable {
            val key = BigInteger(privateKey, PRIVATE_KEY_RADIX)
            val keypair = ECKeyPair.create(key)
            val walletFile = create(newPassword, keypair, N, P)
            ObjectMapper().writeValueAsString(walletFile)
        }.compose { upstream -> importKeystore(upstream.blockingGet(), newPassword, newPassword) }
    }

    fun exportAccount(wallet: Wallet, password: String): Single<String> {
        return Single
                .fromCallable<String> { keyStore.exportKeystore(wallet.address,password)!! }
                .subscribeOn(Schedulers.io())
    }

    fun deleteAccount(address: String, password: String): Completable {
        return Completable.fromCallable { keyStore.deleteWallet(address) }
                .subscribeOn(Schedulers.io())
    }

    fun signTransaction(signer: Wallet, signerPassword: String, toAddress: String, amount: BigInteger, gasPrice: BigInteger, gasLimit: BigInteger, nonce: Long, data: String?, chainId: Long): Single<ByteArray> {
        return Single.fromCallable {
            val keys = keyStore.getWalletCredentials(signerPassword, signer.address)

            val tx = RawTransaction.createTransaction(
                    BigInteger(nonce.toString()),
                    gasPrice,
                    gasLimit,
                    toAddress,
//                    BigDecimal(amount).multiply(ExchangeCalculator.ONE_ETHER).toBigInteger(),
                    BigDecimal(amount).toBigInteger(),
                    data.toString()
            )
            TransactionEncoder.signMessage(tx, chainId.toByte(), keys)
        }.subscribeOn(Schedulers.io())
    }

    fun hasAccount(address: String): Boolean {
        return keyStore.hasAddress(address)
    }

    fun fetchAccounts(): Single<Array<Wallet?>> {
        return Single.fromCallable<Array<Wallet?>> {
            val accounts = keyStore.getAccounts()
            val len = accounts.size
            val result = arrayOfNulls<Wallet>(len)

            for (i in 0 until len) {
                val gethAccount = accounts.get(i)
                result[i] = Wallet(generateAddress(gethAccount.address.toLowerCase()))
            }
            result
        }
                .subscribeOn(Schedulers.io())
    }

    private fun generateAddress(address: String): String {
        return if (!address.startsWith("0x") && !address.startsWith("0X")) {
            "0x$address"
        } else address
    }

    companion object {
        private val PRIVATE_KEY_RADIX = 16
        /**
         * CPU/Memory cost parameter. Must be larger than 1, a power of 2 and less than 2^(128 * r / 8).
         */
        private val N = 1 shl 9
        /**
         * Parallelization parameter. Must be a positive integer less than or equal to Integer.MAX_VALUE / (128 * r * 8).
         */
        private val P = 1
    }

}
