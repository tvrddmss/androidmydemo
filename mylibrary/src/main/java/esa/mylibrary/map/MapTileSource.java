package esa.mylibrary.map;

import android.util.Log;

import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.MapTileIndex;

/**
 * @ProjectName: mydemo
 * @Package: esa.mylibrary.map
 * @ClassName: MapTileSource
 * @Description: java类作用描述
 * @Author: Administrator
 * @CreateDate: 2023/04/03 13:38
 * @UpdateUser: Administrator
 * @UpdateDate: 2023/04/03 13:38
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MapTileSource extends TileSourceFactory {

    public static String appkey = "aea4ec337cdff280788f210a3c88cea4";//我的

    public static String hosturl ="https://www.hotworld.com.cn:1155";// UserInfoModel.service_http + UserInfoModel.service_ip.replace("1122", "1155");//"https://www.hotworld.com.cn:1155";//"http://172.24.89.200:6080/arcgis/rest/services/Wuqing/img_w/MapServer";//"http://174.16.0.3:1155/arcgis/rest/services/Wuqing/img_w/MapServer";

    public static String hosturltdt = "http://t0.tianditu.gov.cn";


    static String tdt_yx_url = hosturltdt + "/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + appkey;
    //影像地图 _W是墨卡托投影  _c是国家2000的坐标系
    public static OnlineTileSourceBase tdt_source_img = new XYTileSource("tdt_img", 0, 19, 256, "", new String[]{tdt_yx_url}) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
//            String url = getBaseUrl() + "/" + MapTileIndex.getZoom(pMapTileIndex) + "/" + MapTileIndex.getY(pMapTileIndex) + "/" + MapTileIndex.getX(pMapTileIndex);
//            Log.d("url", url);
//            return url;

            Log.d("url", getBaseUrl() + "&TILEROW=" + MapTileIndex.getX(pMapTileIndex) + "&TILECOL=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex));
            return getBaseUrl() + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

   static String tdt_cia_url = hosturltdt + "/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&style=default&TILEMATRIXSET=w&FORMAT=tiles&tk=" + appkey;
    public static ITileSource tdt_source_img_bz = new XYTileSource("tdt_cia", 0, 19, 256, "", new String[]{tdt_cia_url}) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
//            String url = getBaseUrl() + "/" + MapTileIndex.getZoom(pMapTileIndex) + "/" + MapTileIndex.getY(pMapTileIndex) + "/" + MapTileIndex.getX(pMapTileIndex);
//            Log.d("url", url);
//            return url;

            Log.d("url", getBaseUrl() + "&TILEROW=" + MapTileIndex.getX(pMapTileIndex) + "&TILECOL=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex));
            return getBaseUrl() + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

}
