package com.hawk.bookmarks.model;

import java.io.File;

public class User {
private String username;
private String password;
private String email;
private File bookmarkFile;
private int saveTime;

public String getUsername() {
	return username;
}
public String getPassword() {
	return password;
}
public String getEmail() {
	return email;
}
public void setUsername(String username) {
	this.username = username;
}
public void setPassword(String password) {
	this.password = password;
}
public void setEmail(String email) {
	this.email = email;
}
public File getBookmarkFile() {
	return bookmarkFile;
}
public int getSaveTime() {
	return saveTime;
}
public void setBookmarkFile(File bookmarkFile) {
	this.bookmarkFile = bookmarkFile;
}
public void setSaveTime(int saveTime) {
	this.saveTime = saveTime;
}

}
