package com.hawk.bookmarks.utils;

import java.lang.reflect.Type;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hawk.bookmarks.utils.common.FileUtils;

public class SharedPrefUtil {
	private static final String SP_NAME = "config";
	private static SharedPrefUtil mInstance;
	private static JsonObject tempData;
	public static final String OBJECTID="objectId";
	public static final String USERNAME="username";
	public static final String PASSWORD="password";
	public static final String DBFILEURL="dbFileUrl";

	public static SharedPrefUtil getInstance() {
		if (null == mInstance) {
			mInstance = new SharedPrefUtil();
		}
		return mInstance;
	}

	public void save(String key, String value) {
		ifNullInit();
		tempData.addProperty(key, value);
		saveTempData();
	}

	public void save(String key, boolean value) {
		ifNullInit();
		tempData.addProperty(key, value);
		saveTempData();
	}

	public void save(String key, int value) {
		ifNullInit();
		tempData.addProperty(key, value);
		saveTempData();
	}

	public <T> void save(String key, List<T> value) {
		ifNullInit();
		tempData.addProperty(key, value.toString());
		saveTempData();
	}
	
	private void saveTempData(){
		int resultCode=FileUtils.writeTxtFile(SP_NAME, tempData.toString());
		if(resultCode!=200){
			JOptionPane.showMessageDialog(null,"保存数据失败");
		}
	}

	public String readString(String key) {
		ifNullInit();
		String str = null;
		try {
			str = tempData.get(key).getAsString();
		} catch (Exception exp) {
		}
		return str;
	}

	public boolean readBool(String key, boolean def) {
		boolean bolean = def;
		try {
			bolean = tempData.get(key).getAsBoolean();
		} catch (Exception exp) {
		}
		return bolean;
	}

	public int readInt(String key) {
		ifNullInit();
		int it = -1;
		it = tempData.get(key).getAsInt();
		return it;
	}

	public JsonArray readArray(String key) {
		ifNullInit();
		JsonArray array = null;
		try {
			String str = tempData.get(key).getAsString();
			array = new JsonParser().parse(str).getAsJsonArray();
		} catch (Exception exp) {
		}
		return array;
	}

	public <T> List<T> readList(String key,Type type) {
		ifNullInit();
		List<T> list =null;
		
		try{
			String str=tempData.get(key).getAsString();
			list=new Gson().fromJson(str,type);
		}catch(Exception exp){
		}
		
		return list;
	}

	public void ifNullInit() {
		if (null == tempData) {
			if (!FileUtils.isFileExist(SP_NAME)) {
				tempData = new JsonObject();
				FileUtils.writeTxtFile(SP_NAME, "{}");
			} else {
				String fileTxt= FileUtils.readTxtFile(SP_NAME);
				if (null==fileTxt) {
					JOptionPane.showMessageDialog(null, "读取数据失败");
					tempData = new JsonObject();
					return;
				}
				tempData = new JsonParser().parse(fileTxt).getAsJsonObject();
			}
		}
	}

	public static void main(String[] args) {
	}
}
