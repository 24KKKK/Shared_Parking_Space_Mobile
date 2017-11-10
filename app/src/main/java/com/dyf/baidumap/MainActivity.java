package com.dyf.baidumap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.List;


public class MainActivity extends AppCompatActivity
{
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Context context;

    //定位相关
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private boolean isFirstIn = true;
    //导航时的起点和终点
    private LatLng mLastLocationData;
    private LatLng mDestLocationData;
    //分别是我在哪，模拟导航，开始导航三个按钮
    private Button mBtnLocation;
    private Button mBtnMockNav;
    private Button mBtnRealNav;
    //回到原位置变量，我的位置的经纬度
    private double mLatitude;
    private double mLongtitude;

    //自定义定位图标
    private BitmapDescriptor mIconLocation;
    private MyOrientationListener myOrientationListener;
    //记录一下当前位置
    private float mCurrentX;
    //切换模式的变量
    private MyLocationConfiguration.LocationMode mLocationMode;

    //覆盖物相关
    //覆盖物图标
    private BitmapDescriptor mMarker;
    //覆盖物信息展示
    private RelativeLayout mMarkerLy;

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
        //初始化覆盖物图标
        initMarker();

        //通过长按设置终点位置
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener()
        {
            @Override
            public void onMapLongClick(LatLng latLng)
            {
                //用户长按设置之后，给用户一个提示
                Toast.makeText(MainActivity.this,"设置目的地成功",Toast.LENGTH_SHORT).show();
                //设置终点的位置
                mDestLocationData = latLng;
                //给终点设置一下图标
                addDestInfoOverlay(latLng);
            }
        });

        //点击我在哪mBtnLocation按钮，地图移动到我的位置
        mBtnLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //设置点击我的位置时，地图放大比例为500米
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15.0f));
                //设置地图中心点为用户位置
                LatLng latLng = new LatLng(mLatitude, mLongtitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //地图位置使用动画效果转过去
                mBaiduMap.animateMapStatus(msu);
            }
        });

        //点击模拟导航按钮
        mBtnMockNav.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        //点击开始导航按钮
        mBtnRealNav.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                Bundle extraInfo = marker.getExtraInfo();
                Info info = (Info) extraInfo.getSerializable("info");
                ImageView iv = (ImageView) mMarkerLy.findViewById(R.id.id_info_img);
                TextView distance = (TextView) mMarkerLy.findViewById(R.id.id_info_distance);
                TextView name = (TextView) mMarkerLy.findViewById(R.id.id_info_name);
                TextView zan = (TextView) mMarkerLy.findViewById(R.id.id_info_zan);
                iv.setImageResource(info.getImgId());
                distance.setText(info.getDistance());
                name.setText(info.getName());
                zan.setText(info.getZan() + "");

                //点击覆盖物，显示文本信息
                InfoWindow infoWindow;
                //infoWindow里面放入一个textView，textView的设置
                TextView tv = new TextView(context);
                tv.setBackgroundResource(R.mipmap.location_tips);
                tv.setPadding(30, 30, 30, 20);
                tv.setText(info.getName());
                tv.setTextColor(Color.parseColor("#ffffff"));

                //获得经纬度
                final LatLng latLng = marker.getPosition();
                //通过经纬度拿到屏幕上实际坐标的值
                Point p = mBaiduMap.getProjection().toScreenLocation(latLng);
                //调整这个坐标
                p.y -= 47;
                //把这个坐标转化为经纬度
                LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);

                //初始化infoWindow，把经纬度传给infowindow
                infoWindow = new InfoWindow(tv, ll, 1);
                /*infoWindow = new InfoWindow(mMarker, ll, 1, new OnInfoWindowClickListener()
                {
                    @Override
                    public void onInfoWindowClick()
                    {
                        //infowindow点击的处理
                        mBaiduMap.hideInfoWindow();
                    }
                });*/

                mBaiduMap.showInfoWindow(infoWindow);

                mMarkerLy.setVisibility(View.VISIBLE);
                return true;
            }
        });

        //点击地图其他地方的时候，展示信息消失
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                //展示信息消失
                mMarkerLy.setVisibility(View.GONE);
                //图标上方文本信息消失
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi)
            {
                return false;
            }
        });
    }

    //给终点设置一下图标，设置终点信息
    private void addDestInfoOverlay(LatLng destInfo)
    {
        //清除原来的
        mBaiduMap.clear();
        //设置地点覆盖物的信息
        OverlayOptions options = new MarkerOptions().position(destInfo)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.myloc_72px))
                .zIndex(5);
        mBaiduMap.addOverlay(options);
    }

    //初始化覆盖物图标
    private void initMarker()
    {
        mMarker = BitmapDescriptorFactory.fromResource(R.mipmap.marker);
        mMarkerLy = (RelativeLayout) findViewById(R.id.id_marker_ly);
    }

    //初始化控件
    private void initView()
    {
        mBtnLocation = (Button) findViewById(R.id.id_btn_location);
        mBtnMockNav = (Button) findViewById(R.id.id_btn_mocknav);
        mBtnRealNav = (Button) findViewById(R.id.id_btn_realnav);
        mMapView = (MapView) findViewById(R.id.id_bmapView);
        mBaiduMap = mMapView.getMap();
        //设置地图初始放大比例，500米
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
    }

    //初始化定位
    private void initLocation()
    {
        //定位模式默认使用普通模式
        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
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

        //初始化自定义图标
        mIconLocation = BitmapDescriptorFactory.fromResource(R.mipmap.navigation);
        myOrientationListener = new MyOrientationListener(context);

        //当方向发生改变的时候，更新地图上方向图标的位置
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener()
        {
            @Override
            public void onOrientationChanged(float x)
            {
                mCurrentX = x;
            }
        });
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
            //普通地图
            case R.id.id_map_common:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;

            //卫星地图
            case R.id.id_map_site:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;

            //开启关闭实时交通
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

            //我的位置
            case R.id.id_map_location:
                //设置点击我的位置时，地图放大比例为500米
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15.0f));
                //设置地图中心点为用户位置
                LatLng latLng = new LatLng(mLatitude, mLongtitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //地图位置使用动画效果转过去
                mBaiduMap.animateMapStatus(msu);
                break;

            //普通模式
            case R.id.id_map_mode_common:
                mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
                break;

            //跟随模式
            case R.id.id_map_mode_following:
                mLocationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                break;

            //罗盘模式
            case R.id.id_map_mode_compass:
                mLocationMode = MyLocationConfiguration.LocationMode.COMPASS;
                break;

            //点击添加覆盖物
            case R.id.id_add_overlay:
                addOverlays(Info.infos);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 添加覆盖物
     *
     * @param infos
     */
    private void addOverlays(List<Info> infos)
    {
        mBaiduMap.clear();
        LatLng latLng = null;
        Marker marker = null;
        OverlayOptions options;
        for (Info info : infos)
        {
            //经纬度
            latLng = new LatLng(info.getLatitude(), info.getLongtitude());
            //图标
            options = new MarkerOptions()//
                    .position(latLng)//指定marker的地图上的位置
                    .icon(mMarker)//marker的图标
                    .zIndex(5);//指定图层的位置，值越大，显示高层
            //实例化marker
            marker = (Marker) mBaiduMap.addOverlay(options);
            Bundle arg0 = new Bundle();
            arg0.putSerializable("info", info);
            marker.setExtraInfo(arg0);
        }
        //每次添加完图层之后，把地图移动到第一个或者最后一个图层的位置，不然图标如果和所在位置差的远，看不到
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
    }

    /**
     * 定位到我的位置
     */
    private class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation bdLocation)
        {
            MyLocationData data = new MyLocationData.Builder()//
                    .direction(mCurrentX)//定位成功之后，才会更新方向
                    .accuracy(bdLocation.getRadius())//
                    .latitude(bdLocation.getLatitude())//
                    .longitude(bdLocation.getLongitude())//
                    .build();
            mBaiduMap.setMyLocationData(data);
            //设置自定义图标，箭头方向暂时不会跟随手机转动，需要设置方向传感器
            MyLocationConfiguration config = new MyLocationConfiguration
                    (mLocationMode, true, mIconLocation);
            mBaiduMap.setMyLocationConfiguration(config);
            //定位成功之后，更新一下我的位置，更新经纬度
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
        //传感器也需要开启和关闭，开启方向传感器
        myOrientationListener.start();
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
        //传感器也需要开启和关闭，关闭方向传感器
        myOrientationListener.stop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
