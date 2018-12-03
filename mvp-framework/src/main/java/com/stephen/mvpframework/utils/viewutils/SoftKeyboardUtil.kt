package com.stephen.mvpframework.utils.viewutils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.stephen.mvpframework.handler.ContextHandler
import java.lang.ref.WeakReference

/**
 * 软键盘工具类
 * Created by Stephen on 2018/11/15.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object SoftKeyboardUtil {
    private var mInputManager: WeakReference<InputMethodManager>? = null
    private fun getInputManager(): InputMethodManager? {
        if (mInputManager == null || mInputManager?.get() == null) {
            mInputManager = WeakReference(ContextHandler.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        }
        return mInputManager?.get()
    }

    //弹出软键盘
    fun popKeyboard(focusView: View) {
        getInputManager()?.showSoftInput(focusView, InputMethodManager.SHOW_FORCED)
    }

    //隐藏软键盘
    fun hideKeyboard(focusView: View) {
        getInputManager()?.hideSoftInputFromWindow(focusView.windowToken, 0)
    }
}