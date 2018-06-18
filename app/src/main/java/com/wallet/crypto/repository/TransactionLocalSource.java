package com.wallet.crypto.repository;

import com.wallet.crypto.entity.Transaction;
import com.wallet.crypto.entity.Wallet;

import io.reactivex.Single;

public interface TransactionLocalSource {
	Single<Transaction[]> fetchTransaction(Wallet wallet);

	void putTransactions(Wallet wallet, Transaction[] transactions);

    void clear();
}
