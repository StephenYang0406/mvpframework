package com.stephen.mvpframework.annotation

/**
 * 条目ID注入注解
 * Created by Stephen on 2018/11/15.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class InjectItemId(val ID: Int = 0)