package com.stephen.mvpframework.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.stephen.mvpframework.R;
import com.stephen.mvpframework.annotation.InjectForm;
import com.stephen.mvpframework.annotation.InjectLayoutId;
import com.stephen.mvpframework.annotation.InjectPresenter;
import com.stephen.mvpframework.annotation.InjectViewHolder;
import com.stephen.mvpframework.autolayout.RudenessScreenHelper;
import com.stephen.mvpframework.context.ContextHandler;
import com.stephen.mvpframework.helper.WarningHelper;
import com.stephen.mvpframework.local.Content;
import com.stephen.mvpframework.network.BaseObserver;
import com.stephen.mvpframework.presenter.BasePresenter;
import com.stephen.mvpframework.ui.IUiOperation;
import com.stephen.mvpframework.ui.activity.AbstractActivity;
import com.stephen.mvpframework.utils.AnnotationUtil;
import com.stephen.mvpframework.utils.LogUtil;
import com.stephen.mvpframework.utils.PhotoUtil;
import com.stephen.mvpframework.utils.SystemUtil;
import com.stephen.mvpframework.utils.UiUtil;
import com.stephen.mvpframework.view.BaseView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * 抽象Fragment
 * Created by Stephen on 2017/10/5.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public abstract class AbstractFragment extends Fragment implements IUiOperation {
    //界面重见时刷新模式，true为默认执行刷新方法，false为不执行
    private boolean mRefreshMode = true;
    protected View mRootView;
    private boolean mIsFirstVisible = true;//是否第一次可见
    protected FrameLayout mFrameLayout;//数据布局容器,控制空布局展示
    protected void start(Class<?extends AbstractActivity> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RudenessScreenHelper.resetDensity(getContext(), Content.Params.DESIGN_WIDTH);
        if (savedInstanceState == null) {
            mRootView = getLayout();
            return mRootView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            autoWire();
            bindPresenter();
            onViewInit();
        }
    }

    //此方法在Fragment搭配ViewPager中不调用
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.testError(getClass().getSimpleName() + hidden);
        if (!hidden) {//用户可见
            ContextHandler.setFragment(this);
            if (mRefreshMode)
                onViewRefresh();
        } else {
            //取消网络订阅
            BaseObserver.unSubscribe(getClass().getName());
        }
    }

    //此方法只在Fragment搭配ViewPager中回调
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!mIsFirstVisible && isVisibleToUser && mRefreshMode) {
            onHiddenChanged(false);
        }
    }

    @Override
    public void onDetach() {
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
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        //如果第一次可见,在这里执行刷新方法
//        if (mIsFirstVisible && mRefreshMode) {
//            mIsFirstVisible = false;
//            onViewRefresh();
//        }
        if (mRefreshMode) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                ContextHandler.setFragment(this);
                onViewRefresh();
            } else {
                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                List<Fragment> fragments = supportFragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    boolean visible = fragment.isVisible();
                    if (visible && fragment == this) {
                        ContextHandler.setFragment(this);
                        onViewRefresh();
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    public View getLayout() {
        if (AnnotationUtil.isHaveAnnotation(getClass(), InjectLayoutId.class)) {
            int layoutId = AnnotationUtil.getAnnotation(getClass(), InjectLayoutId.class).ID();
            LogUtil.testInfo("layoutId------>" + layoutId);
            if (layoutId == 0)
                throw new IllegalArgumentException("layoutId is not defined");
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(layoutId, null);
            View contentView = view.findViewWithTag(WarningHelper.getString(R.string.content_id));
            if (contentView != null) {//内容展示布局
                View frameLayout = view.findViewWithTag(R.string.content_container_id);
                if (frameLayout == null) {
                    ViewGroup contentViewParent = (ViewGroup) contentView.getParent();
                    int contentPosition = contentViewParent.indexOfChild(contentView);
                    contentViewParent.removeView(contentView);
                    mFrameLayout = new FrameLayout(getActivity());
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

    @Override
    public void autoWire() {
        try {
            for (Field field : getClass().getDeclaredFields()) {
                field.setAccessible(true);
                //自动装载Presenter
                if (AnnotationUtil.isHaveAnnotation(field, InjectPresenter.class))
                    field.set(this, field.getType().newInstance());
                    //自动装载ViewHolder
                else if (AnnotationUtil.isHaveAnnotation(field, InjectViewHolder.class)) {
                    field.set(this, field.getType().newInstance());
                    UiUtil.bindViewHolder(mRootView, field.get(this));
                }
                //自动装载Form
                else if (field.isAnnotationPresent(InjectForm.class)) {
                    field.set(this, field.getType().newInstance());
                    UiUtil.fillFragmentForm(this);
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
