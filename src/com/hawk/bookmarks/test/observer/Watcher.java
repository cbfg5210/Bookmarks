package com.hawk.bookmarks.test.observer;

import java.util.Observable;
import java.util.Observer;

public class Watcher implements Observer{
	public Watcher(Watched watched){
		watched.addObserver(this);
	}
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		System.out.println("Data has been changed to:"+((Watched)o).retrieveData());
	}
}
