package esa.mydemo.fragment.main

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.lifecycle.MutableLiveData
import esa.mydemo.base.AppBaseViewModel
import esa.mylibrary.gps.MyGps
import esa.mylibrary.map.MapTileSource
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.tileprovider.cachemanager.CacheManager.CacheManagerCallback
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.TilesOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class OsmViewModel : AppBaseViewModel() {
    // TODO: Implement the ViewModel
    //数据绑定
    var locationStr = MutableLiveData("")
    var sensorStr = MutableLiveData("")
    var angle = MutableLiveData("")
    var layers = MutableLiveData<Map<String, String>>()


    //底部信息显示
//    @JvmField
    var bottomInfo = MutableLiveData("")
    var top = 0
    lateinit var mMapView: MapView
    lateinit var cacheManager: CacheManager
    fun init(view: View) {

        mMapView.setZoomRounding(true)
        mMapView.setMultiTouchControls(true) // 触控放大缩小

//        mMapView.setPadding(0, top, 0, 0);

        //让瓦片适应不同像素密度:默认地图显示的字体小，图片像素高，可设置以下代码，使地图适应不同像素密度，更美观
        mMapView.isTilesScaledToDpi = true
        mMapView.overlays.clear()
        mMapView.setTileSource(MapTileSource.tdt_source_img)
        val tilesOverlay = TilesOverlay(
            MapTileProviderBasic(view.context, MapTileSource.tdt_source_img_bz),
            view.context
        )
        mMapView.getOverlayManager().add(tilesOverlay)
        val mapController = mMapView.getController() as MapController
        //（1）、设置缩放界别
        mapController.setZoom(11) //设置缩放级别
        mMapView.setMaxZoomLevel(18.9)

        //（2）、设置缩放按钮可见
        mMapView.setBuiltInZoomControls(false) //设置缩放按钮可见
        mapController.setCenter(GeoPoint(39.13333, 117.20000)) //设置地图中心

        //指南针，这里用的OSMdroid自带的
        val mCompassOverlay =
            CompassOverlay(view.context, InternalCompassOrientationProvider(view.context), mMapView)
        mCompassOverlay.enableCompass()
        mCompassOverlay.setCompassCenter(30f, (top + 30).toFloat())
        mMapView.getOverlays().add(mCompassOverlay)

        //定位图标与GPS不一致，这里用的OSMdroid自带的
        var mLocationOverlay: MyLocationNewOverlay? = null
        if (mLocationOverlay == null) {
            mLocationOverlay = MyLocationNewOverlay(mMapView)
            val bitmap =
                BitmapFactory.decodeResource(view.resources, org.osmdroid.library.R.drawable.person)
            mLocationOverlay.setDirectionArrow(
                bitmap,
                (mMapView.getContext().resources.getDrawable(org.osmdroid.library.R.drawable.round_navigation_white_48) as BitmapDrawable).bitmap
            )
        }
        mMapView.getOverlays().add(mLocationOverlay)
        mLocationOverlay.enableMyLocation() //设置可视


        cacheManager = CacheManager(mMapView)

    }

    fun onLocation(view: View?) {
        if (MyGps.getInstance().location != null) {
            mMapView.controller.animateTo(
                GeoPoint(
                    MyGps.getInstance().location.latitude,
                    MyGps.getInstance().location.longitude
                )
            )
        }
    }

    fun onChangeLayers(view: View) {

    }

    fun cleanCache(view: View) {

        val bb = BoundingBox()
        val cacheManagerTask = cacheManager.cleanAreaAsync(getContext(), bb, 1, 22)
        cacheManagerTask.addCallback(object : CacheManagerCallback {
            override fun onTaskComplete() {
                esa.mylibrary.map.Osmdroid.clear()
                showMessage("缓存已清理,重启后生效")
            }

            override fun updateProgress(
                progress: Int,
                currentZoomLevel: Int,
                zoomMin: Int,
                zoomMax: Int
            ) {
            }

            override fun downloadStarted() {}
            override fun setPossibleTilesInArea(total: Int) {}
            override fun onTaskFailed(errors: Int) {}
        })
    }
}