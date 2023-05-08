package esa.mylibrary.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.osmdroid.views.overlay.compass.IOrientationConsumer;
import org.osmdroid.views.overlay.compass.IOrientationProvider;

import esa.mylibrary.utils.log.MyLog;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.sensor
 * @ClassName: InternalCompassOrientationProvider
 * @Description: java类作用描述
 * @Author: tvrddmss
 * @CreateDate: 2023/4/11 16:10
 * @UpdateUser: tvrddmss
 * @UpdateDate: 2023/4/11 16:10
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class InternalCompassOrientationProvider implements SensorEventListener, IOrientationProvider {


    //region 静态类

    /**
     * 静态内部类单例
     * 优点：外部类加载的时候并不需要立即去加载内部类，内部类不被加载则不会实例化mInstance，不占内存资源，
     * 保证单例的唯一性，同时也延迟了单例的实例化。
     */
    private static InternalCompassOrientationProvider mInstance;

    /**
     * @return esa.mylibrary.sensor.MySensor
     * @description
     * @author Administrator
     * @time 2023/04/04 16:44
     */
    public static InternalCompassOrientationProvider getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new InternalCompassOrientationProvider(context);
        }
        return mInstance;
    }

    //endregion

    private IOrientationConsumer mOrientationConsumer;
    private SensorManager mSensorManager;
    private float mAzimuth;

    public InternalCompassOrientationProvider(Context context) {
        try {
            mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        } catch (Exception ex) {
            MyLog.e(ex.getMessage());
        }
    }

    //开启方向监听
    @Override
    public boolean startOrientationProvider(IOrientationConsumer orientationConsumer) {
        mOrientationConsumer = orientationConsumer;
        boolean result = false;

        final Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (sensor != null) {
            result = mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
        return result;
    }

    @Override
    public void stopOrientationProvider() {
        mOrientationConsumer = null;
        mSensorManager.unregisterListener(this);
    }

    @Override
    public float getLastKnownOrientation() {
        return mAzimuth;
    }

    @Override
    public void destroy() {
        stopOrientationProvider();
        mOrientationConsumer = null;
        mSensorManager = null;
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
        // This is not interesting for us at the moment
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            if (event.values != null) {
                mAzimuth = event.values[0];
                MyLog.d("角度：" + event.values[0] + "," + event.values[1] + "," + event.values[2]);
                if (mOrientationConsumer != null)
                    mOrientationConsumer.onOrientationChanged(mAzimuth, this);
            }
        }
    }
}

