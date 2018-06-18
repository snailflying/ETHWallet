package com.wallet.crypto.router;

import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.entity.Transaction;
import com.wallet.crypto.ui.TransactionDetailActivity;

import static com.wallet.crypto.TrustConstants.Key.TRANSACTION;

public class TransactionDetailRouter {

    public void open(Context context, Transaction transaction) {
        Intent intent = new Intent(context, TransactionDetailActivity.class);
        intent.putExtra(TRANSACTION, transaction);
        context.startActivity(intent);
    }
}
