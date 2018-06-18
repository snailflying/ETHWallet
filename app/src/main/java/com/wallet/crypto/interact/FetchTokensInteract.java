package com.wallet.crypto.interact;

import android.util.Log;

import com.wallet.crypto.entity.Token;
import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.repository.TokenRepository;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FetchTokensInteract {

    private final TokenRepository tokenRepository;

    public FetchTokensInteract(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Observable<Token[]> fetch(Wallet wallet) {
        Log.d("aaron","FetchTokensInteract address:"+wallet.getAddress());

        return tokenRepository.fetch(wallet.getAddress())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
