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

import com.hawk.bookmarks.api.FirefoxBookmarkApi;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.common.Utils;
import com.hawk.bookmarks.utils.likelynnetool.LikeLynneUtils;

public class FfLikeLynneGetter {
	private static final String TAG="FfLikeLynneGetter";

	private List<Integer> llynneIds;

	public LikeLynne getLikeLynne() {
		LikeLynne resultLynne=null;
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+FirefoxBookmarkApi.getBookmarkPath());

			String sql = "select id from moz_bookmarks where title='如影书签' and type=2 order by dateAdded ASC limit 1;";
			ResultSet resultSet = conn.createStatement().executeQuery(sql);

			llynneIds = new ArrayList<>();
			llynneIds.add(resultSet.getInt("id"));

			gatherLynneIds(conn, llynneIds);

			System.out.println("llynneIds=" + llynneIds);

			String strIds = llynneIds.toString().replace("[", "(").replace("]", ")");
			StringBuffer lynneSql = new StringBuffer();
			lynneSql.append("select * from (")
					.append("select moz_bookmarks.id,moz_bookmarks.type,moz_bookmarks.parent,moz_bookmarks.title,moz_places.url from moz_bookmarks ")
					.append("LEFT OUTER JOIN ").append("moz_places on moz_bookmarks.fk=moz_places.id) ")
					.append("where id in").append(strIds);

			ResultSet lynneResults = conn.createStatement().executeQuery(lynneSql.toString());
			WeakHashMap<Integer, ArrayList<LikeLynne>> groupNodes = new WeakHashMap<>();
			WeakHashMap<Integer, LikeLynne> parentNodes = new WeakHashMap<>();
			List<Integer> parentSets = new ArrayList<>();

			while (lynneResults.next()) {
				LikeLynne lynneItem = new LikeLynne();
				lynneItem.setId(lynneResults.getInt("id"));
				lynneItem.setType(lynneResults.getInt("type"));
				lynneItem.setParent(lynneResults.getInt("parent"));
				lynneItem.setTitle(lynneResults.getString("title"));
				lynneItem.setChildren(new ArrayList<>());

				if (lynneItem.getType() == 1) {
					lynneItem.setUrl(lynneResults.getString("url"));
				} else {
					lynneItem.setUrl(Utils.getUuid());
					if(!parentSets.contains(lynneItem.getId())){
						groupNodes.put(lynneItem.getId(), new ArrayList<>());
						parentSets.add(lynneItem.getId());
					}
				}
				if (lynneItem.getParent() != 0 && !parentSets.contains(lynneItem.getParent())) {
					groupNodes.put(lynneItem.getParent(), new ArrayList<>());
					parentSets.add(lynneItem.getParent());
				}
				if (parentSets.contains(lynneItem.getId())) {
					parentNodes.put(lynneItem.getId(), lynneItem);
				} else {
					groupNodes.get(lynneItem.getParent()).add(lynneItem);
				}
			}

			Collections.sort(parentSets);
			parentSets.remove(0);

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

			LikeLynne theNode = parentNodes.get(parentSets.get(0));
			//重置数据id和parentid,这里可能返回null
			resultLynne=new LikeLynneUtils().resetIdAndParentId(theNode);
		} catch (Exception exp) {
			JOptionPane.showMessageDialog(null,"数据转换出现异常："+exp.getMessage());
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
		return resultLynne;
	}

	private void gatherLynneIds(Connection conn, List<Integer> ids) throws SQLException {
		String strIds = ids.toString().replace("[", "(").replace("]", ")");
		String sql = "select id from moz_bookmarks where parent in" + strIds;
		ResultSet resultSet = conn.createStatement().executeQuery(sql);
		List<Integer> newIds = new ArrayList<>();
		while (resultSet.next()) {
			newIds.add(resultSet.getInt("id"));
		}
		if (newIds.size() > 0) {
			llynneIds.addAll(newIds);
			gatherLynneIds(conn, newIds);
		}
	}
}
