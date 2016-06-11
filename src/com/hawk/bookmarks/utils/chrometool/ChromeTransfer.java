package com.hawk.bookmarks.utils.chrometool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.model.ChromeNode;

public class ChromeTransfer {
	
	public ChromeNode transFromLikeLynne() {
		ChromeNode resultChrome=null;
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+LikeLynneApi.getInstance().getBookmarkPath());

			String sql = "select * from likelynne where id>0";
			ResultSet resultSet = conn.createStatement().executeQuery(sql);

			WeakHashMap<Integer, ArrayList<ChromeNode>> groupNodes= new WeakHashMap<>();
			WeakHashMap<Integer, ChromeNode> parentNodes= new WeakHashMap<>();
			List<Integer> parentSets= new ArrayList<>();

			while (resultSet.next()) {
				ChromeNode chromeItem = new ChromeNode();
				chromeItem.setId(resultSet.getInt("id"));
				int itype=resultSet.getInt("type");
				String stype=itype==1?"url":"folder";
				chromeItem.setType(stype);
				chromeItem.setParent(resultSet.getInt("parent"));
				chromeItem.setName(resultSet.getString("title"));
				chromeItem.setDate_added(""+resultSet.getLong("dateAdded"));
				chromeItem.setDate_modified(""+System.currentTimeMillis());

				if (itype == 1) {
					chromeItem.setUrl(resultSet.getString("url"));
				} else {// 将其信息加入parent行列
					chromeItem.setChildren(new ArrayList<>());
					parentSets.add(chromeItem.getId());
					groupNodes.put(chromeItem.getId(), new ArrayList<>());
				}
				if (chromeItem.getParent() != 0 && !parentSets.contains(chromeItem.getParent())) {// 将其parent信息加入parent行列
					parentSets.add(chromeItem.getParent());
					groupNodes.put(chromeItem.getParent(), new ArrayList<>());
				}
				if (parentSets.contains(chromeItem.getId())) {// 将其加入parent行列
					parentNodes.put(chromeItem.getId(), chromeItem);
				}else{
					groupNodes.get(chromeItem.getParent()).add(chromeItem);
				}
			}

			Collections.sort(parentSets);
			System.out.println(parentSets);
			int loopCount = parentSets.size() - 1;
			for (int i = loopCount; i >= 0; i--) {
				int key = parentSets.get(i);
				if (parentNodes.containsKey(key)) {
					parentNodes.get(key).getChildren().addAll(groupNodes.get(key));
				}
				int parentKey = parentNodes.get(key).getParent();
				if (parentNodes.containsKey(parentKey)) {
					parentNodes.get(parentKey).getChildren().add(parentNodes.get(key));
				}
			}

			resultChrome= parentNodes.get(parentSets.get(0));
		} catch (Exception exp) {
			JOptionPane.showMessageDialog(null,"数据转换出现异常:"+exp.getMessage());
		} finally {
			try {
				if (null != conn) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultChrome;
	}
	
	public static void main(String[] args) {
//		ResponseResult responseResult=new ChromeTransfer().transFromLikeLynne();
//		if(responseResult.getCode()==200){
//			ChromeNode cnode=(ChromeNode) responseResult.getContent();
//			System.out.println("transfer="+new Gson().toJson(cnode));
//		}else{
//			System.out.println("transfer error:"+responseResult.getMessage());
//		}
	}
	
}
