package com.wallet.crypto.interact;

import com.wallet.crypto.entity.NetworkInfo;
import com.wallet.crypto.repository.EthereumNetworkRepository;
import com.wallet.crypto.service.EthplorerTokenService;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class FindDefaultNetworkInteract {

    private final EthereumNetworkRepository ethereumNetworkRepository;

    public FindDefaultNetworkInteract(EthereumNetworkRepository ethereumNetworkRepository) {
        this.ethereumNetworkRepository = ethereumNetworkRepository;
    }

    public Single<NetworkInfo> find() {
        return Single.just(ethereumNetworkRepository.getDefaultNetwork())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
