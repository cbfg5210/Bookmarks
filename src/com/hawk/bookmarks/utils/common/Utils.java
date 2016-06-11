package com.hawk.bookmarks.utils.common;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.hawk.bookmarks.model.LikeLynne;

public class Utils {

	/**
	 * 插入新数据
	 * @param lynne
	 * @param newLikeLynne
	 */
	public static void addNewLikeLynne(LikeLynne lynne, LikeLynne newLikeLynne) {
		if (newLikeLynne.getParent() == lynne.getId()) {
			lynne.getChildren().add(newLikeLynne);
		} else {
			List<LikeLynne> children = lynne.getChildren();
			if (null != children && children.size() > 0) {
				int size = children.size();
				for (int i = 0; i < size; i++) {
					LikeLynne child = children.get(i);
					if (child.getType() == 2) {
						addNewLikeLynne(child, newLikeLynne);
					}
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static String getUuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 从剪切板获得文字
	 */
	public static String getSysClipboardText() {
		String ret = "";
		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
		// 获取剪切板中的内容
		Transferable clipTf = sysClip.getContents(null);

		if (clipTf != null) {
			// 检查内容是否是文本类型
			if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	/**
	 * 调用浏览器打开书签链接
	 * 
	 * @param llynne
	 */
	public static void openUrl(String url) {
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI(url));
			} else {
				JOptionPane.showMessageDialog(null, "无法打开浏览器");
			}
		} catch (IOException | URISyntaxException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "无法打开该链接");
		}
	}

	// !!!这个方法不能判断url格式是否正确，isValidUrl("sd")=true
//	public static boolean isValidUrl(String url) {
//		try {
//			URI uri = new URI(url);
//			if (null == uri) {
//				return false;
//			}
//			return true;
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			return false;
//		}
//	}

	/**
	 * 功能：检测当前URL是否可连接或是否有效, 描述：最多连接网络 5 次, 如果 5 次都不成功，视为该地址不可用
	 * 
	 * @param urlStr
	 *            指定URL网络地址
	 * @return URL
	 */
	public synchronized URL isValidUrl(String urlStr) {
		if (urlStr == null || urlStr.length() <= 0) {
			return null;
		}
		URL url = null;
		HttpURLConnection con;
		int state = -1;
		int counts = 0;
		while (counts < 5) {
			try {
				url = new URL(urlStr);
				con = (HttpURLConnection) url.openConnection();
				state = con.getResponseCode();
				System.out.println(counts + "= " + state);
				if (state == 200) {
					System.out.println("URL可用！");
				}
				break;
			} catch (Exception ex) {
				counts++;
				System.out.println("URL不可用，连接第 " + counts + " 次");
				urlStr = null;
				break;
			}
		}
		return url;
	}
	
	/**
	 * 这里只需大概判断url格式是否正确
	 * isUrlFormat("http://aa.a")=true;
	 * @param url
	 * @return
	 */
	public static boolean isUrlFormat(String url){
		String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}
	
	public static void main(String[] args) {
		JOptionPane.showMessageDialog(null, "" + isUrlFormat("http://aa.a"));
	}
}

