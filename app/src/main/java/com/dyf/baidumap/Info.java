package com.dyf.baidumap;

import java.io.Serializable;

/**
 * @deprecated 用来保存覆盖物信息的bean类
 * Created by diy on 2017-11-06.
 */

public class Info implements Serializable
{

    private static final long serialVersionUID = 7883466441142792991L;
    private int imgId; //图片ID，实际项目中，图片这里应该是URL
    private String parklotName; //停车场名称
    private int distance;//车辆距离停车场的距离，单位 米
    private int time; //车辆行驶到停车场所需的时间 单位秒
    private int zan; //点赞数量
    private int noParkNum; //停车场的未停车数
    private double noParkRate; //停车场的未停车率
    private int parklotAmount; // 停车场车位数量
    private String parklotLng; // 停车场位置精度
    private String parklotLat; // 停车场位置纬度
    private String parklotDescription; // 停车场描述

    //public static List<Info> infos = new ArrayList<Info>();


    /**
     * 没有赞，未停车率和图片id
     * @param parklotName
     * @param distance
     * @param time
     * @param noParkNum
     * @param parklotAmount
     * @param parklotLng
     * @param parklotLat
     * @param parklotDescription
     */
    public Info(String parklotName, int distance, int time, int noParkNum, int parklotAmount, String parklotLng, String parklotLat, String parklotDescription)
    {
        this.parklotName = parklotName;
        this.distance = distance;
        this.time = time;
        this.noParkNum = noParkNum;
        this.parklotAmount = parklotAmount;
        this.parklotLng = parklotLng;
        this.parklotLat = parklotLat;
        this.parklotDescription = parklotDescription;
    }

    public int getImgId()
    {
        return imgId;
    }

    public void setImgId(int imgId)
    {
        this.imgId = imgId;
    }

    public String getParklotName()
    {
        return parklotName;
    }

    public void setParklotName(String parklotName)
    {
        this.parklotName = parklotName;
    }

    public int getDistance()
    {
        return distance;
    }

    public void setDistance(int distance)
    {
        this.distance = distance;
    }

    public int getTime()
    {
        return time;
    }

    public void setTime(int time)
    {
        this.time = time;
    }

    public int getZan()
    {
        return zan;
    }

    public void setZan(int zan)
    {
        this.zan = zan;
    }

    public int getNoParkNum()
    {
        return noParkNum;
    }

    public void setNoParkNum(int noParkNum)
    {
        this.noParkNum = noParkNum;
    }

    public double getNoParkRate()
    {
        return noParkRate;
    }

    public void setNoParkRate(double noParkRate)
    {
        this.noParkRate = noParkRate;
    }

    public int getParklotAmount()
    {
        return parklotAmount;
    }

    public void setParklotAmount(int parklotAmount)
    {
        this.parklotAmount = parklotAmount;
    }

    public String getParklotLng()
    {
        return parklotLng;
    }

    public void setParklotLng(String parklotLng)
    {
        this.parklotLng = parklotLng;
    }

    public String getParklotLat()
    {
        return parklotLat;
    }

    public void setParklotLat(String parklotLat)
    {
        this.parklotLat = parklotLat;
    }

    public String getParklotDescription()
    {
        return parklotDescription;
    }

    public void setParklotDescription(String parklotDescription)
    {
        this.parklotDescription = parklotDescription;
    }
}
