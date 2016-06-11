package com.hawk.bookmarks.test.observer;

public class Tester {
	public static void main(String[] args) {
		Watched watched=new Watched();
		Watcher watcher=new Watcher(watched);
		
		watched.changeData("In C, we create bugs.");
		watched.changeData("In Java, we inherit bugs.");
		watched.changeData("In Java, we inherit bugs.");
		watched.changeData("In Visual Basic, we visualize bugs.");
		System.out.println("======");
		watcher=new Watcher(watched);
		watched.changeData("In Visual Basic, we visualize bugs.");
		watched.changeData("In Java, we inherit bugs.");
	}
}
