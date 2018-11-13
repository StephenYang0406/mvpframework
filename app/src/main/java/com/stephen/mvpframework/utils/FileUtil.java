package com.stephen.mvpframework.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.stephen.mvpframework.local.Content;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文件工具类
 * Created by Stephen on 2018/1/12.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class FileUtil {
    //蒋输入流保存为文件
    public static String saveFile(InputStream is) {
        try {
            String storagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    + File.separator + Content.Params.FOLDER_NAME + File.separator + Content.Params.FILE_FOLDER_NAME;
            File storageFile = new File(storagePath);
            if (!storageFile.exists() || !storageFile.isDirectory())
                storageFile.mkdirs();
            File file = new File(storageFile, String.valueOf(System.currentTimeMillis()));
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            bis.close();
            is.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将输入流保存到指定文件
     */
    public static String saveFile(InputStream is, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            bis.close();
            is.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据系统时间、前缀、后缀产生一个文件
     */
    public static File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory()) {
            boolean mkdirs = folder.mkdirs();
            if (mkdirs) {

            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }

    /**
     * 根据文件名删除文件
     */
    public static boolean deleteFileByName(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.delete();
    }

    /**
     * 根据后缀删除指定文件夹下文件
     */
    public static void deleteFileBySuffix(String folderPath, String suffix) {
        new Thread(() -> {
            File folder = new File(folderPath);
            if (folder.listFiles() != null) {
                for (File file : folder.listFiles()) {
                    if (TextUtils.equals(suffix, (file.getName().split("\\.")[1]))) {
                        LogUtil.testInfo("删除------>" + file.getName());
                        //noinspection ResultOfMethodCallIgnored
                        file.delete();
                    }
                }
            }
        }).start();
    }
}
