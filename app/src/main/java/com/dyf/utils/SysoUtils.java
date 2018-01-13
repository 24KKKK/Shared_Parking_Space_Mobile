package com.dyf.utils;
/**
 * 描述：封装输出打印，设置打印开关
 * @author diy
 * 
 */
public class SysoUtils {
	
	
	private static final boolean DEBUG = true;  //输出开关
	
	/**
	 * 描述：用于调试输出 ，替代syso的工具类
	 * 注意：静态方法，使用类名进行调用
	 * 		使用此工具类请注意重写tostring（），如需定义其他输出样式，请自行修改
	 * 		修改开关DEBUG的值可进行是否输出的切换
	 * @param args 可变参数列表
	 */
	public static void print (Object...args){
		if (DEBUG) {
			for (Object arg:args){
				System.out.println(arg);
			}
			System.out.println();
		}
	}
	
	/**
	 * 不换行的打印输出
	 * @param args
	 */
	public static void p (Object...args){
		if (DEBUG) {
			for (Object arg:args){
				System.out.print(arg);
			}
			System.out.print(" ");
		}
	}
	
	/*
	 * 描述：或者如果只打印字符串类型，可以使用下面这个
	private static final boolean DEBUG = false;
	
	public static void p (String s) {
		if (DEBUG) {
			System.out.println(s);
		}
	}
	 */

}
