package com.stephen.demo.sample.adapter

import android.view.View
import com.stephen.demo.R
import com.stephen.demo.sample.vo.SapmleVo
import com.stephen.mvpframework.annotation.InjectItemId
import com.stephen.mvpframework.ui.AbstractRecyclerAdapter
import kotlinx.android.synthetic.main.adapter_sample.view.*

/**
 * 示例适配器
 * Created by Stephen on 2019/3/21.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
//注入条目布局
@InjectItemId(ID = R.layout.adapter_sample)
class SampleAdapter : AbstractRecyclerAdapter<SapmleVo>() {
    override fun bindData(position: Int, itemView: View, vo: SapmleVo) {
        //在非Activity内直接使用布局ID写法
        itemView.run {
            tv.text = ""
        }
    }
}