package esa.mylibrary.map

import android.content.Context
import org.osmdroid.config.Configuration
import java.io.File

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
object Osmdroid {
    private lateinit var cacahPath: String

    /**
     * @param context
     * @return void
     * @description
     * @author Administrator
     * @time 2023/04/03 15:41
     */
    @JvmStatic
    fun init(context: Context) {
        cacahPath = context.filesDir.toString() + "/osmdroid/tiles/"
        //设置Osmdroid的文件路径，需要在MapView初始化之前进行设置
        Configuration.getInstance().osmdroidBasePath = File(cacahPath)
        //设置Osmdroid的瓦片缓存路径
        Configuration.getInstance().osmdroidTileCache = File(cacahPath)
    }

    fun clear() {
        val dir = File(cacahPath)
        deleteDir(dir)
    }

    private fun deleteDir(dir: File?) {
        if (dir == null || !dir.exists() || !dir.isDirectory) {
            return
        }
        for (file in dir.listFiles()) {
            if (file.isFile) {
                file.delete()
            } else {
                deleteDir(file)
            }
        }
        dir.delete()
    }
}