package com.wallet.crypto.ui.dialog

import android.view.LayoutInflater
import cn.magicwindow.core.config.Config
import com.wallet.crypto.R
import com.wallet.crypto.ext.click
import kotlinx.android.synthetic.main.layout_dialog_export_wallets.view.*

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 21/06/2018 3:04 PM
 * @Version
 */
class ExportWalletsDialog : BaseDialogFragment() {

    override fun build(initialBuilder: Builder): Builder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_export_wallets, null)
        view.keystore.click {
            positiveListeners?.onPositiveButtonClicked(Config.DIALOG_EXPORT_KEYSTORE_CODE)
            dismiss()
        }
        view.privatekey.click {
            positiveListeners?.onPositiveButtonClicked(Config.DIALOG_EXPORT_PRIVATEKEY_CODE)
            dismiss()
        }
        view.cancel.click { dismiss() }
        return initialBuilder.setView(view)
    }
}