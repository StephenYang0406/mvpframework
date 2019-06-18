package com.stephen.mvpframework.utils

import android.text.TextUtils
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 文件工具类
 * Created by Stephen on 2018/11/15.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object FileUtil {
    //蒋输入流保存为文件
    fun saveFile(inputStream: InputStream): String? {
        try {
            val storageDir = StorageUtil.getPackageFixedExternalDir()
            if (!storageDir.exists() || !storageDir.isDirectory)
                storageDir.mkdirs()
            val file = File(storageDir, System.currentTimeMillis().toString())
            val fos = FileOutputStream(file)
            val bis = BufferedInputStream(inputStream)
            val buffer = ByteArray(1024)
            var len: Int = bis.read(buffer)
            while (len > 0) {
                fos.write(buffer, 0, len)
                len = bis.read(buffer)
            }
            fos.flush()
            fos.close()
            bis.close()
            inputStream.close()
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }


    /**
     * 将输入流保存到指定文件
     */
    fun saveFile(inputStream: InputStream, file: File): String? {
        try {
            val fos = FileOutputStream(file)
            val bis = BufferedInputStream(inputStream)
            val buffer = ByteArray(1024)
            var len: Int = bis.read(buffer)
            while (len > 0) {
                fos.write(buffer, 0, len)
                len = bis.read(buffer)
            }
            fos.flush()
            fos.close()
            bis.close()
            inputStream.close()
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 根据系统时间、前缀、后缀产生一个文件
     */
    fun createFile(folder: File, prefix: String, suffix: String): File {
        if (!folder.exists() || !folder.isDirectory) {
            folder.mkdirs()
        }
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
        val filename = prefix + dateFormat.format(Date(System.currentTimeMillis())) + suffix
        return File(folder, filename)
    }

    /**
     * 根据文件名删除文件
     */
    fun deleteFileByName(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists() && file.delete()
    }

    /**
     * 根据后缀删除指定文件夹下文件
     */
    fun deleteFileBySuffix(folderPath: String, suffix: String) {
        Thread {
            val folder = File(folderPath)
            if (folder.listFiles() != null) {
                for (file in folder.listFiles()!!) {
                    if (TextUtils.equals(suffix, file.name.split("\\.".toRegex())[1])) {
                        LogUtil.testInfo("删除------>" + file.name)
                        file.delete()
                    }
                }
            }
        }.start()
    }
}