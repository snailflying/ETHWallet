package com.wallet.crypto.viewmodel;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.wallet.crypto.interact.FindDefaultNetworkInteract;

public class GasSettingsViewModelFactory implements ViewModelProvider.Factory {

    FindDefaultNetworkInteract findDefaultNetworkInteract;

    public GasSettingsViewModelFactory(FindDefaultNetworkInteract findDefaultNetworkInteract) {
        this.findDefaultNetworkInteract = findDefaultNetworkInteract;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GasSettingsViewModel(findDefaultNetworkInteract);
    }
}
