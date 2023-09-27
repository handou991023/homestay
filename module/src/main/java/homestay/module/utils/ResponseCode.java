package homestay.module.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseCode {
    private static final Map<Integer,String> statusMap = new HashMap<Integer,String>();
    static {
        statusMap.put(1001,"ok");
        statusMap.put(1002,"还没有登录哦");
        statusMap.put(1010,"账户密码不匹配或者账号不存在");

        //create user and forget password
        statusMap.put(2014,"账号未注册");

        //console error
        //homestayId error
        statusMap.put(3051,"必填信息不能为空");
        statusMap.put(3052,"民宿id不正确");

        statusMap.put(4003,"权限不足，请重新访问");
        statusMap.put(4004,"链接超时，请重新访问");
    }
    public static String getMsg(Integer code){
        return statusMap.get(code);
    }
}
