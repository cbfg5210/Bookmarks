package com.hawk.bookmarks.test.observer;

import java.util.Observable;

public class Watched extends Observable{
	private String data="";
	public String retrieveData(){
		return data;
	}
	public void changeData(String data){
		if(!this.data.equals(data)){
			this.data=data;
			setChanged();//change=true
		}
		notifyObservers();//if change=true,notify
	}
}
