package com.wallet.crypto.trustapp.ext

import android.view.View


/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Description 点击事件扩展
 */


/***
 * 设置延迟时间的View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @return T
 */
fun <T : View> T.withTrigger(delay: Long = 600): T {
    triggerDelay = delay
    return this
}

/***
 * 点击事件的View扩展
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {

    if (clickEnable()) {
        block(it as T)
    }
}

/***
 * 带延迟过滤的点击事件View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.clickWithTrigger(time: Long = 600, block: (T) -> Unit) {
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            block(it as T)
        }
    }
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else -1
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}