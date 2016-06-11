package com.hawk.bookmarks.utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;


public class WinkUtils {
	public static ClientConfig getClientConfig(){
		ClientConfig config=new ClientConfig();
		SSLContext sc;
		try {
		sc=SSLContext.getInstance("SSL");
		sc.init(null, getTrustManager(), new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		config.setBypassHostnameVerification(true);
		config.connectTimeout(100000);
		config.readTimeout(100000);
		config.followRedirects(false); 
		} catch (NoSuchAlgorithmException e) { 
		e.printStackTrace();
		} catch (KeyManagementException e) { 
		e.printStackTrace();
		}
		return config;
}
	
	public static TrustManager[] getTrustManager(){
		TrustManager[] trustAllCerts=new TrustManager[]{ new X509TrustManager(){
		public void checkClientTrusted(java.security.cert.X509Certificate[] arg0,String arg1){} 
		public void checkServerTrusted(java.security.cert.X509Certificate[] arg0,String arg1){} 
		public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
		return null;
		}
		}
		};
		return trustAllCerts;
		}

	/**
	* 功能：根据传入的url路径插入图片数据
	* @param url 上传图片的url路径
	* @param data 传入到Bmob中的图片二进制数据
	* @return 插入成功返回的json格式字符串
	*/
	public static String setInsertGoodsData(String url,byte[] data){
	String result=null;
	RestClient restClient=new RestClient(WinkUtils.getClientConfig());
	Resource resource=restClient.resource(url);
	resource.header("X-Bmob-Application-Id", "你自己的APPID");
	resource.header("X-Bmob-REST-API-Key","你自己的APPKEY"); 
	resource.header("Content-Type", "image/jpeg");

	ClientResponse response=resource.post(data);
	int code=response.getStatusCode();
	System.out.println("结果码:"+code);
	if(code==201){
	result=response.getEntity(String.class);
	}
	return result; 
	}
}