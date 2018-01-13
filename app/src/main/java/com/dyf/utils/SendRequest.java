package com.dyf.utils;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by diy on 2018-01-13.
 */

public class SendRequest
{
    public static List<Map<String,String>> getBestParklotInfo(String condition,String selfLng,String selfLat)
    {
        //命名空间
        String nameSpace = Constant.NAMESPACE;
        //serviceURL
        String serviceURL = Constant.SERVICEURL;
        Log.i("serviceURL:",serviceURL);
        //调用的方法名称
        String methodName = Constant.GET_BEST_PARKLOTINFO_METHOD_NAME;
        //接收服务器返回的list信息
        //List<ResultParklotInfo> resultParklotInfos = new ArrayList<ResultParklotInfo>();
        List<Map<String,String>> listItems = new ArrayList<Map<String,String>>();
        //创建HttpTransportSE传输对象
        HttpTransportSE transport = new HttpTransportSE(serviceURL);
        //transport.debug = true;

        //使用Soap1.1创建SoapSerializationEnvelope对象
        SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //实例化SoapObject对象
        SoapObject request = new SoapObject(nameSpace, methodName);
        String soapAction = nameSpace +"/"+ methodName;
        //添加发送请求时的参数,包括条件和个人经纬度三项信息
        request.addProperty("condition", condition);
        request.addProperty("selfLng",selfLng);
        request.addProperty("selfLat",selfLat);
        envelop.dotNet = true;
        envelop.bodyOut = request;
        envelop.setOutputSoapObject(request);
        envelop.encodingStyle = "UTF-8";
        //调用webservice
        try
        {
            transport.call(soapAction, envelop);
            Log.i("envelop.getresponse:", envelop.getResponse().toString());
            if (envelop.getResponse().toString() != null)
            {
                SoapObject result = (SoapObject) envelop.bodyIn;
                int num = result.getPropertyCount();
                for(int i =0;i<num;i++)
                {
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

                return listItems;
            }
        } catch (Exception e)
        {
            Log.i("调用webservice出错：",e.toString());
            e.printStackTrace();
        }
        return listItems;
    }
}
