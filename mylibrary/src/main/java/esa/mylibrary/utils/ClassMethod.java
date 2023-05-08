package esa.mylibrary.utils;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.utils
 * @ClassName: ClassMethod
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/03/30 9:58
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/03/30 9:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ClassMethod {
    public static String getCurrentMethod() {
        String method = Thread.currentThread().getStackTrace()[3].getMethodName();
        return method;
    }
}
