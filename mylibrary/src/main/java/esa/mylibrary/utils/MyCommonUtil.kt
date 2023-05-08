package esa.mylibrary.utils

import android.content.Context
import dalvik.system.DexClassLoader
import java.io.File

object MyCommonUtil {
    /**
     * 加载dex
     */
    fun loadDexClass(context: Context, fullPath: String): DexClassLoader? {
        try {
            //下面开始加载dex class
            //1.待加载的dex文件路径，如果是外存路径，一定要加上读外存文件的权限,
            //2.解压后的dex存放位置，此位置一定要是可读写且仅该应用可读写
            //3.指向包含本地库(so)的文件夹路径，可以设为null
            //4.父级类加载器，一般可以通过Context.getClassLoader获取到，也可以通过ClassLoader.getSystemClassLoader()取到。
//            val cacheFile = File(context.filesDir, "dex")
//            val internalPath = cacheFile.absolutePath + File.separator + dexName

//            return DexClassLoader(internalPath, cacheFile.absolutePath, null, context.classLoader)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 加载dex
     */
    fun loadDexClass(context: Context, file: File): DexClassLoader? {
        try {
            //下面开始加载dex class
            //1.待加载的dex文件路径，如果是外存路径，一定要加上读外存文件的权限,
            //2.解压后的dex存放位置，此位置一定要是可读写且仅该应用可读写
            //3.指向包含本地库(so)的文件夹路径，可以设为null
            //4.父级类加载器，一般可以通过Context.getClassLoader获取到，也可以通过ClassLoader.getSystemClassLoader()取到。
//            val cacheFile = File(context.filesDir, "dex")
//            val internalPath = cacheFile.absolutePath + File.separator + dexName

            return DexClassLoader(
                file.absolutePath,
                file.absolutePath.substring(0, file.absolutePath.lastIndexOf(File.separator)),
                null,
                context.classLoader
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    /**
     * 启动Dex中的Activity
     * @param context
     * @param actClas Activity全绝对路径类名，如com.demon.dexlib.TestActivity
     */
    fun getClass(loader: DexClassLoader?, actClas: String): Class<*>? {

        // 加载Activity类
        // 该类中有可执行方法 test()
        var clazz: Class<*>? = null
        try {
            clazz = loader?.loadClass(actClas)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return clazz
    }

}