package com.dyf.baidumap;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dyf.utils.AlipayConstants;
import com.dyf.utils.Convert;
import com.dyf.utils.OrderInfoUtil2_0;
import com.dyf.utils.SendRequest;
import com.dyf.utils.SysoUtils;
import com.dyf.utils.ToastShow;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * 充值界面
 */
public class ReChargeActivity extends FragmentActivity {

    private TextView tv_balance;
    private RadioGroup rg_rechargenuml;
    private RadioButton rb_ten;
    private RadioButton rb_twenty;
    private RadioButton rb_thirty;
    private RadioButton rb_fifty;
    private RadioButton rb_hundred;
    private Button btn_recharge;
    private RadioButton rb_checked;

    // 支付相关
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = AlipayConstants.APPID;
    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = AlipayConstants.TARGET_ID;

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = AlipayConstants.RSA2_PRIVATE;
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private String reChargeNum = "";

    private String openid = "";

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
        SharedPreferences sharedata = getSharedPreferences("data", 0);
        openid = sharedata.getString("openid", "未登陆");
        if (openid.equals("未登陆")) {
            ToastShow.showToastMsg(ReChargeActivity.this, "请先登陆");
            return;
        } else {
            new Thread() {
                @Override
                public void run() {
                    {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = SendRequest.getBalance(openid);
                        Log.i("getBalance:", message.obj.toString());
                        mHandler.sendMessage(message);
                    }
                }
            }.start();
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(ReChargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        tv_balance.setText(Convert.doubleToString(Double.parseDouble(reChargeNum)+Double.parseDouble(tv_balance.getText().toString())));
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SendRequest.insertReChargeOption(openid,reChargeNum,"充值");
                            }
                        }).start();

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ReChargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                case 0:
                    // 获取余额之后，界面显示余额
                    String str_bal = msg.obj.toString();
                    tv_balance.setText(str_bal);
                    break;

                default:
                    break;
            }
        }

        ;
    };

    // 充值按钮点击事件
    private class btnReChargeOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            rb_checked = (RadioButton) findViewById(rg_rechargenuml.getCheckedRadioButtonId());
            reChargeNum = rb_checked.getText().toString();
            SysoUtils.print("sys reChargeNum:" + reChargeNum);
            if (reChargeNum.equals("")) {
                ToastShow.showToastMsg(getApplicationContext(), "请先选择金额");
                return;
            } else {
                // 启动支付业务
                payV2();
            }

        }
    }

    /**
     * 支付宝支付业务
     */
    public void payV2() {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,reChargeNum);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;


        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ReChargeActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
