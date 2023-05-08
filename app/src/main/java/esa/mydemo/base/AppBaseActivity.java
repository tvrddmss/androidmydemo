package esa.mydemo.base;

import android.os.Bundle;

import java.io.File;

import esa.mydemo.MyApplication;
import esa.mydemo.dal.Platform;
import esa.mydemo.dal.spring.PlatformForSpring;
import esa.mylibrary.common.CallBack;
import esa.mylibrary.utils.log.MyLog;

/**
 * @ProjectName: mydemo
 * @Package: esa.mydemo.base
 * @ClassName: AppBaseActivity
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/03/29 12:41
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/03/29 12:41
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AppBaseActivity extends esa.mylibrary.activity.BaseActivity {

    /**
     * @description 是否已检查版本信息
     * @author Administrator
     * @time 2023/04/03 10:27
     */
    protected Boolean isFirstActiviyInitComplete = false;

    /**
     * @param
     * @return null
     * @description 初始化设置，页面不可见
     * @author Administrator
     * @time 2023/03/29 15:33
     */
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);

//        //设置状态栏背景色
//        setStatusBackColor(getResources().getColor(R.color.main));
//        //设置状态栏字体色为深色
//        setStatusDarkColor(true);
//        //以上都有样式设置

        //添加本页面到集合
        ((MyApplication) getApplication()).addActivity(this);
        MyLog.d(this.getClass().getName() + "移入Activity集合");

    }



    /**
     * @param callBack
     * @return void
     * @description 第一个页面创建时，初始化检查各种版本,用于esa
     * @author tvrddmss
     * @time 2023/4/11 17:03
     */
    protected void setIsFirstActiviyInit(CallBack callBack) {
        showloading("检查APP版本。。。");

        //检测APP版本
        Platform.checkVersion(this, new CallBack() {
            @Override
            public void success(Object o) {
                //判断是否有需要安装新版本
                if (o == null) {
                    MyLog.d("检测APP版本完成，不需要更新！");
                    //检测Code版本
                    showloading("检查数据字典版本。。。");
                    Platform.checkCodeVersion(AppBaseActivity.this, new CallBack() {
                        @Override
                        public void success(Object o) {
                            MyLog.d("code更新完成");
                            isFirstActiviyInitComplete = true;
                            callBack.success("");
                        }

                        @Override
                        public void error(String message) {
                            callBack.error("检查Code版本失败：" + message);
                        }
                    });
                } else {
                    MyLog.d("检测APP版本完成，更新中！");
                    Platform.installApp(AppBaseActivity.this, (File) o);
                }

            }

            @Override
            public void error(String message) {
                callBack.error("检查APP版本失败：" + message);
            }
        });

    }

    /**
     * @param callBack
     * @return void
     * @description 第一个页面创建时，初始化检查各种版本,用于spring
     * @author tvrddmss
     * @time 2023/4/11 17:03
     */
    protected void setIsFirstActiviyInitForSpring(CallBack callBack) {
        showloading("检查APP版本。。。");

        //检测APP版本
        PlatformForSpring.checkVersion(this, new CallBack() {
            @Override
            public void success(Object o) {
                //判断是否有需要安装新版本
                if (o == null) {
                    MyLog.d("检测版本完成，不需要更新！");
                    //检测Code版本
                    isFirstActiviyInitComplete = true;
                    callBack.success("");
                } else {
                    MyLog.d("检测APP版本完成，更新中！");
                    Platform.installApp(AppBaseActivity.this, (File) o);
                }

            }

            @Override
            public void error(String message) {
                callBack.error("检查APP版本失败：" + message);
            }
        });

    }

    /**
     * @param
     * @return null
     * @description 由不可见变为可见的时候调用
     * @author Administrator
     * @time 2023/03/29 15:34
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * @param
     * @return null
     * @description 获取焦点时
     * @author Administrator
     * @time 2023/03/29 15:30
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * @param
     * @return null
     * @description 被覆盖或锁屏
     * @author Administrator
     * @time 2023/03/29 15:35
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * @param
     * @return null
     * @description 不可见后台运行
     * @author Administrator
     * @time 2023/03/29 15:35
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * @param
     * @return null
     * @description 停止状态再次启动
     * @author Administrator
     * @time 2023/03/29 15:35
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * @param
     * @return null
     * @description 页面销毁时
     * @author Administrator
     * @time 2023/03/29 15:30
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //本页面移除集合
        ((MyApplication) getApplication()).removeActivity(this);
        MyLog.d(this.getClass().getName() + "移出Activity集合");

    }


}
