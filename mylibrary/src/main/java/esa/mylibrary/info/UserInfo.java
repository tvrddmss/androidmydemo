package esa.mylibrary.info;

import android.content.SharedPreferences;

import org.json.JSONObject;

import esa.mylibrary.utils.MySharedPreferences;
import esa.mylibrary.utils.log.MyLog;

/**
 * @author: Administrator
 * @date: 2023/03/28
 */
public class UserInfo {


    //region 登录信息保存
    public static String getLoginUsername() {
        return MySharedPreferences.user.getString("loginUsername", "");
    }

    public static void setLoginUsername(String loginUsername) {
        loginUsername = loginUsername;
        SharedPreferences.Editor editor = MySharedPreferences.user.edit();
        editor.putString("loginUsername", loginUsername);
        editor.commit();
    }

    public static String getPassword() {
        return MySharedPreferences.user.getString("password", "");
    }

    public static void setPassword(String password) {
        password = password;
        SharedPreferences.Editor editor = MySharedPreferences.user.edit();
        editor.putString("password", password);
        editor.commit();
    }

    private static String loginUsername;
    private static String password;

    //endregion


    /**
     * @param
     * @return
     * @description 包含行政区域、权限、岗位、角色、字段等
     * @author Administrator
     * @time 2023/03/29 10:04
     */
    public static JSONObject getUserInfo() {
        return userInfo;
    }

    /**
     * @param userInfo
     * @return null
     * @description 设置用户信息
     * @author Administrator
     * @time 2023/03/30 9:50
     */
    public static void setUserInfo(JSONObject userInfo) {
        UserInfo.userInfo = userInfo;
        setClientInfo();
    }

    private static JSONObject userInfo;
//    {
//            "sys_userid": "3229",
//            "sys_username": "数据采集",
//            "sys_userloginname": "sjcj",
//            "sys_photourl": "",
//            "sys_value1": "",
//            "sys_value2": "",
//            "sys_value3": "",
//            "sys_value4": "",
//            "sys_value5": "",
//            "sys_value6": "",
//            "sys_value7": "",
//            "sys_value8": "",
//            "sys_value9": "",
//            "sys_value10": "100",
//            "sys_toporgan": "40",
//            "sys_organid": "",
//            "sys_organcode": "",
//            "sys_organname": "",
//            "sys_toporganname": "",
//            "sys_xzqy": [{
//                "id": "022",
//                "text": "天津市"
//             }],
//            "sys_roles": "",
//            "sys_rolenames": "",
//            "sys_rolenameremarks": "",
//            "sys_positionids": "",
//            "sys_positionnames": "",
//            "sys_groups": [],
//            "sys_fields": [],
//            "sys_rules": [{
//                "f_id": "3319",
//                "f_code": "0345",
//                "f_name": "app数据采集",
//                "f_url": "0102",
//                "f_target": "gzrw",
//                "f_tile": "",
//                "f_rulemodel": "3",
//                "f_sys_appcode": "40",
//                "f_children": []
//                }
//            ]
//    }

    /**
     * @description 当前设备及登录信息
     * @param
     * @return
     * @author Administrator
     * @time 2023/03/29 10:12
     */
    public static JSONObject clientInfo = new JSONObject();


    /**
     * @param
     * @return String
     * @description
     * @author Administrator
     * @time 2023/03/29 10:57
     */
    public static void setClientInfo() {
        try {
            //重置ClientInfo
            clientInfo.put("sys_userid", userInfo.getString("sys_userid"));
            clientInfo.put("userid", userInfo.getString("sys_userid"));
            clientInfo.put("username", userInfo.getString("sys_username"));
            clientInfo.put("userip", DeviceInfo.ip);
            clientInfo.put("usermac", DeviceInfo.mac);
            clientInfo.put("devicetype", "android");
            clientInfo.put("userimg", userInfo.getString("sys_photourl"));
            clientInfo.put("appcode", AppInfo.appcode);
            clientInfo.put("appname", AppInfo.appname);
            clientInfo.put("sys_xzqy", userInfo.getJSONArray("sys_xzqy"));
        } catch (Exception ex) {
            MyLog.e(UserInfo.class.getName(), ex.getMessage());
        }
    }

    /**
     * @param
     * @return 获取clientinfo
     * @description 获取clientinfo
     * @author Administrator
     * @time 2023/03/28 17:21
     */
    public static String getClientInfo() {
        return clientInfo.toString();
    }
    //{
    //	"sys_userid": "3229",
    //	"userid": "3229",
    //	"username": "数据采集",
    //	"userip": "162.16.166.229",
    //	"usermac": "f4f5dbecfbff",
    //	"devicetype": "android",
    //	"userimg": "",
    //	"appcode": "40",
    //	"appname": "在建工程抵押",
    //	"sys_xzqy": [{
    //		"id": "022",
    //		"text": "天津市"
    //	}]
    //}
}
