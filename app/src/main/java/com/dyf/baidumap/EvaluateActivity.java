package com.dyf.baidumap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dyf.utils.SendRequest;
import com.dyf.utils.SysoUtils;

public class EvaluateActivity extends AppCompatActivity {

    private Context context;
    private EditText etEvaluateContent;
    private Button mBtnSubmit;
    private TextView tv_nickname;
    private String nickName,openid,evaluateScore;  // 分别是昵称，openid，评价分数
    private RadioGroup rg_evaluatenum;
    private RadioButton rb_one;
    private RadioButton rb_two;
    private RadioButton rb_three;
    private RadioButton rb_four;
    private RadioButton rb_five;
    private RadioButton rb_checked;

    SharedPreferences shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        context = this;

        // 初始化控件
        initView();
        // 获取sharedpreferences的数据（nickname）
        getShareData();

        // 提交按钮点击
        mBtnSubmit.setOnClickListener(new btnSubmitOnClickListener());
    }

    // 初始化控件
    private void initView() {
        tv_nickname = (TextView) findViewById(R.id.id_tv_nickname);
        mBtnSubmit = (Button) findViewById(R.id.id_btn_evaluatesubmit);
        etEvaluateContent = (EditText) findViewById(R.id.id_et_evaluatecontent);
        rg_evaluatenum = (RadioGroup) findViewById(R.id.rg_evaluatenum);
        rb_one = (RadioButton) findViewById(R.id.rb_one);
        rb_two = (RadioButton) findViewById(R.id.rb_two);
        rb_three = (RadioButton) findViewById(R.id.rb_three);
        rb_four = (RadioButton) findViewById(R.id.rb_four);
        rb_five = (RadioButton) findViewById(R.id.rb_five);
    }

    // 获取sharedpreferences的数据（nickname）
    private void getShareData() {
        shareData = getSharedPreferences("data", 0);
        nickName = shareData.getString("nickname", "未登录");
        openid = shareData.getString("openid","未登录");
        tv_nickname.setText(nickName);
    }

    // 提交评价按钮
    private class btnSubmitOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            rb_checked = (RadioButton) findViewById(rg_evaluatenum.getCheckedRadioButtonId());
            evaluateScore = rb_checked.getText().toString();
            SysoUtils.print("sys evaluateScore:" + evaluateScore);
            final String evaluateContent = etEvaluateContent.getText().toString();

            new Thread(){
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what =1;
                    msg.obj = SendRequest.submitEvaluate(openid,evaluateScore,evaluateContent);
                    mHandler.sendMessage(msg);
                }
            }.start();
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int i = Integer.parseInt(msg.obj.toString());
                    if (i == 1) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EvaluateActivity.this);
                        alertDialog.setTitle("提示");
                        alertDialog.setMessage("提交成功");
                        alertDialog.show();
//                        Intent intent = new Intent(EvaluateActivity.this,MainActivity.class);
//                        startActivity(intent);
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EvaluateActivity.this);
                        alertDialog.setTitle("提示");
                        alertDialog.setMessage("提交失败");
                        alertDialog.show();
                    }
                    break;
            }

        }
    };
}
