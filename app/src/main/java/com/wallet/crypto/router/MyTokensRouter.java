package com.wallet.crypto.router;

import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.ui.TokensActivity;

import static com.wallet.crypto.MercuryConstants.Key.WALLET;

public class MyTokensRouter {

    public void open(Context context, Wallet wallet) {
        Intent intent = new Intent(context, TokensActivity.class);
        intent.putExtra(WALLET, wallet);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
