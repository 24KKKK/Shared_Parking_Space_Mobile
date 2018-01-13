package com.dyf.model;

/**
 * 从服务器返回的停车场信息实体类
 * Created by diy on 2018-01-13.
 */

public class ResultParklotInfo
{
    private String parklotName; // 停车场名称
    private int distance; // 车辆距离停车场的距离，单位 米
    private int time;// 车辆行驶到停车场所需的时间
    private int noParkNum; //停车场的未停车数
    private double noParkRate; //停车场的未停车率
    private int parklotAmount; // 停车场车位数量
    private String parklotLng; // 停车场位置精度
    private String parklotLat; // 停车场位置纬度
    private String parklotDescription; // 停车场描述

    public ResultParklotInfo()
    {
    }

    public ResultParklotInfo(String parklotName, int distance, int time, int noParkNum, double noParkRate, int parklotAmount, String parklotLng, String parklotLat, String parklotDescription)
    {
        this.parklotName = parklotName;
        this.distance = distance;
        this.time = time;
        this.noParkNum = noParkNum;
        this.noParkRate = noParkRate;
        this.parklotAmount = parklotAmount;
        this.parklotLng = parklotLng;
        this.parklotLat = parklotLat;
        this.parklotDescription = parklotDescription;
    }

    @Override
    public String toString()
    {
        return "ResultParklotInfo{" +
                "parklotName='" + parklotName + '\'' +
                ", distance=" + distance +
                ", time=" + time +
                ", noParkNum=" + noParkNum +
                ", noParkRate=" + noParkRate +
                ", parklotAmount=" + parklotAmount +
                ", parklotLng='" + parklotLng + '\'' +
                ", parklotLat='" + parklotLat + '\'' +
                ", parklotDescription='" + parklotDescription + '\'' +
                '}';
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
