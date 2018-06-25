package com.wallet.crypto.di;

import com.wallet.crypto.interact.CreateWalletInteract;
import com.wallet.crypto.interact.DeleteWalletInteract;
import com.wallet.crypto.interact.ExportWalletInteract;
import com.wallet.crypto.interact.FetchWalletsInteract;
import com.wallet.crypto.interact.FindDefaultWalletInteract;
import com.wallet.crypto.interact.SetDefaultWalletInteract;
import com.wallet.crypto.repository.WalletRepository;
import com.wallet.crypto.router.ImportWalletRouter;
import com.wallet.crypto.router.MainRouter;
import com.wallet.crypto.viewmodel.WalletsViewModelFactory;

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
            MainRouter mainRouter) {
		return new WalletsViewModelFactory(createWalletInteract,
                setDefaultWalletInteract,
                deleteWalletInteract,
                fetchWalletsInteract,
                findDefaultWalletInteract,
                exportWalletInteract,
                importWalletRouter,
				mainRouter);
	}

	@Provides
    CreateWalletInteract provideCreateAccountInteract(WalletRepository accountRepository) {
		return new CreateWalletInteract(accountRepository);
	}

	@Provides
    SetDefaultWalletInteract provideSetDefaultAccountInteract(WalletRepository accountRepository) {
		return new SetDefaultWalletInteract(accountRepository);
	}

	@Provides
    DeleteWalletInteract provideDeleteAccountInteract(WalletRepository accountRepository) {
		return new DeleteWalletInteract(accountRepository);
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
    ExportWalletInteract provideExportWalletInteract(WalletRepository walletRepository) {
	    return new ExportWalletInteract(walletRepository);
    }

	@Provides
    ImportWalletRouter provideImportAccountRouter() {
		return new ImportWalletRouter();
	}

	@Provides
	MainRouter provideTransactionsRouter() {
	    return new MainRouter();
    }
}
