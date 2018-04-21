package com.dyf.baidumap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dyf.utils.SendRequest;

public class BindPlateNumActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_plateNum;
    private Button btn_bind;

    SharedPreferences shareData;
    String nickname,openid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_plate_num);

        initView();
        // 获取sharedpreferences的数据（nickname）
        getShareData();
        // 查找数据库中是否已经有车牌号，显示在界面中
        getPlateNum();

    }
    // 查找数据库中是否已经有车牌号，显示在界面中
    private void getPlateNum() {
        new Thread(){
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 2;
                msg.obj = SendRequest.selectPlateNum(openid);
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void initView() {
        et_plateNum = (EditText) findViewById(R.id.id_et_platenum);
        btn_bind = (Button) findViewById(R.id.id_btn_bind);

        btn_bind.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_bind :
                String plateNum = et_plateNum.getText().toString();
                bindPlateNum(openid,plateNum);
        }

    }

    // 进行绑定操作
    private void bindPlateNum(final String openid, final String plateNum) {
        new Thread(){
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = SendRequest.updatePlateNum(openid,plateNum);
                mHandler.sendMessage(msg);
            }
        }.start();
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 绑定车牌号
                case 1:
                    int i = Integer.parseInt(msg.obj.toString());
                    if (i == 1) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BindPlateNumActivity.this);
                        alertDialog.setTitle("提示");
                        alertDialog.setMessage("绑定成功");
                        alertDialog.show();
                        Intent intent = new Intent(BindPlateNumActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    break;

                // 查询数据库中如果存在指定账号的车牌号，则显示在界面中
                case 2:
                    String plateNum = msg.obj.toString();
                    et_plateNum.setText(plateNum);
                    break;

                default:
                    break;
            }

        }
    };

    // 获取sharedpreferences的数据（nickname）
    private void getShareData() {
        shareData = getSharedPreferences("data", 0);
        nickname = shareData.getString("nickname", "未登录");
        openid = shareData.getString("openid","未登录");

    }
}
