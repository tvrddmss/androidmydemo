package esa.mylibrary.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.common
 * @ClassName: Comfirm
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/03/29 15:05
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/03/29 15:05
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Comfirm {

    public static void Comfirm(Context context, String content, CallBack callBack) {
        new AlertDialog.Builder(context).setCancelable(false).setTitle("提示").setMessage(content)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        callBack.success(null);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        callBack.error("");
                    }
                }).show();
    }
}
