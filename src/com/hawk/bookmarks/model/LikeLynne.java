package com.hawk.bookmarks.model;

import java.util.List;

/**
 * 如影书签的model类,与数据库对应
 * @author hawk
 *
 */
public class LikeLynne {
	private int id;
	private int type;//folder/url
	private String title;
	private String url;
	private int parent;
	private long dateAdded;
	private List<LikeLynne>children;
	
	public int getId() {
		return id;
	}
	public int getType() {
		return type;
	}
	public String getTitle() {
		return title;
	}
	public String getUrl() {
		return url;
	}
	public int getParent() {
		return parent;
	}
	public long getDateAdded() {
		return dateAdded;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	public void setDateAdded(long dateAdded) {
		this.dateAdded = dateAdded;
	}
	public List<LikeLynne> getChildren() {
		return children;
	}
	public void setChildren(List<LikeLynne> children) {
		this.children = children;
	}
	
	public String getLynneString() {
		return "LikeLynne [id=" + id + ", type=" + type + ", title=" + title + ", url=" + url + ", parent=" + parent
				+ ", dateAdded=" + dateAdded + ", children=" + children + "]";
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title;
	}
	
	
}
