package esa.mylibrary.utils.log;

import android.os.Handler;


/**
 * 功能描述：日志打印策略
 * 使用场景：需要打印日志并输出到本地文件夹中
 **/
public class DiskLogStrategy extends com.orhanobut.logger.DiskLogStrategy {

    private Handler handler;

    public DiskLogStrategy(Handler handler) {
        super(handler);
        this.handler = handler;
    }

    @Override
    public void log(int level, String tag, String message) {
        // do nothing on the calling thread, simply pass the tag/msg to the background thread
        handler.sendMessage(handler.obtainMessage(level, message));
    }

}