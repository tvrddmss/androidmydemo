package esa.mylibrary.fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import java.util.Observable;
import java.util.Observer;

import esa.mylibrary.R;
import esa.mylibrary.databinding.FragmentOsmBinding;
import esa.mylibrary.gps.MyGps;
import esa.mylibrary.sensor.MySensor;

public class OsmFragment extends BaseFragment implements Observer {

    //数据模型
    private OsmViewModel mViewModel;
    //xml绑定实例
    private FragmentOsmBinding binding;
    //当前页面view
    private View view;
    //padingTop
    public int paddingTop;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_osm, container, false);
        //根据xml创建数据绑定
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_osm, container, false);
        //设置数据绑定对象生命周期的拥有者
        binding.setLifecycleOwner(this);
        //获取当前View
        view = binding.getRoot();

        mViewModel = new ViewModelProvider(this).get(OsmViewModel.class);
        // TODO: Use the ViewModel
        //实例设置到页面绑定
        binding.setViewModel(mViewModel);

        mViewModel.top = this.paddingTop;
        mViewModel.init(view);

        //加入观察者
        addAsObserver();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    /**
     * @return
     * @description 销毁时调用, 只调用一次
     * @author Administrator
     * @time 2023/04/03 11:38
     */
    @Override
    public void onDetach() {
        super.onDetach();
        //移出观察者
        removeAsObserver();
    }


    /**
     * @return
     * @description 加入GPS观察者
     * @author Administrator
     * @time 2023/04/03 11:38
     */
    public void addAsObserver() {
        MyGps.getInstance().addObserver(this);
        MySensor.getInstance().addObserver(this);
    }

    /**
     * @return
     * @description 移出GPS观察者
     * @author Administrator
     * @time 2023/04/03 11:38
     */
    public void removeAsObserver() {
        MyGps.getInstance().deleteObserver(this);
        MySensor.getInstance().deleteObserver(this);
    }


    Location location;

    /**
     * @return
     * @description 观察者模式
     * @author Administrator
     * @time 2023/04/03 11:38
     */
    @Override
    public void update(Observable o, Object arg) {
//        MyLog.d(o.getClass().getName());
        switch (o.getClass().getName()) {
            case "esa.mylibrary.sensor.MySensor": {
                MySensor mySensor = (MySensor) o;
                //竖屏
                float angle = mySensor.angle;
                String text = String.format("%.2f", mySensor.values[0]) + " "
                        + String.format("%.2f", mySensor.values[1]) + " "
                        + String.format("%.2f", mySensor.values[2]);
                mViewModel.sensorStr.postValue(text);
                String fwj = String.format("%.0f", angle);
                while (fwj.length() < 3) {
                    fwj = "0" + fwj;
                }
                mViewModel.angle.postValue(fwj);
            }
            break;
            case "esa.mylibrary.gps.MyGps": {
                Location temp = ((MyGps) o).getLocation();
                if (temp == null) {
//                    mViewModel.locationStr.postValue("未获取到数据");
                } else {
                    location = temp;
//                    //组织文字
//                    String text = String.format("%.4f", location.getLongitude()) + "E  "
//                            + String.format("%.4f", location.getLatitude()) + "N  "
//                            + "海拔:" + String.format("%.4f", location.getAltitude());
////                            + "  "
////                            + "方向角:180";
////            MyLog.d("接收到更新通知：" + text);
//                    mViewModel.locationStr.postValue(text);
                }
            }
            break;
        }
        //更新页面底部信息
        mViewModel.bottomInfo.postValue(mViewModel.locationStr.getValue() + " " + mViewModel.angle.getValue());
        String text = getResources().getString(R.string.osm_bottommessage);
        if (location == null) {
            text = text
                    .replace("【Longitude】", "000.0000")
                    .replace("【Latitude】", "00.0000")
                    .replace("【Altitude】", "0.0000");
        } else {
            text = text
                    .replace("【Longitude】", String.format("%.4f", location.getLongitude()))
                    .replace("【Latitude】", String.format("%.4f", location.getLatitude()))
                    .replace("【Altitude】", String.format("%.4f", location.getAltitude()));
        }
        text = text.replace("【Direction】", mViewModel.angle.getValue());

        text += "\r\n卫星总数：" + MyGps.getInstance().satelliteCount + "，信噪比30-37：" + MyGps.getInstance().cn0DbHz30SatelliteCount + "，大于37：" + MyGps.getInstance().cn0DbHz37SatelliteCount;

        mViewModel.bottomInfo.postValue(text);
    }


    /**
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return void
     * @description 设置地图控件的padding
     * @author Administrator
     * @time 2023/04/20 10:16
     */
    public void setMapViewPadding(int left, int top, int right, int bottom) {
        mViewModel.mMapView.setPadding(left, top, right, bottom);
    }
}