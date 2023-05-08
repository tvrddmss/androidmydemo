package esa.mylibrary.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import java.io.IOException;
import java.io.InputStream;

import esa.mylibrary.utils.log.MyLog;

public class AssetManagerTool {

    public static Bitmap GetIcon(Context context, String name) throws IOException {

        AssetManager assetManager = context.getAssets();
        String path = "icon/" + name + ".png";
        InputStream in = assetManager.open(path);
        Bitmap bmp = BitmapFactory.decodeStream(in);
        in.close();
        return bmp;
    }

    public static Bitmap GetImg(Context context, String name) throws IOException {

        AssetManager assetManager = context.getAssets();
        String path = "img/" + name + ".png";
        InputStream in = assetManager.open(path);
        Bitmap bmp = BitmapFactory.decodeStream(in);
        in.close();
        return bmp;
    }

    public static Bitmap GetImageDefault(Context context) throws IOException {

        AssetManager assetManager = context.getAssets();
        String path = "img/errorimage.jpg";
        InputStream in = assetManager.open(path);
        Bitmap bmp = BitmapFactory.decodeStream(in);
        in.close();
        return bmp;
    }

    public static Bitmap GetUserPhotoDefault(Context context) throws IOException {

        AssetManager assetManager = context.getAssets();
        String path = "img/user_default.png";
        InputStream in = assetManager.open(path);
        Bitmap bmp = BitmapFactory.decodeStream(in);
        in.close();
        return bmp;
    }

    public static AssetFileDescriptor GetAssetFileDescriptor(Context context, String filePath) {

        try {
            AssetManager assetManager = context.getAssets();
            return assetManager.openFd(filePath);
        } catch (IOException e) {
            return null;
        }
    }


    static MediaPlayer mp;

    public static void playButtonSound(Context context) {
        try {

            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd("sound/buttonsound.mp3");
            if (assetFileDescriptor != null) {
                if (mp == null) {
                    mp = new MediaPlayer();
                    mp.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                    mp.setVolume(0.03f,0.03f);
                    mp.prepare();
                }
                mp.start();
            }
        } catch (IOException e) {
            MyLog.d(e.getMessage());
        }
    }
}
