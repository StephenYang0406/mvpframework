package com.stephen.mvpframework.ui.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import com.stephen.mvpframework.ui.fragment.MultiFragment
import java.util.*

/**
 * 多Fragment单Activity模式适用
 * Created by Stephen on 2019/3/1.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
abstract class FragmentContainerActivity : AbstractActivity() {
    //Fragment栈
    private val mFragmentStack = Stack<MultiFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentContainer().visibility = View.GONE
    }

    //跳转Fragment
    fun route(fragment: MultiFragment) {
        getFragmentContainer().visibility = View.VISIBLE
        //开启事务
        val beginTransaction = supportFragmentManager.beginTransaction()
        //如果栈内不为空
        if (!mFragmentStack.isEmpty()) {
            //隐藏栈顶Fragment显示
            beginTransaction.hide(mFragmentStack.peek())
        }
        //添加跳转的Fragment
        beginTransaction.add(getFragmentContainer().id, fragment)
        //提交事务
        beginTransaction.commitNowAllowingStateLoss()
        //添加到栈顶
        mFragmentStack.push(fragment)
    }

    fun back() {
        //开启事务
        val beginTransaction = supportFragmentManager.beginTransaction()
        //移除当前栈顶Fragment显示
        beginTransaction.remove(mFragmentStack.peek())
        //移除当前栈顶Fragment
        mFragmentStack.pop()
        //如果栈内不为空
        if (!mFragmentStack.isEmpty()) {
            //显示栈顶Fragment
            beginTransaction.show(mFragmentStack.peek())
        } else {
            //隐藏布局
            getFragmentContainer().visibility = View.GONE
        }
        //提交事务
        beginTransaction.commitNowAllowingStateLoss()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (!mFragmentStack.isEmpty()) {
                back()
                false
            } else {
                onBackDown()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    //按下返回键
    open fun onBackDown(): Boolean {
        return false
    }

    //获取Fragment容器
    abstract fun getFragmentContainer(): ViewGroup
}