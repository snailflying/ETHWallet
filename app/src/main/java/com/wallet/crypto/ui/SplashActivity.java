package com.wallet.crypto.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wallet.crypto.BuildConfig;
import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.router.ManageWalletsRouter;
import com.wallet.crypto.router.TransactionsRouter;
import com.wallet.crypto.viewmodel.SplashViewModel;
import com.wallet.crypto.viewmodel.SplashViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
//import io.fabric.sdk.android.Fabric;

public class SplashActivity extends BaseActivity {

    @Inject
    SplashViewModelFactory splashViewModelFactory;
    SplashViewModel splashViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashViewModel = ViewModelProviders.of(this, splashViewModelFactory)
                .get(SplashViewModel.class);
        splashViewModel.wallets().observe(this, this::onWallets);
    }

    private void onWallets(Wallet[] wallets) {
        // Start home activity
        if (wallets.length == 0) {
            new ManageWalletsRouter().open(this, true);
        } else {
            new TransactionsRouter().open(this, true);
        }
        finish();
    }

}
