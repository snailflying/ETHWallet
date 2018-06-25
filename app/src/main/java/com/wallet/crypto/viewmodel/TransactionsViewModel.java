package com.wallet.crypto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.Uri;

import com.wallet.crypto.entity.NetworkInfo;
import com.wallet.crypto.entity.Transaction;
import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.interact.FetchTransactionsInteract;
import com.wallet.crypto.interact.FindDefaultNetworkInteract;
import com.wallet.crypto.interact.FindDefaultWalletInteract;
import com.wallet.crypto.interact.GetDefaultWalletBalance;
import com.wallet.crypto.router.ExternalBrowserRouter;
import com.wallet.crypto.router.ManageWalletsRouter;
import com.wallet.crypto.router.MyAddressRouter;
import com.wallet.crypto.router.MyTokensRouter;
import com.wallet.crypto.router.SendRouter;
import com.wallet.crypto.router.SettingsRouter;
import com.wallet.crypto.router.TransactionDetailRouter;

import java.util.Map;

import io.reactivex.disposables.Disposable;

public class TransactionsViewModel extends BaseViewModel {
    private static final long GET_BALANCE_INTERVAL = 8;
    private static final long FETCH_TRANSACTIONS_INTERVAL = 10;
    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<Transaction[]> transactions = new MutableLiveData<>();
    private final MutableLiveData<Map<String, String>> defaultWalletBalance = new MutableLiveData<>();

    private final FindDefaultNetworkInteract findDefaultNetworkInteract;
    private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final GetDefaultWalletBalance getDefaultWalletBalance;
    private final FetchTransactionsInteract fetchTransactionsInteract;

    private final ManageWalletsRouter manageWalletsRouter;
    private final SettingsRouter settingsRouter;
    private final SendRouter sendRouter;
    private final TransactionDetailRouter transactionDetailRouter;
    private final MyAddressRouter myAddressRouter;
    private final MyTokensRouter myTokensRouter;
    private final ExternalBrowserRouter externalBrowserRouter;
    private Disposable balanceDisposable;
    private Disposable transactionDisposable;

    TransactionsViewModel(
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            FetchTransactionsInteract fetchTransactionsInteract,
            GetDefaultWalletBalance getDefaultWalletBalance,
            ManageWalletsRouter manageWalletsRouter,
            SettingsRouter settingsRouter,
            SendRouter sendRouter,
            TransactionDetailRouter transactionDetailRouter,
            MyAddressRouter myAddressRouter,
            MyTokensRouter myTokensRouter,
            ExternalBrowserRouter externalBrowserRouter) {
        this.findDefaultNetworkInteract = findDefaultNetworkInteract;
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.getDefaultWalletBalance = getDefaultWalletBalance;
        this.fetchTransactionsInteract = fetchTransactionsInteract;
        this.manageWalletsRouter = manageWalletsRouter;
        this.settingsRouter = settingsRouter;
        this.sendRouter = sendRouter;
        this.transactionDetailRouter = transactionDetailRouter;
        this.myAddressRouter = myAddressRouter;
        this.myTokensRouter = myTokensRouter;
        this.externalBrowserRouter = externalBrowserRouter;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        transactionDisposable.dispose();
        balanceDisposable.dispose();
    }

    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<Transaction[]> transactions() {
        return transactions;
    }

    public LiveData<Map<String, String>> defaultWalletBalance() {
        return defaultWalletBalance;
    }

    public void prepare() {
        getProgress().postValue(true);
        setDisposable(findDefaultNetworkInteract
                .find()
                .subscribe(this::onDefaultNetwork, this::onError));
    }

    public void fetchTransactions() {
        getProgress().postValue(true);
        setDisposable(fetchTransactionsInteract
                .fetch(defaultWallet.getValue()/*new Wallet("0x60f7a1cbc59470b74b1df20b133700ec381f15d3")*/)
                .subscribe(this::onTransactions, this::onError));
    }

    public void getBalance() {
        getDefaultWalletBalance
                .get(defaultWallet.getValue())
                .subscribe(defaultWalletBalance::postValue, t -> {});
    }

    private void onDefaultNetwork(NetworkInfo networkInfo) {
        defaultNetwork.postValue(networkInfo);
        setDisposable(findDefaultWalletInteract
                .find()
                .subscribe(this::onDefaultWallet, this::onError));
    }

    private void onDefaultWallet(Wallet wallet) {
        defaultWallet.setValue(wallet);
        getBalance();
        fetchTransactions();
    }

    private void onTransactions(Transaction[] transactions) {
        getProgress().postValue(false);
        this.transactions.postValue(transactions);
    }

    public void showWallets(Context context) {
        manageWalletsRouter.open(context, false);
    }

    public void showSettings(Context context) {
        settingsRouter.open(context);
    }

    public void showSend(Context context) { sendRouter.open(context); }

    public void showDetails(Context context, Transaction transaction) {
        transactionDetailRouter.open(context, transaction);
    }

    public void showMyAddress(Context context) {
        myAddressRouter.open(context, defaultWallet.getValue());
    }

    public void showTokens(Context context) {
        myTokensRouter.open(context, defaultWallet.getValue());
    }

    public void openDeposit(Context context, Uri uri) {
        externalBrowserRouter.open(context, uri);
    }
}
