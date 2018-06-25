package com.wallet.crypto.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.wallet.crypto.interact.FetchTokensInteract;
import com.wallet.crypto.interact.FindDefaultNetworkInteract;
import com.wallet.crypto.router.AddTokenRouter;
import com.wallet.crypto.router.MainRouter;
import com.wallet.crypto.router.SendTokenRouter;

public class TokensViewModelFactory implements ViewModelProvider.Factory {

    private final FindDefaultNetworkInteract findDefaultNetworkInteract;
    private final FetchTokensInteract fetchTokensInteract;
    private final AddTokenRouter addTokenRouter;
    private final SendTokenRouter sendTokenRouter;
    private final MainRouter mainRouter;

    public TokensViewModelFactory(FindDefaultNetworkInteract findDefaultNetworkInteract,
                                  FetchTokensInteract fetchTokensInteract,
                                  AddTokenRouter addTokenRouter,
                                  SendTokenRouter sendTokenRouter,
                                  MainRouter mainRouter) {
        this.findDefaultNetworkInteract = findDefaultNetworkInteract;
        this.fetchTokensInteract = fetchTokensInteract;
        this.addTokenRouter = addTokenRouter;
        this.sendTokenRouter = sendTokenRouter;
        this.mainRouter = mainRouter;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TokensViewModel(
                findDefaultNetworkInteract,
                fetchTokensInteract,
                addTokenRouter,
                sendTokenRouter,
                mainRouter);
    }
}
