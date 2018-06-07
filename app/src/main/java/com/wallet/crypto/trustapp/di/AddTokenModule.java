package com.wallet.crypto.trustapp.di;

import com.wallet.crypto.trustapp.interact.AddTokenInteract;
import com.wallet.crypto.trustapp.interact.FindDefaultWalletInteract;
import com.wallet.crypto.trustapp.repository.TokenRepository;
import com.wallet.crypto.trustapp.repository.WalletRepository;
import com.wallet.crypto.trustapp.router.MyTokensRouter;
import com.wallet.crypto.trustapp.viewmodel.AddTokenViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class AddTokenModule {

    @Provides
    AddTokenViewModelFactory addTokenViewModelFactory(
            AddTokenInteract addTokenInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            MyTokensRouter myTokensRouter) {
        return new AddTokenViewModelFactory(
                addTokenInteract, findDefaultWalletInteract, myTokensRouter);
    }

    @Provides
    AddTokenInteract provideAddTokenInteract(
            TokenRepository tokenRepository,
            WalletRepository walletRepository) {
        return new AddTokenInteract(walletRepository, tokenRepository);
    }

    @Provides
    FindDefaultWalletInteract provideFindDefaultWalletInteract(WalletRepository walletRepository) {
        return new FindDefaultWalletInteract(walletRepository);
    }

    @Provides
    MyTokensRouter provideMyTokensRouter() {
        return new MyTokensRouter();
    }
}
