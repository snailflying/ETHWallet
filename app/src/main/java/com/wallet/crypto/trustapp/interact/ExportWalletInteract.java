package com.wallet.crypto.trustapp.interact;

import com.wallet.crypto.trustapp.entity.Wallet;
import com.wallet.crypto.trustapp.repository.TrustPasswordStore;
import com.wallet.crypto.trustapp.repository.WalletRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ExportWalletInteract {

    private final WalletRepository walletRepository;
    private final TrustPasswordStore passwordStore;

    public ExportWalletInteract(WalletRepository walletRepository, TrustPasswordStore passwordStore) {
        this.walletRepository = walletRepository;
        this.passwordStore = passwordStore;
    }

    public Single<String> export(Wallet wallet, String backupPassword) {
        return passwordStore
                .getPassword(wallet)
                .flatMap(password -> walletRepository
                    .exportWallet(wallet, password, backupPassword))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
