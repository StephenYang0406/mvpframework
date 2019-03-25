package com.stephen.mvpframework.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stephen.mvpframework.annotation.InjectItemId
import com.stephen.mvpframework.handler.ContextHandler
import com.stephen.mvpframework.model.BaseVo
import com.stephen.mvpframework.utils.AnnotationUtil
import com.stephen.mvpframework.utils.LogUtil
import com.stephen.mvpframework.utils.ScreenUtil

/**
 * 抽象通用Adapter
 * Created by Stephen on 2018/12/3.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
abstract class AbstractRecyclerAdapter<V : BaseVo> : RecyclerView.Adapter<AbstractRecyclerAdapter.BaseViewHolder>() {
    var voList = ArrayList<V>()
    //基础点击事件
    var baseOnItemClickListener: (vo: V) -> Unit = { _ -> }
    //长按点击事件
    var baseOnItemLongClickListener: (vo: V) -> Unit = { _ -> }
    //第一个条目顶部margin
    var firstMarginTopValue: Int = 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AbstractRecyclerAdapter.BaseViewHolder {
        val itemLayoutId = AnnotationUtil.getAnnotation(this.javaClass, InjectItemId::class.java).ID
        if (itemLayoutId == 0) {
            throw RuntimeException("ur itemLayoutId is undefined")
        }
        LogUtil.testInfo("itemLayoutId------>$itemLayoutId")
        return AbstractRecyclerAdapter.BaseViewHolder(LayoutInflater.from(ContextHandler.currentActivity())
                .inflate(itemLayoutId, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: BaseViewHolder, position: Int) {
        initOption(viewHolder, position)
        bindData(position, viewHolder.itemView, voList[position])
    }

    override fun getItemCount() = voList.size

    /*
        初始化可选设置
     */
    private fun initOption(viewHolder: BaseViewHolder, position: Int) {
        //点击事件
        viewHolder.itemView.setOnClickListener {
            baseOnItemClickListener(voList[position])
        }
        //长按点击事件
        viewHolder.itemView.setOnLongClickListener {
            baseOnItemLongClickListener(voList[position])
            true
        }
        //调整第一个条目顶部margin
        if (firstMarginTopValue > 0) {
            if (position == 0) {
                val layoutParams = viewHolder.itemView.layoutParams as RecyclerView.LayoutParams
                layoutParams.topMargin = ScreenUtil.dp2px(firstMarginTopValue)
                viewHolder.itemView.layoutParams = layoutParams
            } else {
                val layoutParams = viewHolder.itemView.layoutParams as RecyclerView.LayoutParams
                layoutParams.topMargin = 0
                viewHolder.itemView.layoutParams = layoutParams
            }
        }
    }


    /*
        绑定数据方法
     */
    protected abstract fun bindData(position: Int, itemView: View, vo: V)

    /**
     * 基础ViewHolder
     */
    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}