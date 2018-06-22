package com.wallet.crypto.interact;


import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.repository.TransactionRepository;

import java.math.BigInteger;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CreateTransactionInteract {
    private final TransactionRepository transactionRepository;

    public CreateTransactionInteract(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Single<String> create(String password,Wallet from, String to, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, String data) {
        return transactionRepository.createTransaction(from, to, subunitAmount, gasPrice, gasLimit, data, password)
                .observeOn(AndroidSchedulers.mainThread());
    }

}
