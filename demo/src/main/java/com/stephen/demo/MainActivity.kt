package com.stephen.demo

import com.stephen.demo.sample.activity.SampleActivity
import com.stephen.mvpframework.annotation.InjectLayoutId
import com.stephen.mvpframework.ui.activity.AbstractActivity

@InjectLayoutId(ID = R.layout.activity_main)
class MainActivity : AbstractActivity() {
    override fun onViewInit() {
        start(SampleActivity::class.java)
    }

    override fun onViewRefresh() {
    }

}
