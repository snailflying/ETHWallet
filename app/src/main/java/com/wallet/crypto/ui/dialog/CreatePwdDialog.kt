package com.wallet.crypto.ui.dialog

import android.view.LayoutInflater
import cn.magicwindow.core.config.Config
import com.wallet.crypto.R
import com.wallet.crypto.ext.click
import com.wallet.crypto.ext.setTextChangeListener
import com.wallet.crypto.ext.toast
import com.wallet.crypto.util.KeyboardUtils
import kotlinx.android.synthetic.main.layout_dialog_create_pwd.*
import kotlinx.android.synthetic.main.layout_dialog_create_pwd.view.*

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 21/06/2018 3:04 PM
 * @Version
 */
class CreatePwdDialog : BaseDialogFragment() {

    var text = ""
    override fun build(initialBuilder: Builder): Builder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_create_pwd, null)
        view.password.setTextChangeListener {
            beforeTextChanged { s, start, before, count -> }
            onTextChanged { s, start, before, count -> }
            afterTextChanged { text = it.toString() }
        }
        view.btn_cancel.click { dismiss() }
        view.btn_ok.click {
            if (text.isEmpty()) return@click toast("请输入密码").show()
            positiveListeners?.onPositiveButtonClicked(Config.DIALOG_CREATE_PWD_REQUEST_CODE, text)
            KeyboardUtils.hideKeyboard(password)
            dismiss()
        }
        view.postDelayed({ KeyboardUtils.showKeyboard(password) }, 500)
        return initialBuilder.setView(view)
    }
}