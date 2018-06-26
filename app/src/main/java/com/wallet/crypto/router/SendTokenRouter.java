package com.wallet.crypto.router;


import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.MercuryConstants;
import com.wallet.crypto.ui.SendActivity;

public class SendTokenRouter {
    public void open(Context context, String address, String symbol, int decimals) {
        Intent intent = new Intent(context, SendActivity.class);
        intent.putExtra(MercuryConstants.EXTRA_SENDING_TOKENS, true);
        intent.putExtra(MercuryConstants.EXTRA_CONTRACT_ADDRESS, address);
        intent.putExtra(MercuryConstants.EXTRA_SYMBOL, symbol);
        intent.putExtra(MercuryConstants.EXTRA_DECIMALS, decimals);
        context.startActivity(intent);
    }
}
