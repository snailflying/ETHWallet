package com.wallet.crypto.trustapp.interact;

import com.wallet.crypto.trustapp.entity.Wallet;
import com.wallet.crypto.trustapp.repository.WalletRepository;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SetDefaultWalletInteract {

	private WalletRepository accountRepository;

	public SetDefaultWalletInteract(WalletRepository walletRepositoryType) {
		this.accountRepository = walletRepositoryType;
	}

	public Completable set(Wallet wallet) {
		return accountRepository
				.setDefaultWallet(wallet)
				.observeOn(AndroidSchedulers.mainThread());
	}
}
