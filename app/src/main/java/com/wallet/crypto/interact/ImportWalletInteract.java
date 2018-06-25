package com.wallet.crypto.interact;

import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.interact.rx.operator.Operators;
import com.wallet.crypto.repository.WalletRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ImportWalletInteract {

    private final WalletRepository walletRepository;

    public ImportWalletInteract(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Single<Wallet> importKeystore(String keystore, String password) {
        return walletRepository
                .importKeystoreToWallet(keystore, password)
                .compose(Operators.savePassword(walletRepository))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Wallet> importPrivateKey(String privateKey,String pwd) {
        return walletRepository
                .importPrivateKeyToWallet(privateKey,pwd)
                .compose(Operators.savePassword(walletRepository))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
