package com.hawk.bookmarks.api;

import javax.swing.JOptionPane;

import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.LogUtil;
import com.hawk.bookmarks.utils.common.SqliteHelper;
import com.hawk.bookmarks.utils.likelynnetool.LikeLynneAdder;
import com.hawk.bookmarks.utils.likelynnetool.LikeLynneGetter;
import com.hawk.bookmarks.utils.likelynnetool.LikeLynneRemover;

public class LikeLynneApi{
	private static final String TAG="LikeLynneApi";
	private static LikeLynneApi mInstance;
	
	private LikeLynneApi(){}
	
	public static LikeLynneApi getInstance(){
		if(null==mInstance){
			mInstance=new LikeLynneApi();
		}
		return mInstance;
	}
	
	public LikeLynne getLikeLynne() {
		// TODO Auto-generated method stub
		return new LikeLynneGetter().getLikeLynne();
	}

	/**
	 * 
	 * @param llynne
	 * @return 119:not ok,200:ok
	 */
	public int addLikeLynne(LikeLynne llynne) {
		// TODO Auto-generated method stub
		return new LikeLynneAdder().addBookmark(llynne);
	}

	public int removeLikeLynne(LikeLynne llynne) {
		// TODO Auto-generated method stub
		return new LikeLynneRemover().removeBookmark(llynne);
	}

	public int updateLikeLynne(LikeLynne llynne) {
		// TODO Auto-generated method stub
		StringBuffer sqlBuffer=new StringBuffer();
		sqlBuffer.append("update likelynne set title='")
				.append(llynne.getTitle())
				.append("',url='")
				.append(llynne.getUrl())
				.append("',parent=")
				.append(llynne.getParent())
				.append(",dateAdded=")
				.append(llynne.getDateAdded())
				.append(" where id=")
				.append(llynne.getId());
//		LogUtil.i(TAG,"sql="+sqlBuffer.toString());
		try {
			int result=SqliteHelper.getInstance(getBookmarkPath()).executeUpdate(sqlBuffer.toString());
			if(result>0){
				return 200;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"更新书签信息出错:"+e.getMessage());
		}
		return 119;
	}

	public String getBookmarkPath(){
		return "likelynne.sqlite";
	}
}
