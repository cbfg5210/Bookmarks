package com.hawk.bookmarks.utils.likelynnetool;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.common.ResultSetExtractor;
import com.hawk.bookmarks.utils.common.SqliteHelper;

public class LikeLynneUtils {
	private SqliteHelper sqliteHelper;
	private int tempInt;
	
	public LikeLynneUtils(){
		sqliteHelper=SqliteHelper.getInstance(LikeLynneApi.getInstance().getBookmarkPath());
	}
	
	public int getMaxId(){
		String lastIdSql="select max(id) as lastid from likelynne";
		try {
			int lastid=sqliteHelper.executeQuery(lastIdSql,new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException {
					// TODO Auto-generated method stub
					return rs.getInt("lastid");
				}
			});
			return lastid;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"查询数据出错:"+e.getMessage());
			return -1;
		}
	}
	
	public LikeLynne resetIdAndParentId(LikeLynne llynne){
		tempInt=getMaxId();
		if(tempInt<0){
			return null;
		}
		LikeLynne updatedLikeLynne=setIdAndParentId(llynne,1);
		
		return updatedLikeLynne;
	}
	
	private LikeLynne setIdAndParentId(LikeLynne llynne,int parentId){
		tempInt++;
		llynne.setId(tempInt);
		llynne.setParent(parentId);
		if(null!=llynne.getChildren()&&llynne.getChildren().size()>0){
			int loopCount=llynne.getChildren().size();
			for(int i=0;i<loopCount;i++){
				llynne.getChildren().set(i,setIdAndParentId(llynne.getChildren().get(i),llynne.getId()));
			}
		}
		return llynne;
	}
}
