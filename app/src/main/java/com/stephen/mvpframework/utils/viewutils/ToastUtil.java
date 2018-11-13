package com.stephen.mvpframework.utils.viewutils;

import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.stephen.mvpframework.context.ContextHandler;
import com.stephen.mvpframework.utils.ScreenUtil;


/**
 * Toast工具类
 * Created by Stephen on 2017/11/1.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class ToastUtil {

    //从上方弹出SnackBar
    public static void showTopSnackBar(String text) {
        SnackbarManager.show(
                Snackbar.with(ContextHandler.getApplication())
                        .text(text)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .margin(ScreenUtil.dp2px(0), ScreenUtil.dp2px(25))
        );
    }

    //从下方弹出SnackBar
    public static void showBottomSnackBar(String text) {
        SnackbarManager.show(
                Snackbar.with(ContextHandler.getApplication())
                        .text(text)
                        .position(Snackbar.SnackbarPosition.BOTTOM)
                        .margin(ScreenUtil.dp2px(0), ScreenUtil.dp2px(25))
        );
    }

    //弹出绿底白字Toast
    public static void showSuccessToast(String text) {
        String toastStr = "<font color='#fff3f3f3'>" + text + "</font>";
        Toast toast = Toast.makeText(ContextHandler.getApplication(), Html.fromHtml(toastStr), Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(android.R.color.holo_green_light);
        view.setPadding(5, 0, 5, 0);
        toast.setView(view);
        toast.show();
    }

    public static void showSuccessToast(int resId) {
        showSuccessToast(ContextHandler.getApplication().getString(resId));
    }

    public static void showToast(String text) {
        Toast.makeText(ContextHandler.getApplication(), text, Toast.LENGTH_LONG).show();
    }

    //弹出绿底红字Toast
    public static void showErrorToast(String text) {
        String toastStr = "<font color='#fff3f3f3'>" + text + "</font>";
        Toast toast = Toast.makeText(ContextHandler.getApplication(), Html.fromHtml(toastStr), Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(android.R.color.holo_red_light);
        view.setPadding(5, 0, 5, 0);
        toast.setView(view);
        toast.show();
    }

    public static void showErrorToast(int resId) {
        showErrorToast(ContextHandler.getApplication().getString(resId));
    }

}
