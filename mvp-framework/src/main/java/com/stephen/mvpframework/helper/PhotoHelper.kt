package com.stephen.mvpframework.helper

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.stephen.mvpframework.handler.ContextHandler
import com.stephen.mvpframework.local.Content
import com.stephen.mvpframework.ui.activity.AbstractActivity
import com.stephen.mvpframework.utils.FileUtil
import com.stephen.mvpframework.utils.LogUtil
import com.stephen.mvpframework.utils.StorageUtil
import java.io.File

/**
 * 照片帮助类
 * Created by Stephen on 2018/11/19.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object PhotoHelper {
    //当前拍照文件
    private var currentPhotoFile: File? = null

    /*
        带前缀后缀拍照
     */
    fun takePhoto(prefix: String = "IMG", suffix: String = ".jpg") {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        if (takePictureIntent.resolveActivity(ContextHandler.currentActivity().packageManager) != null) {
            //获取通用Android相册目录
            val photoFolder = StorageUtil.getFixedExternalDir(Environment.DIRECTORY_DCIM)
            //照片路径
            val photoFile: File = FileUtil.createFile(photoFolder, prefix, suffix)
            val photoUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(ContextHandler.currentActivity(),
                        ContextHandler.currentActivity().packageName + ".provider", photoFile)
            } else {
                Uri.fromFile(photoFile)
            }
            LogUtil.testInfo("photoUri----->$photoUri")
            currentPhotoFile = photoFile
            // 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            // 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
            // 可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
            // 如果没有指定uri，则data就返回有数据！
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            if (ContextHandler.current() is AbstractActivity) {
                ContextHandler.currentActivity().startActivityForResult(takePictureIntent,
                        Content.Params.TAKE_PHOTO_REQUEST_CODE)
            } else {
                ContextHandler.currentFragment().startActivityForResult(takePictureIntent,
                        Content.Params.TAKE_PHOTO_REQUEST_CODE)
            }
            LogUtil.testInfo("photoPath---->" + photoFile.absolutePath)
        }
    }

    /*
        通知系统添加图片,并返回图片文件
     */
    fun galleryAddPic(): File? {
        return if (currentPhotoFile != null) {
            LogUtil.testInfo("file--" + currentPhotoFile?.absolutePath)
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(currentPhotoFile)
            mediaScanIntent.data = contentUri
            ContextHandler.currentActivity().sendBroadcast(mediaScanIntent)
            currentPhotoFile
        } else {
            null
        }
    }

}