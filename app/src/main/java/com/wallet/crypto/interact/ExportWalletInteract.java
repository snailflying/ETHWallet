package com.wallet.crypto.interact;

import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.repository.TrustPasswordStore;
import com.wallet.crypto.repository.WalletRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ExportWalletInteract {

    private final WalletRepository walletRepository;
    private final TrustPasswordStore passwordStore;

    public ExportWalletInteract(WalletRepository walletRepository, TrustPasswordStore passwordStore) {
        this.walletRepository = walletRepository;
        this.passwordStore = passwordStore;
    }

    public Single<String> export(Wallet wallet, String oldPassword) {
        return
                passwordStore
                .getPassword(wallet)
                .flatMap(password ->
                        walletRepository
                    .exportWallet(wallet, oldPassword)
                )
                .observeOn(AndroidSchedulers.mainThread());
    }
}
