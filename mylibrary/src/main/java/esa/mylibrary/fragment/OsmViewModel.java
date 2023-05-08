package esa.mylibrary.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import esa.mylibrary.R;
import esa.mylibrary.gps.MyGps;
import esa.mylibrary.map.MapTileSource;


public class OsmViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    //数据绑定
    public MutableLiveData<String> locationStr = new MutableLiveData<>("");
    public MutableLiveData<String> sensorStr = new MutableLiveData<>("");
    public MutableLiveData<String> angle = new MutableLiveData<>("");

    //底部信息显示
    public MutableLiveData<String> bottomInfo = new MutableLiveData<>("");

    public int top;
    public MapView mMapView;

    public void init(View view) {
        mMapView = view.findViewById(R.id.map);
        mMapView.setZoomRounding(true);
        mMapView.setMultiTouchControls(true);// 触控放大缩小

//        mMapView.setPadding(0, top, 0, 0);

        //让瓦片适应不同像素密度:默认地图显示的字体小，图片像素高，可设置以下代码，使地图适应不同像素密度，更美观
        mMapView.setTilesScaledToDpi(true);

        mMapView.getOverlays().clear();


        mMapView.setTileSource(MapTileSource.tdt_source_img);
        TilesOverlay tilesOverlay = new TilesOverlay(new MapTileProviderBasic(view.getContext(), MapTileSource.tdt_source_img_bz), view.getContext());
        mMapView.getOverlayManager().add(tilesOverlay);


        MapController mapController = (MapController) mMapView.getController();
        //（1）、设置缩放界别
        mapController.setZoom(11);//设置缩放级别
        mMapView.setMaxZoomLevel(18.9D);

        //（2）、设置缩放按钮可见
        mMapView.setBuiltInZoomControls(false);//设置缩放按钮可见
        mapController.setCenter(new GeoPoint(39.13333, 117.20000));//设置地图中心

        //指南针，这里用的OSMdroid自带的
        CompassOverlay mCompassOverlay = new CompassOverlay(view.getContext(), new InternalCompassOrientationProvider(view.getContext()), mMapView);
        mCompassOverlay.enableCompass();
        mCompassOverlay.setCompassCenter(30, top + 30);
        mMapView.getOverlays().add(mCompassOverlay);

        //定位图标与GPS不一致，这里用的OSMdroid自带的
        MyLocationNewOverlay mLocationOverlay = null;
        if (mLocationOverlay == null) {
            mLocationOverlay = new MyLocationNewOverlay(mMapView);
            Bitmap bitmap = BitmapFactory.decodeResource(view.getResources(), org.osmdroid.library.R.drawable.person);

            mLocationOverlay.setDirectionArrow(bitmap,
                    ((BitmapDrawable)
                            mMapView.getContext().getResources().getDrawable(org.osmdroid.library.R.drawable.round_navigation_white_48)).getBitmap());
        }
        mMapView.getOverlays().add(mLocationOverlay);
        mLocationOverlay.enableMyLocation();  //设置可视
    }

    public void onLocation(View view) {
        if (MyGps.getInstance().getLocation() != null) {

            mMapView.getController().animateTo(new GeoPoint(MyGps.getInstance().getLocation().getLatitude(), MyGps.getInstance().getLocation().getLongitude()));//valueOf(MyGps.getInstance().getLocation())

        }
    }
}