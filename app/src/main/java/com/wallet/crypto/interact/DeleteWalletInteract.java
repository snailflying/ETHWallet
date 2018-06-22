package com.wallet.crypto.interact;

import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.repository.TrustPasswordStore;
import com.wallet.crypto.repository.WalletRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Delete and fetch wallets
 */
public class DeleteWalletInteract {
    private final WalletRepository walletRepository;
    private final TrustPasswordStore passwordStore;

    public DeleteWalletInteract(WalletRepository walletRepository, TrustPasswordStore passwordStore) {
        this.walletRepository = walletRepository;
        this.passwordStore = passwordStore;
    }

    public Single<Wallet[]> delete(Wallet wallet) {
        return walletRepository.deleteWallet(wallet.getAddress())
                .andThen(walletRepository.fetchWallets())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
