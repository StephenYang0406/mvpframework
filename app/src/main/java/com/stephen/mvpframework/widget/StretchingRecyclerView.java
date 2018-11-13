package com.stephen.mvpframework.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 拉伸RecyclerView
 * Created by Stephen on 2018/1/23.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class StretchingRecyclerView extends RecyclerView {
    public StretchingRecyclerView(Context context) {
        super(context);
    }

    public StretchingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StretchingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
