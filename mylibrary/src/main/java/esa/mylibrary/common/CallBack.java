package esa.mylibrary.common;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.common
 * @ClassName: CallBack
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/03/29 15:06
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/03/29 15:06
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public abstract class CallBack<T> {
    public abstract void success(T t);
    public abstract void error(String message);
}
