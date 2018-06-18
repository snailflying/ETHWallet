package com.wallet.crypto.interact;

import com.wallet.crypto.entity.Transaction;
import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.repository.TransactionRepository;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class FetchTransactionsInteract {

    private final TransactionRepository transactionRepository;

    public FetchTransactionsInteract(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Observable<Transaction[]> fetch(Wallet wallet) {
        return transactionRepository
                .fetchTransaction(wallet)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
