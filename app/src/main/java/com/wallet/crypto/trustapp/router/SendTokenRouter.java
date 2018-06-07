package com.wallet.crypto.trustapp.router;


import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.trustapp.TrustConstants;
import com.wallet.crypto.trustapp.ui.SendActivity;

public class SendTokenRouter {
    public void open(Context context, String address, String symbol, int decimals) {
        Intent intent = new Intent(context, SendActivity.class);
        intent.putExtra(TrustConstants.EXTRA_SENDING_TOKENS, true);
        intent.putExtra(TrustConstants.EXTRA_CONTRACT_ADDRESS, address);
        intent.putExtra(TrustConstants.EXTRA_SYMBOL, symbol);
        intent.putExtra(TrustConstants.EXTRA_DECIMALS, decimals);
        context.startActivity(intent);
    }
}
