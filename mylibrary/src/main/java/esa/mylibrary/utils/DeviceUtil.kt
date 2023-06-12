package esa.mylibrary.utils

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface

/**
 * @author: Administrator
 * @date: 2023/03/29
 */
object DeviceUtil {
    /**
     * @param
     * @return 当前设备IP
     * @description 获取当前设备IP
     * @author Administrator
     * @time 2023/03/29 10:09
     */
    @JvmStatic
    @Throws(Exception::class)
    fun GetLocalIp(): String {
        val ip = ""
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf = en.nextElement()
            val enumIpAddr = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                    if (inetAddress.getHostAddress().toString() != "10.0.2.15") {
                        return inetAddress.getHostAddress().toString()
                    }
                }
            }
        }
        return ip
    }

    /**
     * @param
     * @return 当前设备mac
     * @Description  获取网卡物理地址
     * @author Administrator
     * @time 2023/03/29 10:10
     */
    @JvmStatic
    val localMacAddressFromIp: String
        get() {
            var mac_s = ""
            try {
                val mac: ByteArray
                val ne = NetworkInterface.getByInetAddress(InetAddress.getByName(GetLocalIp()))
                mac = ne.hardwareAddress
                mac_s = byte2hex(mac)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return mac_s
        }

    /**
     * @param
     * @return
     * @description
     * @author Administrator
     * @time 2023/03/29 10:11
     */
    private fun byte2hex(b: ByteArray): String {
        var hs = StringBuffer(b.size)
        var stmp = ""
        val len = b.size
        for (n in 0 until len) {
            stmp = Integer.toHexString(b[n].toInt() and 0xFF)
            hs = if (stmp.length == 1) hs.append("0").append(stmp) else {
                hs.append(stmp)
            }
        }
        return hs.toString()
    }
}