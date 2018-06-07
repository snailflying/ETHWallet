package com.wallet.crypto.trustapp.interact;

import com.wallet.crypto.trustapp.entity.Wallet;
import com.wallet.crypto.trustapp.repository.TrustPasswordStore;
import com.wallet.crypto.trustapp.repository.WalletRepository;

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
		return passwordStore.getPassword(wallet)
				.flatMapCompletable(password -> walletRepository.deleteWallet(wallet.getAddress(), password))
				.andThen(walletRepository.fetchWallets())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
