package com.hawk.bookmarks.utils.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;

import com.hawk.bookmarks.api.ChromeBookmarkApi;

public class FileUtils {
	private static final String TAG="FileUtils";
	
	public static void main(String[] args) {
//		readTxtFile("C:\\Users\\hawk\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks");
		String filePath=ChromeBookmarkApi.getBookmarkPath();
		System.out.println(isFileExist(filePath));
		System.out.println(readTxtFile(filePath));
	}

	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static String readTxtFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			JOptionPane.showMessageDialog(null, "文件不存在");
			return null;
		}
		String fileTxt = null;
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String lineTxt = null;
			StringBuffer fileContent = new StringBuffer();
			while ((lineTxt = bufferedReader.readLine()) != null) {
				fileContent.append(lineTxt);
			}
			fileTxt = fileContent.toString();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "读取文件出错:" + e.getMessage());
		} finally {
			try {
				if (null != bufferedReader) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fileTxt;
	}

	public static int writeTxtFile(String filePath, String content) {
		int resultCode=119;
		OutputStreamWriter osw = null;
		File file;
		try {
			file = new File(filePath);
			osw = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			osw.write(content);
			osw.flush();
			resultCode=200;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"写入文件出错："+e.getMessage());
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultCode;
	}
}
