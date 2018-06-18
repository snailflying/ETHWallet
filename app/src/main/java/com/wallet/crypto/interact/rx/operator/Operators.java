package com.wallet.crypto.interact.rx.operator;

import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.repository.TrustPasswordStore;
import com.wallet.crypto.repository.WalletRepository;

import io.reactivex.CompletableOperator;
import io.reactivex.SingleTransformer;

public class Operators {

    public static SingleTransformer<Wallet, Wallet> savePassword(
            TrustPasswordStore passwordStore, WalletRepository walletRepository, String password) {
        return new SavePasswordOperator(passwordStore, walletRepository, password);
    }

    public static CompletableOperator completableErrorProxy(Throwable throwable) {
        return new CompletableErrorProxyOperator(throwable);
    }
}
