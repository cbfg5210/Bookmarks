package com.hawk.bookmarks.utils.likelynnetool;

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
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.LogUtil;

public class LikeLynneGetter {
	private static final String TAG="LikeLynneGetter";
	public static void main(String[] args) {
		LikeLynne lynne=new LikeLynneGetter().getLikeLynne();
	}
	
	public LikeLynne getLikeLynne() {
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+LikeLynneApi.getInstance().getBookmarkPath());

			String sql = "select * from likelynne where id>0 order by id ASC";
			ResultSet resultSet = conn.createStatement().executeQuery(sql);

			WeakHashMap<Integer, ArrayList<LikeLynne>> groupNodes= new WeakHashMap<>();
			WeakHashMap<Integer, LikeLynne> parentNodes= new WeakHashMap<>();
			List<Integer> parentSets= new ArrayList<>();
			
			/**
			 * 1、将文件夹与书签分开
			 */
			while (resultSet.next()) {
				LikeLynne llynneItem = new LikeLynne();
				llynneItem.setId(resultSet.getInt("id"));
				llynneItem.setType(resultSet.getInt("type"));
				llynneItem.setParent(resultSet.getInt("parent"));
				llynneItem.setTitle(resultSet.getString("title"));
				llynneItem.setDateAdded(resultSet.getLong("dateAdded"));
				llynneItem.setUrl(resultSet.getString("url"));

				if (llynneItem.getType()==2){//将其信息加入parent行列
					llynneItem.setChildren(new ArrayList<>());
					if(!parentSets.contains(llynneItem.getId())){//在添加前要进行判断，不然会导致重复
						parentSets.add(llynneItem.getId());
						groupNodes.put(llynneItem.getId(), new ArrayList<>());
					}
				}
				if (llynneItem.getParent() != 0 && !parentSets.contains(llynneItem.getParent())) {// 将其parent信息加入parent行列
					parentSets.add(llynneItem.getParent());
					groupNodes.put(llynneItem.getParent(), new ArrayList<>());
				}
				if (parentSets.contains(llynneItem.getId())) {// 将其加入parent行列
					parentNodes.put(llynneItem.getId(), llynneItem);
				}else{
					groupNodes.get(llynneItem.getParent()).add(llynneItem);
				}
			}
			/**
			 * 2、将书签添加到对应文件夹以及将文件夹添加到对应父文件夹(可能是因为引用的原因，使得文件夹添加到父文件夹变得简单)
			 */
			int loopCount = parentSets.size();
			for (int i = 0; i<loopCount; i++) {
				int key = parentSets.get(i);
				if (parentNodes.containsKey(key)) {
					parentNodes.get(key).getChildren().addAll(groupNodes.get(key));
				}
				int parentKey = parentNodes.get(key).getParent();
				if (parentNodes.containsKey(parentKey)) {
					parentNodes.get(parentKey).getChildren().add(parentNodes.get(key));
				}
			}

			LikeLynne llynne = parentNodes.get(parentSets.get(0));
			return llynne;
		} catch (Exception exp) {
			JOptionPane.showMessageDialog(null,"获取数据出错:"+exp.getMessage());
		} finally {
			try {
				if (null != conn) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		return null;
	}
}
