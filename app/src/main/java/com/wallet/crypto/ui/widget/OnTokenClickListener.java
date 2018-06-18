package com.wallet.crypto.ui.widget;

import android.view.View;

import com.wallet.crypto.entity.Token;

public interface OnTokenClickListener {
    void onTokenClick(View view, Token token);
}
