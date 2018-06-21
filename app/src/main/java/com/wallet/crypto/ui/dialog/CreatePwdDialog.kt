package com.wallet.crypto.ui.dialog

import android.view.LayoutInflater
import com.wallet.crypto.R

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 21/06/2018 3:04 PM
 * @Version
 */
class CreatePwdDialog :BaseDialogFragment() {

    override fun build(initialBuilder: Builder): Builder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_backup, null)
        return initialBuilder.setView(view)
    }
}