package com.wallet.crypto.di;

import com.wallet.crypto.interact.ImportWalletInteract;
import com.wallet.crypto.repository.WalletRepository;
import com.wallet.crypto.viewmodel.ImportWalletViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
class ImportModule {
    @Provides
    ImportWalletViewModelFactory provideImportWalletViewModelFactory(
            ImportWalletInteract importWalletInteract) {
        return new ImportWalletViewModelFactory(importWalletInteract);
    }

    @Provides
    ImportWalletInteract provideImportWalletInteract(WalletRepository walletRepository) {
        return new ImportWalletInteract(walletRepository);
    }
}
