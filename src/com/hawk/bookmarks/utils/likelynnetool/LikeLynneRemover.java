package com.hawk.bookmarks.utils.likelynnetool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.common.SqliteHelper;

public class LikeLynneRemover {
	/*public static void main(String[] args) {
		LikeLynne llynne = new LikeLynne();
		llynne.setId(19);

		LikeLynneRemover llynneRemover = new LikeLynneRemover();
		ResponseResult responseResult=llynneRemover.removeFolder(llynne);
		if(responseResult.getCode()==200){
			JOptionPane.showMessageDialog(null,"移除文件夹成功");
		}else{
			JOptionPane.showMessageDialog(null,responseResult.getMessage());
		}
	}*/

	private SqliteHelper sqliteHelper;
	private List<Integer> removeIds;
	
	public LikeLynneRemover(){
		sqliteHelper=SqliteHelper.getInstance(LikeLynneApi.getInstance().getBookmarkPath());
	}
	
	/**
	 * 移除书签或文件夹
	 * @param llynne
	 * @return
	 */
	public int removeBookmark(LikeLynne llynne){
		try {
			removeIds = new ArrayList<>();
			gatherRemoveIds(llynne);
			System.out.println("removeIds=" + removeIds);

			String strIds = removeIds.toString().replace("[", "(").replace("]", ")");
			String sqlDeleteLynne = "delete from likelynne where id in" + strIds;
			
			int res=sqliteHelper.executeUpdate(sqlDeleteLynne);
			if(res>0){
				return 200;
			}
		} catch (ClassNotFoundException|SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"移除书签失败："+e.getMessage());
		}
		return 119;
	}

	public void gatherRemoveIds(LikeLynne lynne){
		removeIds.add(lynne.getId());
		if(null!=lynne.getChildren()&&lynne.getChildren().size()>0){
			List<LikeLynne>children=lynne.getChildren();
			int size=children.size();
			for(int i=0;i<size;i++){
				LikeLynne child=children.get(i);
				gatherRemoveIds(child);
			}
		}
	}
}
