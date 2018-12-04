package com.stephen.mvpframework.constraint

import com.stephen.mvpframework.annotation.InjectForm
import com.stephen.mvpframework.annotation.InjectPresenter
import com.stephen.mvpframework.utils.AnnotationUtil

/**
 * Created by Stephen on 2018/12/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
abstract class AbstractPresenter<T : IView> : IPresenter {
    protected lateinit var view: T
    @Suppress("UNCHECKED_CAST")
    override fun bindView(view: IView) {
        this.view = view as T
        autowire()
    }

    override fun recycle() {
    }

    /*
        自动装填
     */
    private fun autowire() {
        try {
            for (field in javaClass.declaredFields) {
                field.isAccessible = true
                //自动装载Form
                if (field.isAnnotationPresent(InjectForm::class.java)) {
                    field.set(this, field.type.newInstance())
                }
            }
        } catch (e: Exception) {
        }
    }
}