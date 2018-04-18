package com.dyf.utils;

import org.ksoap2.serialization.SoapObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by diy on 2018-01-13.
 */

public class Convert
{
    /**
     * 将double转换为string
     * @param dou 传过来的double类型参数
     * @return
     */
    public static String doubleToString(double dou) {
        Double dou_obj = new Double(dou);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        String dou_str = nf.format(dou_obj);
        SysoUtils.print("转换后的double："+dou_str);
        return dou_str;
    }

    /**
     * 将服务器返回的soapObject结果转换为List<Map<String,String>>类型数据
     * @param result 服务器返回的soapObject结果
     * @return List<Map<String,String>> 转换之后的list Map类型数据
     */
    public static List<Map<String,String>> soapResultToListMap(SoapObject result)
    {
        List<Map<String,String>> listItems = new ArrayList<Map<String,String>>();
        int num = result.getPropertyCount();
        if (num>0)
        {
            for (int i =0;i<num;i++)
            {
                Map<String,String> map = new HashMap<String,String>();
                Map<String,String> listItem = new HashMap<String,String>();
                SoapObject soapInfo = (SoapObject) result.getProperty(i);
                listItem.put("parklotName",soapInfo.getProperty("parklotName").toString());
                listItem.put("distance",soapInfo.getProperty("distance").toString());
                listItem.put("time",soapInfo.getProperty("time").toString());
                listItem.put("noParkNum",soapInfo.getProperty("noParkNum").toString());
                listItem.put("noParkRate",soapInfo.getProperty("noParkRate").toString());
                listItem.put("parklotAmount",soapInfo.getProperty("parklotAmount").toString());
                listItem.put("parklotLng",soapInfo.getProperty("parklotLng").toString());
                listItem.put("parklotLat",soapInfo.getProperty("parklotLat").toString());
                listItem.put("parklotDescription",soapInfo.getProperty("parklotDescription").toString());
                listItems.add(listItem);
            }
        }else {
            listItems=null;
        }
        return listItems;
    }
}
