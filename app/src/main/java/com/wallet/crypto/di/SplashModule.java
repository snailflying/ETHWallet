package com.wallet.crypto.di;

import com.wallet.crypto.interact.FetchWalletsInteract;
import com.wallet.crypto.repository.WalletRepository;
import com.wallet.crypto.viewmodel.SplashViewModelFactory;

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
