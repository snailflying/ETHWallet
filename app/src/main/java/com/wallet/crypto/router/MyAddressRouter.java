package com.wallet.crypto.router;

import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.ui.MyAddressActivity;

import static com.wallet.crypto.MercuryConstants.Key.WALLET;

public class MyAddressRouter {

    public void open(Context context, Wallet wallet) {
        Intent intent = new Intent(context, MyAddressActivity.class);
        intent.putExtra(WALLET, wallet);
        context.startActivity(intent);
    }
}
