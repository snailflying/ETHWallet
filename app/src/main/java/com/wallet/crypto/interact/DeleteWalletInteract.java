package com.wallet.crypto.interact;

import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.repository.WalletRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Delete and fetch wallets
 */
public class DeleteWalletInteract {
    private final WalletRepository walletRepository;

    public DeleteWalletInteract(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Single<Wallet[]> delete(Wallet wallet) {
        return walletRepository.deleteWallet(wallet.getAddress())
                .andThen(walletRepository.fetchWallets())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
