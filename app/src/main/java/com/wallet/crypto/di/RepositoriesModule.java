package com.wallet.crypto.di;

import android.content.Context;

import com.google.gson.Gson;
import com.wallet.crypto.repository.EthereumNetworkRepository;
import com.wallet.crypto.repository.SharedPreferenceRepository;
import com.wallet.crypto.repository.RealmTokenSource;
import com.wallet.crypto.repository.SharedPreferenceRepository;
import com.wallet.crypto.repository.RealmTokenSource;
import com.wallet.crypto.repository.TokenRepository;
import com.wallet.crypto.repository.TokenRepository;
import com.wallet.crypto.repository.TransactionInMemorySource;
import com.wallet.crypto.repository.TransactionLocalSource;
import com.wallet.crypto.repository.TransactionRepository;
import com.wallet.crypto.repository.TransactionRepository;
import com.wallet.crypto.repository.WalletRepository;
import com.wallet.crypto.repository.WalletRepository;
import com.wallet.crypto.service.BlockExplorerClient;
import com.wallet.crypto.service.EthplorerTokenService;
import com.wallet.crypto.service.GethKeystoreAccountService;
import com.wallet.crypto.service.TickerService;
import com.wallet.crypto.service.TrustWalletTickerService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class RepositoriesModule {
    @Singleton
    @Provides
    SharedPreferenceRepository providePreferenceRepository(Context context) {
        return new SharedPreferenceRepository(context);
    }

    @Singleton
    @Provides
    GethKeystoreAccountService provideAccountKeyStoreService(Context context) {
//        File file = new File(context.getFilesDir(), "keystore/keystore");
        return new GethKeystoreAccountService();
    }

    @Singleton
    @Provides
    TickerService provideTickerService(OkHttpClient httpClient, Gson gson) {
        return new TrustWalletTickerService(httpClient, gson);
    }

    @Singleton
    @Provides
    EthereumNetworkRepository provideEthereumNetworkRepository(
            SharedPreferenceRepository preferenceRepository,
            TickerService tickerService) {
        return new EthereumNetworkRepository(preferenceRepository, tickerService);
    }

    @Singleton
    @Provides
    WalletRepository provideWalletRepository(
            OkHttpClient okHttpClient,
            SharedPreferenceRepository preferenceRepositoryType,
            GethKeystoreAccountService accountKeystoreService,
            EthereumNetworkRepository networkRepository) {
        return new WalletRepository(
                okHttpClient, preferenceRepositoryType, accountKeystoreService, networkRepository);
    }

    @Singleton
    @Provides
    TransactionRepository provideTransactionRepository(
            EthereumNetworkRepository networkRepository,
            GethKeystoreAccountService accountKeystoreService,
            BlockExplorerClient blockExplorerClient) {
        TransactionLocalSource inMemoryCache = new TransactionInMemorySource();
        TransactionLocalSource inDiskCache = null;
        return new TransactionRepository(
                networkRepository,
                accountKeystoreService,
                inMemoryCache,
                inDiskCache,
                blockExplorerClient);
    }

    @Singleton
    @Provides
    BlockExplorerClient provideBlockExplorerClient(
            OkHttpClient httpClient,
            Gson gson,
            EthereumNetworkRepository ethereumNetworkRepository) {
        return new BlockExplorerClient(httpClient, gson, ethereumNetworkRepository);
    }

    @Singleton
    @Provides
    TokenRepository provideTokenRepository(
            OkHttpClient okHttpClient,
            EthereumNetworkRepository ethereumNetworkRepository,
            EthplorerTokenService tokenExplorerClientType,
            RealmTokenSource tokenLocalSource) {
        return new TokenRepository(
                okHttpClient,
                ethereumNetworkRepository,
                tokenExplorerClientType,
                tokenLocalSource);
    }

    @Singleton
    @Provides
    EthplorerTokenService provideTokenService(OkHttpClient okHttpClient, Gson gson) {
        return new EthplorerTokenService(okHttpClient, gson);
    }

    @Singleton
    @Provides
    RealmTokenSource provideRealmTokenSource() {
        return new RealmTokenSource();
    }
}
