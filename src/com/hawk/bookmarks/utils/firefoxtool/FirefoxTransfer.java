package com.hawk.bookmarks.utils.firefoxtool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

import javax.swing.JOptionPane;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.model.FirefoxNode;

public class FirefoxTransfer {
	public static void main(String[] args) {
//		ResponseResult responseResult=new FirefoxTransfer().transFromLikeLynne();
//		if(responseResult.getCode()!=200){
//			System.out.println("transfer error:"+responseResult.getMessage());
//			return;
//		}
//		FirefoxNode firefoxNode=(FirefoxNode) responseResult.getContent();
//		System.out.println("firefoxNode="+new Gson().toJson(firefoxNode));
	}
	
	public FirefoxNode transFromLikeLynne() {
		FirefoxNode resultNode=null;
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+LikeLynneApi.getInstance().getBookmarkPath());

			String sql = "select * from likelynne where id>0";
			ResultSet resultSet = conn.createStatement().executeQuery(sql);

			WeakHashMap<Integer, ArrayList<FirefoxNode>> groupNodes= new WeakHashMap<>();
			WeakHashMap<Integer, FirefoxNode> parentNodes= new WeakHashMap<>();
			List<Integer> parentSets= new ArrayList<>();

			while (resultSet.next()) {
				FirefoxNode firefoxItem = new FirefoxNode();
				firefoxItem.setId(resultSet.getInt("id"));
				firefoxItem.setType(resultSet.getInt("type"));
				firefoxItem.setParent(resultSet.getInt("parent"));
				firefoxItem.setTitle(resultSet.getString("title"));
				firefoxItem.setDateAdded(resultSet.getLong("dateAdded"));

				if (firefoxItem.getType() == 1) {
					firefoxItem.setUrl(resultSet.getString("url"));
				} else {// 将其信息加入parent行列
					firefoxItem.setChildren(new ArrayList<>());
					parentSets.add(firefoxItem.getId());
					groupNodes.put(firefoxItem.getId(), new ArrayList<>());
				}
				if (firefoxItem.getParent() != 0 && !parentSets.contains(firefoxItem.getParent())) {// 将其parent信息加入parent行列
					parentSets.add(firefoxItem.getParent());
					groupNodes.put(firefoxItem.getParent(), new ArrayList<>());
				}
				if (parentSets.contains(firefoxItem.getId())) {// 将其加入parent行列
					parentNodes.put(firefoxItem.getId(), firefoxItem);
				}else{
					groupNodes.get(firefoxItem.getParent()).add(firefoxItem);
				}
			}

			Collections.sort(parentSets);
//			System.out.println(parentSets);
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

			resultNode= parentNodes.get(parentSets.get(0));
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
		return resultNode;
	}
}
