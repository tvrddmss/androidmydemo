package esa.mylibrary.gps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.Observable;

import esa.mylibrary.common.CallBack;
import esa.mylibrary.common.Comfirm;
import esa.mylibrary.utils.log.MyLog;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.gps
 * @ClassName: GpsConfig
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/04/03 17:12
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/04/03 17:12
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MyGps extends Observable {

    private final String TAG = "GpsConfig";

    //当前位置
    private Location location;


    /**
     * 静态内部类单例
     * 优点：外部类加载的时候并不需要立即去加载内部类，内部类不被加载则不会实例化mInstance，不占内存资源，
     * 保证单例的唯一性，同时也延迟了单例的实例化。
     */
    private static class GpsConfigHolder {
        private static MyGps mInstance = new MyGps();
    }

    /**
     * @return esa.mylibrary.gps.GpsConfig
     * @description
     * @author Administrator
     * @time 2023/04/04 16:44
     */
    public static MyGps getInstance() {
        return GpsConfigHolder.mInstance;
    }


    LocationManager lm;
    LocationListener locationListener;

    /**
     * @param context
     * @return void
     * @description GPS初始化
     * @author Administrator
     * @time 2023/04/04 16:54
     */
    public void init(Context context) {

        MyLog.d(TAG, "GPS初始化");
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            askLocationSettings(context);
            return;
        }
        initiative(context);
        //定义监听
        locationListener = new LocationListener() {
            /**
             * 位置信息变化时触发
             */
            public void onLocationChanged(Location location) {
                updateView(location);
//                MyLog.d(TAG, "时间：" + location.getTime());
//                MyLog.d(TAG, "经度：" + location.getLongitude());
//                MyLog.d(TAG, "纬度：" + location.getLatitude());
//                MyLog.d(TAG, "海拔：" + location.getAltitude());
            }

            /**
             * GPS状态变化时触发
             */
            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch (status) {
                    //GPS状态为可见时
                    case LocationProvider.AVAILABLE:
                        MyLog.d(TAG, "当前GPS状态为可见状态");
                        break;
                    //GPS状态为服务区外时
                    case LocationProvider.OUT_OF_SERVICE:
                        MyLog.d(TAG, "当前GPS状态为服务区外状态");
                        break;
                    //GPS状态为暂停服务时
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        MyLog.d(TAG, "当前GPS状态为暂停服务状态");
                        break;
                }
            }

            /**
             * GPS开启时触发
             */
            public void onProviderEnabled(String provider) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    return;
                }
                //主动获取
                Location location = lm.getLastKnownLocation(provider);
                updateView(location);
            }

            /**
             * GPS禁用时触发
             */
            public void onProviderDisabled(String provider) {
                updateView(null);
            }
        };

        //开启监听
        Criteria crt = new Criteria();                        // 位置监听标准
        crt.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);    // 水平精度高
        crt.setAltitudeRequired(true);                        // 需要高度
        String provider = lm.getBestProvider(crt, true);    // 寻找最匹配的provider

        location = lm.getLastKnownLocation(provider);    // 取最后已知位置，即缓存中的位置
//        if (l != null) tvResult.append(provider + "-LastKnown:" + l.toString() + "\n");
        long period = Long.parseLong("1");        // 最小时间间隔
        int distance = Integer.parseInt("1");    // 最小距离

        //如果未获取定位信息，说明当前无法获取GPS信号，所以调用NewWork的定位服务，同时也调用GPS的
        if (location == null) {
            LocationProvider networkProvider = lm.getProvider(LocationManager.NETWORK_PROVIDER);
            lm.requestLocationUpdates(networkProvider.getName(), period * 2000, distance, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location1) {

                    location = location1;
                }
            });
        }
        lm.requestLocationUpdates(provider, period * 1000, distance, locationListener);    // 开始监听
