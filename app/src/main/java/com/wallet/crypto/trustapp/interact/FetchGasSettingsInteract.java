package com.wallet.crypto.trustapp.interact;


import com.wallet.crypto.trustapp.entity.GasSettings;
import com.wallet.crypto.trustapp.repository.SharedPreferenceRepository;

public class FetchGasSettingsInteract {
    private final SharedPreferenceRepository repository;

    public FetchGasSettingsInteract(SharedPreferenceRepository repository) {
        this.repository = repository;
    }

    public GasSettings fetch(boolean forTokenTransfer) {
        return repository.getGasSettings(forTokenTransfer);
    }

}
