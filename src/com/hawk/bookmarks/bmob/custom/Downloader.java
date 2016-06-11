package com.hawk.bookmarks.bmob.custom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Downloader {
public static void main(String[] args) {
//	downLoad("https://publicobject.com/helloworld.txt","","helloworld.txt");
//	System.out.println(System.getProperty("user.dir"));//user.dir指定了当前的路径
	
//	String dbPath=System.getProperty("user.dir");
//	downLoad("https://publicobject.com/helloworld.txt",dbPath,"helloworld.txt");
}


public static boolean downLoad(String fileUrl,String fileDir, String fileName){
	Request request = new Request.Builder()
	        .url(fileUrl)
	        .build();

	OkHttpClient client = new OkHttpClient.Builder()
	        .addNetworkInterceptor(new Interceptor() {
	          @Override public Response intercept(Chain chain) throws IOException {
	            Response originalResponse = chain.proceed(chain.request());
	            return originalResponse.newBuilder()
	                .build();
	          }
	        })
	        .build();
	
	Response response;
	try {
		response = client.newCall(request).execute();
		if(response.isSuccessful()){
//			parseFile(response,"D:\\","okhttp.txt");
			return parseFile(response,fileDir,fileName);
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
}

/**
 * 从响应流里面读取文件数据
 *
 * @param response  响应
 * @param fileDir   文件目录
 * @param fileName  文件名
 * @param tag       请求标签
 * @param mCallBack 回调
 */
private static boolean parseFile(Response response,String fileDir, String fileName) {
	boolean result=false;
    if (response.isSuccessful()) {
        // 读取文件
        InputStream is = null;
        byte[] buffer = new byte[1024];
        int len;
//        long currentTotalLen = 0L;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            File file = new File(fileDir, fileName);
            if (file.exists()) // 如果文件存在 则删除
                file.delete();
            fos = new FileOutputStream(file);
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
//                currentTotalLen += len;
            }
            fos.flush();
            result=true;
            System.out.println("success");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("failed");
            result=false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    } else {
    	System.out.println("failed");
    	result=false;
    }
    return result;
}

}