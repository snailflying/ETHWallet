package com.wallet.crypto.trustapp.di;

import com.wallet.crypto.trustapp.interact.CreateWalletInteract;
import com.wallet.crypto.trustapp.interact.DeleteWalletInteract;
import com.wallet.crypto.trustapp.interact.ExportWalletInteract;
import com.wallet.crypto.trustapp.interact.FetchWalletsInteract;
import com.wallet.crypto.trustapp.interact.FindDefaultWalletInteract;
import com.wallet.crypto.trustapp.interact.SetDefaultWalletInteract;
import com.wallet.crypto.trustapp.repository.TrustPasswordStore;
import com.wallet.crypto.trustapp.repository.WalletRepository;
import com.wallet.crypto.trustapp.router.ImportWalletRouter;
import com.wallet.crypto.trustapp.router.TransactionsRouter;
import com.wallet.crypto.trustapp.viewmodel.WalletsViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
class AccountsManageModule {

	@Provides
    WalletsViewModelFactory provideAccountsManageViewModelFactory(
			CreateWalletInteract createWalletInteract,
			SetDefaultWalletInteract setDefaultWalletInteract,
			DeleteWalletInteract deleteWalletInteract,
			FetchWalletsInteract fetchWalletsInteract,
			FindDefaultWalletInteract findDefaultWalletInteract,
			ExportWalletInteract exportWalletInteract,
			ImportWalletRouter importWalletRouter,
            TransactionsRouter transactionsRouter) {
		return new WalletsViewModelFactory(createWalletInteract,
                setDefaultWalletInteract,
                deleteWalletInteract,
                fetchWalletsInteract,
                findDefaultWalletInteract,
                exportWalletInteract,
                importWalletRouter,
                transactionsRouter);
	}

	@Provides
    CreateWalletInteract provideCreateAccountInteract(
            WalletRepository accountRepository, TrustPasswordStore passwordStore) {
		return new CreateWalletInteract(accountRepository, passwordStore);
	}

	@Provides
    SetDefaultWalletInteract provideSetDefaultAccountInteract(WalletRepository accountRepository) {
		return new SetDefaultWalletInteract(accountRepository);
	}

	@Provides
    DeleteWalletInteract provideDeleteAccountInteract(
            WalletRepository accountRepository, TrustPasswordStore store) {
		return new DeleteWalletInteract(accountRepository, store);
	}

	@Provides
    FetchWalletsInteract provideFetchAccountsInteract(WalletRepository accountRepository) {
		return new FetchWalletsInteract(accountRepository);
	}

	@Provides
    FindDefaultWalletInteract provideFindDefaultAccountInteract(WalletRepository accountRepository) {
		return new FindDefaultWalletInteract(accountRepository);
	}

	@Provides
    ExportWalletInteract provideExportWalletInteract(
            WalletRepository walletRepository, TrustPasswordStore passwordStore) {
	    return new ExportWalletInteract(walletRepository, passwordStore);
    }

	@Provides
    ImportWalletRouter provideImportAccountRouter() {
		return new ImportWalletRouter();
	}

	@Provides
    TransactionsRouter provideTransactionsRouter() {
	    return new TransactionsRouter();
    }
}
