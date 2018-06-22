package com.wallet.crypto.interact;

import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.interact.rx.operator.Operators;
import com.wallet.crypto.repository.WalletRepository;

import io.reactivex.Single;

import static com.wallet.crypto.interact.rx.operator.Operators.completableErrorProxy;

public class CreateWalletInteract {

    private final WalletRepository walletRepository;

    public CreateWalletInteract(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * 创建钱包
     *
     * @return
     * @param pwd
     */
    public Single<Wallet> create(String pwd) {
        return walletRepository
                .createWallet(pwd)
                .compose(Operators.savePassword(walletRepository))
                .flatMap(this::passwordVerification);
    }

    private Single<Wallet> passwordVerification(Wallet wallet) {
        return walletRepository
                .exportWallet(wallet)
                .flatMap(keyStore -> walletRepository.findWallet(wallet.getAddress()))
                .onErrorResumeNext(throwable -> walletRepository
                        .deleteWallet(wallet.getAddress())
                        .lift(completableErrorProxy(throwable))
                        .toSingle(() -> wallet));
    }
}
