package com.dyf.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dyf.baidumap.ReChargeActivity;
import com.dyf.baidumap.ReserveActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by diy on 2018-01-13.
 */

public class SendRequest
{
    private Context context;
    /**
     * 根据条件和个人经纬度获得合适的停车场信息
     * @param condition
     * @param selfLng
     * @param selfLat
     * @return 返回合适的停车场信息
     */
    public static List<Map<String,String>> getBestParklotInfo(String condition,String selfLng,String selfLat)
    {
        //命名空间
        String nameSpace = Constant.NAMESPACE;
        //serviceURL
        String serviceURL = Constant.SERVICEURL;
        Log.i("serviceURL:",serviceURL);
        //调用的方法名称
        String methodName = Constant.GET_BEST_PARKLOTINFO_METHOD_NAME;

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
                //listItems接收服务器返回的list信息
                List<Map<String,String>> listItems = new ArrayList<Map<String,String>>();
                SoapObject result = (SoapObject) envelop.bodyIn;
                listItems = Convert.soapResultToListMap(result);
                return listItems;
            }
        } catch (Exception e)
        {
            Log.i("调用webservice出错：",e.toString());
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 根据个人经纬度信息获得所有的停车场信息，以及时间距离等信息
     * @param selfLng
     * @param selfLat
     * @return
     */
    public static List<Map<String,String>> getAllParklotInfo(String selfLng,String selfLat)
    {
        //命名空间
        String nameSpace = Constant.NAMESPACE;
        //serviceURL
        String serviceURL = Constant.SERVICEURL;
        Log.i("serviceURL:",serviceURL);
        //调用的方法名称
        String methodName = Constant.GET_All_PARKLOTINFO_METHOD_NAME;
        //创建HttpTransportSE传输对象
        HttpTransportSE transport = new HttpTransportSE(serviceURL);
        //transport.debug = true;
        //使用Soap1.1创建SoapSerializationEnvelope对象
        SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //实例化SoapObject对象
        SoapObject request = new SoapObject(nameSpace, methodName);
        String soapAction = nameSpace +"/"+ methodName;
        //添加发送请求时的参数,包括个人经纬度信息
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
                //接收服务器返回的list信息
                List<Map<String,String>> listItems = new ArrayList<Map<String,String>>();
                SoapObject result = (SoapObject) envelop.bodyIn;
                listItems = Convert.soapResultToListMap(result);
                return listItems;
            }
        } catch (Exception e)
        {
            Log.i("调用webservice出错：",e.toString());
            e.printStackTrace();
        }
        return null;
    }

    // 将登录的QQ用户信息保存在数据库中
    public static void insertQQUserInfo(String openid, String nickname, String gender, String province, String city, String figureurl) {
        //命名空间
        String nameSpace = Constant.NAMESPACE;
        //serviceURL
        String serviceURL = Constant.SERVICEURL;
        Log.i("serviceURL:",serviceURL);
        //调用的方法名称
        String methodName = Constant.INSERT_QQUSER_INFO;
        //创建HttpTransportSE传输对象
        HttpTransportSE transport = new HttpTransportSE(serviceURL);
        //transport.debug = true;
        //使用Soap1.1创建SoapSerializationEnvelope对象
        SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //实例化SoapObject对象
        SoapObject request = new SoapObject(nameSpace, methodName);
        String soapAction = nameSpace +"/"+ methodName;
        //添加发送请求时的参数,包括用户基本信息
        request.addProperty("openid",openid);
        request.addProperty("nickname",nickname);
        request.addProperty("gender",gender);
        request.addProperty("province",province);
        request.addProperty("city",city);
        request.addProperty("figureurl",figureurl);
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
                SysoUtils.print("sys 用户信息保存成功");
            }
        } catch (Exception e)
        {
            Log.i("调用webservice出错：",e.toString());
            e.printStackTrace();
        }

    }

    // 获取QQ用户的余额信息
    public static double getBalance(String openid)
    {
        //命名空间
        String nameSpace = Constant.NAMESPACE;
        //serviceURL
        String serviceURL = Constant.SERVICEURL;
        Log.i("serviceURL:",serviceURL);
        //调用的方法名称
        String methodName = Constant.GET_BALANCE;
        //创建HttpTransportSE传输对象
        HttpTransportSE transport = new HttpTransportSE(serviceURL);
        //transport.debug = true;
        //使用Soap1.1创建SoapSerializationEnvelope对象
        SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //实例化SoapObject对象
        SoapObject request = new SoapObject(nameSpace, methodName);
        String soapAction = nameSpace +"/"+ methodName;
        //添加发送请求时的参数
        request.addProperty("openid",openid);
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
                //接收服务器返回的list信息
                SoapObject result = (SoapObject) envelop.bodyIn;
                SysoUtils.print("sys soapobject balance:"+result.toString());
                double balance = Double.parseDouble( result.getProperty("resultBalance").toString()) ;
                return balance;
            }
        } catch (Exception e)
        {
            Log.i("调用webservice出错：",e.toString());
            e.printStackTrace();
        }
        return 0;
    }

    // 将充值操作保存记录
    public static double insertReChargeOption(String openid,String reChargeNum,String optionName)
    {
        //命名空间
        String nameSpace = Constant.NAMESPACE;
        //serviceURL
        String serviceURL = Constant.SERVICEURL;
        Log.i("serviceURL:",serviceURL);
        //调用的方法名称
        String methodName = Constant.INSERT_RECHARGEOPTION;
        //创建HttpTransportSE传输对象
        HttpTransportSE transport = new HttpTransportSE(serviceURL);
        //transport.debug = true;
        //使用Soap1.1创建SoapSerializationEnvelope对象
        SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //实例化SoapObject对象
        SoapObject request = new SoapObject(nameSpace, methodName);
        String soapAction = nameSpace +"/"+ methodName;
        //添加发送请求时的参数
        request.addProperty("openid",openid);
        request.addProperty("reChargeNum",reChargeNum);
        request.addProperty("optionName",optionName);
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
                //接收服务器返回的list信息
                SoapObject result = (SoapObject) envelop.bodyIn;
                SysoUtils.print("sys soapobject balance:"+result.toString());
                int insertResult = Integer.parseInt( result.getProperty("insertResult").toString()) ;
                if (insertResult == 1){
                    SysoUtils.print("sys 记录保存成功");
                }else {
                    SysoUtils.print("sys 记录保存失败");
                }
            }
        } catch (Exception e)
        {
            Log.i("调用webservice出错：",e.toString());
            e.printStackTrace();
        }
        return 0;
    }

    // 将预定车位操作保存记录
    public static Object insertReserveOption(String openid, String parklotName, String startTime, String endTime, String startDate, String endDate) {
        //命名空间
        String nameSpace = Constant.NAMESPACE;
        //serviceURL
        String serviceURL = Constant.SERVICEURL;
        Log.i("serviceURL:",serviceURL);
        //调用的方法名称
        String methodName = Constant.INSERT_RESERVEOPTION;
        //创建HttpTransportSE传输对象
        HttpTransportSE transport = new HttpTransportSE(serviceURL);
        //transport.debug = true;
        //使用Soap1.1创建SoapSerializationEnvelope对象
        SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //实例化SoapObject对象
        SoapObject request = new SoapObject(nameSpace, methodName);
        String soapAction = nameSpace +"/"+ methodName;
        //添加发送请求时的参数
        request.addProperty("openid",openid);
        request.addProperty("parklotName",parklotName);
        request.addProperty("startTime",startTime);
        request.addProperty("endTime",endTime);
        request.addProperty("startDate",startDate);
        request.addProperty("endDate",endDate);
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
                //接收服务器返回的信息
                SoapObject result = (SoapObject) envelop.bodyIn;
                SysoUtils.print("sys soapobject insertreserve:"+result.toString());
                int insertResult = Integer.parseInt( result.getProperty("insertResult").toString()) ;
                if (insertResult == 1){
                    SysoUtils.print("sys 记录保存成功");
                    return 1;
                }else {
                    SysoUtils.print("sys 记录保存失败");
                    return 0;
                }
            }
        } catch (Exception e)
        {
            Log.i("调用webservice出错：",e.toString());
            e.printStackTrace();
        }
        return 0;
    }

    // 将绑定的车牌号信息保存进数据库
    public static Object updatePlateNum(String openid, String plateNum) {
        //命名空间
        String nameSpace = Constant.NAMESPACE;
        //serviceURL
        String serviceURL = Constant.SERVICEURL;
        Log.i("serviceURL:",serviceURL);
        //调用的方法名称
        String methodName = Constant.UPDATE_PLATENUM;
        //创建HttpTransportSE传输对象
        HttpTransportSE transport = new HttpTransportSE(serviceURL);
        //transport.debug = true;
        //使用Soap1.1创建SoapSerializationEnvelope对象
        SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //实例化SoapObject对象
        SoapObject request = new SoapObject(nameSpace, methodName);
        String soapAction = nameSpace +"/"+ methodName;
        //添加发送请求时的参数
        request.addProperty("openid",openid);
        request.addProperty("plateNum",plateNum);
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
                //接收服务器返回的信息
                SoapObject result = (SoapObject) envelop.bodyIn;
                SysoUtils.print("sys soapobject updatebindplatenum:"+result.toString());
                int insertResult = Integer.parseInt( result.getProperty("insertResult").toString()) ;
                if (insertResult == 1){
                    SysoUtils.print("sys 记录保存成功");
                    return 1;
                }else {
                    SysoUtils.print("sys 记录保存失败");
                    return 0;
                }
            }
        } catch (Exception e)
        {
            Log.i("调用webservice出错：",e.toString());
            e.printStackTrace();
        }
        return 0;
    }

    public static String selectPlateNum(String openid) {
        //命名空间
        String nameSpace = Constant.NAMESPACE;
        //serviceURL
        String serviceURL = Constant.SERVICEURL;
        Log.i("serviceURL:",serviceURL);
        //调用的方法名称
        String methodName = Constant.SELECT_PLATENUM;
        //创建HttpTransportSE传输对象
        HttpTransportSE transport = new HttpTransportSE(serviceURL);
        //transport.debug = true;
        //使用Soap1.1创建SoapSerializationEnvelope对象
        SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //实例化SoapObject对象
        SoapObject request = new SoapObject(nameSpace, methodName);
        String soapAction = nameSpace +"/"+ methodName;
        //添加发送请求时的参数
        request.addProperty("openid",openid);
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
                //接收服务器返回的信息
                SoapObject result = (SoapObject) envelop.bodyIn;
                SysoUtils.print("sys soapobject selectbindplatenum:"+result.toString());
                String selectResult = ( result.getProperty("selectResult").toString()) ;
                return selectResult;
            }
        } catch (Exception e)
        {
            Log.i("调用webservice出错：",e.toString());
            e.printStackTrace();
        }
        return "";
    }
}
