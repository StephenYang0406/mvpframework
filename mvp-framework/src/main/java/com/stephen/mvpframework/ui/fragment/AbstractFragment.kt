package com.stephen.mvpframework.ui.fragment

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.stephen.mvpframework.R
import com.stephen.mvpframework.annotation.InjectForm
import com.stephen.mvpframework.annotation.InjectLayoutId
import com.stephen.mvpframework.annotation.InjectPresenter
import com.stephen.mvpframework.handler.ContextHandler
import com.stephen.mvpframework.helper.PhotoHelper
import com.stephen.mvpframework.helper.WarningHelper
import com.stephen.mvpframework.local.Content
import com.stephen.mvpframework.network.AbstractObserver
import com.stephen.mvpframework.constraint.IPresenter
import com.stephen.mvpframework.constraint.IUiOperation
import com.stephen.mvpframework.ui.activity.AbstractActivity
import com.stephen.mvpframework.utils.AnnotationUtil
import com.stephen.mvpframework.utils.LogUtil
import com.stephen.mvpframework.utils.SystemUtil
import com.stephen.mvpframework.constraint.BaseView
import java.io.File

/**
 * Created by Stephen on 2018/11/28.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
abstract class AbstractFragment : Fragment(), IUiOperation {
    //界面重见时刷新模式，true为默认执行刷新方法，false为不执行
    private var mRefreshMode = true
    protected var mRootView: View? = null
    private var mIsFirstVisible = true//是否第一次可见
    protected var mFrameLayout: FrameLayout? = null//数据布局容器,控制空布局展示
    protected fun start(clazz: Class<out AbstractActivity>) {
        val intent = Intent(activity, clazz)
        startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (savedInstanceState == null) {
            mRootView = getLayout()
            mRootView
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            autoWire()
            bindPresenter()
            onViewInit()
        }
    }

    //此方法在Fragment搭配ViewPager中不调用
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        LogUtil.testError(javaClass.simpleName + hidden)
        if (!hidden) {//用户可见
            ContextHandler.setFragment(this)
            if (mRefreshMode)
                onViewRefresh()
        } else {
            //取消网络订阅
            AbstractObserver.unSubscribe(javaClass.name)
        }
    }

    //此方法只在Fragment搭配ViewPager中回调
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!mIsFirstVisible && isVisibleToUser && mRefreshMode) {
            onHiddenChanged(false)
        }
    }

    override fun onDetach() {
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
        super.onDetach()
    }

    override fun onStart() {
        super.onStart()
        //如果第一次可见,在这里执行刷新方法
        //        if (mIsFirstVisible && mRefreshMode) {
        //            mIsFirstVisible = false;
        //            onViewRefresh();
        //        }
        if (mRefreshMode) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false
                ContextHandler.setFragment(this)
                onViewRefresh()
            } else {
                val supportFragmentManager = activity!!.supportFragmentManager
                val fragments = supportFragmentManager.fragments
                for (fragment in fragments) {
                    val visible = fragment.isVisible
                    if (visible && fragment === this) {
                        ContextHandler.setFragment(this)
                        onViewRefresh()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Content.Params.TAKE_PHOTO_REQUEST_CODE//拍照返回
                -> onTakePhotoResult(PhotoHelper.galleryAddPic())
            }
        } else if (resultCode == RESULT_CANCELED && SystemUtil.isMIUi()) {
            when (requestCode) {
                Content.Params.TAKE_PHOTO_REQUEST_CODE//拍照返回
                -> onTakePhotoResult(PhotoHelper.galleryAddPic())
            }
        }
    }

    private fun getLayout(): View? {
        if (AnnotationUtil.isHaveAnnotation(javaClass, InjectLayoutId::class.java)) {
            val layoutId = AnnotationUtil.getAnnotation(javaClass, InjectLayoutId::class.java).ID
            LogUtil.testInfo("layoutId------>$layoutId")
            if (layoutId == 0)
                throw IllegalArgumentException("layoutId is not defined")
            val layoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater.inflate(layoutId, null)
            val contentView = view.findViewWithTag<View>(WarningHelper.getString(R.string.content_id))
            if (contentView != null) {//内容展示布局
                val frameLayout = view.findViewWithTag<View>(R.string.content_container_id)
                if (frameLayout == null) {
                    val contentViewParent = contentView.parent as ViewGroup
                    val contentPosition = contentViewParent.indexOfChild(contentView)
                    contentViewParent.removeView(contentView)
                    mFrameLayout = FrameLayout(activity!!)
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

    override fun autoWire() {
        try {
            for (field in javaClass.declaredFields) {
                field.isAccessible = true
                //自动装载Presenter
                if (AnnotationUtil.isHaveAnnotation(field, InjectPresenter::class.java))
                    field.set(this, field.type.newInstance())
                else if (field.isAnnotationPresent(InjectForm::class.java)) {
                    field.set(this, field.type.newInstance())
                }//自动装载ViewHolder
                //自动装载Form
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
                    (field.get(this) as IPresenter).bindView(this as BaseView)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: ClassCastException) {
                    throw ClassCastException("u must implement BaseView")
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