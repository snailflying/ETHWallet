package com.wallet.crypto.trustapp.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import com.wallet.crypto.trustapp.R
import kotlinx.android.synthetic.main.activity_wallet_gen.*

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 08/03/2018 18:35
 * @description
 */
class WalletGenActivity : BaseActivity() {

    private var privateKeyProvided: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_gen)

        email_sign_in_button.setOnClickListener { genCheck() }

        if (intent.hasExtra("PRIVATE_KEY")) {
            privateKeyProvided = intent.getStringExtra("PRIVATE_KEY")
            walletGenText!!.text = resources.getText(R.string.import_text)
            email_sign_in_button.setText(R.string.import_button)
        }
    }

    private fun genCheck() {
        if (passwordConfirm!!.text.toString() != password!!.text.toString()) {
            snackError(resources.getString(R.string.error_incorrect_password))
            return
        }
        if (!isPasswordValid(passwordConfirm!!.text.toString())) {
            snackError(resources.getString(R.string.error_invalid_password))
            return
        }
        writeDownPassword(this)
    }

    fun gen() {
//        Settings.walletBeingGenerated = true // Lock so a user can only generate one wallet at a time

        val data = Intent()
        data.putExtra("PASSWORD", passwordConfirm!!.text.toString())
        if (privateKeyProvided != null)
            data.putExtra("PRIVATE_KEY", privateKeyProvided)
        setResult(RESULT_OK, data)
        finish()
    }


    fun snackError(s: String) {
        if (main_content == null) return
        val mySnackbar = Snackbar.make(main_content!!, s, Snackbar.LENGTH_SHORT)
        mySnackbar.show()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 9
    }


    fun writeDownPassword(c: WalletGenActivity) {
        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= 24)
        // Otherwise buttons on 7.0+ are nearly invisible
            builder = AlertDialog.Builder(c, R.style.AlertDialogTheme)
        else
            builder = AlertDialog.Builder(c)
        builder.setTitle(R.string.dialog_write_down_pw_title)
        builder.setMessage(c.getString(R.string.dialog_write_down_pw_text))
        builder.setPositiveButton(R.string.action_sign_in, DialogInterface.OnClickListener { dialog, which ->
            c.gen()
            dialog.cancel()
        })
        builder.setNegativeButton(R.string.dialog_back_button, DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

    companion object {

        val REQUEST_CODE = 401
    }

}