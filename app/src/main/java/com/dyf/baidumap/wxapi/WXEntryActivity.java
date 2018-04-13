package com.dyf.baidumap.wxapi;

import android.app.Activity;
import android.widget.Toast;

import com.dyf.baidumap.R;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * Created by diy on 2018-01-15.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler
{

    //APP向微信发请求之后，这个方法接收从微信返回的消息
    @Override
    public void onResp(BaseResp baseResp)
    {
        int result = 0;

        Toast.makeText(this, "baseresp.getType = " + baseResp.getType(), Toast.LENGTH_SHORT).show();

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_ok;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.errcode_unsupported;
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                result = R.string.errcode_sentfailed;
                break;

            case BaseResp.ErrCode.ERR_COMM:
                result = R.string.errcode_comm;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }

        Toast.makeText(this, "微信请求登录返回的值："+result, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReq(BaseReq baseReq)
    {

    }
}