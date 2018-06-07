package com.wallet.crypto.trustapp.di;


import com.wallet.crypto.trustapp.interact.FindDefaultNetworkInteract;
import com.wallet.crypto.trustapp.repository.EthereumNetworkRepository;
import com.wallet.crypto.trustapp.viewmodel.GasSettingsViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class GasSettingsModule {

    @Provides
    public GasSettingsViewModelFactory provideGasSettingsViewModelFactory(FindDefaultNetworkInteract findDefaultNetworkInteract) {
        return new GasSettingsViewModelFactory(findDefaultNetworkInteract);
    }

    @Provides
    FindDefaultNetworkInteract provideFindDefaultNetworkInteract(
            EthereumNetworkRepository ethereumNetworkRepositoryType) {
        return new FindDefaultNetworkInteract(ethereumNetworkRepositoryType);
    }
}
