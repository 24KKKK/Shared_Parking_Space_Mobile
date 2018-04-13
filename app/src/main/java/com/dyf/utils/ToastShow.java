package com.dyf.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by diy on 2018-01-13.
 */

/**
 * 进一步封装一下toast类，让打印语句简单一点
 */
public class ToastShow
{
    public static void showToastMsg(Context context,final String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
