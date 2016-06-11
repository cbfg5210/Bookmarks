package com.hawk.bookmarks.utils.firefoxtool;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.hawk.bookmarks.api.FirefoxBookmarkApi;
import com.hawk.bookmarks.model.FirefoxNode;
import com.hawk.bookmarks.utils.common.ResultSetExtractor;
import com.hawk.bookmarks.utils.common.SqliteHelper;

public class FirefoxUtils {
	private SqliteHelper sqliteHelper;
	private int tempInt;

	public FirefoxUtils() {
		sqliteHelper = SqliteHelper.getInstance(FirefoxBookmarkApi.getBookmarkPath());
	}

	public int getMaxId() {
		String lastIdSql = "select max(id) as lastid from moz_bookmarks";
		try {
			int lastid = sqliteHelper.executeQuery(lastIdSql, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException {
					// TODO Auto-generated method stub
					return rs.getInt("lastid");
				}
			});
			return lastid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"查询数据出现异常："+e.getMessage());
			return -1;
		}
	}

	/**
	 * 重置FirefoxNode数据的id和parentid
	 * 
	 * @param ffnode
	 * @return
	 */
	public FirefoxNode resetIdAndParentId(FirefoxNode ffnode) {
		tempInt =getMaxId();
		if(tempInt<0){
			return null;
		}
		FirefoxNode updatedFirefoxNode = setIdAndParentId(ffnode, 1);

		return updatedFirefoxNode;
	}

	private FirefoxNode setIdAndParentId(FirefoxNode ffnode, int parentId) {
		tempInt++;
		ffnode.setId(tempInt);
		ffnode.setParent(parentId);
		if (null != ffnode.getChildren() && ffnode.getChildren().size() > 0) {
			int loopCount = ffnode.getChildren().size();
			for (int i = 0; i < loopCount; i++) {
				ffnode.getChildren().set(i, setIdAndParentId(ffnode.getChildren().get(i), ffnode.getId()));
			}
		}
		return ffnode;
	}

	public static void main(String[] args) {
//		// 将likelynne书签转成firefox书签
//		ResponseResult responseResult =new FirefoxBookmarkApi().transFromLikeLynne();
//		if (responseResult.getCode() != 200){
//			System.out.println("error:"+responseResult.getMessage());
//			return;
//		}
//		// 对新的firefox书签进行id和parentid的重置
//		FirefoxNode firefoxNode = (FirefoxNode) responseResult.getContent();
//		// System.out.println("firefoxNode="+new Gson().toJson(firefoxNode));
//		// if(true)return null;
//		responseResult = new FirefoxUtils().resetIdAndParentId(firefoxNode);
//		if (responseResult.getCode() != 200){
//			System.out.println("error:"+responseResult.getMessage());
//			return;
//		}
//		firefoxNode = (FirefoxNode) responseResult.getContent();
//		System.out.println("resetIdAndParentId后,firefoxNode=" + new Gson().toJson(firefoxNode));
	}
}
