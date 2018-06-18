package com.wallet.crypto.interact;

import com.wallet.crypto.repository.TokenRepository;
import com.wallet.crypto.repository.WalletRepository;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class AddTokenInteract {
    private final TokenRepository tokenRepository;
    private final WalletRepository walletRepository;

    public AddTokenInteract(
            WalletRepository walletRepository, TokenRepository tokenRepository) {
        this.walletRepository = walletRepository;
        this.tokenRepository = tokenRepository;
    }

    public Completable add(String address, String symbol, int decimals) {
        return walletRepository
                .getDefaultWallet()
                .flatMapCompletable(
                        wallet -> tokenRepository
                        .addToken(wallet, address, symbol, decimals)
                        .observeOn(AndroidSchedulers.mainThread()));
    }
}
