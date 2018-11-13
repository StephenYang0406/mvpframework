package com.stephen.mvpframework.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;


import com.stephen.mvpframework.context.ContextHandler;
import com.stephen.mvpframework.local.Content;
import com.stephen.mvpframework.ui.activity.AbstractActivity;

import java.io.File;
import java.io.IOException;


/**
 * 照片工具类
 * Created by Stephen on 2018/1/12.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class PhotoUtil {

    private static File mCurrentTakePhoto;//当前拍照的图片,扫描后清除

    //拍照,并指定照片前缀和后缀
    private static File takePhoto(String prefix, String suffix) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        File photoFile = null;
        if (takePictureIntent.resolveActivity(ContextHandler.currentActivity().getPackageManager()) != null) {
            File photoFolder;
            if (SystemUtil.existSDCard())
                photoFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                        File.separator + Content.Params.FOLDER_NAME
                                + File.separator + Content.Params.PICTURE_FOLDER_NAME);
            else
                photoFolder = new File(Environment.getDataDirectory(),
                        File.separator + Content.Params.FOLDER_NAME
                                + File.separator + Content.Params.PICTURE_FOLDER_NAME);
            if (TextUtils.isEmpty(prefix))
                prefix = "IMG";
            if (TextUtils.isEmpty(suffix))
                suffix = ".jpg";
            photoFile = FileUtil.createFile(photoFolder, prefix, suffix);
            try {
                boolean newFile = photoFile.createNewFile();
//                if (newFile) {
//
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri photoUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoUri = FileProvider.getUriForFile(ContextHandler.currentActivity(),
                        ContextHandler.currentActivity().getPackageName() + ".provider"
                        , photoFile);
            } else {
                photoUri = Uri.fromFile(photoFile);
            }
            LogUtil.testInfo("photoUri----->" + photoUri.toString());
            mCurrentTakePhoto = photoFile;
            // 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            // 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
            // 可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
            // 如果没有指定uri，则data就返回有数据！
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            if (ContextHandler.current() instanceof AbstractActivity)
                ContextHandler.currentActivity().startActivityForResult(takePictureIntent,
                        Content.Params.TAKE_PHOTO_REQUEST_CODE);
            else
                ContextHandler.currentFragment().startActivityForResult(takePictureIntent,
                        Content.Params.TAKE_PHOTO_REQUEST_CODE);
            LogUtil.testInfo("photoPath---->" + photoFile.getAbsolutePath());
        }
        return photoFile;
    }

    //默认拍照
    public static File takePhoto() {
        return takePhoto("IMG", ".jpg");
    }

    //拍照扫描图片
    public static File galleryAddPic() {
        if (mCurrentTakePhoto != null) {
            LogUtil.testInfo("file--" + mCurrentTakePhoto.getAbsolutePath());
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(mCurrentTakePhoto);
            mediaScanIntent.setData(contentUri);
            ContextHandler.currentActivity().sendBroadcast(mediaScanIntent);
            return mCurrentTakePhoto;
        } else
            return null;
    }

    //选择照片
//    public static void pickPhoto(int maxPhotoNum) {
//        if (ContextHandler.current() instanceof AbstractActivity)
//            PhotoPicker.builder()
//                    .setPhotoCount(maxPhotoNum)
//                    .setShowCamera(true)
//                    .setPreviewEnabled(false)
//                    .setShowCamera(false)
//                    .start(ContextHandler.currentActivity(), Content.Params.PICK_PHOTO_REQUEST_CODE);
//        else
//            PhotoPicker.builder()
//                    .setPhotoCount(maxPhotoNum)
//                    .setShowCamera(true)
//                    .setPreviewEnabled(false)
//                    .setShowCamera(false)
//                    .start(ContextHandler.currentFragment().getActivity(), Content.Params.PICK_PHOTO_REQUEST_CODE);
//    }

}
