package com.hawk.bookmarks.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.utils.LogUtil;
import com.hawk.bookmarks.utils.common.RowMapper;
import com.hawk.bookmarks.utils.common.SqliteHelper;

public class Test {
public static void main(String[] args) {
	String dbPath=LikeLynneApi.getInstance().getBookmarkPath();
	String sql="select browser from brsconfig where isSupported=1 and openStatus=1 and path is not null;";
try {
	List<String>brs=SqliteHelper.getInstance(dbPath).executeQuery(sql,new RowMapper<String>() {
		@Override
		public String mapRow(ResultSet rs, int index) throws SQLException {
			// TODO Auto-generated method stub
			return rs.getString("browser");
		}
	});
	System.out.println("brs="+brs);
} catch (ClassNotFoundException | SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	
}

}
