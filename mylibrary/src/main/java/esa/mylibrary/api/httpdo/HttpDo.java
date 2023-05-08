package esa.mylibrary.api.httpdo;

import java.io.IOException;

import esa.mylibrary.api.ApiCallBack;
import okhttp3.Call;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.api.httpdo
 * @ClassName: HttpDo
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/04/23 15:44
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/04/23 15:44
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class HttpDo {


    public void run(HttpDoParameter httpDoParameter) {
        doSync(httpDoParameter);
    }

    public synchronized void doSync(HttpDoParameter httpDoParameter) {
    }


    private boolean checkMainThread(HttpDoParameter httpDoParameter) {
        //检查线程是否存在避免报错
        boolean result = true;
//        switch (httpDoParameter.mainThread.getState()) {
//            case NEW://这个状态最简单，但我们 new 出一个线程对象，并没有调用 start 方法时，它就处于 New 状态，例如下面的代码。
//                break;
//            case RUNNABLE://运行中，包含  Running 、 Ready To Run 不区分
//                break;
//            case BLOCKED:
//                //(1)需要等待监视器锁去进入一个同步块时。
//                //(2)在一个同步块中，执行 Object.wait 过后，再次等待进入同步块时。等待咖啡机加牛奶
//                break;
//            case WAITING://等待生产者通知
//                break;
//            case TIMED_WAITING://等待生产者通知,有失效性
//                break;
//            case TERMINATED://线程执行结束过后，就进入了 TERMINATED 状态。且无法再恢复到其他状态中去了。
//                result = false;
//                break;
//        }
//        MyLog.d(httpDoParameter.mainThread.getName() + "--" + httpDoParameter.mainThread.getState());
        return result;
    }

    /**
     * 成功信息
     *
     * @param callBack
     * @param result
     */
    protected void sendonSuccessMessage(HttpDoParameter httpDoParameter, final ApiCallBack callBack, final Object result) {
        if (!checkMainThread(httpDoParameter)) {
            return;
        }
        httpDoParameter.mHnadler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(result);
            }
        });
    }

    /**
     * 失败信息
     *
     * @param callBack
     * @param call
     * @param e
     */
    protected void sendonFailureMessage(HttpDoParameter httpDoParameter, final ApiCallBack callBack, final Call call, final IOException e) {
        if (!checkMainThread(httpDoParameter)) {
            return;
        }
        httpDoParameter.mHnadler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onFailure(e);
            }
        });
    }

    /**
     * 错误信息
     *
     * @param callBack
     * @param code
     */
    protected void sendonErrorMessage(HttpDoParameter httpDoParameter, final ApiCallBack callBack, final int code) {
        if (!checkMainThread(httpDoParameter)) {
            return;
        }
        httpDoParameter.mHnadler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(code);
            }
        });
    }

}
