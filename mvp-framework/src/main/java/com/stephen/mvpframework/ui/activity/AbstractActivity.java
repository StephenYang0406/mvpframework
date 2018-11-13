package com.stephen.mvpframework.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.stephen.mvpframework.R;
import com.stephen.mvpframework.annotation.InjectActionBar;
import com.stephen.mvpframework.annotation.InjectForm;
import com.stephen.mvpframework.annotation.InjectLayoutId;
import com.stephen.mvpframework.annotation.InjectPresenter;
import com.stephen.mvpframework.annotation.InjectViewHolder;
import com.stephen.mvpframework.context.ContextHandler;
import com.stephen.mvpframework.local.Content;
import com.stephen.mvpframework.network.BaseObserver;
import com.stephen.mvpframework.presenter.BasePresenter;
import com.stephen.mvpframework.ui.IUiOperation;
import com.stephen.mvpframework.utils.AnnotationUtil;
import com.stephen.mvpframework.utils.LogUtil;
import com.stephen.mvpframework.utils.PhotoUtil;
import com.stephen.mvpframework.utils.SystemUtil;
import com.stephen.mvpframework.utils.UiUtil;
import com.stephen.mvpframework.view.BaseView;

import java.io.File;
import java.lang.reflect.Field;

/**
 * 抽象activity
 * Created by Stephen on 2017/10/5.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public abstract class AbstractActivity extends AppCompatActivity implements IUiOperation {
    protected View mRootView;
    //界面重见时刷新模式，true为默认执行刷新方法，false为不执行
    private boolean mRefreshMode = true;
    protected FrameLayout mFrameLayout;//数据布局容器,控制空布局展示

    public void start(Class<?extends AbstractActivity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        if (savedInstanceState == null) {
            mRootView = getLayout();
            setContentView(mRootView);
            autoWire();
            bindPresenter();
            onViewInit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mRefreshMode)
            onViewRefresh();
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        //取消网络订阅
        BaseObserver.unSubscribe(getClass().getName());
        if (ContextHandler.containsActivity(this))
            ContextHandler.removeLast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消网络订阅
        BaseObserver.unSubscribe(getClass().getName());
        for (Field field : getClass().getDeclaredFields()) {
            if (BasePresenter.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    ((BasePresenter) field.get(this)).recycle();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (ContextHandler.containsActivity(this))
            ContextHandler.removeTargetActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Content.Params.TAKE_PHOTO_REQUEST_CODE://拍照返回
                    onTakePhotoResult(PhotoUtil.galleryAddPic());
                    break;
            }
        } else if (resultCode == RESULT_CANCELED && SystemUtil.isMIUI()) {
            switch (requestCode) {
                case Content.Params.TAKE_PHOTO_REQUEST_CODE://拍照返回
                    onTakePhotoResult(PhotoUtil.galleryAddPic());
                    break;
            }
        }
    }

    private View getLayout() {
        //将Activity注入到栈
        ContextHandler.addActivity(this);
        if (AnnotationUtil.isHaveAnnotation(getClass(), InjectLayoutId.class)) {
            int layoutId = AnnotationUtil.getAnnotation(getClass(), InjectLayoutId.class).ID();
            LogUtil.testInfo("layoutId------>" + layoutId);
            if (layoutId == 0)
                throw new IllegalArgumentException("layoutId is not defined");
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(layoutId, null);
            View contentView = view.findViewWithTag(R.string.content_id);
            if (contentView != null) {//内容展示布局
                View frameLayout = view.findViewWithTag(R.string.content_container_id);
                if (frameLayout == null) {
                    ViewGroup contentViewParent = (ViewGroup) contentView.getParent();
                    int contentPosition = contentViewParent.indexOfChild(contentView);
                    contentViewParent.removeView(contentView);
                    mFrameLayout = new FrameLayout(this);
                    mFrameLayout.setBackgroundColor(Color.WHITE);
                    mFrameLayout.addView(contentView);
                    mFrameLayout.setLayoutParams(contentView.getLayoutParams());//设置和内容展示布局一样的大小
                    contentViewParent.addView(mFrameLayout, contentPosition);
                } else {
                    mFrameLayout = (FrameLayout) frameLayout;
                }
            } else {
                LogUtil.testError(getClass().getName() + "---->data content view not undefined");
            }
            return view;
        }
        return null;
    }

    //自动装填方法
    @Override
    public void autoWire() {
        try {
            for (Field field : getClass().getDeclaredFields()) {
                field.setAccessible(true);
                //自动装载Presenter
                if (AnnotationUtil.isHaveAnnotation(field, InjectPresenter.class)) {
                    field.set(this, field.getType().newInstance());
                }
                //自动装载ViewHolder
                else if (AnnotationUtil.isHaveAnnotation(field, InjectViewHolder.class)) {
                    field.set(this, field.getType().newInstance());
                    UiUtil.bindViewHolder(mRootView, field.get(this));
                }
                //自动装载ActionBar
                else if (AnnotationUtil.isHaveAnnotation(field, InjectActionBar.class)) {
                    field.set(this, field.getType().newInstance());
                }
                //自动装载Form
                else if (field.isAnnotationPresent(InjectForm.class)) {
                    field.set(this, field.getType().newInstance());
                    UiUtil.fillActivityForm(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindPresenter() {
        for (Field field : getClass().getDeclaredFields()) {
            if (BasePresenter.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    ((BasePresenter) field.get(this)).bindView((BaseView) this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    throw new ClassCastException("u must implement BaseView");
                }
            }
        }
    }

    //是否全屏
    public boolean isFullScreen() {
        return false;
    }

    //设置刷新模式
    public void setRefreshMode(boolean refreshMode) {
        this.mRefreshMode = refreshMode;
    }


    //获取刷新模式
    public boolean getRefreshMode() {
        return mRefreshMode;
    }

    //拍照返回结果
    @Override
    public void onTakePhotoResult(File file) {

    }
}
