package com.hawk.bookmarks.utils.likelynnetool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.common.SqliteHelper;

public class LikeLynneAdder {
	private SqliteHelper sqliteHelper;
	private List<String>sqls;
	
	public LikeLynneAdder(){
		sqliteHelper=SqliteHelper.getInstance(LikeLynneApi.getInstance().getBookmarkPath());
	}
	
	/**
	 * 要根据type判断插入的是书签还是文件夹
	 * @param llynne
	 * @return
	 */
	public int addBookmark(LikeLynne llynne){
		try {
			sqls=new ArrayList<>();
			getInsertSqls(llynne);
//			System.out.println("sqls="+sqls);
			sqliteHelper.executeBatch(sqls);
			return 200;
		} catch (ClassNotFoundException|SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"书签保存失败："+e.getMessage());
		}
		return 119;
	}
	
	/**
	 * 批量sql
	 * @param llynne
	 */
	private void getInsertSqls(LikeLynne llynne){
		StringBuffer sqlBuffer=new StringBuffer();
		sqlBuffer.append("insert or ignore into likelynne(id,title,url,parent,type,dateAdded) values(")
					.append(llynne.getId()).append(",'")
					.append(llynne.getTitle()).append("','")
					.append(llynne.getUrl()).append("',")
					.append(llynne.getParent()).append(",")
					.append(llynne.getType()).append(",")
					.append(System.currentTimeMillis())
					.append(");");
		
		sqls.add(sqlBuffer.toString());
		
		//type=url的likelynne的getchildren=null
		if(null!=llynne.getChildren()&&llynne.getChildren().size()>0){
			int childrenSize=llynne.getChildren().size();
			for(int i=0;i<childrenSize;i++){
				getInsertSqls(llynne.getChildren().get(i));
			}
		}
	}
	
	public static void main(String[] args) {
		LikeLynne lynne=new LikeLynne();
		lynne.setTitle("test22222222");
		lynne.setType(2);
		lynne.setParent(1);
		lynne.setDateAdded(System.currentTimeMillis());
		
//		ResponseResult responseResult=new LikeLynneAdder().addNewBookmarkOrNewFolder(lynne);
//		if(responseResult.getCode()==119){
//			System.out.println("add failed:"+responseResult.getMessage());
//		}
	}
}
