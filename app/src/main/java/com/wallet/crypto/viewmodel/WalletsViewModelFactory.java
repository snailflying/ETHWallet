package com.wallet.crypto.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.wallet.crypto.interact.CreateWalletInteract;
import com.wallet.crypto.interact.DeleteWalletInteract;
import com.wallet.crypto.interact.ExportWalletInteract;
import com.wallet.crypto.interact.FetchWalletsInteract;
import com.wallet.crypto.interact.FindDefaultWalletInteract;
import com.wallet.crypto.interact.SetDefaultWalletInteract;
import com.wallet.crypto.router.ImportWalletRouter;
import com.wallet.crypto.router.MainRouter;

import javax.inject.Inject;

public class WalletsViewModelFactory implements ViewModelProvider.Factory {

	private final CreateWalletInteract createWalletInteract;
	private final SetDefaultWalletInteract setDefaultWalletInteract;
	private final DeleteWalletInteract deleteWalletInteract;
	private final FetchWalletsInteract fetchWalletsInteract;
	private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final ExportWalletInteract exportWalletInteract;

	private final ImportWalletRouter importWalletRouter;
    private final MainRouter mainRouter;


    @Inject
	public WalletsViewModelFactory(
            CreateWalletInteract createWalletInteract,
            SetDefaultWalletInteract setDefaultWalletInteract,
            DeleteWalletInteract deleteWalletInteract,
            FetchWalletsInteract fetchWalletsInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            ExportWalletInteract exportWalletInteract,
            ImportWalletRouter importWalletRouter,
            MainRouter mainRouter) {
		this.createWalletInteract = createWalletInteract;
		this.setDefaultWalletInteract = setDefaultWalletInteract;
		this.deleteWalletInteract = deleteWalletInteract;
		this.fetchWalletsInteract = fetchWalletsInteract;
		this.findDefaultWalletInteract = findDefaultWalletInteract;
		this.exportWalletInteract = exportWalletInteract;
		this.importWalletRouter = importWalletRouter;
		this.mainRouter = mainRouter;
	}

	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		return (T) new WalletsViewModel(
                createWalletInteract,
                setDefaultWalletInteract,
                deleteWalletInteract,
                fetchWalletsInteract,
                findDefaultWalletInteract,
                exportWalletInteract,
                importWalletRouter,
				mainRouter);
	}
}
