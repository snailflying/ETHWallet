package com.wallet.crypto.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.text.TextUtils
import com.wallet.crypto.App
import java.io.File

/**
 * @Author Aaron
 * @Date 2018/5/18
 * @Email aaron@magicwindow.cn
 * @Description
 */
object FileUtils {
    // 钱包文件外置存储目录
    val WALLET_DIR: String = generaFilePath(getExternalDir(), "wallet")

    fun checkPermission(context: Context, permission: String): Boolean {

        val localPackageManager = context.applicationContext.packageManager
        return localPackageManager.checkPermission(permission, context.applicationContext.packageName) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasExternalMediaMounted(context: Context): Boolean {
        return ((Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable())
                && checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE"))
    }


    private fun generaFilePath(path: String, childFir: String): String {
        val generaFile = File(path, childFir)
        if (!generaFile.exists()) {
            generaFile.mkdir()
        }
        return generaFile.path
    }

    fun getDiskCacheDir(context: Context = App.context): String {
        var cacheDirectory: File
        if (hasExternalMediaMounted(context) && context.externalCacheDir != null) {
            cacheDirectory = context.externalCacheDir
        } else {
            cacheDirectory = context.cacheDir
        }
        if (cacheDirectory == null) {
            cacheDirectory = context.cacheDir
        }
        return cacheDirectory.path
    }

    /**
     * 这种目录下的文件在应用被卸载时不会被删除
     * 钱包等数据可以存放到这里
     *
     * @return
     */
    fun getExternalDir(context: Context = App.context): String {
        return if (hasExternalMediaMounted(context)) {
            context.filesDir.path
//            Environment.getExternalStorageDirectory().path
        } else {
            getDiskCacheDir(context)
        }
    }

    /**
     * 删除该目录下的文件
     *
     * @param path
     */
    fun delFile(path: String) {
        if (!TextUtils.isEmpty(path)) {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
    }
}