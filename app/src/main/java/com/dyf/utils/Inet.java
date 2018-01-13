package com.dyf.utils;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Inet {
	
	public static String getIP(){
		String IP = "0.0.0.0";
		try {
			IP = Inet4Address.getLocalHost().getHostAddress().toString();
			System.out.println("本机IP地址为："+IP);
		} catch (UnknownHostException e) {
			System.out.println("获取IP失败："+e.toString());
			e.printStackTrace();
		}
		return IP;
	}
}
