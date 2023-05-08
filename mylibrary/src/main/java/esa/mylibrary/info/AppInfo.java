package esa.mylibrary.info;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.security
 * @ClassName: AppInfo
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/03/29 14:05
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/03/29 14:05
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AppInfo {

    public static String apppagename;
    public static String appcode;
    public static String appname;
    public static String appnameen;
    public static String appVersionName;
    public static Long appVersionCode;

    public static String apkurl;

    /**
     * @param
     * @return null
     * @description 初始化
     * @author Administrator
     * @time 2023/03/29 14:07
     */
    public static void init(String pagename, String code, String name, String nameen, String versionName, Long versionCode) {
        apppagename = pagename;
        appcode = code;
        appname = name;
        appnameen = nameen;
        appVersionName = versionName;
        appVersionCode = versionCode;
    }


}
