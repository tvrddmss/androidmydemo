package esa.mylibrary.crash;

import org.json.JSONObject;

import java.io.IOException;

import esa.mylibrary.api.ApiCallBack;
import esa.mylibrary.api.Http;
import esa.mylibrary.info.UserInfo;

/**
 * @author: Administrator
 * @date: 2023/03/28
 */
public class Control {

    private static String baseUrl = "platform/auth/app/";

    public static void addAppCrashLog(CrashEntry crashEntry) throws Exception {
        JSONObject data = new JSONObject();
        data.put("message", crashEntry.message);
        data.put("clientInf", UserInfo.getClientInfo());
        String url = baseUrl + "AddAndroidAppLog";
        Http.post(url, data, new ApiCallBack() {
            @Override
            public void onSuccess(Object o) {
            }

            @Override
            public void onError(int code) {
            }

            @Override
            public void onFailure(IOException e) {
            }
        });


    }
}
