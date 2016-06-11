package com.hawk.bookmarks.api;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hawk.bookmarks.model.ChromeBookmark;
import com.hawk.bookmarks.model.ChromeNode;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.chrometool.CChromeDeserializer;
import com.hawk.bookmarks.utils.chrometool.ChromeDeserializer;
import com.hawk.bookmarks.utils.chrometool.ChromeTransfer;
import com.hawk.bookmarks.utils.chrometool.ChromeUtils;
import com.hawk.bookmarks.utils.common.FileUtils;
import com.hawk.bookmarks.utils.common.ResultSetExtractor;
import com.hawk.bookmarks.utils.common.SqliteHelper;

public class ChromeBookmarkApi implements IBookmarkApi {
	@Override
	public LikeLynne parseToLikeLynne() {
		// TODO Auto-generated method stub

		String bookmarkTxt =FileUtils.readTxtFile(getBookmarkPath());
		if(null==bookmarkTxt){
			return null;
		}

		if (bookmarkTxt.indexOf("如影书签") == -1) {
			return null;
		}

		// "\"bookmark_bar\":" "\"other\":"
		// "\"other\":" "\"synced\":"

		int startIndx, endIndx;

		String keyword = "\"bookmark_bar\":";
		startIndx = bookmarkTxt.indexOf(keyword) + keyword.length();

		keyword = "\"other\":";
		endIndx = bookmarkTxt.indexOf(keyword, startIndx);
		String root_folder = bookmarkTxt.substring(startIndx, endIndx);

		startIndx = root_folder.lastIndexOf("}") + 1;
		root_folder = root_folder.substring(0, startIndx);

		if (root_folder.indexOf("如影书签") == -1) {
			keyword = "\"synced\":";

			startIndx = endIndx;
			endIndx = bookmarkTxt.indexOf(keyword, startIndx);
			root_folder = bookmarkTxt.substring(startIndx, endIndx);

			startIndx = root_folder.lastIndexOf("}") + 1;
			root_folder = root_folder.substring(0, startIndx);
		}

		Gson gson__bookmark_bar = new GsonBuilder().registerTypeAdapter(LikeLynne.class, new ChromeDeserializer())
				.create();
		LikeLynne llynne = gson__bookmark_bar.fromJson(root_folder, LikeLynne.class);

		return llynne;
	}

	@Override
	public int insertBookmarks() {
		// TODO Auto-generated method stub
		String bookmarkTxt =FileUtils.readTxtFile(getBookmarkPath());
		if(null==bookmarkTxt){
			return 119;
		}

		Gson gson = new GsonBuilder().registerTypeAdapter(ChromeBookmark.class, new CChromeDeserializer()).create();
		// 获取chrome全部书签
		ChromeBookmark chromeBookmark = gson.fromJson(bookmarkTxt, ChromeBookmark.class);
//		System.out.println("chromeBookmark="+new Gson().toJson(chromeBookmark));
		
		// 对新的chrome书签进行id和parentid的重置
		ChromeNode lynneNode =transFromLikeLynne();
		if(null==lynneNode){
			return 119;
		}
//		System.out.println("lynneNode="+new Gson().toJson(lynneNode));
//		if(true)return null;
		int currentMaxId = chromeBookmark.getCurrentMaxId();
//		System.out.println("currentMaxId="+currentMaxId);
		lynneNode=new ChromeUtils().resetIdAndParentId(lynneNode, currentMaxId);
//		System.out.println("lynneNode="+new Gson().toJson(lynneNode));
//		if(true)return null;
		// 将新书签添加到chrome书签栏里
		chromeBookmark.getRoots().getBookmark_bar().getChildren().add(lynneNode);
		//保存书签数据
		int res = FileUtils.writeTxtFile(getBookmarkPath(), new Gson().toJson(chromeBookmark));

		return res;
	}

	@Override
	public ChromeNode transFromLikeLynne() {
		// TODO Auto-generated method stub
		return new ChromeTransfer().transFromLikeLynne();
	}
	
	public static String getBookmarkPath() {
		// TODO Auto-generated method stub
		String sqlGetChromePath="select path from brsconfig where browser='chrome浏览器' limit 1;";
		try {
			String path=SqliteHelper.getInstance(LikeLynneApi.getInstance().getBookmarkPath()).executeQuery(sqlGetChromePath,new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException {
					// TODO Auto-generated method stub
					return rs.getString("path");
				}
			});
			return path;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"获取chrome浏览器书签数据库路径出错");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
//		ResponseResult responseResult =new ChromeBookmarkApi().insertBookmarks();
		
	}
}
