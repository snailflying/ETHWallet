package com.wallet.crypto.trustapp.interact;

import com.wallet.crypto.trustapp.entity.Transaction;
import com.wallet.crypto.trustapp.entity.Wallet;
import com.wallet.crypto.trustapp.repository.TransactionRepository;

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
