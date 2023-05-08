package esa.mylibrary.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author: Administrator
 * @date: 2023/03/29
 */
public class DeviceUtil {

    /**
     * @param
     * @return 当前设备IP
     * @description 获取当前设备IP
     * @author Administrator
     * @time 2023/03/29 10:09
     */
    public static String GetLocalIp() throws Exception {
        String ip = "";
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                    if (!inetAddress.getHostAddress().toString().equals("10.0.2.15")) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        return ip;
    }

    /**
     * @param
     * @return 当前设备mac
     * @Description  获取网卡物理地址
     * @author Administrator
     * @time 2023/03/29 10:10
     */
    public static String getLocalMacAddressFromIp() {
        String mac_s = "";
        try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(GetLocalIp()));
            mac = ne.getHardwareAddress();
            mac_s = byte2hex(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mac_s;
    }

    /**
     * @param
     * @return
     * @description
     * @author Administrator
     * @time 2023/03/29 10:11
     */
    private static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }
}
