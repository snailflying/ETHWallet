package com.wallet.crypto.repository;

import com.wallet.crypto.entity.Transaction;
import com.wallet.crypto.entity.Wallet;

import io.reactivex.Single;

public class TransactionInDiskSource implements TransactionLocalSource {
	@Override
	public Single<Transaction[]> fetchTransaction(Wallet wallet) {
		return null;
	}

	@Override
	public void putTransactions(Wallet wallet, Transaction[] transactions) {

	}

    @Override
    public void clear() {

    }
}
