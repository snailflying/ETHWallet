package com.wallet.crypto.trustapp.di;

import com.wallet.crypto.trustapp.interact.FetchWalletsInteract;
import com.wallet.crypto.trustapp.repository.WalletRepository;
import com.wallet.crypto.trustapp.viewmodel.SplashViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashModule {

    @Provides
    SplashViewModelFactory provideSplashViewModelFactory(FetchWalletsInteract fetchWalletsInteract) {
        return new SplashViewModelFactory(fetchWalletsInteract);
    }

    @Provides
    FetchWalletsInteract provideFetchWalletInteract(WalletRepository walletRepository) {
        return new FetchWalletsInteract(walletRepository);
    }
}
