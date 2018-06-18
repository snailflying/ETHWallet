package com.wallet.crypto.interact;


import com.wallet.crypto.entity.GasSettings;
import com.wallet.crypto.repository.SharedPreferenceRepository;

public class FetchGasSettingsInteract {
    private final SharedPreferenceRepository repository;

    public FetchGasSettingsInteract(SharedPreferenceRepository repository) {
        this.repository = repository;
    }

    public GasSettings fetch(boolean forTokenTransfer) {
        return repository.getGasSettings(forTokenTransfer);
    }

}
