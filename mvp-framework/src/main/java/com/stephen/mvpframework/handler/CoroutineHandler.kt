package com.stephen.mvpframework.handler


import kotlinx.coroutines.*


/**
 *  协程控制类
 * Created by Stephen on 2018/11/20.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

/**
 * 单一子线程运行 - 采用kotlin协程方式
 */
fun network(block: () -> Unit) = runBlocking<Unit> { launch(Dispatchers.IO) { block() } }

/**
 * 单一子线程运行 - 采用kotlin协程方式
 */
fun io(block: () -> Unit) = runBlocking<Unit> { launch(Dispatchers.IO) { block() } }

/**
 * 主线程运行 - 采用kotlin协程方式
 */
fun ui(block: () -> Unit) = runBlocking<Unit> { launch(Dispatchers.Main) { block() } }