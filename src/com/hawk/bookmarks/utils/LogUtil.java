package com.hawk.bookmarks.utils;

public class LogUtil {
	private static boolean isDebug = true;

	public static void setDebug(boolean isDbug){
		isDebug=isDbug;
	}
	
	public static void i(String TAG, String msg) {
		if (isDebug) {
			System.out.println("["+TAG + "]:" + msg);
		}
	}
}
