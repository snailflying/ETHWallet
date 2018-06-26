package com.wallet.crypto.router;


import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.MercuryConstants;
import com.wallet.crypto.ui.ConfirmationActivity;

import java.math.BigInteger;

public class ConfirmationRouter {
    public void open(Context context, String to, BigInteger amount, String contractAddress, int decimals, String symbol, boolean sendingTokens) {
        Intent intent = new Intent(context, ConfirmationActivity.class);
        intent.putExtra(MercuryConstants.EXTRA_TO_ADDRESS, to);
        intent.putExtra(MercuryConstants.EXTRA_AMOUNT, amount.toString());
        intent.putExtra(MercuryConstants.EXTRA_CONTRACT_ADDRESS, contractAddress);
        intent.putExtra(MercuryConstants.EXTRA_DECIMALS, decimals);
        intent.putExtra(MercuryConstants.EXTRA_SYMBOL, symbol);
        intent.putExtra(MercuryConstants.EXTRA_SENDING_TOKENS, sendingTokens);
        context.startActivity(intent);
    }
}
