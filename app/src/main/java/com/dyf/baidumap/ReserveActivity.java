package com.dyf.baidumap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dyf.utils.SendRequest;

import java.util.Calendar;

public class ReserveActivity extends AppCompatActivity implements View.OnClickListener, DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {

    private Context context;
    private LinearLayout llstarttime, llendtime, llstartdate, llenddate;
    private TextView tv_startdate, tv_enddate, tv_starttime, tv_endtime, tv_username, tv_parklotName;
    private Button btn_reserve;
    private int year, month, day, hour, minute; // 当前时间
    private StringBuffer date, time;  // 创建两个StringBuffer变量，用于拼接获取到的时间数据
    String nickname,openid,parklotName;

    SharedPreferences shareData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        context = this;

        date = new StringBuffer();
        time = new StringBuffer();

        // 初始化获取当前的日期和时间
        initDateTime();
        // 初始化控件
        initView();
        // 获取从上一个页面传过来的数据
        getIntentData();
        // 获取sharedpreferences的数据（nickname）
        getShareData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 开始停车日期
            case R.id.id_ll_startdate:
                setDate(tv_startdate);
                break;
            // 结束停车日期
            case R.id.id_ll_enddate:
                setDate(tv_enddate);
                break;
            // 开始停车时间
            case R.id.id_ll_starttime:
                setTime(tv_starttime);
                break;
            // 结束停车时间
            case R.id.id_ll_endtime:
                setTime(tv_endtime);
                break;
            // 预定按钮
            case R.id.btn_reserve:
                btnReserveOnClickListener();
                break;


            default:
                break;
        }


    }

    // 点击预定按钮
    private void btnReserveOnClickListener() {
        final String startTime = tv_starttime.getText().toString();
        final String endTime = tv_endtime.getText().toString();
        final String startDate = tv_startdate.getText().toString();
        final String endDate = tv_enddate.getText().toString();
        new Thread(){
            @Override
            public void run() {
                Message msg = new Message();
                msg.what =1;
                msg.obj = SendRequest.insertReserveOption(openid,parklotName,startTime,endTime,startDate,endDate);
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int i = Integer.parseInt(msg.obj.toString());
                    if (i == 1) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReserveActivity.this);
                        alertDialog.setTitle("提示");
                        alertDialog.setMessage("预定成功");
                        alertDialog.show();
                    }
                    break;
            }

        }
    };

    public void setDate(final TextView view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (date.length() > 0) { //清除上次记录的日期
                    date.delete(0, date.length());
                }
                view.setText(date.append(String.valueOf(year)).append("-").append(String.valueOf(month + 1)).append("-").append(String.valueOf(day)));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(context, R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(year, month, day, this);
    }

    public void setTime(final TextView view) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        builder2.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (time.length() > 0) { //清除上次记录的日期
                    time.delete(0, time.length());
                }
                view.setText(time.append(String.valueOf(hour)).append(":").append(String.valueOf(minute)));
                dialog.dismiss();
            }
        });
        builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog2 = builder2.create();
        View dialogView2 = View.inflate(context, R.layout.dialog_time, null);
        TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setIs24HourView(true); //设置24小时制
        timePicker.setOnTimeChangedListener(this);
        dialog2.setTitle("设置时间");
        dialog2.setView(dialogView2);
        dialog2.show();
    }


    @Override
    public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
    }

    // 初始化获取当前的日期和时间
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    // 获取sharedpreferences的数据（nickname）
    private void getShareData() {
        shareData = getSharedPreferences("data", 0);
        nickname = shareData.getString("nickname", "未登录");
        openid = shareData.getString("openid","未登录");
        tv_username.setText(nickname);

    }

    // 获取从上一个页面传过来的数据
    private void getIntentData() {
        Intent intent = getIntent();
        parklotName = intent.getStringExtra("parklotName");
        tv_parklotName.setText(parklotName);
    }

    // 初始化控件
    private void initView() {
        llstartdate = (LinearLayout) findViewById(R.id.id_ll_startdate);
        llenddate = (LinearLayout) findViewById(R.id.id_ll_enddate);
        llstarttime = (LinearLayout) findViewById(R.id.id_ll_starttime);
        llendtime = (LinearLayout) findViewById(R.id.id_ll_endtime);
        tv_startdate = (TextView) findViewById(R.id.id_tv_startdate);
        tv_enddate = (TextView) findViewById(R.id.id_tv_enddate);
        tv_starttime = (TextView) findViewById(R.id.id_tv_starttime);
        tv_endtime = (TextView) findViewById(R.id.id_tv_endtime);
        tv_username = (TextView) findViewById(R.id.id_tv_nickname);
        tv_parklotName = (TextView) findViewById(R.id.id_tv_parklotname);
        btn_reserve = (Button) findViewById(R.id.btn_reserve);

        tv_starttime.setText(hour+":"+minute);
        tv_endtime.setText(hour+":"+minute);
        tv_startdate.setText(year+"-"+(month+1)+"-"+day);
        tv_enddate.setText(year+"-"+(month+1)+"-"+day);

        llstartdate.setOnClickListener(this);
        llenddate.setOnClickListener(this);
        llstarttime.setOnClickListener(this);
        llendtime.setOnClickListener(this);
        btn_reserve.setOnClickListener(this);
    }
}
