package com.wallet.crypto.trustapp.interact;

import com.wallet.crypto.trustapp.entity.Wallet;
import com.wallet.crypto.trustapp.repository.EthereumNetworkRepository;
import com.wallet.crypto.trustapp.repository.WalletRepository;
import com.wallet.crypto.trustapp.util.BalanceUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.wallet.crypto.trustapp.TrustConstants.USD_SYMBOL;
import static com.wallet.crypto.trustapp.util.BalanceUtils.weiToEth;

public class GetDefaultWalletBalance {

    private final WalletRepository walletRepository;
    private final EthereumNetworkRepository ethereumNetworkRepository;

    public GetDefaultWalletBalance(
            WalletRepository walletRepository,
            EthereumNetworkRepository ethereumNetworkRepository) {
        this.walletRepository = walletRepository;
        this.ethereumNetworkRepository = ethereumNetworkRepository;
    }

    public Single<Map<String, String>> get(Wallet wallet) {
        return walletRepository.balanceInWei(wallet)
                .flatMap(ethBallance -> {
                    Map<String, String> balances = new HashMap<>();
                    balances.put(ethereumNetworkRepository.getDefaultNetwork().symbol, weiToEth(ethBallance, 5));
                    return Single.just(balances);
                })
                .flatMap(balances -> ethereumNetworkRepository
                        .getTicker()
                        .observeOn(Schedulers.io())
                        .flatMap(ticker -> {
                            String ethBallance = balances.get(ethereumNetworkRepository.getDefaultNetwork().symbol);
                            balances.put(USD_SYMBOL, BalanceUtils.ethToUsd(ticker.price, ethBallance));
                            return Single.just(balances);
                        })
                        .onErrorResumeNext(throwable -> Single.just(balances)))
                .observeOn(AndroidSchedulers.mainThread());
    }


}