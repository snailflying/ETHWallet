package com.wallet.crypto.trustapp.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log

import com.wallet.crypto.trustapp.TrustConstants
import com.wallet.crypto.trustapp.entity.ErrorEnvelope
import com.wallet.crypto.trustapp.entity.ServiceException

import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    protected val error = MutableLiveData<ErrorEnvelope>()
    protected val progress = MutableLiveData<Boolean>()
    protected var disposable: Disposable? = null

    override fun onCleared() {
        cancel()
    }

    protected fun cancel() {
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }


    fun error(): LiveData<ErrorEnvelope> {
        return error
    }

    fun progress(): LiveData<Boolean> {
        return progress
    }

    protected fun onError(throwable: Throwable) {
        if (throwable is ServiceException) {
            error.postValue(throwable.error)
        } else {
            error.postValue(ErrorEnvelope(TrustConstants.ErrorCode.UNKNOWN, null, throwable))
            // TODO: Add dialog with offer send error log to developers: notify about error.
            Log.d("SESSION", "Err", throwable)
        }
    }
}
