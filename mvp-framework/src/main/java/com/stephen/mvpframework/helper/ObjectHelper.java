package com.stephen.mvpframework.helper;

import android.text.TextUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 2018/2/5.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class ObjectHelper {
    public static boolean checkNull(Object object) {
        if (object == null)
            return true;
        if (object instanceof String) {
            return TextUtils.isEmpty((CharSequence) object);
        } else if (object instanceof List) {
            return ((List) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map) object).isEmpty();
        }
        return false;
    }
}
