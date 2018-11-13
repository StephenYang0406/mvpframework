package com.stephen.mvpframework.utils.viewutils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.stephen.mvpframework.context.ContextHandler;


/**
 * 软键盘工具类
 * Created by Stephen on 2018/1/10.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class SoftKeyboardUtil {
    private static InputMethodManager mInputManager;

    private static InputMethodManager getInputManager() {
        if (mInputManager == null)
            mInputManager = (InputMethodManager) ContextHandler.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        return mInputManager;
    }

    //弹出软键盘
    public static void popKeyboard(View focusView) {
        getInputManager().showSoftInput(focusView, InputMethodManager.SHOW_FORCED);
    }

    //隐藏软键盘
    public static void hideKeyboard(View focusView) {
        getInputManager().hideSoftInputFromWindow(focusView.getWindowToken(), 0);
    }
}
