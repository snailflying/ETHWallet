package com.wallet.crypto.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.ProgressBar
import com.wallet.crypto.R
import com.wallet.crypto.entity.ErrorEnvelope
import com.wallet.crypto.ext.click
import com.wallet.crypto.ext.setTextChangeListener
import com.wallet.crypto.repository.SharedPreferenceRepository
import com.wallet.crypto.viewmodel.ChangePasswordViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_change_pwd.*
import javax.inject.Inject

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 25/06/2018 2:08 PM
 * @Version
 */
class ChangePasswordActivity : BaseActivity() {

    @Inject
    @JvmField
    var sharedPreferenceRepository: SharedPreferenceRepository? = null
    lateinit var viewModel: ChangePasswordViewModel
    internal var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pwd)
        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java)
        old_password.setTextChangeListener { afterTextChanged { viewModel.oldPwd = it.toString() } }
        new_password.setTextChangeListener { afterTextChanged { viewModel.newPwd = it.toString() } }
        viewModel.progress().observe(this, Observer<Boolean> { this.onProgress(it!!) })
        viewModel.error().observe(this, Observer { this.onError(it) })
        btn_ok.click {
            viewModel.change(sharedPreferenceRepository) { finish() }
        }
    }

    private fun onError(error: ErrorEnvelope?) {
        hideDialog()
        dialog = AlertDialog.Builder(this)
                .setTitle(R.string.error_transaction_failed)
                .setMessage(error?.message)
                .setPositiveButton(R.string.button_ok, { dialog1, id ->
                    dialog1?.dismiss()
                }).create()
        dialog?.show()
    }

    private fun onProgress(shouldShowProgress: Boolean) {
        hideDialog()
        if (shouldShowProgress) {
            dialog = AlertDialog.Builder(this)
                    .setTitle(R.string.title_dialog_sending)
                    .setView(ProgressBar(this))
                    .setCancelable(false)
                    .create()
            dialog?.show()
        }
    }

    private fun hideDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog?.dismiss()
        }
    }
}