package com.stephen.mvpframework.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stephen.mvpframework.annotation.InjectItemId;
import com.stephen.mvpframework.handler.ContextHandler;
import com.stephen.mvpframework.utils.AnnotationUtil;
import com.stephen.mvpframework.utils.LogUtil;
import com.stephen.mvpframework.utils.ScreenUtil;

import java.util.ArrayList;

/**
 * 通用RecyclerView Adapter封装,java版本
 * Created by Stephen on 2018/12/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
public abstract class AbstractRecyclerAdapter4Java<V> extends RecyclerView.Adapter<AbstractRecyclerAdapter4Java.BaseViewHolder> {
    @SuppressWarnings("unchecked")
    protected ArrayList<V> mList = new ArrayList();
    protected int mFirstMarginTop = 10;
    protected AbstractRecyclerAdapter4Java.BaseOnItemClickListener<V> mBaseOnItemClickListener;

    public AbstractRecyclerAdapter4Java() {
    }

    @NonNull
    public AbstractRecyclerAdapter4Java.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int itemLayoutId = (AnnotationUtil.INSTANCE.getAnnotation(this.getClass(), InjectItemId.class)).ID();
        if (itemLayoutId == 0) {
            throw new RuntimeException("ur itemLayoutId is undefined");
        } else {
            LogUtil.INSTANCE.testInfo("itemLayoutId------>" + itemLayoutId);
            return new AbstractRecyclerAdapter4Java.BaseViewHolder(LayoutInflater.from(ContextHandler.INSTANCE.currentActivity()).inflate(itemLayoutId, parent, false));
        }
    }

    public void onBindViewHolder(@NonNull AbstractRecyclerAdapter4Java.BaseViewHolder holder, int position) {
        this.bindData(holder, position, this.mList.get(position));
    }

    public int getItemCount() {
        return this.mList.size();
    }

    public void setDataList(ArrayList<V> list) {
        this.mList = list;
    }

    public void setBaseOnItemClickListener(AbstractRecyclerAdapter4Java.BaseOnItemClickListener<V> listener) {
        this.mBaseOnItemClickListener = listener;
    }

    public void setFirstMarginTopValue(int firstMarginTop) {
        this.mFirstMarginTop = firstMarginTop;
    }

    protected void useFirstMarginTop(AbstractRecyclerAdapter4Java.BaseViewHolder holder, int position) {
        RecyclerView.LayoutParams layoutParams;
        if (position == 0) {
            layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.topMargin = ScreenUtil.INSTANCE.dp2px(this.mFirstMarginTop);
            holder.itemView.setLayoutParams(layoutParams);
        } else {
            layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.topMargin = 0;
            holder.itemView.setLayoutParams(layoutParams);
        }

    }

    protected void useBaseOnItemClickListener(AbstractRecyclerAdapter4Java.BaseViewHolder holder, int position) {
        if (this.mBaseOnItemClickListener != null) {
            holder.itemView.setOnClickListener((view) -> {
                this.mBaseOnItemClickListener.onItemClickListener(this.mList.get(position), position);
            });
        }

    }

    protected abstract void bindData(@NonNull AbstractRecyclerAdapter4Java.BaseViewHolder holder, int position, V vo);

    public interface BaseOnItemClickListener<T> {
        void onItemClickListener(T vo, int position);
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViewCacheContainer = new SparseArray();

        BaseViewHolder(View itemView) {
            super(itemView);
        }

        public View get(int resourceId) {
            View cacheView = this.mViewCacheContainer.get(resourceId);
            if (cacheView == null) {
                cacheView = this.itemView.findViewById(resourceId);
                this.mViewCacheContainer.put(resourceId, cacheView);
            }

            return cacheView;
        }
    }
}