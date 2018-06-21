package com.wallet.crypto.ext

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText


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

fun EditText.setTextChangeListener(init: MyTextWatcher.() -> Unit) {
    val textWatcher = MyTextWatcher()
    textWatcher.init()
    addTextChangedListener(textWatcher)
}

class MyTextWatcher : TextWatcher {

    private var onTextChanged: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null
    private var beforeTextChanged: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null
    private var afterTextChanged: ((s: Editable?) -> Unit)? = null

    fun beforeTextChanged(listener: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit) {
        beforeTextChanged = listener
    }

    fun afterTextChanged(listener: (s: Editable?) -> Unit) {
        afterTextChanged = listener
    }

    fun onTextChanged(listener: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit) {
        onTextChanged = listener
    }

    override fun afterTextChanged(s: Editable?) {
        afterTextChanged?.let { it(s) }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChanged?.let { it(s, start, count, after) }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged?.let { it(s, start, before, count) }
    }
}