package com.wallet.crypto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.wallet.crypto.entity.NetworkInfo;
import com.wallet.crypto.entity.Token;
import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.interact.FetchTokensInteract;
import com.wallet.crypto.interact.FindDefaultNetworkInteract;
import com.wallet.crypto.router.AddTokenRouter;
import com.wallet.crypto.router.MainRouter;
import com.wallet.crypto.router.SendTokenRouter;

public class TokensViewModel extends BaseViewModel {
    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();
    private final MutableLiveData<Wallet> wallet = new MutableLiveData<>();
    private final MutableLiveData<Token[]> tokens = new MutableLiveData<>();

    private final FindDefaultNetworkInteract findDefaultNetworkInteract;
    private final FetchTokensInteract fetchTokensInteract;
    private final AddTokenRouter addTokenRouter;
    private final SendTokenRouter sendTokenRouter;
    private final MainRouter mainRouter;

    TokensViewModel(
            FindDefaultNetworkInteract findDefaultNetworkInteract,
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

    public void prepare() {
        getProgress().postValue(true);
        findDefaultNetwork();
    }

    private void findDefaultNetwork() {
        setDisposable(findDefaultNetworkInteract
                .find()
                .subscribe(this::onDefaultNetwork, this::onError));
    }

    private void onDefaultNetwork(NetworkInfo networkInfo) {
        defaultNetwork.postValue(networkInfo);
        fetchTokens();
    }

    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    public MutableLiveData<Wallet> wallet() {
        return wallet;
    }

    public LiveData<Token[]> tokens() {
        return tokens;
    }

    public void fetchTokens() {
        getProgress().postValue(true);
        if (defaultNetwork.getValue() == null) {
            findDefaultNetwork();
        }
        Log.d("aaron","fetchTokens address:"+wallet.getValue().getAddress());

        setDisposable(fetchTokensInteract
                .fetch(wallet.getValue())
                .subscribe(this::onTokens, this::onError));
    }

    private void onTokens(Token[] tokens) {
        getProgress().postValue(false);
        this.tokens.postValue(tokens);
    }

    public void showAddToken(Context context) {
        addTokenRouter.open(context);
    }

    public void showSendToken(Context context, String address, String symbol, int decimals) {
        sendTokenRouter.open(context, address, symbol, decimals);

    }

    public void showTransactions(Context context, boolean isClearStack) {
        mainRouter.open(context, isClearStack);
    }
}
