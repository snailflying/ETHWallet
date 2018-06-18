package com.wallet.crypto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.interact.ImportWalletInteract;
import com.wallet.crypto.ui.widget.OnImportKeystoreListener;
import com.wallet.crypto.ui.widget.OnImportPrivateKeyListener;

public class ImportWalletViewModel extends BaseViewModel implements OnImportKeystoreListener, OnImportPrivateKeyListener {

    private final ImportWalletInteract importWalletInteract;
    private final MutableLiveData<Wallet> wallet = new MutableLiveData<>();

    ImportWalletViewModel(ImportWalletInteract importWalletInteract) {
        this.importWalletInteract = importWalletInteract;
    }

    @Override
    public void onKeystore(String keystore, String password) {
        getProgress().postValue(true);
        importWalletInteract
                .importKeystore(keystore, password)
                .subscribe(this::onWallet, this::onError);
    }

    @Override
    public void onPrivateKey(String key) {
        getProgress().postValue(true);
        importWalletInteract
                .importPrivateKey(key)
                .subscribe(this::onWallet, this::onError);
    }

    public LiveData<Wallet> wallet() {
        return wallet;
    }

    private void onWallet(Wallet wallet) {
        getProgress().postValue(false);
        this.wallet.postValue(wallet);
    }
}
