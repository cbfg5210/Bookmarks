package com.hawk.bookmarks.api;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.hawk.bookmarks.model.FirefoxNode;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.common.ResultSetExtractor;
import com.hawk.bookmarks.utils.common.SqliteHelper;
import com.hawk.bookmarks.utils.firefoxtool.FfLikeLynneGetter;
import com.hawk.bookmarks.utils.firefoxtool.FirefoxAdder;
import com.hawk.bookmarks.utils.firefoxtool.FirefoxTransfer;
import com.hawk.bookmarks.utils.firefoxtool.FirefoxUtils;

public class FirefoxBookmarkApi implements IBookmarkApi {
	@Override
	public LikeLynne parseToLikeLynne() {
		// TODO Auto-generated method stub
		return new FfLikeLynneGetter().getLikeLynne();
	}

	@Override
	public int insertBookmarks() {
		// TODO Auto-generated method stub
		// 将likelynne书签转成firefox书签
		// 对新的firefox书签进行id和parentid的重置
		FirefoxNode firefoxNode =transFromLikeLynne();
		if(null==firefoxNode){
			return 119;
		}
//		System.out.println("firefoxNode="+new Gson().toJson(firefoxNode));
//		if(true)return null;
		firefoxNode =new FirefoxUtils().resetIdAndParentId(firefoxNode);
		if(null==firefoxNode){
			return 119;
		}
//		System.out.println("resetIdAndParentId后,firefoxNode="+new Gson().toJson(firefoxNode));
//		if(true)return null;
		// 添加到书签工具栏
		firefoxNode.setParent(3);
		// 将新书签添加到firefox书签里
		int resultCode= new FirefoxAdder().addBookmark(firefoxNode);

		return resultCode;
	}

	@Override
	public FirefoxNode transFromLikeLynne() {
		// TODO Auto-generated method stub
		return new FirefoxTransfer().transFromLikeLynne();
	}
	
	public static String getBookmarkPath() {
		// TODO Auto-generated method stub
		String sqlGetChromePath="select path from brsconfig where browser='firefox浏览器' limit 1;";
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
			JOptionPane.showMessageDialog(null,"获取firefox浏览器书签数据库路径出错");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		new FirefoxBookmarkApi().insertBookmarks();
	}
}
