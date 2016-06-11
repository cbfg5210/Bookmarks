package com.hawk.bookmarks.bmob.custom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

public class ProgressBar extends JDialog {
	JProgressBar progressbar;

	public ProgressBar(String title) {
		setTitle(title);
		setBounds(100, 100, 400, 80);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		Container contentPanel = getContentPane();

		progressbar = new JProgressBar();
		progressbar.setOrientation(JProgressBar.HORIZONTAL);
		progressbar.setMinimum(0);
		progressbar.setMaximum(100);
		progressbar.setValue(0);
		progressbar.setStringPainted(true);
		progressbar.setPreferredSize(new Dimension(300, 20));
		progressbar.setBorderPainted(true);
		progressbar.setBackground(Color.pink);

		contentPanel.add(progressbar, BorderLayout.CENTER);

		setVisible(true);
	}
	
	public void setProgressValue(int value){
		progressbar.setValue(value);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			Logger.getLogger(ProgressBar.class.getName()).log(Level.FINE, e.getMessage());
			e.printStackTrace();
		}
		new ProgressBar("下载");
	}

}