package com.dyf.baidumap;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.dyf.model.ResultParklotInfo;
import com.dyf.utils.Constant;
import com.dyf.utils.Convert;
import com.dyf.utils.SendRequest;
import com.dyf.utils.SysoUtils;
import com.dyf.utils.ToastShow;
import com.dyf.utils.Util;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.dyf.baidumap.R.mipmap.marker;


public class MainActivity extends AppCompatActivity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Context context;
    private ProgressBar bar;

    //定位相关
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private boolean isFirstIn = true;
    //导航时的起点和终点
    private LatLng mLastLocationData;
    private LatLng mDestLocationData;
    //分别是我在哪，模拟导航，开始导航，登录 四个按钮
    private Button mBtnLocation;
    private Button mBtnMockNav;
    private Button mBtnRealNav;
    private Button mBtnLogin;
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
    private Button btn_reserve;

    //导航相关
    public static List<Activity> activityList = new LinkedList<Activity>();
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    private static final String APP_FOLDER_NAME = "SharedPS";
    private String mSDCardPath = null;
    private final static String authBaseArr[] =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
    private final static String authComArr[] = {Manifest.permission.READ_PHONE_STATE};
    private final static int authBaseRequestCode = 1;
    private final static int authComRequestCode = 2;

    private boolean hasInitSuccess = false;
    private boolean hasRequestComAuth = false;

    //微信 登录 相关
    //APP_ID
    private static final String APP_ID = Constant.WEIXIN_APP_ID;
    // IWXAPI 是 第三方 APP 和 微信通信 的 openapi 接口
    private IWXAPI api;

    // QQ登录
    public static Tencent mTencent;
    private static final String TAG = MainActivity.class.getName();
    public static String QQAPP_ID = "1106834232";
    private Button mNewLoginButton;
    //private Button mServerSideLoginBtn;
    private TextView mUserInfo;
    private ImageView mUserLogo;
    private UserInfo mInfo;
    private EditText mEtAppid = null;
    private static Intent mPrizeIntent = null;
    private static boolean isServerSideLogin = false;
    SharedPreferences.Editor shareData ;// 将QQ登录的相关信息缓存一下
    private String openid;  // QQ用户识别码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉顶部标题栏,不过好像不好用，和继承自activity还是AppCompatActivity好像有关系
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        activityList.add(this);
        setContentView(R.layout.activity_main);

        this.context = this;
        //初始化控件
        initView();
        //初始化定位
        initLocation();
        //初始化覆盖物图标
        initMarker();
        //注册 微信
        regToWx();
        // 检查QQ是否是登录状态
        //isLogin();


        //通过长按设置终点位置
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //用户长按设置之后，给用户一个提示
                Toast.makeText(MainActivity.this, "设置目的地成功", Toast.LENGTH_SHORT).show();
                //设置终点的位置
                mDestLocationData = latLng;
                //给终点设置一下图标
                addDestInfoOverlay(latLng);
            }
        });

        //点击 我在哪 mBtnLocation 按钮，地图移动到我的位置
        mBtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置点击我的位置时，地图放大比例
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(Constant.BASIC_ZOOM));
                //设置地图中心点为用户位置
                LatLng latLng = new LatLng(mLatitude, mLongtitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //地图位置使用动画效果转过去
                mBaiduMap.animateMapStatus(msu);
            }
        });

        //点击 模拟 导航 按钮
        mBtnMockNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDestLocationData == null) {
                    Toast.makeText(MainActivity.this, "长按地图设置目标地点", Toast.LENGTH_SHORT).show();
                    return;
                }
                routeplanToNavi(false);
            }
        });

        //点击 开始 导航 按钮
        mBtnRealNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDestLocationData == null) {
                    Toast.makeText(MainActivity.this, "长按地图设置目标地点", Toast.LENGTH_SHORT).show();
                    return;
                }
                routeplanToNavi(true);
            }
        });

        //点击 登录 按钮
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastShow.showToastMsg(MainActivity.this, "调用QQ登录");
                qqLogin();
            }
        });

        //点击 覆盖物 图标
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle extraInfo = marker.getExtraInfo();
                ResultParklotInfo resultParklotInfo = (ResultParklotInfo) extraInfo.getSerializable("result");
                //ImageView iv = (ImageView) mMarkerLy.findViewById(R.id.id_info_img);
                TextView distance = (TextView) mMarkerLy.findViewById(R.id.id_info_distance);
                final TextView name = (TextView) mMarkerLy.findViewById(R.id.id_info_name);
                TextView time = (TextView) mMarkerLy.findViewById(R.id.id_info_time);
                TextView noparknum = (TextView) mMarkerLy.findViewById(R.id.id_info_noparknum);
                //TextView zan = (TextView) mMarkerLy.findViewById(R.id.id_info_zan);
                //iv.setImageResource(info.getImgId());
                distance.setText(String.valueOf(resultParklotInfo.getDistance()));
                name.setText(resultParklotInfo.getParklotName());
                time.setText(String.valueOf(resultParklotInfo.getTime()/60));
                noparknum.setText(String.valueOf(resultParklotInfo.getNoParkNum()));

                btn_reserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String parklotName = name.getText().toString();
                        reservePark(parklotName);
                    }
                });
                //zan.setText(0);

                //点击覆盖物，显示文本信息
                InfoWindow infoWindow;
                //infoWindow里面放入一个textView，textView的设置
                TextView tv = new TextView(context);
                tv.setBackgroundResource(R.mipmap.location_tips);
                tv.setPadding(30, 30, 30, 20);
                tv.setText(resultParklotInfo.getParklotName());
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
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //展示信息消失
                mMarkerLy.setVisibility(View.GONE);
                //图标上方文本信息消失
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        //导航部分，copy的导航demo，初始化导航相关
        if (initDirs()) {
            initNavi();
        }

        // 预定车位
        btn_reserve.setOnClickListener(new btnReserveClickListener());


    }

    // 预定停车位
    private void reservePark(String parklotName) {
        SysoUtils.print("sys 停车场名称为："+parklotName);

        SharedPreferences shareData = getSharedPreferences("data",0);
        String nickname = shareData.getString("nickname","未登录");
        if (nickname.equals("未登录")) {
            ToastShow.showToastMsg(MainActivity.this,"请先登录");
            return;
        }
        // 查找数据库中是否已经有车牌号，显示在界面中
        getPlateNum();

        Intent intent = new Intent(MainActivity.this,ReserveActivity.class);
        intent.putExtra("parklotName",parklotName);
        startActivity(intent);
    }

    // 查找数据库中是否已经有车牌号，显示在界面中
    private void getPlateNum() {
        new Thread(){
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 3;
                msg.obj = SendRequest.selectPlateNum(openid);
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 微信 注册
     */
    private void regToWx() {
        // 通过 WXAPIFactory 工厂，获取 IWXAPI 的 实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        // 将 应用 的 APP_ID 注册 到 微信
        api.registerApp(APP_ID);
    }

    public static void initOpenidAndToken(Object value) {
        try {
            SysoUtils.print(" sys initOpenidAndToken方法");
            JSONObject jsonObject = (JSONObject) value;
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    // QQ登录的loginlistener

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(Object values) {
            SysoUtils.print("sys login doComplete:" + values.toString());
            JSONObject val = (JSONObject)values;
            shareData = getSharedPreferences("data",0).edit();
            try {
                if (val.getInt("ret")==0)
                {
                    openid = val.getString("openid");
                    shareData.putString("openid",openid);
                }

                Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            initOpenidAndToken(values);
            updateUserInfo();
            //updateLoginButton();
        }
    };

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {

                }

                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    new Thread() {

                        @Override
                        public void run() {
                            JSONObject json = (JSONObject) response;
                            if (json.has("figureurl")) {
                                Bitmap bitmap = null;
                                try {
                                    bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                                } catch (JSONException e) {

                                }
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                            }
                        }

                    }.start();
                }

                @Override
                public void onCancel() {

                }
            };
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        } else {
            mUserInfo.setText("");
            mUserInfo.setVisibility(android.view.View.GONE);
            //mUserLogo.setVisibility(android.view.View.GONE);
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                        //mUserInfo.setVisibility(android.view.View.VISIBLE);
                        //mUserInfo.setText(response.getString("nickname"));
                        SysoUtils.print("sys response:"+response.toString());
                        String nickname = response.getString("nickname");
                        String gender = response.getString("gender");
                        String province = response.getString("province");
                        String city = response.getString("city");
                        String figureurl = response.getString("figureurl");
                        SysoUtils.print(" sys 用户基本信息："+nickname+","+gender+","+province+","+city+","+figureurl);

                        ToastShow.showToastMsg(MainActivity.this,"欢迎"+nickname);
                        // 将用户名称以类似session的形式保存
                        shareData.putString("nickname",nickname);
                        shareData.commit();
                        insertQQUserInfo(openid, nickname,gender,province,city,figureurl);
                        //SysoUtils.print(" sys "+shareData.commit());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(msg.what == 1){
                Bitmap bitmap = (Bitmap)msg.obj;
                //mUserLogo.setImageBitmap(bitmap);
                //mUserLogo.setVisibility(android.view.View.VISIBLE);
            }
            else if (msg.what == 3) {
                String plateNum = msg.obj.toString();
                if(plateNum.equals("")||plateNum.equals(null)){
                    ToastShow.showToastMsg(MainActivity.this,"请先绑定车牌号");
                    return;
                }
            }
        }

    };

    // QQ用户登录之后，将信息放入系统数据库
    private void insertQQUserInfo(final String openid,final String nickname, final String gender, final String province,final  String city, final String figureurl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SendRequest.insertQQUserInfo(openid,nickname,gender,province,city,figureurl);
            }
        }).start();

    }


    private class BaseUiListener implements IUiListener {


        @Override
        public void onComplete(Object response) {
            doComplete(response);
        }


        protected void doComplete(Object values) {

        }

        @Override

        public void onError(UiError e) {
            SysoUtils.print("sys cancel"+ e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override

        public void onCancel() {
            SysoUtils.print("sys cancel");
        }
    }

    /*private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Util.showResultDialog(MainActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Util.showResultDialog(MainActivity.this, "返回为空", "登录失败");
                return;
            }
            Util.showResultDialog(MainActivity.this, response.toString(), "登录成功");
            // 有奖分享处理
            //handlePrizeShare();
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {       }

        @Override
        public void onError(UiError e) {
            Util.toastMessage(MainActivity.this, "onError: " + e.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.toastMessage(MainActivity.this, "onCancel: ");
            Util.dismissDialog();
            if (isServerSideLogin) {
                isServerSideLogin = false;
            }
        }
    }*/

    /**
     * 点击 登录 按钮，调用 QQ 登录,通过下面这个方法就可以弹出QQ第三方授权页面了，点击确认即可。
     */
    private void qqLogin() {
        if ((mBtnLogin.getText().toString().equals("退出登录"))){
            mTencent.logout(this);
            return;
        }
        SysoUtils.print("sys 进入QQlogin方法");
        mTencent = Tencent.createInstance(QQAPP_ID, MainActivity.this);
        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
        if (!mTencent.isSessionValid()) {
            SysoUtils.print("sys login第一个if");
            mTencent.login(this, "all", loginListener);
            isServerSideLogin = false;
            Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());

        } else {
            if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
                SysoUtils.print("sys login第二个if");
                mTencent.logout(this);
                mTencent.login(this, "all", loginListener);
                isServerSideLogin = false;
                Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                return;
            }
            mTencent.logout(this);
            //updateUserInfo();
            //updateLoginButton();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //mTencent.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);

        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, loginListener);
            }
        }
    }

    // 异步获取QQ用户信息
    /*public void getUserInfo()
    {
        mTencent.requestAsync(Constants.GRAPH_SIMPLE_USER_INFO, null,
                Constants.HTTP_GET, new BaseApiListener("get_simple_userinfo", false), null);
    }*/

    /*{
        // send oauth request
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);
        *//*
        {
    // send oauth request
    Final SendAuth.Req req = new SendAuth.Req();
    req.scope = "snsapi_userinfo";
    req.state = "wechat_sdk_demo_test";
    api.sendReq(req);
}
         *//*
    }*/

    //导航部分，copy的导航demo，初始化导航相关
    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    String authinfo = null;

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };

    public void showToastMsg(final String msg) {
        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean hasCompletePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 初始化导航
     */
    private void initNavi() {
        BNOuterTTSPlayerCallback ttsCallback = null;
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }

        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                Toast.makeText(MainActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                hasInitSuccess = true;
                initSetting();
            }

            public void initStart() {
                Toast.makeText(MainActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText(MainActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        }, null, ttsHandler, ttsPlayStateListener);
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private BNRoutePlanNode.CoordinateType mCoordinateType = null;

    /**
     * @param mock 设置是否是真实导航，false为模拟，TRUE为真实导航
     */
    private void routeplanToNavi(boolean mock) {
        BNRoutePlanNode.CoordinateType coType = BNRoutePlanNode.CoordinateType.BD09LL;
        mCoordinateType = coType;
        if (!hasInitSuccess) {
            Toast.makeText(MainActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        // 权限申请
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth()) {
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    this.requestPermissions(authComArr, authComRequestCode);
                    return;
                } else {
                    Toast.makeText(MainActivity.this, "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;

        //设置的真实的起点和终点
        //mLastLocationData
        //mDestLocationData
        //sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436, "百度大厦", null, coType);
        //eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085, "北京天安门", null, coType);
        mLastLocationData = new LatLng(mLatitude, mLongtitude);
        Log.i("我的mLastLocationData", mLastLocationData.toString());
        Log.i("我的mDestLocationData", mDestLocationData.toString());
        sNode = new BNRoutePlanNode(mLastLocationData.longitude, mLastLocationData.latitude, "我的地点", null, coType);
        eNode = new BNRoutePlanNode(mDestLocationData.longitude, mDestLocationData.latitude, "目标地点", null, coType);

        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            // 开发者可以使用旧的算路接口，也可以使用新的算路接口,可以接收诱导信息等
            // BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, mock, new DemoRoutePlanListener(sNode),
                    eventListerner);
        }
    }

    BaiduNaviManager.NavEventListener eventListerner = new BaiduNaviManager.NavEventListener() {

        @Override
        public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {
            BNEventHandler.getInstance().handleNaviEvent(what, arg1, arg2, bundle);
        }
    };

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {
        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
             */
            for (Activity ac : activityList) {
                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
                    return;
                }
            }
            Intent intent = new Intent(MainActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            Log.e("TAG", "算路失败");
            Toast.makeText(MainActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "10277611");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }

    //给终点设置一下图标，设置终点信息
    private void addDestInfoOverlay(LatLng destInfo) {
        //清除原来的
        mBaiduMap.clear();
        //设置地点覆盖物的信息
        OverlayOptions options = new MarkerOptions().position(destInfo)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.myloc_72px))
                .zIndex(5);
        mBaiduMap.addOverlay(options);
    }

    //初始化覆盖物图标
    private void initMarker() {
        mMarker = BitmapDescriptorFactory.fromResource(marker);
        mMarkerLy = (RelativeLayout) findViewById(R.id.id_marker_ly);

    }

    //初始化控件
    private void initView() {
        bar = (ProgressBar) findViewById(R.id.id_pgb_main);
        bar.setVisibility(View.GONE);
        mBtnLocation = (Button) findViewById(R.id.id_btn_location);
        mBtnMockNav = (Button) findViewById(R.id.id_btn_mocknav);
        mBtnRealNav = (Button) findViewById(R.id.id_btn_realnav);
        //登录 按钮
        mBtnLogin = (Button) findViewById(R.id.id_btn_login);
        mMapView = (MapView) findViewById(R.id.id_bmapView);
        mBaiduMap = mMapView.getMap();
        //设置地图初始放大比例
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(Constant.BASIC_ZOOM);
        mBaiduMap.setMapStatus(msu);
        // 预定车位按钮
        btn_reserve = (Button) findViewById(R.id.id_info_reserve);
    }

    //初始化定位
    private void initLocation() {
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
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //普通地图
            /*case R.id.id_map_common:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;*/

            //卫星地图
            /*case R.id.id_map_site:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;*/

            //开启关闭实时交通
            case R.id.id_map_traffic:
                if (mBaiduMap.isTrafficEnabled()) {
                    mBaiduMap.setTrafficEnabled(false);
                    item.setTitle("开启实时交通");
                } else {
                    mBaiduMap.setTrafficEnabled(true);
                    item.setTitle("关闭实时交通");
                }
                break;

            //我的位置
            /*case R.id.id_map_location:
                //设置点击我的位置时，地图放大比例为500米
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15.0f));
                //设置地图中心点为用户位置
                LatLng latLng = new LatLng(mLatitude, mLongtitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //地图位置使用动画效果转过去
                mBaiduMap.animateMapStatus(msu);
                break;*/

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
                addOverlays();
                break;

            //根据距离查找停车场
            case R.id.id_search_bydistance:
                searchBestParklotInfo("distance", mLongtitude, mLatitude);
                break;
            //根据时间查找停车场
            case R.id.id_search_bytime:
                searchBestParklotInfo("time", mLongtitude, mLatitude);
                break;
            //根据未停车数查找停车场
            case R.id.id_search_bynoparknum:
                searchBestParklotInfo("noParkNum", mLongtitude, mLatitude);
                break;
            // 测试sharepreferences是否能用
            case R.id.id_testusername:
                SharedPreferences shareData = getSharedPreferences("data",0);
                String testnickname = shareData.getString("nickname","昵称获取失败");
                String testopenid = shareData.getString("openid","账号获取失败");
                ToastShow.showToastMsg(MainActivity.this,"欢迎："+testnickname+","+testopenid);
                break;

            // 点击充值按钮，跳转充值界面
            case R.id.id_recharge:
                shareData = getSharedPreferences("data",0);

                openid = shareData.getString("openid","账号获取失败");
                if (openid.equals("账号获取失败")){
                    ToastShow.showToastMsg(MainActivity.this,"请先登陆");
                    SysoUtils.print("账号获取失败");
                    break;
                }
                else {
                Intent intent = new Intent(MainActivity.this,ReChargeActivity.class);
                startActivity(intent);
                break;
                }
                // 绑定车牌号
            case R.id.id_bindplatenum:
                Intent bindplatenumIntent = new Intent(MainActivity.this,BindPlateNumActivity.class);
                    startActivity(bindplatenumIntent);
                break;

            // 评价
            case R.id.id_evaluate:
                Intent evaluateIntent = new Intent(MainActivity.this,EvaluateActivity.class);
                startActivity(evaluateIntent);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 根据条件获取合适停车场的handler
     */
    private Handler getBestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int type = msg.what;
            List<Map<String, String>> listInfos = (List<Map<String, String>>) msg.obj;
            switch (type) {
                case 1:
                    bar.setVisibility(View.GONE);
                    if (listInfos.size() > 0) {
                        showAlertDialog("提示", listInfos);
                    } else {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("提示")
                                .setMessage("没有获取到停车场信息")
                                .setPositiveButton("确定", null)
                                .show();
                    }

                    break;
            }
        }
    };

    /**
     * 获取全部停车场信息的handler
     */
    private Handler getAllHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case 1:
                    bar.setVisibility(View.GONE);
                    mBaiduMap.clear();
                    LatLng latLng = null;
                    Marker marker = null;
                    OverlayOptions options;
                    List<Map<String, String>> listItems = (List<Map<String, String>>) msg.obj;
                    List<ResultParklotInfo> resultParklotInfos = new ArrayList<ResultParklotInfo>();
                    if (listItems.size() > 0) {
                        for (int i = 0; i < listItems.size(); i++) {
                            String parklotName = listItems.get(i).get("parklotName");
                            int distance = Integer.parseInt(listItems.get(i).get("distance")); // 车辆距离停车场的距离，单位 米
                            int time = Integer.parseInt(listItems.get(i).get("time"));// 车辆行驶到停车场所需的时间
                            int noParkNum = Integer.parseInt(listItems.get(i).get("noParkNum")); //停车场的未停车数
                            double noParkRate = Double.parseDouble(listItems.get(i).get("noParkRate")); //停车场的未停车率
                            int parklotAmount = Integer.parseInt(listItems.get(i).get("parklotAmount")); // 停车场车位数量
                            String parklotLng = listItems.get(i).get("parklotLng"); // 停车场位置精度
                            String parklotLat = listItems.get(i).get("parklotLat");// 停车场位置纬度
                            String parklotDescription = listItems.get(i).get("parklotDescription");// 停车场描述
                            ResultParklotInfo resultParklotInfo = new ResultParklotInfo(parklotName, distance, time, noParkNum, parklotAmount, parklotLng, parklotLat, parklotDescription);
                            resultParklotInfos.add(resultParklotInfo);
                        }

                        for (ResultParklotInfo result : resultParklotInfos) {
                            //经纬度
                            latLng = new LatLng(Double.parseDouble(result.getParklotLat()), Double.parseDouble(result.getParklotLng()));
                            //图标
                            options = new MarkerOptions()//
                                    .position(latLng)//指定marker的地图上的位置
                                    .icon(mMarker)//marker的图标
                                    .zIndex(5);//指定图层的位置，值越大，显示高层
                            //实例化marker
                            marker = (Marker) mBaiduMap.addOverlay(options);
                            Bundle arg0 = new Bundle();
                            arg0.putSerializable("result", result);
                            marker.setExtraInfo(arg0);
                        }
                    }

                    //每次添加完图层之后，把地图移动到第一个或者最后一个图层的位置，不然图标如果和所在位置差的远，看不到
                    //newLatLng方法里面的参数写latlng，就是移动到第一个或者最后一个图层的位置，我这里还让地图以我为中心
                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(new LatLng(mLatitude, mLongtitude));
                    mBaiduMap.setMapStatus(msu);
                    break;
            }
        }
    };

    /**
     * 创建确定取消对话框，服务器端返回消息后，在界面显示第一个结果的信息
     *
     * @param title 对话框标题
     * @param body  对话框要显示的内容
     */
    private void showAlertDialog(String title, final List<Map<String, String>> body) {
        final int num = 0;
        int time = Integer.parseInt(body.get(num).get("time")) / 60;
        if (time < 1) {
            time = 1;
        }

        String message = "名称：" + body.get(num).get("parklotName")
                + " ，距离为：" + body.get(num).get("distance")
                + "米，大概时间为：" + time + "分钟，剩余车位数为："
                + body.get(num).get("noParkNum");
        //新建对话框，使用Builder来创建带确定，取消的对话框
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        //alertDialog.setIcon(R.drawable.img_border);  //设置对话框的图标
        alertDialog.setTitle(title);  //设置对话框的标题
        alertDialog.setMessage(message);  //设置对话框要显示的内容
        //添加确定，取消按钮，先添加 确定  按钮
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double destLng = Double.parseDouble(body.get(num).get("parklotLng"));
                double destLat = Double.parseDouble(body.get(num).get("parklotLat"));
                //设置终点的位置
                LatLng latLng = new LatLng(destLat, destLng);
                mDestLocationData = latLng;
                //给终点设置一下图标
                addDestInfoOverlay(latLng);
                showToastMsg("终点坐标：" + destLng + " " + destLat);
                routeplanToNavi(true);
            }
        });
        //添加取消按钮
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        //添加（中立）下一个按钮
                /*alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "下一个", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        showToastMsg("下一个  ");
                    }
                });*/
        //显示对话框
        alertDialog.show();
    }

    /**
     * 根据不同的条件查找适合的停车场信息
     *
     * @param condi       条件
     * @param mLongtitude 个人经度信息
     * @param mLatitude   个人纬度信息
     */
    private void searchBestParklotInfo(final String condi, final double mLongtitude, final double mLatitude) {
        bar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                String selfLng = Convert.doubleToString(mLongtitude);
                String selfLat = Convert.doubleToString(mLatitude);
                message.obj = SendRequest.getBestParklotInfo(condi, selfLng, selfLat);
                Log.i("getBestParklotInfo:", message.obj.toString());
                getBestHandler.sendMessage(message);
            }
        }.start();
    }

    /**
     * 添加覆盖物
     */
    private void addOverlays() {
        bar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                String selfLng = Convert.doubleToString(mLongtitude);
                String selfLat = Convert.doubleToString(mLatitude);
                message.obj = SendRequest.getAllParklotInfo(selfLng, selfLat);
                Log.i("getAllParklotInfo:", message.obj.toString());
                getAllHandler.sendMessage(message);
            }
        }.start();

    }

    /**
     * 定位到我的位置
     */
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
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

            if (isFirstIn) {
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                //地图位置使用动画效果转过去
                mBaiduMap.animateMapStatus(msu);
                //将isFirstIn设置为FALSE，以至于不会让屏幕一秒转一次
                isFirstIn = false;
                //定位完成后，弹出定位信息
                Toast.makeText(context, bdLocation.getAddrStr(), Toast.LENGTH_LONG).show();
                // 打开客户端时，应该显示覆盖物
                addOverlays();
            }

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            mLocationClient.start();
        //传感器也需要开启和关闭，开启方向传感器
        myOrientationListener.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        //传感器也需要开启和关闭，关闭方向传感器
        myOrientationListener.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    // 预定车位按钮点击
    private class btnReserveClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

        }
    }
}
