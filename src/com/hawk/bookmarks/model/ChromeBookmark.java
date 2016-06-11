package com.hawk.bookmarks.model;

import com.google.gson.annotations.Expose;

/**
 * jackson
 * http://www.blogjava.net/wangxinsh55/archive/2012/09/06/387180.html
 * @author hawk
 *
 */
public class ChromeBookmark {
	@Expose
	private String checksum;
	@Expose
	private ChromeRoots roots;
	@Expose
	private int version;
	//额外添加以下数据，方便后续数据操作
	@Expose(serialize=false)
	private int currentMaxId;//当前最大的id
	
	public String getChecksum() {
		return checksum;
	}
	public ChromeRoots getRoots() {
		return roots;
	}
	public int getVersion() {
		return version;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	public void setRoots(ChromeRoots roots) {
		this.roots = roots;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getCurrentMaxId() {
		return currentMaxId;
	}
	public void setCurrentMaxId(int currentMaxId) {
		this.currentMaxId = currentMaxId;
	}
	@Override
	public String toString() {
		return "ChromeBookmark [checksum=" + checksum + ", roots=" + roots + ", version=" + version + ", currentMaxId="
				+ currentMaxId + "]";
	}
}
