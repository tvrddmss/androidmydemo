package esa.mydemo.fragment.main

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import esa.mydemo.R
import esa.mydemo.base.AppBaseFragment
import esa.mydemo.databinding.FragmentMainBinding
import esa.mylibrary.gps.MyGps
import esa.mylibrary.sensor.MySensor
import java.util.Observable
import java.util.Observer

class OsmFragment : AppBaseFragment(), Observer {
    //数据模型
    private lateinit var mViewModel: OsmViewModel

    //xml绑定实例
    private lateinit var binding: FragmentMainBinding

    //当前页面view
    private lateinit var view: View

    //padingTop
    var paddingTop = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        View view = inflater.inflate(R.layout.fragment_osm, container, false);
        //根据xml创建数据绑定
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        //设置数据绑定对象生命周期的拥有者
        binding.setLifecycleOwner(this)
        //获取当前View
        view = binding.root
        mViewModel = ViewModelProvider(this).get(
            OsmViewModel::class.java
        )
        mViewModel.setContext(binding.root.context)
        // TODO: Use the ViewModel
        //实例设置到页面绑定
        binding.setViewModel(mViewModel)
        mViewModel.top = paddingTop
        mViewModel.mMapView = binding.map
        mViewModel.init(view)

        //加入观察者
        addAsObserver()
        return view
    }

    override fun onResume() {
        super.onResume()
    }

    /**
     * @return
     * @description 销毁时调用, 只调用一次
     * @author Administrator
     * @time 2023/04/03 11:38
     */
    override fun onDetach() {
        super.onDetach()
        //移出观察者
        removeAsObserver()
    }

    /**
     * @return
     * @description 加入GPS观察者
     * @author Administrator
     * @time 2023/04/03 11:38
     */
    fun addAsObserver() {
        MyGps.getInstance().addObserver(this)
        MySensor.getInstance().addObserver(this)
    }

    /**
     * @return
     * @description 移出GPS观察者
     * @author Administrator
     * @time 2023/04/03 11:38
     */
    fun removeAsObserver() {
        MyGps.getInstance().deleteObserver(this)
        MySensor.getInstance().deleteObserver(this)
    }

    lateinit var location: Location

    /**
     * @return
     * @description 观察者模式
     * @author Administrator
     * @time 2023/04/03 11:38
     */
    override fun update(o: Observable, arg: Any?) {
//        MyLog.d(o.getClass().getName());
        when (o.javaClass.name) {
            "esa.mylibrary.sensor.MySensor" -> {
                val mySensor = o as MySensor
                //竖屏
                val angle = mySensor.angle
                val text = (String.format("%.2f", mySensor.values[0]) + " "
                        + String.format("%.2f", mySensor.values[1]) + " "
                        + String.format("%.2f", mySensor.values[2]))
                mViewModel.sensorStr.postValue(text)
                var fwj = String.format("%.0f", angle)
                while (fwj.length < 3) {
                    fwj = "0$fwj"
                }
                mViewModel.angle.postValue(fwj)
            }

            "esa.mylibrary.gps.MyGps" -> {
                val temp = (o as MyGps).location
                if (temp != null) {
                    location = temp
                }
            }
        }
        //更新页面底部信息
        mViewModel.bottomInfo.postValue(mViewModel.locationStr.value + " " + mViewModel.angle.value)
        var text = resources.getString(R.string.osm_bottommessage)
        text = if (!this::location.isInitialized) {
            text
                .replace("【Longitude】", "000.0000")
                .replace("【Latitude】", "00.0000")
                .replace("【Altitude】", "0.0000")
        } else {
            text
                .replace("【Longitude】", String.format("%.4f", location.longitude))
                .replace("【Latitude】", String.format("%.4f", location.latitude))
                .replace("【Altitude】", String.format("%.4f", location.altitude))
        }
        text = text.replace("【Direction】", mViewModel.angle.value!!)

        //text += "\r\n卫星总数：" + MyGps.getInstance().satelliteCount + "，信噪比30-37：" + MyGps.getInstance().cn0DbHz30SatelliteCount + "，大于37：" + MyGps.getInstance().cn0DbHz37SatelliteCount;
        mViewModel.bottomInfo.postValue(text)
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
    fun setMapViewPadding(left: Int, top: Int, right: Int, bottom: Int) {
        mViewModel.mMapView.setPadding(left, top, right, bottom)
    }
}