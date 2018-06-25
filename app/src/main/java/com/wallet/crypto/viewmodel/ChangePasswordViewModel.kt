package com.wallet.crypto.viewmodel

import com.wallet.crypto.entity.Wallet
import com.wallet.crypto.ext.toast
import com.wallet.crypto.repository.SharedPreferenceRepository
import com.wallet.crypto.util.WalletStorage
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 25/06/2018 2:37 PM
 * @Version
 */
class ChangePasswordViewModel : BaseViewModel() {

    var newPwd: String? = null
    var oldPwd: String? = null

    fun change(sp: SharedPreferenceRepository?, block: () -> Unit) {
        if (oldPwd.isNullOrEmpty()) {
            toast("请输入原始密码").show()
            return
        }
        if (newPwd.isNullOrEmpty()) {
            toast("请输入新密码").show()
            return
        }
        progress.postValue(true)
        Completable.fromCallable {
            val account = WalletStorage.getInstance().modifyPassword(sp!!.currentWalletAddress, oldPwd!!, newPwd!!)
            Wallet(account.address.toLowerCase())
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    toast("修改成功").show()
                    progress.value = false
                    block()
                }, this::onError)
    }
}