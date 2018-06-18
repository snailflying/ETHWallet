package com.wallet.crypto.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context

import com.wallet.crypto.interact.AddTokenInteract
import com.wallet.crypto.interact.FindDefaultWalletInteract
import com.wallet.crypto.router.MyTokensRouter
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

class AddTokenViewModel internal constructor(
        private val addTokenInteract: AddTokenInteract,
        private val findDefaultWalletInteract: FindDefaultWalletInteract,
        private val myTokensRouter: MyTokensRouter) : BaseViewModel() {

    private val result = MutableLiveData<Boolean>()

    fun save(address: String, symbol: String, decimals: Int) {
        addTokenInteract
                .add(address, symbol, decimals)
                .subscribe({ this.onSaved() }, { this.onError(it) })
    }

    private fun onSaved() {
        progress.postValue(false)
        result.postValue(true)
    }

    fun result(): LiveData<Boolean> {
        return result
    }

    fun showTokens(context: Context) {
        findDefaultWalletInteract
                .find()
                .subscribe { w -> myTokensRouter.open(context, w) }

    }
}
