package com.dyf.baidumap;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.carrier.CarrierMessagingService;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dyf.utils.Convert;
import com.dyf.utils.SendRequest;
import com.dyf.utils.SysoUtils;
import com.dyf.utils.ToastShow;

/**
 * 充值界面
 */
public class ReCharge extends Activity {

    private TextView tv_balance;
    private RadioGroup rg_rechargenuml;
    private RadioButton rb_ten;
    private RadioButton rb_twenty;
    private RadioButton rb_thirty;
    private RadioButton rb_fifty;
    private RadioButton rb_hundred;
    private Button btn_recharge;
    private RadioButton rb_checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_charge);

        // 初始化控件
        tv_balance = (TextView) findViewById(R.id.tv_balance);
        rg_rechargenuml = (RadioGroup) findViewById(R.id.rg_rechargenum);
        rb_ten = (RadioButton) findViewById(R.id.rb_ten);
        rb_twenty = (RadioButton) findViewById(R.id.rb_twenty);
        rb_thirty = (RadioButton) findViewById(R.id.rb_thirty);
        rb_fifty = (RadioButton) findViewById(R.id.rb_fifty);
        rb_hundred = (RadioButton) findViewById(R.id.rb_hundred);
        btn_recharge = (Button) findViewById(R.id.btn_recharge);

        // 界面一打开，需要查询余额
        getbalance();

        btn_recharge.setOnClickListener(new btnReChargeOnClickListener());
    }


    // 界面一打开，需要查询余额
    private void getbalance() {
        SharedPreferences sharedata = getSharedPreferences("data",0);
        final String openid = sharedata.getString("openid","未登陆");
        if (openid.equals("未登陆")) {
            ToastShow.showToastMsg(ReCharge.this,"请先登陆");
            return;
        }
        else {
            new Thread() {
                @Override
                public void run() {
                    {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = SendRequest.getBalance(openid);
                        Log.i("getBalance:", message.obj.toString());
                        getBalHandler.sendMessage(message);
                    }
                }
            }.start();
        }
    }

    private Handler getBalHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 获取余额之后，界面显示余额
                    String str_bal = msg.obj.toString();
                    tv_balance.setText(str_bal);
                    break;

                default:
                    break;
            }
        }
    };

    // 充值按钮点击事件
    private class btnReChargeOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            rb_checked = (RadioButton) findViewById(rg_rechargenuml.getCheckedRadioButtonId());
            String reChargeNum = rb_checked.getText().toString();
            SysoUtils.print("sys reChargeNum:"+reChargeNum);
        }
    }
}
