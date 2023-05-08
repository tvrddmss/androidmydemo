package esa.mylibrary.utils.log;

import android.os.Handler;
import android.os.HandlerThread;

import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.Logger;

public class LogHandle implements LogInterface {

    @Override
    public void init(String logPath, String tag) {
        if (logPath != "") {
            HandlerThread ht = new HandlerThread("AndroidFileLogger." + logPath);
            ht.start();
            try {
                //单个文件最大限制5M 超过则创建新文件记录日志
                int maxFileSize = 5 * 1024 * 1024;
                //日志打印线程
                Handler cxHandle = new FileWriteHandler(ht.getLooper(), logPath, maxFileSize);
                //创建缓存策略
                LogStrategy diskLogStrategy = new DiskLogStrategy(cxHandle);
                //构建格式策略
                FormatStrategy strategy = CsvFormatStrategy.newBuilder()
//                        .showThreadInfo(false)  // (可选) 是否显示线程信息，默认为ture
//                        .methodCount(0)         // (可选) 显示的方法行数，默认为2
//                        .methodOffset(7)        // (可选)
                        .logStrategy(diskLogStrategy) // (可选) 更改要打印的日志策略
                        .tag(tag)   // (可选) 每个日志的全局标记. 默认 PRETTY_LOGGER
                        .build();
                //创建适配器
                DiskLogAdapter adapter = new DiskLogAdapter(strategy);
                //设置日志适配器
                Logger.addLogAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
                ht.quit();//退出
            }
        }
    }

    //程序调试bug时使用
    public void d(String test) {
        Logger.d(test);
    }

    public void i(String test) {
        Logger.i(test);
    }

    public void w(String test) {
        Logger.w(test);
    }

    public void e(String test) {
        Logger.e(test);
    }

}
