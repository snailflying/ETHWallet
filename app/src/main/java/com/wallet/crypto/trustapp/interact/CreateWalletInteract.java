package com.wallet.crypto.trustapp.interact;

import com.wallet.crypto.trustapp.entity.Wallet;
import com.wallet.crypto.trustapp.interact.rx.operator.Operators;
import com.wallet.crypto.trustapp.repository.TrustPasswordStore;
import com.wallet.crypto.trustapp.repository.WalletRepository;

import io.reactivex.Single;

import static com.wallet.crypto.trustapp.interact.rx.operator.Operators.completableErrorProxy;

public class CreateWalletInteract {

	private final WalletRepository walletRepository;
	private final TrustPasswordStore passwordStore;

	public CreateWalletInteract(WalletRepository walletRepository, TrustPasswordStore passwordStore) {
		this.walletRepository = walletRepository;
		this.passwordStore = passwordStore;
	}

	public Single<Wallet> create() {
	    return passwordStore.generatePassword()
		.flatMap(masterPassword -> walletRepository
			.createWallet(masterPassword)
			.compose(Operators.savePassword(passwordStore, walletRepository, masterPassword))
                       	.flatMap(wallet -> passwordVerification(wallet, masterPassword)));
	}
	
	private Single<Wallet> passwordVerification(Wallet wallet, String masterPassword) {
            return passwordStore
                .getPassword(wallet)
                .flatMap(password -> walletRepository
                        .exportWallet(wallet, password, password)
                        .flatMap(keyStore -> walletRepository.findWallet(wallet.getAddress())))
                .onErrorResumeNext(throwable -> walletRepository
                        .deleteWallet(wallet.getAddress(), masterPassword)
                        .lift(completableErrorProxy(throwable))
                        .toSingle(() -> wallet));
	}
}
