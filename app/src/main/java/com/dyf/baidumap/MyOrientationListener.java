package com.dyf.baidumap;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.hardware.Sensor.TYPE_ORIENTATION;

/**
 * @deprecated 使用手机的方向传感器
 * Created by diy on 2017-11-05.
 */

public class MyOrientationListener extends Object implements SensorEventListener
{
    //使用传感器需要先获得传感器的管理者
    private SensorManager mSensorManager;
    private Context mContext;
    private Sensor mSensor;
    //方向传感器关心三个轴的坐标，xyz
    private float lastX;

    public MyOrientationListener(Context context)
    {
        this.mContext = context;
    }

    //开始监听
    public void start()
    {
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager!=null)
        {
            //获得方向传感器
            mSensor = mSensorManager.getDefaultSensor(TYPE_ORIENTATION);
        }
        if(mSensor != null)
        {
            mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_UI);

        }
    }

    //结束监听
    public void stop()
    {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //当检测到的是方向传感器
        if ((event.sensor.getType() == Sensor.TYPE_ORIENTATION))
        {
            float x = event.values[SensorManager.DATA_X];
            //方向改变之后，和原来做对比，如果大于1度的话
            if (Math.abs(x-lastX) > 1.0)
            {
                //通知主界面去更新
                if (mOnOrientationListener != null)
                {
                    //进行一个回调
                    mOnOrientationListener.onOrientationChanged(x);
                }
            }
            lastX = x;
        }
    }

    private OnOrientationListener mOnOrientationListener;

    public void setOnOrientationListener(OnOrientationListener mOnOrientationListener)
    {
        this.mOnOrientationListener = mOnOrientationListener;
    }

    public interface OnOrientationListener
    {
        void onOrientationChanged(float x);
    }

    //经度改变
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}
