package com.stephen.mvpframework.utils

import android.content.Context
import android.os.Environment
import com.stephen.mvpframework.handler.ContextHandler
import java.io.File

/**
 * 手机储存工具类
 * Created by Stephen on 2018/11/15.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object StorageUtil {
    /**
     * Android储存API简解
     *  1.获取外部储存(包含SD卡)
     *      A:context.getExternalFilesDirs(string)
     *      该方法获取外部储存路径,返回File数组,如果有SD卡则返回两个路径,
     *      一个机身外部储存路径,一个SD卡储存路径,接收参数为目录名称,作为新建
     *      目录名称.返回路径:
     *      /storage/emulated/0/Android/data/包名/files/参数 (机身内置外部储存)
     *      /storage/手机型号不同/Android/data/包名/files/参数  (SD卡)
     *
     *      B:context.getExternalFilesDir(string)
     *      该方法获取外部储存路径,返回File对象,只返回机身储存路径.接收参数为目录名称,
     *      作为新建目录名称.返回路径:
     *      /storage/emulated/0/Android/data/包名/files/参数 (机身内置外部储存)
     *
     *      C:Environment.getExternalStorageDirectory()
     *      该方法获取外部储存根目录,返回File对象,只返回机身储存路径.返回路径:
     *      /storage/emulated/0(机身内置外部储存)
     *
     *      D:Environment.getExternalStoragePublicDirectory(string)
     *      该方法获取外部储存根目录并接收一个字符参数,参数为目录名称,在根目录下创建并返回
     *      只返回机身储存路径.返回路径:
     *      /storage/emulated/0/参数
     *
     *      E:context.getExternalCacheDir()
     *      该方法获取该应用在外部储存的缓存路径.只返回机身储存路径.返回路径:
     *      /storage/emulated/0/Android/data/包名/cache
     *
     *  2.获取内部储存
     *      A:Environment.getDataDirectory()
     *      该方法获取手机内部储存根目录
     *      返回路径:
     *      /data
     *
     *      B:context.getFilesDir()
     *      该方法获取手机当前用户当前应用内部储存文件目录
     *      返回路径:
     *      /data/user/0/包名/files
     *
     *      C:context.getCacheDir()
     *      该方法获取手机当前用户当前应用内部储存缓存目录
     *      返回路径:
     *      /data/user/0/包名/cache
     *
     *      D:context.getDir(string,int(目录读取模式))
     *      该方法获取手机当前用户当前应用内部储存自定义目录.接收两个参数
     *      参数一为目录名称,参数二为读取模式
     *      返回路径:
     *      /data/user/0/包名/app_参数一
     *
     *  3.通用
     *      A:Environment.getDownloadCacheDirectory()
     *      该方法获取下载缓存目录
     *      返回路径:
     *      /cache
     *
     *      B:Environment.getRootDirectory()
     *      该方法获取系统根目录
     *      返回路径:
     *      /system
     */

    /*
       获取当前应用内置外部储存目录
     */
    fun getPackageFixedExternalDir(dirName: String = "") = ContextHandler.getApplication().getExternalFilesDir(dirName)!!

    /*
        获取内置外部储存目录
     */
    fun getFixedExternalDir(dirName: String = "") = Environment.getExternalStoragePublicDirectory(dirName)

    /*
        获取当前应用内置外部储存缓存目录
     */
    fun getPackageFixedExternalCacheDir() = ContextHandler.getApplication().externalCacheDir

    /*
        获取当前应用SD卡储存目录
     */
    fun getPackageSDExternalDir(dirName: String = ""): File? {
        ContextHandler.getApplication().getExternalFilesDirs(dirName).forEach {
            if (!it.absolutePath.contains("emulated")) {
                return it
            }
        }
        return null
    }

    /*
        获取内部储存根目录
     */
    fun getInternalDir() = Environment.getDataDirectory()

    /*
        获取当前应用内部储存文件目录
     */
    fun getPackageInternalFilesDir() = ContextHandler.getApplication().filesDir

    /*
        获取当前应用内部储存缓存目录
    */
    fun getPackageInternalCacheDir() = ContextHandler.getApplication().cacheDir

    /*
        获取当前应用内部储存自定义目录
     */
    fun getPackageInternalCustomDir(dirName: String = "", mode: Int = Context.MODE_PRIVATE) = ContextHandler.getApplication().getDir(dirName, mode)

    /*
        获取通用缓存目录
     */
    fun getCommonCacheDir() = Environment.getDownloadCacheDirectory()

    /*
        获取根目录
     */
    fun getRootDir() = Environment.getRootDirectory()

    /*
        检查SD卡是否可用
     */
    fun existSDCard(): Boolean {
        val externalStorageState = Environment.getExternalStorageState()
        return externalStorageState == Environment.MEDIA_MOUNTED
    }
}