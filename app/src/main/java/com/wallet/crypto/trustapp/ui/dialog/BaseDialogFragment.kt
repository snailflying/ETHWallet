/*
 * Copyright 2013 Inmite s.r.o. (www.inmite.eu).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wallet.crypto.trustapp.ui.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import com.wallet.crypto.trustapp.R
import com.wallet.crypto.trustapp.ui.dialog.interfaces.INegativeButtonDialogListener
import com.wallet.crypto.trustapp.ui.dialog.interfaces.IPositiveButtonDialogListener
import com.wallet.crypto.trustapp.ui.dialog.interfaces.ISimpleDialogCancelListener
import io.reactivex.disposables.CompositeDisposable

/**
 * @Description baseDialogFragment
 * @Author lucio
 * @Email xiao.lu@magicwindow.cn
 * @Date 21/08/2017 6:26 PM
 * @Version 1.0.0
 */
abstract class BaseDialogFragment : DialogFragment() {

    protected var mRequestCode: Int = 0
    private var canceledOnTouchOutside: Boolean = false
    private var fullScreen: Boolean = false
    private var mTheme: Int = 0   //主题
    private var dimAmount: Float = 0.toFloat()//灰度深浅
    private var showBottom: Boolean = false//是否底部显示
    private var animStyle: Int = 0
    private var scale: Double = 0.toDouble()
    protected var compositeDisposable = CompositeDisposable()

    //获取关闭按钮回调
    protected val cancelListeners: ISimpleDialogCancelListener?
        get() = getDialogListeners(ISimpleDialogCancelListener::class.java)

    //获取Positive按钮回调
    protected val positiveListeners: IPositiveButtonDialogListener?
        get() = getDialogListeners(IPositiveButtonDialogListener::class.java)

    //获取Negative按钮回调
    protected val negativeListeners: INegativeButtonDialogListener?
        get() = getDialogListeners(INegativeButtonDialogListener::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
            canceledOnTouchOutside = args.getBoolean(DialogBuilder.ARG_CANCELABLE_ON_TOUCH_OUTSIDE)
            fullScreen = args.getBoolean(DialogBuilder.ARG_FULL_SCREEN)
            showBottom = args.getBoolean(DialogBuilder.ARG_SHOW_BUTTOM)
            mTheme = args.getInt(DialogBuilder.ARG_USE_THEME, R.style.DialogTheme)
            animStyle = args.getInt(DialogBuilder.ARG_ANIM_STYLE)
            dimAmount = args.getFloat(DialogBuilder.ARG_DIM_AMOUNT)
            scale = args.getDouble(DialogBuilder.ARG_SCALE)
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, mTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val builder = Builder(inflater, container)
        return build(builder).create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val targetFragment = targetFragment
        if (targetFragment != null) {
            mRequestCode = targetRequestCode
        } else {
            val args = arguments
            if (args != null) {
                mRequestCode = args.getInt(DialogBuilder.ARG_REQUEST_CODE, 0)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initParams()
    }

    private fun initParams() {
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
        val window = dialog.window
        if (window != null) {
            val lp = window.attributes
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount
            //是否在底部显示
            if (showBottom) {
                lp.gravity = Gravity.BOTTOM
                if (animStyle == 0) {
                    animStyle = R.style.AnimBottom
                }
            }
            //占用屏幕宽度一定比例
            if (scale != 1.0) {
                lp.width = (getScreenWidth(context!!) * scale).toInt()
            }
            if (fullScreen) {
                lp.width = getScreenWidth(context!!)
            }
            //设置dialog高度
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            //设置dialog进入、退出的动画
            window.setWindowAnimations(animStyle)
            window.attributes = lp
        }
    }

    override fun onDestroyView() {
        if (dialog != null && retainInstance) {
            dialog.setDismissMessage(null)
        }
        super.onDestroyView()
        compositeDisposable.clear() // 防止内存泄露
    }

    override fun onDismiss(dialog: DialogInterface?) {
        cancelListeners?.onCancelled(0)
        super.onDismiss(dialog)

    }


    fun showWithDismissPreDialog(manager: FragmentManager, tag: String, dismissPreDialog: Boolean) {
        val ft = manager.beginTransaction()
        //将之前的dialog隐藏
        val targetFragment = manager.findFragmentByTag(tag)
        if (dismissPreDialog && targetFragment != null && targetFragment is BaseDialogFragment) {
            ft.remove(targetFragment).commit()
        }
        show(manager, tag)
    }

    fun showAllowingStateLoss(manager: FragmentManager, tag: String, dismissPreDialog: Boolean) {
        val ft = manager.beginTransaction()
        //将之前的dialog隐藏
        val targetFragment = manager.findFragmentByTag(tag)
        if (dismissPreDialog && targetFragment != null && targetFragment is BaseDialogFragment) {
            ft.remove(targetFragment)
        }
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    fun <T> getDialogListeners(listenerInterface: Class<T>): T? {

        val targetFragment = targetFragment
        if (targetFragment != null && listenerInterface.isAssignableFrom(targetFragment.javaClass)) {
            return targetFragment as T
        }

        if (activity != null && listenerInterface.isAssignableFrom(activity!!.javaClass)) {
            return activity as T
        }
        return null
    }

    protected abstract fun build(initialBuilder: Builder): Builder

    protected class Builder(val layoutInflater: LayoutInflater, private val mContainer: ViewGroup?) {

        private var mCustomView: View? = null

        //必须调用
        fun setView(view: View): Builder {
            mCustomView = view
            return this
        }

        internal fun create(): View? {
            return mCustomView
        }
    }

    companion object {

        fun getScreenWidth(context: Context): Int {

            val dm = context.resources.displayMetrics
            return dm.widthPixels
        }

        fun getScreenHeight(context: Context): Int {

            val dm = context.resources.displayMetrics
            return dm.heightPixels
        }
    }
}