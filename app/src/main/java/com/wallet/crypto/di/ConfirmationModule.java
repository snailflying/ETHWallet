package com.wallet.crypto.di;


import com.wallet.crypto.interact.CreateTransactionInteract;
import com.wallet.crypto.interact.FetchGasSettingsInteract;
import com.wallet.crypto.interact.FindDefaultWalletInteract;
import com.wallet.crypto.repository.TrustPasswordStore;
import com.wallet.crypto.repository.SharedPreferenceRepository;
import com.wallet.crypto.repository.TransactionRepository;
import com.wallet.crypto.repository.WalletRepository;
import com.wallet.crypto.router.GasSettingsRouter;
import com.wallet.crypto.viewmodel.ConfirmationViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfirmationModule {
    @Provides
    public ConfirmationViewModelFactory provideConfirmationViewModelFactory(
            FindDefaultWalletInteract findDefaultWalletInteract,
            FetchGasSettingsInteract fetchGasSettingsInteract,
            CreateTransactionInteract createTransactionInteract,
            GasSettingsRouter gasSettingsRouter
    ) {
        return new ConfirmationViewModelFactory(findDefaultWalletInteract, fetchGasSettingsInteract, createTransactionInteract, gasSettingsRouter);
    }

    @Provides
    FindDefaultWalletInteract provideFindDefaultWalletInteract(WalletRepository walletRepository) {
        return new FindDefaultWalletInteract(walletRepository);
    }

    @Provides
    FetchGasSettingsInteract provideFetchGasSettingsInteract(SharedPreferenceRepository preferenceRepositoryType) {
        return new FetchGasSettingsInteract(preferenceRepositoryType);
    }

    @Provides
    CreateTransactionInteract provideCreateTransactionInteract(TransactionRepository transactionRepository, TrustPasswordStore passwordStore) {
        return new CreateTransactionInteract(transactionRepository, passwordStore);
    }

    @Provides
    GasSettingsRouter provideGasSettingsRouter() {
        return new GasSettingsRouter();
    }
}
