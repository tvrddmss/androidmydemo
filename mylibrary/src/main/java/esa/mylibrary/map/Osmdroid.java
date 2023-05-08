package esa.mylibrary.map;

import android.content.Context;

import org.osmdroid.config.Configuration;

import java.io.File;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.map
 * @ClassName: Osmdroid
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/04/03 15:41
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/04/03 15:41
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Osmdroid {

    static String cacah_path;

    /**
     * @description
     * @param context
     * @return void
     * @author Administrator
     * @time 2023/04/03 15:41
     */
    public static void init(Context context) {
        cacah_path = context.getFilesDir() + "/osmdroid/tiles/";//Environment.getExternalStorageDirectory() +
        //Log(MapTool.cacah_path,4);
        //设置Osmdroid的文件路径，需要在MapView初始化之前进行设置
        Configuration.getInstance().setOsmdroidBasePath(new File(cacah_path));
        //设置Osmdroid的瓦片缓存路径
        Configuration.getInstance().setOsmdroidTileCache(new File(cacah_path));
    }
}
