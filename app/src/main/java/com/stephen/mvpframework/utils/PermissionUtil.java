package com.stephen.mvpframework.utils;


import com.stephen.mvpframework.context.ContextHandler;
import com.stephen.mvpframework.ui.activity.AbstractActivity;
import com.stephen.mvpframework.ui.fragment.AbstractFragment;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Request;
import com.yanzhenjie.permission.SettingService;

/**
 * 权限工具类
 * Created by Stephen on 2018/1/11.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class PermissionUtil {
    //申请权限
//    public static void requestPermission(int requestCode, final OnPermissionListener onPermissionListener, String... permissions) {
//        AndPermission.with(ContextHandler.currentActivity())
//                .requestCode(requestCode)
//                .callback(new PermissionListener() {
//                    @Override
//                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
//                        //适配,有的手机没有取得权限,回调了成功
//                        if (hasPermission(grantPermissions.toArray(new String[grantPermissions.size()]))) {
//                            //成功回调
//                            onPermissionListener.onSuccess();
//                        } else {
//                            for (String permission : grantPermissions) {
//                                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                        || permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                                    ToastUtil.showErrorToast("请在设置中开启SD卡储存权限,程序即将关闭");
//                                    System.exit(0);
//                                }
//                            }
//                            //是否有不再提示并拒绝的权限
//                            if (AndPermission.hasAlwaysDeniedPermission(ContextHandler.currentActivity(), grantPermissions)) {
//                                if (SystemUtil.isMIUI()) {//适配小米
//                                    ToastUtil.showErrorToast("请在设置中开启权限");
//                                } else
//                                    AndPermission.defaultSettingDialog(ContextHandler.currentActivity(),
//                                            Content.PermissionParams.ALWAYS_DENIED_REQUEST_CODE)
//                                            .show();
//                            }else{
//                                onPermissionListener.onFailed();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
//                        //适配,有的手机已经取得了权限,还是回调了失败
//                        if (hasPermission(deniedPermissions.toArray(new String[deniedPermissions.size()]))) {
//                            //成功回调
//                            onPermissionListener.onSuccess();
//                        } else {
//                            for (String permission : deniedPermissions) {
//                                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                        || permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                                    ToastUtil.showErrorToast("请在设置中开启SD卡储存权限,程序即将关闭");
//                                    System.exit(0);
//                                }
//                            }
//                            if (SystemUtil.isMIUI()) {//适配小米
//                                ToastUtil.showErrorToast("请在设置中开启权限");
//                            } else
//                                //是否有不再提示并拒绝的权限
//                                if (AndPermission.hasAlwaysDeniedPermission(ContextHandler.currentActivity(), deniedPermissions)) {
//                                    AndPermission.defaultSettingDialog(ContextHandler.currentActivity(),
//                                            Content.PermissionParams.ALWAYS_DENIED_REQUEST_CODE)
//                                            .show();
//                                }
//                        }
//                    }
//                })
//                .rationale((code, rationale) -> {
//                    // 拒绝一次后继续提醒
//                    AndPermission.rationaleDialog(ContextHandler.currentActivity(), rationale).show();
//                })
//                .permission(permissions)
//                .start();
//    }
//
//    //是否有此权限
//    public static boolean hasPermission(String... permissions) {
//        return AndPermission.hasPermission(ContextHandler.currentActivity(), permissions);
//    }
    //请求权限
    public static void requestPermission(OnPermissionListener onPermissionListener, String... permission) {
        Request request;
        if (ContextHandler.current() instanceof AbstractActivity) {
            request = AndPermission.with((AbstractActivity) ContextHandler.current());
        } else {
            request = AndPermission.with((AbstractFragment) ContextHandler.current());
        }
        request.permission(permission)
                .onGranted(permissions -> {
                    onPermissionListener.onSuccess();
                })
                .onDenied(permissions -> {
                    if (AndPermission.hasAlwaysDeniedPermission(ContextHandler.currentActivity(), permissions)) {
                        // 这些权限被用户总是拒绝。
                        StringBuilder sb = new StringBuilder();
                        for (String permissionName : Permission.transformText(ContextHandler.currentActivity(), permissions)) {
                            sb.append(permissionName).append(",");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        SettingService settingService = AndPermission.permissionSetting(ContextHandler.currentActivity());
//                        new CommonPromptDialog.Builder(ContextHandler.currentActivity())
//                                .setContentText("请设置" + sb.toString() + "权限,否则应用将无法正常运行")
//                                .setConfirmText("设置")
//                                .setCancelText("拒绝")
//                                .setCancelListener(v -> {
//                                    // 如果用户不同意去设置：
//                                    settingService.cancel();
//                                    onPermissionListener.onFailed();
//                                })
//                                .setConfirmListener(v -> {
//                                    // 如果用户同意去设置：
//                                    settingService.execute();
//                                }).create().show();
                    } else {
                        onPermissionListener.onFailed();
                    }
                })
                .rationale((context, permissions, executor) -> {
                    StringBuilder sb = new StringBuilder();
                    for (String permissionName : Permission.transformText(ContextHandler.currentActivity(), permissions)) {
                        sb.append(permissionName).append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
//                    new CommonPromptDialog.Builder(ContextHandler.currentActivity())
//                            .setContentText("请同意" + sb.toString() + "权限,否则应用将无法正常运行")
//                            .setConfirmText("同意")
//                            .setCancelText("拒绝")
//                            .setCancelListener(v -> {
//                                // 如果用户不同意：
//                                executor.cancel();
//                                onPermissionListener.onFailed();
//                            })
//                            .setConfirmListener(v -> {
//                                // 如果用户同意：
//                                executor.execute();
//                            }).create().show();
                }).start();
    }

    //请求权限组
    public static void requestPermission(OnPermissionListener onPermissionListener, String[]... permission) {
        Request request;
        if (ContextHandler.current() instanceof AbstractActivity) {
            request = AndPermission.with((AbstractActivity) ContextHandler.current());
        } else {
            request = AndPermission.with((AbstractFragment) ContextHandler.current());
        }
        request.permission(permission)
                .onGranted(permissions -> {
                    onPermissionListener.onSuccess();
                })
                .onDenied(permissions -> {
                    if (AndPermission.hasAlwaysDeniedPermission(ContextHandler.currentActivity(), permissions)) {
                        // 这些权限被用户总是拒绝。
                        StringBuilder sb = new StringBuilder();
                        for (String permissionName : Permission.transformText(ContextHandler.currentActivity(), permissions)) {
                            sb.append(permissionName).append(",");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        SettingService settingService = AndPermission.permissionSetting(ContextHandler.currentActivity());
//                        new CommonPromptDialog.Builder(ContextHandler.currentActivity())
//                                .setContentText("请设置" + sb.toString() + "权限,否则应用将无法正常运行")
//                                .setConfirmText("设置")
//                                .setCancelText("拒绝")
//                                .setCancelListener(v -> {
//                                    // 如果用户不同意去设置：
//                                    settingService.cancel();
//                                    onPermissionListener.onFailed();
//                                })
//                                .setConfirmListener(v -> {
//                                    // 如果用户同意去设置：
//                                    settingService.execute();
//                                }).create().show();
                    } else {
                        onPermissionListener.onFailed();
                    }
                })
                .rationale((context, permissions, executor) -> {
                    StringBuilder sb = new StringBuilder();
                    for (String permissionName : Permission.transformText(ContextHandler.currentActivity(), permissions)) {
                        sb.append(permissionName).append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
//                    new CommonPromptDialog.Builder(ContextHandler.currentActivity())
//                            .setContentText("请同意" + sb.toString() + "权限,否则应用将无法正常运行")
//                            .setConfirmText("同意")
//                            .setCancelText("拒绝")
//                            .setCancelListener(v -> {
//                                // 如果用户不同意：
//                                executor.cancel();
//                                onPermissionListener.onFailed();
//                            })
//                            .setConfirmListener(v -> {
//                                // 如果用户同意：
//                                executor.execute();
//                            }).create().show();
                }).start();
    }


    //权限请求成功回调
    public interface OnPermissionListener {
        void onSuccess();

        void onFailed();
    }
}
