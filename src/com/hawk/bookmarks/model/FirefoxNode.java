package com.hawk.bookmarks.model;

import java.util.List;

public class FirefoxNode {
	private int id;
	private int type;
	private int fk;
	private String url;
	private int parent;
	private int position;
	private String title;
	private int keyword_id;
	private String folder_type;
	private long dateAdded;
	private long lastModified;
	private String guid;
	private List<FirefoxNode>children;
	public int getId() {
		return id;
	}
	public int getType() {
		return type;
	}
	public int getFk() {
		return fk;
	}
	public int getParent() {
		return parent;
	}
	public int getPosition() {
		return position;
	}
	public String getTitle() {
		return title;
	}
	public int getKeyword_id() {
		return keyword_id;
	}
	public String getFolder_type() {
		return folder_type;
	}
	public long getDateAdded() {
		return dateAdded;
	}
	public long getLastModified() {
		return lastModified;
	}
	public String getGuid() {
		return guid;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setFk(int fk) {
		this.fk = fk;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setKeyword_id(int keyword_id) {
		this.keyword_id = keyword_id;
	}
	public void setFolder_type(String folder_type) {
		this.folder_type = folder_type;
	}
	public void setDateAdded(long dateAdded) {
		this.dateAdded = dateAdded;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	public List<FirefoxNode> getChildren() {
		return children;
	}
	public void setChildren(List<FirefoxNode> children) {
		this.children = children;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	/*@Override
	public String toString() {
		return "FirefoxNode [id=" + id + ", type=" + type + ", fk=" + fk + ", parent=" + parent + ", position="
				+ position + ", title=" + title + ", keyword_id=" + keyword_id + ", folder_type=" + folder_type
				+ ", dateAdded=" + dateAdded + ", lastModified=" + lastModified + ", guid=" + guid + "]";
	}*/
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title;
	}
}
