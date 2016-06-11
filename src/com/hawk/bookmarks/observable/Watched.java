package com.hawk.bookmarks.observable;

import java.util.List;
import java.util.Observable;

import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.LogUtil;

public class Watched extends Observable {
	private String TAG="Watched";
	private LikeLynne theLynne;
	private LikeLynne newLynneItem;

	public Watched(LikeLynne theLynne) {
		this.theLynne = theLynne;
	}

	public LikeLynne getTheLynne() {
		return theLynne;
	}

	public void addNewLynneItem(LikeLynne newLynneItem) {
		this.newLynneItem = newLynneItem;
		addNewLikeLynne(theLynne, newLynneItem);
		setChanged();
		notifyObservers();
	}
	
	public void updateLynneItem(LikeLynne newLynneItem){
		deleteLynneItem(newLynneItem,false);
		addNewLynneItem(newLynneItem);
	}
	
	public void deleteLynneItem(LikeLynne lynneItem,boolean isNotify){
		deleteLynneItem(theLynne,lynneItem,isNotify);
	}

	public LikeLynne getNewLynneItem() {
		return newLynneItem;
	}

	public void setTheLynne(LikeLynne newLynne) {
		theLynne = newLynne;
		setChanged();
		notifyObservers();
	}

	/**
	 * 插入新数据
	 * 
	 * @param lynne
	 * @param newLikeLynne
	 */
	private void addNewLikeLynne(LikeLynne lynne, LikeLynne newLikeLynne) {
		if (newLikeLynne.getParent() == lynne.getId()) {
			lynne.getChildren().add(newLikeLynne);
			return;
		}
		List<LikeLynne> children = lynne.getChildren();
		if (null != children && children.size() > 0) {
			int size = children.size();
			for (int i = 0; i < size; i++) {
				LikeLynne child = children.get(i);
				if (child.getType() == 2) {
					addNewLikeLynne(child, newLikeLynne);
				}
			}
		}
	}

	private void deleteLynneItem(LikeLynne theLynne,LikeLynne deleteItem,boolean isNotify){
		List<LikeLynne>children=theLynne.getChildren();
		if(null!=children&&children.size()>0){
			int size=children.size();
			for(int i=0;i<size;i++){
				if(children.get(i).getId()==deleteItem.getId()){
					theLynne.getChildren().remove(i);
					if(isNotify){
						setChanged();
						notifyObservers();
					}
					return;
				}
				if(children.get(i).getType()==2){
					deleteLynneItem(theLynne.getChildren().get(i),deleteItem,isNotify);
				}
			}
		}
	}
}
