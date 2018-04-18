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
    //二教的无线IP地址
    //public static String SERVICEURL = "http://192.168.43.195:8080/parkservice";
    //毕设室无线IP地址，两个
    //public static String SERVICEURL = "http://10.201.128.111:8080/parkservice";
    //public static String SERVICEURL = "http://10.201.23.19:8080/parkservice";
    //public static String SERVICEURL = "http://"+Inet.getIP()+"/parkservice";
    public static String SERVICEURL = "http://192.168.191.1:8080/parkservice";
    /**
     * 向服务器发送的方法名称，根据条件获取合适的停车场信息
     */
    public static String GET_BEST_PARKLOTINFO_METHOD_NAME = "getBestParklot";

    /**
     * 向服务器发送的方法名称，根据条件获取所有的停车场信息
     */
    public static String GET_All_PARKLOTINFO_METHOD_NAME = "getAllParklot";

    /**
     * 向服务器发送方法名称，将QQ登录用户信息保存在数据库
     */
    public static String INSERT_QQUSER_INFO = "insertQQUserInfo";

    /**
     * 向服务器发送的方法名称，根据openid获得余额
     */
    public static String GET_BALANCE = "getBalance";

    /**
     * 向服务器发送的方法名称，将充值操作保存进数据库
     */
    public static String INSERT_RECHARGEOPTION = "insertReChargeOption";

    /**
     * 设置地图的初始放大比例和点击我在哪按钮时的地图放大比例
     */
    public static float BASIC_ZOOM = 16;

    /**
     * 微信的APP_ID
     */
    public static String WEIXIN_APP_ID = "";

    /**
     * QQ的APP_ID
     */
    public static String QQ_APP_ID = "1106834232";
}
