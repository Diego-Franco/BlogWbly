package com.defch.blogwbly.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by DiegoFranco on 4/21/15.
 */
public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();
    private static final String IMG_NAME = "_image";
    private static final String FOLDER_NAME = "pictures";

    private static FileUtil fileUtil;
    private static Context context;

    public static FileUtil getInstance(Context c) {
        if(fileUtil == null) {
            fileUtil = new FileUtil(c);
        }
        return fileUtil;
    }

    private FileUtil(Context context) {
        this.context = context;
        createDirIfNotExists();
    }

    public static boolean createDirIfNotExists() {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator  + context.getPackageName());
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
                return false;
            }
        }
        return true;
    }

    private static String getPath() {
        return Environment.getExternalStorageDirectory() + File.separator  + context.getPackageName();
    }

    public void saveImages(ArrayList<Bitmap> bitmaps, int idPost) {
        for(int i = 0; i < bitmaps.size(); i++) {
            Bitmap bmp = bitmaps.get(i);
            saveIndividualImage(bmp, idPost, i);
        }
    }

   private void saveIndividualImage(Bitmap bmp, int idPost, int photoNumber) {
       File file = new File(getPath() + File.separator + idPost + IMG_NAME + photoNumber + ".png");

       FileOutputStream fos = null;
       try {
           fos = new FileOutputStream(file);
           if (fos != null) {
               bmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
               bmp.compress(Bitmap.CompressFormat.PNG, 50, fos);
               fos.close();
           }
       }catch (Exception e) {
           LogUtil.v(TAG, "error save image");
       }
   }

    public static ArrayList<Bitmap> getImagesFromFolder(int id) {
        ArrayList<Bitmap> pictures = new ArrayList<>();
        File file= new File(getPath());

        if (file.isDirectory()) {
            File[] listFile = file.listFiles();
            pictures = new ArrayList<>();
            for (int i = 0; i < listFile.length; i++) {
                if(listFile[i].getAbsolutePath().contains(id + IMG_NAME)) {
                    pictures.add(BitmapFactory.decodeFile(listFile[i].getAbsolutePath()));
                }
            }
        }
        return pictures;
    }

}
