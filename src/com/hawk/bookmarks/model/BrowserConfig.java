package com.hawk.bookmarks.model;

import com.google.gson.annotations.Expose;

public class BrowserConfig {
private String browser;
private String path;
private int isSupported;//0:not support;1:support
private String fileName;
private int openStatus;

public String getBrowser() {
	return browser;
}

public void setBrowser(String browser) {
	this.browser = browser;
}

public String getPath() {
	return path;
}

public void setPath(String path) {
	this.path = path;
}

public int isSupported() {
	return isSupported;
}

public void setSupported(int isSupported) {
	this.isSupported = isSupported;
}

public String getFileName() {
	return fileName;
}

public void setFileName(String fileName) {
	this.fileName = fileName;
}

public int getIsSupported() {
	return isSupported;
}

public int getOpenStatus() {
	return openStatus;
}

public void setIsSupported(int isSupported) {
	this.isSupported = isSupported;
}

public void setOpenStatus(int openStatus) {
	this.openStatus = openStatus;
}

@Override
public String toString() {
	return "BrowserConfig [browser=" + browser + ", path=" + path + ", isSupported=" + isSupported + ", fileName="
			+ fileName + ", openStatus=" + openStatus + "]";
}

}
