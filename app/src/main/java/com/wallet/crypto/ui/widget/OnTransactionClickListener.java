package com.wallet.crypto.ui.widget;

import android.view.View;

import com.wallet.crypto.entity.Transaction;

public interface OnTransactionClickListener {
    void onTransactionClick(View view, Transaction transaction);
}
