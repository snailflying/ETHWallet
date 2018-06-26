package com.wallet.crypto.router;


import android.app.Activity;
import android.content.Intent;

import com.wallet.crypto.MercuryConstants;
import com.wallet.crypto.entity.GasSettings;
import com.wallet.crypto.ui.GasSettingsActivity;
import com.wallet.crypto.viewmodel.GasSettingsViewModel;

public class GasSettingsRouter {
    public void open(Activity context, GasSettings gasSettings) {
        Intent intent = new Intent(context, GasSettingsActivity.class);
        intent.putExtra(MercuryConstants.EXTRA_GAS_PRICE, gasSettings.gasPrice.toString());
        intent.putExtra(MercuryConstants.EXTRA_GAS_LIMIT, gasSettings.gasLimit.toString());
        context.startActivityForResult(intent, GasSettingsViewModel.SET_GAS_SETTINGS);
    }
}