//        tvResult.append(provider + "-Location listener started.\n");

    }

    /**
     * @param context
     * @return void
     * @description 询问权限
     * @author Administrator
     * @time 2023/04/04 16:52
     */
    void askLocationSettings(Context context) {
        Comfirm.Comfirm(context, "本应用需要开启位置服务，是否去设置界面开启位置服务？", new CallBack() {
            @Override
            public void success(Object o) {
                Intent intent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void error(String message) {

            }
        });
    }

    /**
     * @param location
     * @return void
     * @description 更新位置
     * @author Administrator
     * @time 2023/04/04 16:52
     */
    private void updateView(Location location) {
        //更新当前位置信息
        this.location = location;
//        MyLog.d(TAG, location.toString());
        //被观察者的通知更新
        setChanged();
        notifyObservers();
    }

    /**
     * @param
     * @return location
     * @description 获取当前位置
     * @author Administrator
     * @time 2023/04/04 16:52
     */
    public Location getLocation() {
        return this.location;
    }


    public int cn0DbHz30SatelliteCount = 0;
    public int cn0DbHz37SatelliteCount = 0;

    public void initiative(Context context) {

//注册监听
//        LocationManagerCompat.registerGnssStatusCallback(lm, gnssStatusListener, new Handler(Looper.myLooper()));


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        lm.registerGnssStatusCallback(new android.location.GnssStatus.Callback() {
            public void onSatelliteStatusChanged(GnssStatus status) {
                super.onSatelliteStatusChanged(status);

                // 可以搜索到的卫星总数
                int satelliteCount = status.getSatelliteCount();

                var satelliteInfo = "";
                for (int index = 0; index < satelliteCount; index++) {
                    // 每个卫星的载波噪声密度
//                    float cn0DbHz = status.getCn0DbHz(index);
                    float cn0DbHz = status.getCn0DbHz(index);
                    if (cn0DbHz >= 30) {
                        cn0DbHz30SatelliteCount++;
                    }
                    if (cn0DbHz >= 37) {
                        cn0DbHz37SatelliteCount++;
                    }
                }

                //被观察者的通知更新
                setChanged();
                notifyObservers();


            }
        });

//
//        //实例化
//        GpsStatus gpsStatus = lm.getGpsStatus(null); // 获取当前状态
//        // 获取默认最大卫星数
//        int maxSatellites = gpsStatus.getMaxSatellites();
//        //获取第一次定位时间（启动到第一次定位）
//        int costTime = gpsStatus.getTimeToFirstFix();
//        //获取卫星
//        Iterable<GpsSatellite> iterable = gpsStatus.getSatellites();
//        //一般再次转换成Iterator
//        Iterator<GpsSatellite> itrator = iterable.iterator();
//
//        //通过遍历重新整理为ArrayList
//        ArrayList<GpsSatellite> satelliteList = new ArrayList<GpsSatellite>();
//        int count = 0;
//        while (itrator.hasNext() && count <= maxSatellites) {
//            GpsSatellite satellite = itrator.next();
//            satelliteList.add(satellite);
//            count++;
//        }
//        System.out.println("总共搜索到" + count + "颗卫星");
//        //输出卫星信息
//        for (int i = 0; i < satelliteList.size(); i++) {
//            //卫星的方位角，浮点型数据
//            System.out.println(satelliteList.get(i).getAzimuth());
//            //卫星的高度，浮点型数据
//            System.out.println(satelliteList.get(i).getElevation());
//            //卫星的伪随机噪声码，整形数据
//            System.out.println(satelliteList.get(i).getPrn());
//            //卫星的信噪比，浮点型数据
//            System.out.println(satelliteList.get(i).getSnr());
//            //卫星是否有年历表，布尔型数据
//            System.out.println(satelliteList.get(i).hasAlmanac());
//            //卫星是否有星历表，布尔型数据
//            System.out.println(satelliteList.get(i).hasEphemeris());
//            //卫星是否被用于近期的GPS修正计算
//            System.out.println(satelliteList.get(i).hasAlmanac());
//        }
    }


    /**
     * 将经纬度转换为度分秒格式
     *
     * @param du 116.41884740.0897315
     * @return 116°25'7.85"       40°5'23.03"
     */
    public String latLng2DfmForExif(double du) {
        int du1 = (int) du;
        double tp = (du - du1) * 60;
        int fen = (int) tp;
        String miao = (Math.abs(((tp - fen) * 60)) * 1000000) + "";
        return du1 + "/1," + Math.abs(fen) + "/1," + miao + "/1000000";
    }


    /**
     * 将经纬度转换为度分秒格式
     *
     * @param du 116.41884740.0897315
     * @return 116°25'7.85"       40°5'23.03"
     */
    public String latLng2Dfm(double du) {
        int du1 = (int) du;
        double tp = (du - du1) * 60;
        int fen = (int) tp;
        String miao = String.format("%.2f", Math.abs(((tp - fen) * 60)));
        return du1 + "°" + Math.abs(fen) + "'" + miao + "\"";
    }

    /**
     * 度分秒转经纬度
     *
     * @param dms 116°25'7.85"
     * @return 116.418847
     */
    public double dfm2LatLng(String dms) {
        if (dms == null) return 0;
        try {
            dms = dms.replace(" ", "");
            String[] str2 = dms.split("°");
            if (str2.length < 2) return 0;
            int d = Integer.parseInt(str2[0]);
            String[] str3 = str2[1].split("\'");
            if (str3.length < 2) return 0;
            int f = Integer.parseInt(str3[0]);
            String str4 = str3[1].substring(0, str3[1].length() - 1);
            double m = Double.parseDouble(str4);

            double fen = f + (m / 60);
            double du = (fen / 60) + Math.abs(d);
            if (d < 0) du = -du;
            return Double.parseDouble(String.format("%.7f", du));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将经纬度转换为度分格式
     *
     * @param du 116.418847      40.0897315
     * @return 116°25'  40°5'
     */
    private String latLng2Df(double du) {
        int du1 = (int) du;
        double tp = (du - du1) * 60;
        int fen = (int) tp;
        return du1 + "°" + Math.abs(fen) + "'";
    }

    /**
     * 度分转经纬度
     * 全球经纬度的取值范围为：纬度-90~90，经度-180~180
     * 度分转换： 将度分单位数据转换为度单位数据，公式：度=度+分/60
     * 例如： 经度 = 116°20.12'，纬度 = 39°12.34'
     * 经度 = 116 + 20.12 / 60 = 116.33533°
     * 纬度 = 39 + 12.34 / 60 = 39.20567°
     *
     * @param dm 4005.38389 ddmm.mmmmm
     * @return 40.0897315
     * 11616.02846 dddmm.mmmmm
     * 116.267141
     */
    public double df2LatLng(String dm) {
        if (dm == null) {
            return 0;
        }
        try {
            dm = dm.replace(" ", "");
            int d = Integer.parseInt(dm.substring(0, dm.lastIndexOf(".") - 2));
            // 兼容经纬度的转换
            double fen = Double.parseDouble(dm.substring(String.valueOf(d).length()));

            double lat = (fen / 60) + Math.abs(d);
            if (lat < 0) {
                lat = -lat;
            }
            return Double.parseDouble(String.format("%.7f", lat));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
