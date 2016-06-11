package com.hawk.bookmarks.utils.firefoxtool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.hawk.bookmarks.api.FirefoxBookmarkApi;
import com.hawk.bookmarks.model.FirefoxNode;

public class FirefoxAdder {
	/**
	 * @param ffnode
	 * @return
	 */
	public int addBookmark(FirefoxNode ffnode) {
		Connection conn = null;
		int resultCode=119;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + FirefoxBookmarkApi.getBookmarkPath());
			
			insertFirefoxNode(ffnode, conn);
			resultCode=200;
		} catch (Exception exp) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"添加书签失败:"+exp.getMessage());
		} finally {
			try {
				if (null != conn) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		return resultCode;
	}

	private void insertFirefoxNode(FirefoxNode ffnode,Connection conn) throws SQLException {
		if (ffnode.getType() == 1) {
			String countSql = "select count(*) as urlcount from moz_places where url='" + ffnode.getUrl() + "';";
			ResultSet resultSet=conn.createStatement().executeQuery(countSql);
			int urlcount=resultSet.getInt("urlcount");
			
//			System.out.println("urlcount="+urlcount);
			
			if(urlcount!=0)return;
			
			List<String>sqls=getInsertUrlSqls(ffnode);
//			System.out.println("sqls="+sqls);
			
			Statement stmt=conn.createStatement();
			stmt.addBatch(sqls.get(0));
			stmt.addBatch(sqls.get(1));
			stmt.executeBatch();
		} else if (ffnode.getType() == 2) {
			String sql=getInsertFolderSql(ffnode);
			conn.createStatement().executeUpdate(sql);
			
			if(null!=ffnode.getChildren()&&ffnode.getChildren().size()>0){
				int size=ffnode.getChildren().size();
				for(int i=0;i<size;i++){
					insertFirefoxNode(ffnode.getChildren().get(i), conn);
				}
			}
		}
		// String countSql="select count(*) as urlcount where url='"+ffnode.get;
	}
	
	private String getInsertFolderSql(FirefoxNode ffnode){
		StringBuffer sqlBuffer=new StringBuffer();
		sqlBuffer.append("insert into moz_bookmarks(type,parent,title,dateAdded) values(2,")
				.append(ffnode.getParent()).append(",'")
				.append(ffnode.getTitle()).append("',")
				.append(System.currentTimeMillis()).append(");");
		return sqlBuffer.toString();
	}
	
	private List<String> getInsertUrlSqls(FirefoxNode ffnode){
		List<String>sqls=new ArrayList<>();
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("insert into moz_places(url) values('")
				.append(ffnode.getUrl()).append("');");
		sqls.add(sqlBuffer.toString());
		
		sqlBuffer=new StringBuffer();
		sqlBuffer.append("insert into moz_bookmarks(fk,type,parent,title,dateAdded) values(")
				.append("(select last_insert_rowid()),1,")
				.append(ffnode.getParent()).append(",'")
				.append(ffnode.getTitle()).append("',")
				.append(System.currentTimeMillis()).append(");");
		sqls.add(sqlBuffer.toString());
		
		return sqls;
	}

	public static void main(String[] args) {
		FirefoxNode lynne = new FirefoxNode();
		lynne.setTitle("test22222222");
		lynne.setType(2);
		lynne.setParent(1);
		lynne.setDateAdded(System.currentTimeMillis());

		// ResponseResult responseResult=new
		// FirefoxNodeAdder().addNewBookmarkOrNewFolder(lynne);
		// if(responseResult.getCode()==119){
		// System.out.println("add failed:"+responseResult.getMessage());
		// }
	}
}
