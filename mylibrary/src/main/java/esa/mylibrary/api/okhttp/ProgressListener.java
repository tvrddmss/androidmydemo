package esa.mylibrary.api.okhttp;

/**
 * 进度条监听器
 * Created by leict on 2017/6/9.
 */

public interface ProgressListener {
    /**
     * 显示进度
     *
     * @param mProgress
     */
    public void onProgress(int mProgress,long contentSize);

    /**
     * 完成状态
     *
     * @param totalSize
     */
    public void onDone(long totalSize);
}
