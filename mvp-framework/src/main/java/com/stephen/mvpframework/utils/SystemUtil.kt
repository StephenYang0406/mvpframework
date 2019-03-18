package com.stephen.mvpframework.utils

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import android.text.TextUtils
import com.stephen.mvpframework.handler.ContextHandler
import com.stephen.mvpframework.helper.PermissionHelper
import com.yanzhenjie.permission.Permission
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 * 系统工具类
 * Created by Stephen on 2018/11/16.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object SystemUtil {

    //直接拨打电话
    fun callPhone(phoneNumber: String) {
        PermissionHelper.requestPermission(PermissionHelper.Builder()
                .permission(arrayOf(Permission.CALL_PHONE))
                .success { callPhoneIgnorePermission(phoneNumber) })
    }

    //跳转拨号界面,可以传递号码
    fun skipToPhoneButton(phoneNumber: String?) {
        if (TextUtils.isEmpty(phoneNumber))
            ContextHandler.currentActivity().startActivity(Intent(Intent.ACTION_CALL_BUTTON))
        else
            ContextHandler.currentActivity().startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber!!)))
    }

    //拨打电话,已经判断过权限
    @SuppressLint("MissingPermission")
    private fun callPhoneIgnorePermission(phoneNumber: String) {
        ContextHandler.currentActivity().startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber")))
    }

    //复制文本到粘贴板
    fun copyText(copyText: String) {
        val clipboardManager = ContextHandler.currentActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.primaryClip = ClipData.newPlainText(null, copyText)
    }

    //获取版本号
    fun getVersionCode(): Long {
        var versionCode = 0L
        try {
            versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ContextHandler.currentActivity().packageManager
                        .getPackageInfo(ContextHandler.currentActivity().packageName, 0)
                        .longVersionCode
            } else {
                ContextHandler.currentActivity().packageManager
                        .getPackageInfo(ContextHandler.currentActivity().packageName, 0)
                        .versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    //获取版本名称
    fun getVersionName(): String {
        var versionName = ""
        try {
            versionName = ContextHandler.currentActivity().packageManager
                    .getPackageInfo(ContextHandler.currentActivity().packageName, 0)
                    .versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }

    //安装APK
    fun installApk(apkFilePath: String) {
        LogUtil.testInfo("开始安装APK----->$apkFilePath")
        val apkFile = File(apkFilePath)
        val installIntent = Intent(Intent.ACTION_VIEW)
        installIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val apkUri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkUri = FileProvider.getUriForFile(ContextHandler.currentActivity(),
                    ContextHandler.currentActivity().packageName + ".provider", apkFile)
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            apkUri = Uri.fromFile(apkFile)
        }
        installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        ContextHandler.currentActivity().startActivity(installIntent)
    }


    //检测MIUI
    private const val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"
    private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private const val KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage"

    //检查手机是否是MIUI，小米吃屎又司马
    fun isMIUi(): Boolean {
        val device = Build.MANUFACTURER
        println("Build.MANUFACTURER = $device")
        if (device == "Xiaomi") {
            println("this is a xiaomi device")
            val prop = Properties()
            try {
                prop.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }
            return (prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null)
        } else {
            return false
        }
    }

}