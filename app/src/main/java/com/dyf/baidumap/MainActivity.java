package com.dyf.baidumap;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;


public class MainActivity extends AppCompatActivity
{
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Context context;

    //定位相关
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private boolean isFirstIn = true;
    //回到原位置变量，我的位置的经纬度
    private double mLatitude;
    private double mLongtitude;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //去掉顶部标题栏,不过好像不好用，和继承自activity还是AppCompatActivity好像有关系
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        this.context = this;
        //初始化控件
        initView();
        //初始化定位
        initLocation();
    }

    //初始化控件
    private void initView()
    {
        mMapView = (MapView) findViewById(R.id.id_bmapView);
        mBaiduMap = mMapView.getMap();
        //设置地图初始放大比例，500米
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
    }

    //初始化定位
    private void initLocation()
    {
        mLocationClient = new LocationClient(this);
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);

        LocationClientOption option = new LocationClientOption();
        //坐标系类型
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("BD09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.id_map_common:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;

            case R.id.id_map_site:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;

            case R.id.id_map_traffic:
                if (mBaiduMap.isTrafficEnabled())
                {
                    mBaiduMap.setTrafficEnabled(false);
                    item.setTitle("开启实时交通");
                } else
                {
                    mBaiduMap.setTrafficEnabled(true);
                    item.setTitle("关闭实时交通");
                }
                break;

            case R.id.id_map_location:
                //设置点击我的位置时，地图放大比例为500米
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15.0f));
                //设置地图中心点为用户位置
                LatLng latLng = new LatLng(mLatitude, mLongtitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //地图位置使用动画效果转过去
                mBaiduMap.animateMapStatus(msu);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            mLocationClient.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        //停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mMapView.onDestroy();
    }

    private class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation bdLocation)
        {
            MyLocationData data = new MyLocationData.Builder()//
                    .accuracy(bdLocation.getRadius())//
                    .latitude(bdLocation.getLatitude())//
                    .longitude(bdLocation.getLongitude())//
                    .build();
            mBaiduMap.setMyLocationData(data);

            //MyLocationConfiguration conf = new MyLocationConfiguration(LocationMode.NORMAL,,)
            //定位成功之后，更新一下我的位置
            mLatitude = bdLocation.getLatitude();
            mLongtitude = bdLocation.getLongitude();

            if (isFirstIn)
            {
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //地图位置使用动画效果转过去
                mBaiduMap.animateMapStatus(msu);
                //将isFirstIn设置为FALSE，以至于不会让屏幕一秒转一次
                isFirstIn = false;
                //定位完成后，弹出定位信息
                Toast.makeText(context, bdLocation.getAddrStr(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
