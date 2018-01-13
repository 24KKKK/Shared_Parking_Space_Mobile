package com.dyf.utils;

/**
 * Created by diy on 2018-01-13.
 */

public class Constant
{

    /**
     * 命名空间
     */
    public static String NAMESPACE="http://service.dyf.com";
    /**
     * 请求的serviceURL
     */
    public static String SERVICEURL = "http://10.201.128.111:8080/parkservice";
    //public static String SERVICEURL = "http://10.201.23.19:8080/parkservice";
    //public static String SERVICEURL = "http://"+Inet.getIP()+"/parkservice";
    /**
     * 向服务器发送的方法名称，根据条件获取合适的停车场信息
     */
    public static String GET_BEST_PARKLOTINFO_METHOD_NAME = "getBestParklot";

    /**
     * 设置地图的初始放大比例和点击我在哪按钮时的地图放大比例
     */
    public static float BASIC_ZOOM = 19;
}
