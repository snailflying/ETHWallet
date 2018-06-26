package com.wallet.crypto.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wallet.crypto.MercuryConstants;
import com.wallet.crypto.entity.GasSettings;

import java.math.BigInteger;

public class SharedPreferenceRepository {

    private static final String CURRENT_ACCOUNT_ADDRESS_KEY = "current_account_address";
    private static final String DEFAULT_NETWORK_NAME_KEY = "default_network_name";
    private static final String GAS_PRICE_KEY = "gas_price";
    private static final String GAS_LIMIT_KEY = "gas_limit";
    private static final String GAS_LIMIT_FOR_TOKENS_KEY = "gas_limit_for_tokens";

    private final SharedPreferences pref;

    public SharedPreferenceRepository(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getCurrentWalletAddress() {
        return pref.getString(CURRENT_ACCOUNT_ADDRESS_KEY, null);
    }

    public void setCurrentWalletAddress(String address) {
        pref.edit().putString(CURRENT_ACCOUNT_ADDRESS_KEY, address).apply();
    }

    public String getDefaultNetwork() {
        return pref.getString(DEFAULT_NETWORK_NAME_KEY, null);
    }

    public void setDefaultNetwork(String netName) {
        pref.edit().putString(DEFAULT_NETWORK_NAME_KEY, netName).apply();
    }

    public GasSettings getGasSettings(boolean forTokenTransfer) {
        BigInteger gasPrice = new BigInteger(pref.getString(GAS_PRICE_KEY, MercuryConstants.DEFAULT_GAS_PRICE));
        BigInteger gasLimit = new BigInteger(pref.getString(GAS_LIMIT_KEY, MercuryConstants.DEFAULT_GAS_LIMIT));
        if (forTokenTransfer) {
            gasLimit = new BigInteger(pref.getString(GAS_LIMIT_FOR_TOKENS_KEY, MercuryConstants.DEFAULT_GAS_LIMIT_FOR_TOKENS));
        }

        return new GasSettings(gasPrice, gasLimit);
    }

    public void setGasSettings(GasSettings gasSettings) {
        pref.edit().putString(GAS_PRICE_KEY, gasSettings.gasPrice.toString()).apply();
        pref.edit().putString(GAS_PRICE_KEY, gasSettings.gasLimit.toString()).apply();
    }
}
