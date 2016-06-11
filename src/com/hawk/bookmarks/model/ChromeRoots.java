package com.hawk.bookmarks.model;

import com.google.gson.annotations.Expose;

public class ChromeRoots {
	@Expose
	private ChromeNode bookmark_bar;
	@Expose
	private ChromeNode other;
	@Expose
	private ChromeNode synced;
	public ChromeNode getBookmark_bar() {
		return bookmark_bar;
	}
	public ChromeNode getOther() {
		return other;
	}
	public ChromeNode getSynced() {
		return synced;
	}
	public void setBookmark_bar(ChromeNode bookmark_bar) {
		this.bookmark_bar = bookmark_bar;
	}
	public void setOther(ChromeNode other) {
		this.other = other;
	}
	public void setSynced(ChromeNode synced) {
		this.synced = synced;
	}
	@Override
	public String toString() {
		return "ChromeRoots [bookmark_bar=" + bookmark_bar + ", other=" + other + ", synced=" + synced + "]";
	}
	
}
