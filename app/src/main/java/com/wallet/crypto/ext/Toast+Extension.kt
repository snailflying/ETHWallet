package com.wallet.crypto.ext

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.wallet.crypto.App
import com.wallet.crypto.R


/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 2018/4/10 12:50
 * @description
 */


/**
 * 向Toast中添加自定义view
 * @param view
 * @param postion
 * @return
 */
fun Toast.addView(addView: View, position: Int): Toast {

    (view as LinearLayout).addView(addView, position)

    return this
}

fun Toast.addView(block: () -> View, position: Int): Toast {

    (view as LinearLayout).addView(block(), position)

    return this
}

fun Toast.setGravityCenter(): Toast {
    setGravity(Gravity.CENTER, 0, 0)
    return this
}

/**
 * 设置Toast字体及背景颜色
 * @param messageColor
 * @param backgroundColor
 * @return
 */
fun Toast.setToastColor(@ColorInt messageColor: Int, @ColorInt backgroundColor: Int) {
    val view = view
    if (view != null) {
        val message = view.findViewById(android.R.id.message) as TextView
        message.setBackgroundColor(backgroundColor)
        message.setTextColor(messageColor)
    }
}

/**
 * 设置Toast字体及背景
 * @param messageColor
 * @param background
 * @return
 */
fun Toast.setBackground(@ColorInt messageColor: Int = Color.WHITE, @DrawableRes background: Int = R.drawable.shape_toast_bg): Toast {
    val view = view
    if (view != null) {
        val message = view.findViewById(android.R.id.message) as TextView
        view.setBackgroundResource(background)
        message.setTextColor(messageColor)
    }
    return this
}

@SuppressLint("ShowToast")
fun toast(text: CharSequence): Toast = Toast.makeText(App.context, text, Toast.LENGTH_LONG)
        .setGravityCenter()
        .setBackground()


@SuppressLint("ShowToast")
fun toast(@StringRes res: Int): Toast = Toast.makeText(App.context, App.context.resources.getString(res), Toast.LENGTH_LONG)
        .setGravityCenter()
        .setBackground()

@SuppressLint("ShowToast")
fun toastSuccess(text: CharSequence): Toast = Toast.makeText(App.context, text, Toast.LENGTH_LONG)
        .setGravityCenter()
        .setBackground()


@SuppressLint("ShowToast")
fun toastSuccess(@StringRes res: Int): Toast = Toast.makeText(App.context, App.context.resources.getString(res), Toast.LENGTH_LONG)
        .setGravityCenter()
        .setBackground()

fun Toast.withIcon(@DrawableRes iconRes: Int = R.drawable.ic_toast_error): Toast {
    val view = view
    if (view != null) {
        val layout = this.view as LinearLayout

        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val icon = ImageView(App.context)
        icon.setImageResource(iconRes)
        icon.setPadding(dip2px(20f), dip2px(20f), dip2px(20f), dip2px(10f))
        icon.layoutParams = layoutParams
        layout.gravity = Gravity.CENTER
        layout.addView(icon, 0)

        val message = this.view.findViewById(android.R.id.message) as TextView
        message.setPadding(dip2px(5f), 0, dip2px(5f), dip2px(20f))
    }

    return this
}

/**
 * dp to px
 */
fun dip2px(dpValue: Float): Int {
    val scale = App.context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * px to dp
 */
fun px2dip(pxValue: Float): Int {
    val scale = App.context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}