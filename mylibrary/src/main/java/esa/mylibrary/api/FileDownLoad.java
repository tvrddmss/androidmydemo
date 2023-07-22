package esa.mylibrary.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import esa.mylibrary.common.CallBack;
import esa.mylibrary.utils.log.MyLog;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.api
 * @ClassName: FileDownLoad
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/03/29 15:46
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/03/29 15:46
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FileDownLoad {


    /**
     * @description 下载进度
     * @author Administrator
     * @time 2023/03/29 15:15
     */
    private static ProgressDialog pd;

    /**
     * @param
     * @return null
     * @description 下载APP
     * @author Administrator
     * @time 2023/03/29 14:56
     */
    public static void downloadFile(final Context context, final String appDownLoadUrl, CallBack callBack) {

        pd = new ProgressDialog(context);
        pd.setMessage("正在下载...");
//        pd.setMax(filesize);
        //这里设置为不可以通过按取消按钮关闭进度条
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //这里设置的是是否显示进度,设为false才是显示的哦！
        pd.setIndeterminate(false);
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                InputStream in = null;
                FileOutputStream out = null;
                HttpURLConnection conn = null;
                try {
                    url = new URL(appDownLoadUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    long fileLength = conn.getContentLength();
                    pd.setMax(100);
                    in = conn.getInputStream();
                    File filePath = creatFile(appDownLoadUrl);
                    out = new FileOutputStream(filePath);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    long readedLength = 0l;
                    while ((len = in.read(buffer)) != -1) {
                        // 用户点击“取消”按钮，下载中断
//                        if (isCancel) {
//                            break;
//                        }
                        out.write(buffer, 0, len);
                        readedLength += len;
                        pd.setProgress((int) (((float) readedLength / fileLength) * 100));
//                        handler.sendEmptyMessage(UPDATE_TOKEN);
                        if (readedLength >= fileLength) {
                            pd.dismiss();
                            // 下载完毕，通知安装
//                            handler.sendEmptyMessage(INSTALL_TOKEN);
                            callBack.success(filePath);
                            break;
                        }
                    }
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    MyLog.e(e.getMessage());
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }


    /**
     * @param url
     * @return File
     * @description 根据url创建本地文件
     * @author Administrator
     * @time 2023/03/29 15:00
     */
    private static File creatFile(String url) {

        //判断Download文件夹是否存在
        String dirPath = Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "Download";
        File folder = new File(dirPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        //创建文件
        File files = new File(dirPath, getFileName(url));
        return files;
    }


    /**
     * @param url
     * @return File
     * @description 根据url创建本地文件
     * @author Administrator
     * @time 2023/03/29 15:00
     */
    private static File creatFileByContext(Context context, String url) {
        //判断Download文件夹是否存在
        String dirPath = context.getFilesDir().getPath();
        File folder = new File(dirPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        //创建文件
        File files = new File(dirPath, getFileName(url));
        return files;
    }



    /**
     * @param url
     * @return String
     * @description 获取URL文件名
     * @author Administrator
     * @time 2023/03/29 15:00
     */
    private static String getFileName(String url) {
        return url.substring(url.lastIndexOf("/"), url.length());
    }
}
