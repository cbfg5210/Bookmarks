package com.hawk.bookmarks.model;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * type=folder:
 * children,date_added,date_modified,id,name,type
 * 
 * type=url:
 * date_added,id,name,type,url
 * 
 * @author hawk
 *
 */
public class ChromeNode {
	@Expose
	private List<ChromeNode>children;//json
	@Expose
	private String date_added;
	@Expose
	private String date_modified;
	@Expose
	private int id;
	@Expose(serialize=false,deserialize=true)
	private int parent;
	@Expose
	private String name;
	@Expose
	private String type;//folder/url
	@Expose
	private String url;
	//额外添加以下数据，方便后续数据操作
//	@Expose(serialize=false)
//	private String idPath;//记录root到该节点的路径
	
	public String getDate_added() {
		return date_added;
	}
	public String getDate_modified() {
		return date_modified;
	}
	public int getId() {
		return id;
	}
	
	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public void setDate_added(String date_added) {
		this.date_added = date_added;
	}
	public void setDate_modified(String date_modified) {
		this.date_modified = date_modified;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<ChromeNode> getChildren() {
		return children;
	}
	public void setChildren(List<ChromeNode> children) {
		this.children = children;
	}
//	public String getIdPath() {
//		return idPath;
//	}
//	public void setIdPath(String idPath) {
//		this.idPath = idPath;
//	}
	/*@Override
	public String toString() {
		return "ChromeNode [children=" + children + ", date_added=" + date_added + ", date_modified=" + date_modified
				+ ", id=" + id + ", name=" + name + ", type=" + type + ", url=" + url + ", idPath=" + idPath + "]";
	}*/
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}
