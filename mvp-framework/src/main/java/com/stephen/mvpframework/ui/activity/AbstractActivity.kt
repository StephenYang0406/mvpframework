package com.stephen.mvpframework.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.FrameLayout
import com.stephen.mvpframework.R
import com.stephen.mvpframework.annotation.InjectLayoutId
import com.stephen.mvpframework.annotation.InjectPresenter
import com.stephen.mvpframework.handler.ContextHandler
import com.stephen.mvpframework.helper.PhotoHelper
import com.stephen.mvpframework.local.Content
import com.stephen.mvpframework.network.AbstractObserver
import com.stephen.mvpframework.constraint.IPresenter
import com.stephen.mvpframework.constraint.IUiOperation
import com.stephen.mvpframework.utils.AnnotationUtil
import com.stephen.mvpframework.utils.LogUtil
import com.stephen.mvpframework.utils.SystemUtil
import com.stephen.mvpframework.constraint.IView
import java.io.File

/**
 * Created by Stephen on 2018/11/28.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
abstract class AbstractActivity : AppCompatActivity(), IUiOperation {
    protected var mRootView: View? = null
    //界面重见时刷新模式，true为默认执行刷新方法，false为不执行
    private var mRefreshMode = true
    protected var mFrameLayout: FrameLayout? = null//数据布局容器,控制空布局展示

    fun start(clazz: Class<out AbstractActivity>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            mRootView = getLayout()
            setContentView(mRootView)
            autowire()
            bindPresenter()
            onViewInit()
        }
    }

    override fun onStart() {
        super.onStart()
        if (mRefreshMode)
            onViewRefresh()
    }

    override fun finish() {
        super.finish()
        //取消网络订阅
        AbstractObserver.unSubscribe(javaClass.name)
        if (ContextHandler.containsActivity(this))
            ContextHandler.removeLast()
    }

    override fun onDestroy() {
        super.onDestroy()
        //取消网络订阅
        AbstractObserver.unSubscribe(javaClass.name)
        for (field in javaClass.declaredFields) {
            if (IPresenter::class.java.isAssignableFrom(field.type)) {
                field.isAccessible = true
                try {
                    (field.get(this) as IPresenter).recycle()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

            }
        }
        if (ContextHandler.containsActivity(this))
            ContextHandler.removeTargetActivity(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Content.Params.TAKE_PHOTO_REQUEST_CODE//拍照返回
                -> onTakePhotoResult(PhotoHelper.galleryAddPic())
            }
        } else if (resultCode == Activity.RESULT_CANCELED && SystemUtil.isMIUi()) {
            when (requestCode) {
                Content.Params.TAKE_PHOTO_REQUEST_CODE//拍照返回
                -> onTakePhotoResult(PhotoHelper.galleryAddPic())
            }
        }
    }

    private fun getLayout(): View? {
        //将Activity注入到栈
        ContextHandler.addActivity(this)
        if (AnnotationUtil.isHaveAnnotation(javaClass, InjectLayoutId::class.java)) {
            val layoutId = AnnotationUtil.getAnnotation(javaClass, InjectLayoutId::class.java).ID
            LogUtil.testInfo("layoutId------>$layoutId")
            if (layoutId == 0)
                throw IllegalArgumentException("layoutId is not defined")
            val layoutInflater = LayoutInflater.from(this)
            val view = layoutInflater.inflate(layoutId, null)
            val contentView = view.findViewWithTag<View>(R.string.content_id)
            if (contentView != null) {//内容展示布局
                val frameLayout = view.findViewWithTag<View>(R.string.content_container_id)
                if (frameLayout == null) {
                    val contentViewParent = contentView.parent as ViewGroup
                    val contentPosition = contentViewParent.indexOfChild(contentView)
                    contentViewParent.removeView(contentView)
                    mFrameLayout = FrameLayout(this)
                    mFrameLayout?.setBackgroundColor(Color.WHITE)
                    mFrameLayout?.addView(contentView)
                    mFrameLayout?.layoutParams = contentView.layoutParams//设置和内容展示布局一样的大小
                    contentViewParent.addView(mFrameLayout, contentPosition)
                } else {
                    mFrameLayout = frameLayout as FrameLayout
                }
            } else {
                LogUtil.testError(javaClass.name + "---->data content view not undefined")
            }
            return view
        }
        return null
    }

    //自动装填方法
    override fun autowire() {
        try {
            for (field in javaClass.declaredFields) {
                field.isAccessible = true
                //自动装载Presenter
                if (AnnotationUtil.isHaveAnnotation(field, InjectPresenter::class.java)) {
                    field.set(this, field.type.newInstance())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun bindPresenter() {
        for (field in javaClass.declaredFields) {
            if (IPresenter::class.java.isAssignableFrom(field.type)) {
                field.isAccessible = true
                try {
                    (field.get(this) as IPresenter).bindView(this as IView)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: ClassCastException) {
                    throw ClassCastException("u must implement IView")
                }

            }
        }
    }

    //设置刷新模式
    override fun setRefreshMode(refreshMode: Boolean) {
        this.mRefreshMode = refreshMode
    }


    //获取刷新模式
    override fun getRefreshMode(): Boolean {
        return mRefreshMode
    }

    //拍照返回结果
    override fun onTakePhotoResult(file: File?) {

    }
}