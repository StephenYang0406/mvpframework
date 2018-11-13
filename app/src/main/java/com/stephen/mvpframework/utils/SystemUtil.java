package com.stephen.mvpframework.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.stephen.mvpframework.context.ContextHandler;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 系统相关工具类
 * Created by Stephen on 2018/1/10.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class SystemUtil {
    //直接拨打电话
    public static void callPhone(String phoneNumber) {
        PermissionUtil.requestPermission(new PermissionUtil.OnPermissionListener() {
            @Override
            public void onSuccess() {
                callPhoneIgnorePermission(phoneNumber);
            }

            @Override
            public void onFailed() {

            }
        }, Permission.CALL_PHONE);
    }

    //跳转拨号界面,可以传递号码
    public static void skipToPhoneButton(@Nullable String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber))
            ContextHandler.currentActivity().startActivity(new Intent(Intent.ACTION_CALL_BUTTON));
        else
            ContextHandler.currentActivity().startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }


    //拨打电话,已经判断过权限
    @SuppressLint("MissingPermission")
    private static void callPhoneIgnorePermission(String phoneNumber) {
        ContextHandler.currentActivity().startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
    }

    //复制文本到粘贴板
    public static void copyText(String copyText) {
        ClipboardManager clipboardManager = (ClipboardManager) ContextHandler.currentActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, copyText));
        }
    }

    //检查SD卡是否可用
    public static boolean existSDCard() {
        String externalStorageState = Environment.getExternalStorageState();
        return externalStorageState.equals(Environment.MEDIA_MOUNTED);
    }

    //获取版本号
    public static int getVersionCode() {
        int versionCode = 0;
        try {
            versionCode = ContextHandler.currentActivity().
                    getPackageManager()
                    .getPackageInfo(ContextHandler.currentActivity().getPackageName(), 0)
                    .versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    //获取版本名称
    public static String getVersionName() {
        String versionName = "";
        try {
            versionName = ContextHandler.currentActivity().
                    getPackageManager()
                    .getPackageInfo(ContextHandler.currentActivity().getPackageName(), 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    //安装APK
    public static void installApk(String apkFilePath) {
        LogUtil.testInfo("开始安装APK----->" + apkFilePath);
        File apkFile = new File(apkFilePath);
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkUri = FileProvider.getUriForFile(ContextHandler.currentActivity(),
                    ContextHandler.currentActivity().getPackageName() + ".provider"
                    , apkFile);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            apkUri = Uri.fromFile(apkFile);
        }
        installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        ContextHandler.currentActivity().startActivity(installIntent);
    }

    //检测MIUI
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    //检查手机是否是MIUI，小米吃屎又司马
    public static boolean isMIUI() {
        String device = Build.MANUFACTURER;
        System.out.println("Build.MANUFACTURER = " + device);
        if (device.equals("Xiaomi")) {
            System.out.println("this is a xiaomi device");
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } else {
            return false;
        }
    }

    //检查MIUI权限，小米吃屎又司马
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkOpsPermission(Context context, String permission) {
        try {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            String opsName = AppOpsManager.permissionToOp(permission);
            if (opsName == null) {
                return true;
            }
            int opsMode = appOpsManager.checkOpNoThrow(opsName, android.os.Process.myUid(), context.getPackageName());
            return opsMode == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {
            return true;
        }
    }

}
