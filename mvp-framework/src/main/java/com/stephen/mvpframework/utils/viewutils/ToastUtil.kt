package com.stephen.mvpframework.utils.viewutils

import android.widget.Toast
import com.nispok.snackbar.Snackbar
import com.nispok.snackbar.SnackbarManager
import com.stephen.mvpframework.handler.ContextHandler
import com.stephen.mvpframework.helper.WarningHelper
import com.stephen.mvpframework.utils.ScreenUtil

/**
 * Created by Stephen on 2018/11/15.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object ToastUtil {

    //从上方弹出SnackBar
    fun showTopSnackBar(text: String) {
        SnackbarManager.show(
                Snackbar.with(ContextHandler.getApplication())
                        .text(text)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .margin(ScreenUtil.dp2px(0), ScreenUtil.dp2px(25))
        )
    }

    //从下方弹出SnackBar
    fun showBottomSnackBar(text: String) {
        SnackbarManager.show(
                Snackbar.with(ContextHandler.getApplication())
                        .text(text)
                        .position(Snackbar.SnackbarPosition.BOTTOM)
                        .margin(ScreenUtil.dp2px(0), ScreenUtil.dp2px(25))
        )
    }

    //弹出绿底白字Toast
    fun showSuccessToast(text: String) {
        val toastStr = "<font color='#fff3f3f3'>$text</font>"
        val toast = Toast.makeText(ContextHandler.getApplication(), WarningHelper.fromHtml(toastStr), Toast.LENGTH_LONG)
        val view = toast.view
        view.setBackgroundResource(android.R.color.holo_green_light)
        view.setPadding(5, 0, 5, 0)
        toast.view = view
        toast.show()
    }

    fun showSuccessToast(resId: Int) {
        showSuccessToast(ContextHandler.getApplication().getString(resId))
    }

    fun showToast(text: String) {
        Toast.makeText(ContextHandler.getApplication(), text, Toast.LENGTH_LONG).show()
    }

    //弹出绿底红字Toast
    fun showErrorToast(text: String) {
        val toastStr = "<font color='#fff3f3f3'>$text</font>"
        val toast = Toast.makeText(ContextHandler.getApplication(), WarningHelper.fromHtml(toastStr), Toast.LENGTH_LONG)
        val view = toast.view
        view.setBackgroundResource(android.R.color.holo_red_light)
        view.setPadding(5, 0, 5, 0)
        toast.view = view
        toast.show()
    }

    fun showErrorToast(resId: Int) {
        showErrorToast(ContextHandler.getApplication().getString(resId))
    }
}