package com.hawk.bookmarks.ui;


import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JWindow;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.bmob.custom.MyBmob;
import com.hawk.bookmarks.utils.CheckUtil;
import com.hawk.bookmarks.utils.SharedPrefUtil;
import com.hawk.bookmarks.utils.common.ResultSetExtractor;
import com.hawk.bookmarks.utils.common.SqliteHelper;

public class SplashUI extends JWindow{
public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
		public void run() {
			try {
				new SplashUI();
			} catch (Exception e) {
				e.printStackTrace();	
			}
		}
	});
}

public SplashUI() {
	//bmob初始化
	MyBmob.initBmob("f65486c1b37997b65ded06aa5edd127d",
			"772c160fca271ed63de7320ecf1f5116");
	
	int repResult=checkTableNullInit();
	if(repResult==1){
		String objectId=SharedPrefUtil.getInstance().readString("objectId");
		if(CheckUtil.isEmpty(objectId)){
			new LoginUI();
		}else{
			new MainUI();
		}
		dispose();
	}	
}

	/**
	 * 检查数据库表是否存在，如果不存在则创建
	 */
	private int checkTableNullInit(){
		int repResult=1;
		SqliteHelper sqliteHelper=SqliteHelper.getInstance(LikeLynneApi.getInstance().getBookmarkPath());
		
		ResultSetExtractor<Integer>resultSetExtractor=new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				return rs.getInt("result");
			}
		};
		try {
			String sqlIfBmarkTbExist="select count(*) as result from sqlite_master where type='table' and name='likelynne';";
			int result=sqliteHelper.executeQuery(sqlIfBmarkTbExist,resultSetExtractor);
			if(result==0){
				createBmarkTable(sqliteHelper);
			}
			String sqlIfBrsConfsTbExist="select count(*) as result from sqlite_master where type='table' and name='brsconfig'";
			result=sqliteHelper.executeQuery(sqlIfBrsConfsTbExist,resultSetExtractor);
			if(result==0){
				createBrsConfsTable(sqliteHelper);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(SplashUI.this,"初始化数据出现异常");
			repResult=-1;
		}
		return repResult;
	}
	
	/**
	 * 创建书签表并初始化数据
	 * @param sqliteHelper
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void createBmarkTable(SqliteHelper sqliteHelper) throws ClassNotFoundException, SQLException{
		String sqlCreateUETable="create table likelynne(id integer primary key autoincrement,type integer default 1,title longvarchar,url longvarchar,parent integer default 0,dateAdded integer);";
		String sqlInsertUEFolder="insert into likelynne(id,type,title,dateAdded) values(1,2,'如影书签',"+System.currentTimeMillis()+");";
		String sqlInsertBlogBmark="insert into likelynne(type,title,url,parent,dateAdded) values(1,'沉璧浮光的博客','http://cbfg5210.github.io/',1,"+System.currentTimeMillis()+");";
		List<String>sqls=new ArrayList<>();
		sqls.add(sqlCreateUETable);
		sqls.add(sqlInsertUEFolder);
		sqls.add(sqlInsertBlogBmark);
		sqliteHelper.executeBatch(sqls);
	}
	
	/**
	 * 创建浏览器配置表并初始化数据
	 * @param sqliteHelper
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void createBrsConfsTable(SqliteHelper sqliteHelper) throws ClassNotFoundException, SQLException{
		String sqlCreateBrsConfTable="create table brsconfig(id integer primary key autoincrement,browser varchar(32),path varchar(256),isSupported smallint default 0,fileName varchar(32),openStatus smallint default 0);";
		List<String>sqls=new ArrayList<>();
		sqls.add(sqlCreateBrsConfTable);
		sqls.addAll(getSqlInsertConfs());
		sqliteHelper.executeBatch(sqls);
	}
	
	private List<String>getSqlInsertConfs(){
		List<String>sqlInsertConfs=new ArrayList<>();
		sqlInsertConfs.add(getInsertSql("chrome浏览器",1,"Bookmarks"));
		sqlInsertConfs.add(getInsertSql("firefox浏览器",1,"places.sqlite"));
		sqlInsertConfs.add(getInsertSql("edge浏览器",0,""));
		sqlInsertConfs.add(getInsertSql("ie浏览器",0,""));
		sqlInsertConfs.add(getInsertSql("opera浏览器",0,""));
		sqlInsertConfs.add(getInsertSql("360浏览器",0,""));
		sqlInsertConfs.add(getInsertSql("百度浏览器",0,""));
		sqlInsertConfs.add(getInsertSql("qq浏览器",0,""));
		sqlInsertConfs.add(getInsertSql("猎豹浏览器",0,""));
		return sqlInsertConfs;
	}
	
	private String getInsertSql(String browser,int isSupported,String fileName){
		String sqlInsertConf="insert into brsconfig(browser,isSupported,fileName,openStatus) values('"+browser+"',"+isSupported+",'"+fileName+"',0);";
		return sqlInsertConf;
	}
}
