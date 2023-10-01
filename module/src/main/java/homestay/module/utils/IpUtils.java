package homestay.module.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtils {
    public static String getIpAddress(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-Ip");
        }
        if (ip == null ||ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-Ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")){
                ip = getLocalIpAddress();
            }
        }
        if (ip.contains(",")){
            return ip.split(",")[0];
        }else {
            return ip;
        }
    }
    /**
     * 获取本机ip
     * @return
     */
    public static String getLocalIpAddress(){
        String addr = "";
        try{
            addr = InetAddress.getLocalHost().getHostAddress();
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
        return addr;
    }
}
