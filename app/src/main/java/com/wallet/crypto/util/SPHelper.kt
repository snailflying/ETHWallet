package com.wallet.crypto.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.wallet.crypto.App
import java.util.*


/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 14/12/25 7:49 PM
 * @description
 */
class SPHelper private constructor(private val context: Context?) {
    private val SP_KEY_DEFAULT = "persistent_data"
    private val TAG = "SPHelper"

    private val sp: SharedPreferences?
        get() {
            if (context == null) {
                return null
            }

            try {
                return context.getSharedPreferences(SP_KEY_DEFAULT, preferenceMode)
            } catch (ignored: OutOfMemoryError) {

            }

            return null
        }


    init {
        preferenceMode = Context.MODE_MULTI_PROCESS

    }

    fun putBoolean(key: String, value: Boolean) {
        sp?.let {
            val editor = it.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        var value = defaultValue
        sp?.let {
            value = it.getBoolean(key, defaultValue)

        }
        return value
    }

    fun getBoolean(key: String): Boolean {
        var value = false
        sp?.let {
            value = it.getBoolean(key, false)

        }
        return value
    }

    fun putInt(key: String, value: Int) {
        sp?.let {
            val editor = it.edit()
            editor.putInt(key, value)
            editor.apply()
        }
    }

    fun getInt(key: String, defaultValue: Int): Int {
        var value = defaultValue
        sp?.let {
            value = it.getInt(key, defaultValue)

        }
        return value
    }

    fun getInt(key: String): Int {
        var value = 0
        sp?.let {
            value = it.getInt(key, 0)
        }
        return value
    }

    /*fun put(key: String,value:Any?){
        if (sp == null) {
            return
        }
        val editor = sp!!.edit()
        with(editor){
            when (value){
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Boolean -> putBoolean(key, value)
                is Set<*> -> when{
                    value.toTypedArray().isArrayOf<String>() ->
                        putSet(key, value as Set<String>)
                }
                else -> apply()

            }
            apply()
        }

    }*/


    fun put(key: String, value: String?) {
        sp?.let {
            val editor = it.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }

    fun remove(key: String) {
        sp?.let {
            val editor = it.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    operator fun get(key: String, defaultValue: String): String {
        var value: String = defaultValue
        sp?.let {
            value = it.getString(key, defaultValue)
        }
        return value
    }

    operator fun get(key: String): String {
        var value = ""
        sp?.let {
            value = it.getString(key, "")
        }
        return value
    }

    fun putString(key: String, value: String?) {
        sp?.let {
            val editor = it.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }

    fun getSet(key: String): Set<String> {
        var value: Set<String> = HashSet()
        sp?.let {
            value = it.getStringSet(key, value)
        }
        return value
    }

    fun getSet(key: String, defaultValue: Set<String>): Set<String> {
        var value: Set<String> = defaultValue
        sp?.let {
            value = it.getStringSet(key, defaultValue)
        }
        return value
    }

    fun putSet(key: String, set: Set<String>) {
        sp?.let {
            val editor = it.edit()
            editor.putStringSet(key, set)
            editor.apply()
        }
    }

    fun addSet(key: String, setValue: String) {
        sp?.let { sp_ ->
            val editor = sp_.edit()
            val set = sp_.getStringSet(key, HashSet())
            set.add(setValue)
            editor.putStringSet(key, set)
            editor.apply()
        }
    }

    fun getString(key: String, defaultValue: String): String {
        var value: String = defaultValue
        sp?.let {
            value = it.getString(key, defaultValue)
        }
        return value
    }

    fun getString(key: String): String {
        var value: String = ""
        sp?.let {
            value = it.getString(key, "")
        }
        return value
    }

    fun putLong(key: String, value: Long) {
        sp?.let {
            val editor = it.edit()
            editor.putLong(key, value)
            editor.apply()
        }
    }

    fun getLong(key: String): Long {
        var value = 0L
        sp?.let {
            value = it.getLong(key, 0L)

        }
        return value
    }

    fun getLong(key: String, defaultValue: Long): Long {
        var value = defaultValue
        sp?.let {
            value = it.getLong(key, defaultValue)
        }
        return value
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var defaultInstance: SPHelper? = null
        private var preferenceMode = Context.MODE_PRIVATE

        val TRACKING_DEVICE_ID = "device_id"         //device_id

        fun create(context: Context = App.context): SPHelper {
            if (defaultInstance == null) {
                synchronized(SPHelper::class.java) {
                    if (defaultInstance == null) {
                        defaultInstance = SPHelper(context.applicationContext)
                    }
                }
            }
            return defaultInstance!!
        }
    }
}
