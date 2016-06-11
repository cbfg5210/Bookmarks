package com.hawk.bookmarks.utils.chrometool;

import com.hawk.bookmarks.model.ChromeNode;

public class ChromeUtils {
	private int tempInt;
	/**
	 * 重置ChromeNode数据的id和parentid
	 * @param chrnode
	 * @return
	 */
	public ChromeNode resetIdAndParentId(ChromeNode chrnode,int currentMaxId){
		tempInt=currentMaxId;
		ChromeNode updatedChromeNode=setIdAndParentId(chrnode,1);
		return updatedChromeNode;
	}
	
	private ChromeNode setIdAndParentId(ChromeNode chrnode,int parentId){
		tempInt++;
		chrnode.setId(tempInt);
		chrnode.setParent(parentId);
		if(null!=chrnode.getChildren()&&chrnode.getChildren().size()>0){
			int loopCount=chrnode.getChildren().size();
			for(int i=0;i<loopCount;i++){
				chrnode.getChildren().set(i,setIdAndParentId(chrnode.getChildren().get(i),chrnode.getId()));
			}
		}
		return chrnode;
	}
}
