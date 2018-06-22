package com.wallet.crypto.interact;

import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.repository.WalletRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ExportWalletInteract {

    private final WalletRepository walletRepository;

    public ExportWalletInteract(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Single<String> export(Wallet wallet) {
        return walletRepository
                .exportWallet(wallet)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
