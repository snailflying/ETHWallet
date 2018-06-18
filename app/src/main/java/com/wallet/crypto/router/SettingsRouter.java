package com.wallet.crypto.router;

import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.ui.SettingsActivity;

public class SettingsRouter {

    public void open(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }
}
