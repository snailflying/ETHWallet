package com.wallet.crypto.trustapp.ui.dialog

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.wallet.crypto.trustapp.R
import java.io.Serializable


/**
 * @Description 如果想在dialog展示前取消之前的dialog。则不要调用setTag，参考@link{BaseDialogFragment.showAllowingStateLoss}
 * @Author Sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 09/05/2018 2:35 PM
 * @Version
 */
open class DialogBuilder(val context: FragmentActivity, private val mClass: Class<out BaseDialogFragment>) {

    private var mTag = DEFAULT_TAG
    var mRequestCode = DEFAULT_REQUEST_CODE
    private var mTargetFragment: Fragment? = null
    private var mCancelable = true
    private var mCancelableOnTouchOutside = true   //默认点击屏幕取消dialog
    private var mfullScreen = false   //默认全屏
    private var mTheme = R.style.DialogTheme   //主题
    private var mShowButtom: Boolean = false    //是否底部显示
    private var mDimAmount = 0.5f//灰度深浅
    private var mAnimStyle: Int = 0  //动画
    private var mScale = 0.88 //329.0 / 375, 占用屏幕宽度一定比例
    private var args: Bundle? = null
    private var mDismissPreDialog: Boolean = true

    open fun prepareArguments(): Bundle {
        if (args == null) {
            args = Bundle()
        }
        return args!!
    }

    fun putArgs(key: String, value: Serializable): DialogBuilder {
        prepareArguments().putSerializable(key, value)
        return this
    }

    fun setCancelable(cancelable: Boolean): DialogBuilder {
        mCancelable = cancelable
        return this
    }

    fun setCancelableOnTouchOutside(cancelable: Boolean): DialogBuilder {
        mCancelableOnTouchOutside = cancelable
        if (cancelable) {
            mCancelable = cancelable
        }
        return this
    }

    fun setFullScreen(fullScreen: Boolean): DialogBuilder {
        mfullScreen = fullScreen
        return this
    }

    fun setDimAmount(dimAmount: Float): DialogBuilder {
        mDimAmount = dimAmount
        return this
    }

    fun setScale(scale: Double): DialogBuilder {
        mScale = scale
        return this
    }

    fun setShowButtom(showButtom: Boolean): DialogBuilder {
        mShowButtom = showButtom
        return this
    }

    fun setAnimStyle(animStyle: Int): DialogBuilder {
        mAnimStyle = animStyle
        return this
    }

    fun setTargetFragment(fragment: Fragment, requestCode: Int): DialogBuilder {
        mTargetFragment = fragment
        mRequestCode = requestCode
        return this
    }

    fun setRequestCode(requestCode: Int): DialogBuilder {
        mRequestCode = requestCode
        return this
    }

    fun setTag(tag: String): DialogBuilder {
        mTag = tag
        return this
    }

    fun setTheme(theme: Int): DialogBuilder {
        mTheme = theme
        return this
    }

    /**
     * 如果设置了mTag则自动不会隐藏，否则可调用此方法不隐藏
     * @param dismissPreDialog Boolean
     * @return DialogBuilder
     */
    fun setDismissPreDialog(dismissPreDialog: Boolean): DialogBuilder {
        mDismissPreDialog = dismissPreDialog
        return this
    }

    private fun create(): BaseDialogFragment {
        val args = prepareArguments()
        val fragment = Fragment.instantiate(context, mClass.name, args) as BaseDialogFragment
        args.putBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE, mCancelableOnTouchOutside)
        //全屏
        args.putBoolean(ARG_FULL_SCREEN, mfullScreen)
        //显示底部
        args.putBoolean(ARG_SHOW_BUTTOM, mShowButtom)
        //设置主题
        args.putInt(ARG_USE_THEME, mTheme)
        //透明度
        args.putFloat(ARG_DIM_AMOUNT, mDimAmount)
        //动画
        args.putInt(ARG_ANIM_STYLE, mAnimStyle)
        //占用屏幕宽度一定比例
        args.putDouble(ARG_SCALE, mScale)

        if (mTargetFragment != null) {
            fragment.setTargetFragment(mTargetFragment, mRequestCode)
        } else {
            args.putInt(ARG_REQUEST_CODE, mRequestCode)
        }
        fragment.isCancelable = mCancelable
        return fragment
    }

    fun show(): BaseDialogFragment {
        val fragment = create()
        fragment.showWithDismissPreDialog(context.supportFragmentManager, mTag, mDismissPreDialog)
        return fragment
    }

    //IllegalStateException : Can not perform this action after onSaveInstanceState()报该异常的时候使用此show
    fun showAllowingStateLoss(): BaseDialogFragment {
        val fragment = create()
        fragment.showAllowingStateLoss(context.supportFragmentManager, mTag, mDismissPreDialog)
        return fragment
    }

    companion object {

        const val ARG_REQUEST_CODE = "request_code"
        const val ARG_FULL_SCREEN = "arg_full_screen"
        const val ARG_SHOW_BUTTOM = "arg_show_buttom"
        const val ARG_CANCELABLE_ON_TOUCH_OUTSIDE = "cancelable_oto"
        var ARG_USE_THEME = "arg_use_theme_type"
        var ARG_DIM_AMOUNT = "arg_dim_amount"
        var ARG_ANIM_STYLE = "arg_anim_style"
        var ARG_SCALE = "arg_scale"
        const val DEFAULT_TAG = "simple_dialog"
        const val DEFAULT_REQUEST_CODE = -42
    }
}
