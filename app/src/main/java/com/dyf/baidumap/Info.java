package com.dyf.baidumap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated 用来保存覆盖物信息的bean类
 * Created by diy on 2017-11-06.
 */

public class Info implements Serializable
{
    private static final long serialVersionUID = 7883466441142792991L;
    //经纬度
    private double latitude;
    private double longtitude;
    //图片ID，实际项目中，图片这里应该是URL
    private int imgId;
    //商家名称
    private String name;
    //距离
    private String distance;
    //点赞数量
    private int zan;

    public static List<Info> infos  = new ArrayList<Info>();

    static
    {
        infos.add(new Info(38.0913962358,114.5238703587,R.mipmap.a01,"贵族旅馆","距离209米",1435));
        infos.add(new Info(38.0945803686,114.5186195402,R.mipmap.a02,"洗浴会所","距离363米",1623));
        infos.add(new Info(38.0850569672,114.5118463520,R.mipmap.a03,"金亿城","距离273米",7745));
        infos.add(new Info(38.0900803666,114.5078895053,R.mipmap.a04,"服装城","距离426米",3465));
    }

    public Info(double latitude, double longtitude, int imgId, String name, String distance, int zan)
    {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.imgId = imgId;
        this.name = name;
        this.distance = distance;
        this.zan = zan;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongtitude()
    {
        return longtitude;
    }

    public void setLongtitude(double longtitude)
    {
        this.longtitude = longtitude;
    }

    public int getImgId()
    {
        return imgId;
    }

    public void setImgId(int imgId)
    {
        this.imgId = imgId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDistance()
    {
        return distance;
    }

    public void setDistance(String distance)
    {
        this.distance = distance;
    }

    public int getZan()
    {
        return zan;
    }

    public void setZan(int zan)
    {
        this.zan = zan;
    }
}
