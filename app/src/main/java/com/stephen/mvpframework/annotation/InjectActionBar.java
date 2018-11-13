package com.stephen.mvpframework.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ActionBar注入注解
 * Created by Stephen on 2017/11/27.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectActionBar {
    //文字资源
    int TEXT_RES_ID() default 0;

    //背景资源
    int BG_RES_ID() default 0;

    //左侧工具，默认返回控件
    int TOOL_LEFT() default 1;

    //右侧工具,默认为空
    int TOOL_RIGHT() default 0;

    //状态栏的颜色 默认是主题色
    int SYSTEM_BAR_ID() default 0;
}
