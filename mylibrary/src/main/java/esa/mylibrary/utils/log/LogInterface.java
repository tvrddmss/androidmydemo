package esa.mylibrary.utils.log;

public interface LogInterface {

    /**
     * 日志工具初始化
     *
     * @param logPath 日志输出路径
     */
    void init(String logPath, String tag);

    //程序调试bug时使用
    void d(String test);

    void i(String test);

    void w(String test);

    void e(String test);
}
