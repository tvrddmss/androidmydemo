package esa.mylibrary.sensor;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Observable;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.sensor
 * @ClassName: MySensor
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/04/06 13:33
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/04/06 13:33
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MySensor extends Observable {


    /**
     * 静态内部类单例
     * 优点：外部类加载的时候并不需要立即去加载内部类，内部类不被加载则不会实例化mInstance，不占内存资源，
     * 保证单例的唯一性，同时也延迟了单例的实例化。
     */
    private static class MySensorHolder {
        private static MySensor mInstance = new MySensor();
    }

    /**
     * @return esa.mylibrary.sensor.MySensor
     * @description
     * @author Administrator
     * @time 2023/04/04 16:44
     */
    public static MySensor getInstance() {
        return MySensor.MySensorHolder.mInstance;
    }

    //传感器管理
    private SensorManager sensorManager;
    //传感器
    private Sensor sensor;

    /**
     * @param context
     * @return void
     * @description 初始化
     * @author Administrator
     * @time 2023/04/06 13:42
     */
    public void init(Context context) {
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
//        //已弃用
//        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        /*查看手机所有传感器类型
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor s : sensorList){
            System.out.println(s.getName());
        }
        */
        Sensoreventlistener ll = new Sensoreventlistener();
        sensorManager.registerListener(ll, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        //注册加速度传感器监听
        Sensor acceleSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(ll, acceleSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //注册磁场传感器监听
        Sensor magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(ll, magSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    //加速度参数与磁场参数
    private float[] gravity = null;
    private float[] geomagnetic = null;

    //上一次刷新时间
    private long lastrefresh = System.currentTimeMillis();
    //刷新频率
    private long tik = 500;
    //方位角、俯仰角与翻滚角
    public float[] values = new float[3];

    //传感器监听类
    private class Sensoreventlistener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            if (System.currentTimeMillis() - lastrefresh > tik) {
                lastrefresh = System.currentTimeMillis();
                try {
                    switch (sensorEvent.sensor.getType()) {
                        case Sensor.TYPE_ACCELEROMETER: //加速度传感器
                            gravity = sensorEvent.values.clone();
                            break;
                        case Sensor.TYPE_MAGNETIC_FIELD://磁场传感器
                            geomagnetic = sensorEvent.values.clone();
                            break;
                    }
                    //计算-方位角、俯仰角与翻滚角
                    if (gravity != null && geomagnetic != null) {
                        float[] R = new float[9];
                        if (SensorManager.getRotationMatrix(R, null, gravity, geomagnetic)) {
                            SensorManager.getOrientation(R, values);

//                            //横屏
//                            currentDegreeLandscape = (float) ((360f + values[0] * 180f / Math.PI) % 360);
//                            //右上横屏
//                            currentDegreeLandscape = (currentDegreeLandscape + 90) % 360;
//                            //右下横屏
//                            if (values[2] > 0) {
//                                currentDegreeLandscape = (currentDegreeLandscape + 180) % 360;
//                            }

//                            //竖屏
//                            currentDegreePortrait = (float) ((360f + values[0] * 180f / Math.PI) % 360);
//
//                            //判断是否有翻转
//                            //取绝对值
//                            float v2 = (values[2] < 0) ? -values[2] : values[2];
//                            if (v2 > Math.PI / 2) {
//                                currentDegreePortrait = (currentDegreePortrait + 180) % 360;
//                            }

                            updateView();
                        }
                    }

                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


    public float angle;

    /**
     * @param
     * @return void
     * @description 更新数据
     * @author Administrator
     * @time 2023/04/04 16:52
     */
    private void updateView() {
        //竖屏
        angle = (float) ((360f + values[0] * 180f / Math.PI) % 360);

        //判断是否有翻转
        //取绝对值
        float v2 = (values[2] < 0) ? -values[2] : values[2];
        if (v2 > Math.PI / 2) {
            angle = (angle + 180) % 360;
        }

        //被观察者的通知更新
        setChanged();
        notifyObservers();
    }


}
