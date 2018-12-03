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
    protected var voList = ArrayList<V>()
    private var option: OptionBuilder? = null
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
        initOption(option, viewHolder, position)
        bindData(position, viewHolder.itemView, voList[position])
    }

    override fun getItemCount() = voList.size

    /*
        初始化可选设置
     */
    private fun initOption(option: OptionBuilder?, viewHolder: BaseViewHolder, position: Int) {
        option?.run {
            if (baseOnItemClickListener != null) {
                viewHolder.itemView.setOnClickListener {
                    baseOnItemClickListener?.onItemClickListener(voList[position], position)
                }
            }
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
    }

    /*
        设置数据列表
     */
    fun setDataList(list: ArrayList<V>): AbstractRecyclerAdapter<*> {
        voList = list
        return this
    }

    /*
        设置可选项
     */
    fun setOption(option: OptionBuilder): AbstractRecyclerAdapter<*> {
        this.option = option
        return this
    }

    /*
        绑定数据方法
     */
    protected abstract fun bindData(position: Int, itemView: View, vo: V)

    /**
     * 基础ViewHolder
     */
    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /*
        基础点击监听
     */
    interface BaseOnItemClickListener {
        fun <T> onItemClickListener(t: T, position: Int)
    }

    /**
     * 选项构造器
     */
    class OptionBuilder {
        var baseOnItemClickListener: BaseOnItemClickListener? = null
        var firstMarginTopValue: Int = 0
        /*
            添加基础监听
         */
        fun addBaseOnItemClickListener(listener: BaseOnItemClickListener): OptionBuilder {
            this.baseOnItemClickListener = listener
            return this
        }

        fun addFirstMarginTopValue(firstMarginTopValue: Int): OptionBuilder {
            this.firstMarginTopValue = firstMarginTopValue
            return this
        }
    }
}