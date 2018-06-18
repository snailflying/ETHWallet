package com.wallet.crypto.di;


import com.wallet.crypto.interact.FindDefaultNetworkInteract;
import com.wallet.crypto.repository.EthereumNetworkRepository;
import com.wallet.crypto.viewmodel.GasSettingsViewModelFactory;

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
