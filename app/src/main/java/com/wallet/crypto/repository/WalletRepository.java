package com.wallet.crypto.repository;

import android.util.Log;

import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.service.GethKeystoreAccountService;

import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class WalletRepository {

    private final SharedPreferenceRepository preferenceRepositoryType;
    private final GethKeystoreAccountService accountKeystoreService;
    private final EthereumNetworkRepository networkRepository;
    private final OkHttpClient httpClient;

    public WalletRepository(
            OkHttpClient okHttpClient,
            SharedPreferenceRepository preferenceRepositoryType,
            GethKeystoreAccountService accountKeystoreService,
            EthereumNetworkRepository networkRepository) {
        this.httpClient = okHttpClient;
        this.preferenceRepositoryType = preferenceRepositoryType;
        this.accountKeystoreService = accountKeystoreService;
        this.networkRepository = networkRepository;
    }

    public Single<Wallet[]> fetchWallets() {
        return accountKeystoreService.fetchAccounts();
    }

    public Single<Wallet> findWallet(String address) {
        return fetchWallets()
                .flatMap(accounts -> {
                    for (Wallet wallet : accounts) {
                        if (wallet.sameAddress(address)) {
                            return Single.just(wallet);
                        }
                    }
                    return null;
                });
    }

    public Single<Wallet> createWallet(String password) {
        return accountKeystoreService
                .createAccount(password);
    }

    public Single<Wallet> importKeystoreToWallet(String store, String password, String newPassword) {
        return accountKeystoreService.importKeystore(store, password, newPassword);
    }

    public Single<Wallet> importPrivateKeyToWallet(String privateKey, String newPassword) {
        return accountKeystoreService.importPrivateKey(privateKey, newPassword);
    }

    public Single<String> exportWallet(Wallet wallet, String password) {
        return accountKeystoreService.exportAccount(wallet, password);
    }

    public Completable deleteWallet(String address, String password) {
        return accountKeystoreService.deleteAccount(address, password);
    }

    public Completable setDefaultWallet(Wallet wallet) {
        return Completable.fromAction(() -> preferenceRepositoryType.setCurrentWalletAddress(wallet.getAddress()));
    }

    public Single<Wallet> getDefaultWallet() {
        return Single.fromCallable(preferenceRepositoryType::getCurrentWalletAddress)
                .flatMap(this::findWallet);
    }

    public Single<BigInteger> balanceInWei(Wallet wallet) {
        Log.d("aaron", "address:" + wallet.getAddress());

        return Single.fromCallable(() -> Web3jFactory
                .build(new HttpService(networkRepository.getDefaultNetwork().rpcServerUrl, httpClient, false))
                .ethGetBalance(wallet.getAddress(), DefaultBlockParameterName.LATEST)
                .send()
                .getBalance())
                .subscribeOn(Schedulers.io());
    }
}