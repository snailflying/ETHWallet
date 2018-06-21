package com.wallet.crypto.interact;

import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.interact.rx.operator.Operators;
import com.wallet.crypto.repository.TrustPasswordStore;
import com.wallet.crypto.repository.WalletRepository;

import io.reactivex.Single;

import static com.wallet.crypto.interact.rx.operator.Operators.completableErrorProxy;

public class CreateWalletInteract {

	private final WalletRepository walletRepository;
	private final TrustPasswordStore passwordStore;

	public CreateWalletInteract(WalletRepository walletRepository, TrustPasswordStore passwordStore) {
		this.walletRepository = walletRepository;
		this.passwordStore = passwordStore;
	}

	/**
	 * 创建钱包
	 * @return
	 */
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
                        .exportWallet(wallet, password)
                        .flatMap(keyStore -> walletRepository.findWallet(wallet.getAddress())))
                .onErrorResumeNext(throwable -> walletRepository
                        .deleteWallet(wallet.getAddress(), masterPassword)
                        .lift(completableErrorProxy(throwable))
                        .toSingle(() -> wallet));
	}

	public void createdPwd(String pwd) {
		passwordStore.createdPwd(pwd);
	}
}
