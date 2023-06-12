package esa.mydemo.dal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import esa.mylibrary.api.ApiCallBack;
import esa.mylibrary.api.FileDownLoad;
import esa.mylibrary.api.Http;
import esa.mylibrary.common.CallBack;
import esa.mylibrary.common.Comfirm;
import esa.mylibrary.config.Config;
import esa.mylibrary.info.AppInfo;
import esa.mylibrary.info.CodeInfo;
import esa.mylibrary.info.DeviceInfo;
import esa.mylibrary.info.UserInfo;
import esa.mylibrary.utils.MyJson;
import esa.mylibrary.utils.log.MyLog;

/**
 * @ProjectName: mydemo
 * @Package: esa.mydemo.dal
 * @ClassName: Platform
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/03/30 9:39
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/03/30 9:39
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Platform {


    /**
     * @description 基础网络路径
     * @author Administrator
     * @time 2023/03/30 9:51
     */
    public static String baseUrl = "platform/auth/app/";

    /**
     * @param context
     * @return null
     * @description 检测版本
     * @author Administrator
     * @time 2023/03/29 15:05
     */
    public static void checkVersion(Context context, CallBack callBack) {
        String url = baseUrl + "checkVersion";
        try {
            //app版本对比
            JSONObject data = new JSONObject();
            data.put("name", AppInfo.appnameen);
            data.put("version", AppInfo.appVersionName);
            data.put("deviceTypeString", DeviceInfo.platform);
            data.put("clientInf", UserInfo.getClientInfo());
            Http.post(url, data, new ApiCallBack() {
                @Override
                public void onSuccess(Object o) {
                    try {
                        JSONObject jsonObject = (JSONObject) MyJson.parse(o.toString());
                        if (jsonObject.getString("update").equals("true")) {
                            AppInfo.apkurl = jsonObject.getString("url");
                            Comfirm.Comfirm(context, "有新版本，请下载安装！", new CallBack() {
                                @Override
                                public void success(Object o) {
                                    FileDownLoad.downloadFile(context, AppInfo.apkurl, new CallBack() {
                                        @Override
                                        public void success(Object o) {
//                                            installApp(context, (File) o);
                                            callBack.success(o);
                                        }

                                        @Override
                                        public void error(String message) {
//                                            Config.showErrorMessage(url + "APK下载错误：" + message);
                                            callBack.error(Platform.class.getName() + "\r\n" + AppInfo.apkurl + ",\r\nAPK下载错误！" + message);
                                        }
                                    });
                                }

                                @Override
                                public void error(String message) {
                                    AppInfo.apkurl = "";
                                    callBack.success(null);
                                }
                            });
                        } else {
                            AppInfo.apkurl = "";
                            callBack.success(null);
                        }
                    } catch (Exception ex) {
                        callBack.error(Platform.class.getName() + "\r\n" + url + ",\r\n网络请求结果解析失败！" + ex.getMessage());
                    }
                }

                @Override
                public void onError(int code) {
                    callBack.error(Platform.class.getName() + "\r\n" + url + ",\r\n网络请求错误！错误编码：" + code);
                }

                @Override
                public void onFailure(IOException e) {
                    callBack.error(Platform.class.getName() + "\r\n" + url + ",\r\n网络请求失败！失败：" + e.toString());
                }
            });

        } catch (Exception ex) {
            callBack.error(Platform.class.getName() + "\r\n" + url + ",\r\n,报错：" + ex.getMessage());
        }
    }

    /**
     * @param
     * @return null
     * @description
     * @author Administrator
     * @time 2023/03/29 15:55
     */
    public static void installApp(Context context, File appFile) {
        try {
            //提示
            Toast.makeText(context, "开始更新", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (!appFile.exists()) {
                MyLog.e("apk文件不存在!" + appFile.getAbsolutePath());
                throw new Exception("文件不存在！");
            }
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                uri = FileProvider.getUriForFile(context.getApplicationContext(), AppInfo.apppagename + ".provider", appFile);
            } else {
                uri = Uri.fromFile(appFile);
            }
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            MyLog.e(e.getMessage());
            //网页下载
            Uri uri = Uri.parse(AppInfo.apkurl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //检测是否可以正常打开
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                // 网址正确 跳转成功
                context.startActivity(intent);
            } else {
                //提示
                Toast.makeText(context, "无法打开浏览器", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * @param context
     * @param callBack
     * @return void
     * @description
     * @author Administrator
     * @time 2023/03/31 11:31
     */
    public static void checkCodeVersion(Context context, CallBack callBack) {
        String url = baseUrl + "checkCodeVersion";
        try {
            JSONObject data = new JSONObject();
            data.put("codeversion", CodeInfo.codeVersion);
            data.put("clientInf", UserInfo.getClientInfo());
            Http.post(url, data, new ApiCallBack() {
                @Override
                public void onSuccess(Object o) {
                    try {
                        JSONObject jsonObject = (JSONObject) MyJson.parse(o.toString());
                        CodeInfo.setCode(jsonObject);
                        callBack.success(null);
                    } catch (Exception ex) {
                        MyLog.e(this.getClass() + ":" + ex.getMessage());
                        callBack.error(Platform.class.getName() + "\r\n" + url + ",\r\n网络请求返回结果解析失败：" + ex.getMessage());
                    }
                }

                @Override
                public void onError(int code) {
                    callBack.error(Platform.class.getName() + "\r\n" + url + ",\r\n网络请求错误！错误编码：" + code);
                }

                @Override
                public void onFailure(IOException e) {
                    callBack.error(Platform.class.getName() + "\r\n" + url + ",\r\n网络请求失败！失败：" + e.toString());
                }
            });

        } catch (Exception ex) {
            callBack.error(Platform.class.getName() + "\r\n" + url + ",\r\n报错：" + ex.getMessage());
        }
    }

    /**
     * @param userLoginNameString, userPasswordString,  callBack
     * @return null
     * @description 登录
     * @author Administrator
     * @time 2023/03/30 9:52
     */
    public static void Login(String userLoginNameString, String userPasswordString, CallBack callBack) {
        String url = baseUrl + "UserLogin";
        try {
            JSONObject data = new JSONObject();
            data.put("userLoginNameString", userLoginNameString);
            data.put("userPasswordString", userPasswordString);
            data.put("deviceTypeString", DeviceInfo.platform);
            data.put("deviceTokenString", DeviceInfo.deviceTokenString);
            data.put("clientInf", UserInfo.getClientInfo());

            Http.post(url, data, new ApiCallBack<JSONObject>() {
                /**
                 * @description result=true
                 * @param o
                 * @return void
                 * @author Administrator
                 * @time 2023/03/30 9:48
                 */
                @Override
                public void onSuccess(JSONObject o) {
                    try {
                        JSONObject jsonObject = o;
                        Config.api.loginToken = jsonObject.getString("loginToken");
                        UserInfo.setUserInfo(jsonObject.getJSONObject("userinf"));
                        callBack.success(null);
                    } catch (Exception ex) {
                        MyLog.e(this.getClass() + ":" + ex.getMessage());
                        callBack.error(Platform.class.getName() + "\r\n" + url + ",\r\n网络请求返回结果解析失败：" + ex.getMessage());
                    }
                }

                /**
                 * @description 网络错误
                 * @param code
                 * @return void
                 * @author Administrator
                 * @time 2023/03/30 9:49
                 */
                @Override
                public void onError(int code) {
                    callBack.error(Platform.class.getName() + "\r\n" + url + ",\r\n网络请求错误！错误编码：" + code);
                }

                /**
                 * @description result=false
                 * @param e
                 * @return void
                 * @author Administrator
                 * @time 2023/03/30 9:48
                 */
                @Override
                public void onFailure(IOException e) {
                    callBack.error(Platform.class.getName() + "\r\n" + url + ",\r\n网络请求失败！失败：" + e.toString());
                }
            });

        } catch (Exception ex) {
            callBack.error(Platform.class.getName() + "\r\n" + url + ",\r\n,报错：" + ex.getMessage());
        }

    }

}
