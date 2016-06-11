package com.hawk.bookmarks.utils;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ScreenUtil {
	public static Dimension getScreenSize(){
		Toolkit kit = Toolkit.getDefaultToolkit(); 
		Dimension screenSize = kit.getScreenSize();  
		return screenSize;
	}
	public static int getScreeWidth(){
		Dimension screenSize=ScreenUtil.getScreenSize();
		return screenSize.width;  
	}
	public static int getScreeHeight(){
		Dimension screenSize=ScreenUtil.getScreenSize();
		return screenSize.height;
	}
}
