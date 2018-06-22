package com.wallet.crypto.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.wallet.crypto.util.KS;
import com.wallet.pwd.trustapp.PasswordManager;

import java.util.Map;

import io.reactivex.Single;

public class TrustPasswordStore {

    private final Context context;
    private String pwd = "";

    public TrustPasswordStore(Context context) {
        this.context = context;

        migrate();
    }

    private void migrate() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, ?> passwords = pref.getAll();
        for (String key : passwords.keySet()) {
            if (key.contains("-pwd")) {
                String address = key.replace("-pwd", "");
                try {
                    KS.put(context, address.toLowerCase(), PasswordManager.getPassword(address, context));
                } catch (Exception ex) {
                    Toast.makeText(context, "Could not process passwords.", Toast.LENGTH_LONG)
                            .show();
                    ex.printStackTrace();
                }
            }
        }
    }

    public Single<String> generatePassword() {
        return Single.fromCallable(() -> pwd);
    }

    public void createdPwd(String pwd) {
        this.pwd = pwd;
    }
}
