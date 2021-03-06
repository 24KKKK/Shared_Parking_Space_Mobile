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
    //public static String SERVICEURL = "http://192.168.43.195:8081/parkservice";
    //毕设室无线IP地址，两个
    //public static String SERVICEURL = "http://10.201.128.111:8081/parkservice";
    //public static String SERVICEURL = "http://10.201.23.19:8081/parkservice";
    //public static String SERVICEURL = "http://"+Inet.getIP()+"/parkservice";
    //public static String SERVICEURL = "http://192.168.191.1:8081/parkservice";
    //public static String SERVICEURL = "http://119.28.138.30:8081/parkservice";
    //public static String SERVICEURL = "http://192.168.1.5:8081/parkservice";
    public static String SERVICEURL = "http://192.168.206.132:8081/parkservice";
    //public static String SERVICEURL = "http://192.168.23.1:8081/parkservice";
    // 腾讯云服务器地址
    //public static String SERVICEURL = "http://119.28.138.30:8081/parkservice";
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
     * 向服务器发送的方法名称，将预定车位操作保存进数据库
     */
    public static final String INSERT_RESERVEOPTION = "insertReserveOption";

    /**
     * 向服务器发送的方法名称，将绑定的车牌号信息保存进数据库
     */
    public static final String UPDATE_PLATENUM = "updatePlateNum";

    /**
     * 向服务器发送的方法名称，将评价内容保存进数据库
     */
    public static final String INSERT_EVALUATE = "insertEvaluate";

    /**
     * 向服务器发送的方法名称，查询数据库中是否有指定的车牌号
     */
    public static final String SELECT_PLATENUM = "selectPlateNum";

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
