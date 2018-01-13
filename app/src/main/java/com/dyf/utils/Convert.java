package com.dyf.utils;

import java.text.NumberFormat;

/**
 * Created by diy on 2018-01-13.
 */

public class Convert
{
    /**
     * 将double转换为string
     * @param dou 传过来的double类型参数
     * @return
     */
    public static String doubleToString(double dou) {
        Double dou_obj = new Double(dou);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        String dou_str = nf.format(dou_obj);
        return dou_str;
    }
}
