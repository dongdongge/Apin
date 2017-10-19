package com.android.apin.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by apin on 2017/10/18.
 */

public class DownloadImgUtils {

    /**
     * 获取SDCard的目录路径功能
     *
     * @return
     */
    public static String getSDCardPath() {
        File sdcardDir = null;
        //判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }

    public static void getNetworkImgUrlToSaveLocalFile(final String imgUrl, final String imgFilePath, final String imgName, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                InputStream is = null;
                FileOutputStream fos = null;
                Bitmap bitmap = null;
                try {
                    url = new URL(imgUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    File file = new File(imgFilePath);
                    if (!file.exists()){
                        file.mkdirs();
                    }
                    File ImgFile = new File(file,imgName);
                    if (!ImgFile.exists()){
                        ImgFile.createNewFile();
                    }

                    File file1 = new File(imgFilePath+imgName);
                    if (!file1.exists()){
                        file1.getParentFile().mkdirs();
                        file1.createNewFile();
                    }
                    fos = new FileOutputStream(file1);
                    if (httpURLConnection.getResponseCode() == 200) {
                        is = httpURLConnection.getInputStream();
                        byte[] bytes = new byte[1024];
                        int len = 0;
                        while ((len = is.read(bytes)) != -1) {
                            fos.write(bytes, 0, len);
                        }
                        fos.flush();
                        Message message = new Message();
                        message.what=300;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is!=null){
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static Bitmap decodeImage(String filePath) {
        /** Decode image size */
        BitmapFactory.Options o = new BitmapFactory.Options();
        /** 只取宽高防止oom */
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
//        int scale=calculateInSampleSize(o, displayStats.maxItemWidthHeight, displayStats.maxItemWidthHeight);
        BitmapFactory.Options options=new BitmapFactory.Options();
        /** Decode with inSampleSize，比直接算出options中的使用更少的内存*/
        options.inSampleSize=2;
        /** 内存不足的时候可被擦除 */
        options.inPurgeable = true;
        /** 深拷贝 */
        options.inInputShareable = true;
        Bitmap result = BitmapFactory.decodeFile(filePath, options);
        return result;
    }

}
