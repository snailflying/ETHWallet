package com.wallet.crypto.trustapp.interact;


import com.wallet.crypto.trustapp.entity.Wallet;
import com.wallet.crypto.trustapp.repository.TrustPasswordStore;
import com.wallet.crypto.trustapp.repository.TokenRepository;
import com.wallet.crypto.trustapp.repository.TransactionRepository;

import java.math.BigInteger;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CreateTransactionInteract {
    private final TransactionRepository transactionRepository;
    private final TrustPasswordStore passwordStore;

    public CreateTransactionInteract(TransactionRepository transactionRepository, TrustPasswordStore passwordStore) {
        this.transactionRepository = transactionRepository;
        this.passwordStore = passwordStore;
    }

    public Single<String> create(Wallet from, String to, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data) {
        return passwordStore.getPassword(from)
                .flatMap(password ->
                        transactionRepository.createTransaction(from, to, subunitAmount, gasPrice, gasLimit, data, password)
                .observeOn(AndroidSchedulers.mainThread()));
    }

}
