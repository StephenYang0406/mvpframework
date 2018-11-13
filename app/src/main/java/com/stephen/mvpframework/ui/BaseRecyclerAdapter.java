package com.stephen.mvpframework.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stephen.mvpframework.annotation.InjectItemId;
import com.stephen.mvpframework.context.ContextHandler;
import com.stephen.mvpframework.utils.AnnotationUtil;
import com.stephen.mvpframework.utils.LogUtil;
import com.stephen.mvpframework.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装RecyclerView的Adapter
 * Created by Stephen on 2018/8/10.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
public abstract class BaseRecyclerAdapter<V> extends RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder> {
    //数据列表
    protected List<V> mList = new ArrayList<>();
    //默认第一条数据marginTop
    protected int mFirstMarginTop = 10;
    //条目点击监听
    protected BaseOnItemClickListener<V> mBaseOnItemClickListener;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int itemLayoutId = AnnotationUtil.getAnnotation(getClass(), InjectItemId.class).ID();
        if (itemLayoutId == 0) {
            throw new RuntimeException("ur itemLayoutId is undefined");
        }
        LogUtil.testInfo("itemLayoutId------>" + itemLayoutId);
        return new BaseViewHolder(LayoutInflater.from(ContextHandler.currentActivity())
                .inflate(itemLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        bindData(holder, position, mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setDataList(List<V> list) {
        mList = list;
    }

    public void setBaseOnItemClickListener(BaseOnItemClickListener<V> listener) {
        mBaseOnItemClickListener = listener;
    }

    public void setFirstMarginTopValue(int firstMarginTop) {
        mFirstMarginTop = firstMarginTop;
    }

    protected void useFirstMarginTop(BaseViewHolder holder, int position) {
        if (position == 0) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.topMargin = ScreenUtil.dp2px(mFirstMarginTop);
            holder.itemView.setLayoutParams(layoutParams);
        } else {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.topMargin = 0;
            holder.itemView.setLayoutParams(layoutParams);
        }
    }

    protected void useBaseOnItemClickListener(BaseViewHolder holder, int position) {
        if (mBaseOnItemClickListener != null) {
            holder.itemView.setOnClickListener(view -> mBaseOnItemClickListener.
                    onItemClickListener(mList.get(position), position));
        }
    }

    protected abstract void bindData(@NonNull BaseViewHolder holder, int position, V vo);

    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViewCacheContainer;

        BaseViewHolder(View itemView) {
            super(itemView);
            mViewCacheContainer = new SparseArray<>();
        }

        public View get(int resourceId) {
            View cacheView = mViewCacheContainer.get(resourceId);
            if (cacheView == null) {
                cacheView = itemView.findViewById(resourceId);
                mViewCacheContainer.put(resourceId, cacheView);
            }
            return cacheView;
        }
    }

    public interface BaseOnItemClickListener<T> {
        void onItemClickListener(T t, int position);
    }
}
