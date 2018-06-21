package com.wallet.crypto.ui.dialog

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.wallet.crypto.R

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 21/06/2018 3:53 PM
 * @Version
 */
fun Fragment.showCusDialog(dialogFragment: BaseDialogFragment): BaseDialogFragment =
        DialogBuilder(activity!!, dialogFragment.javaClass)
                .setCancelableOnTouchOutside(false)
                .setTargetFragment(this, 10001)
                .setAnimStyle(R.style.AnimScaleCenter)
                .showAllowingStateLoss()


fun FragmentActivity.showCusDialog(dialogFragment: BaseDialogFragment): BaseDialogFragment =
        DialogBuilder(this, dialogFragment.javaClass)
                .setCancelableOnTouchOutside(false)
                .setAnimStyle(R.style.AnimScaleCenter)
                .showAllowingStateLoss()

fun FragmentActivity.dialogBuilder(dialogFragment: BaseDialogFragment): DialogBuilder =
        DialogBuilder(this, dialogFragment.javaClass)
                .setCancelableOnTouchOutside(false)