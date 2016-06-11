package com.hawk.bookmarks.test;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hawk.bookmarks.model.ChromeBookmark;
import com.hawk.bookmarks.model.ChromeNode;
import com.hawk.bookmarks.model.ResponseResult;
import com.hawk.bookmarks.utils.chrometool.CChromeDeserializer;
import com.hawk.bookmarks.utils.common.FileUtils;

public class TestJpopupMenu extends JFrame implements ActionListener{
	private JPopupMenu jpMenu;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestJpopupMenu frame = new TestJpopupMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initJpMenu(){
		jpMenu=new JPopupMenu();
		
		String json=null;
    	ResponseResult responseResult = FileUtils.readTxtFile("Bookmarks");
    	if(responseResult.getCode()!=200)return;
    	
    	json=(String) responseResult.getContent();
        Gson gson = new GsonBuilder().registerTypeAdapter(ChromeBookmark.class, new CChromeDeserializer()).create();
        ChromeBookmark chromeBookmark = gson.fromJson(json,ChromeBookmark.class);
        ChromeNode chromeNode=chromeBookmark.getRoots().getOther();
		
        List<ChromeNode>children=chromeNode.getChildren();
		if(null!=children&&children.size()!=0){
			int size=children.size();
			for(int i=0;i<size;i++){
				ChromeNode child=children.get(i);
				if(null==child.getChildren()||child.getChildren().size()==0){
					JMenuItem jMenuItem=new JMenuItem(child.getName());
					jMenuItem.setActionCommand(child.getUrl());
					jMenuItem.addActionListener(this);
					jpMenu.add(jMenuItem);
				}else{
					JMenu jMenu=new JMenu(child.getName());
					addMenuItem(jMenu,child);
					jpMenu.add(jMenu);
				}
			}
		}
	}
	
	private void addMenuItem(JMenu jMenu,ChromeNode chromeNode){
		List<ChromeNode>children=chromeNode.getChildren();
		if(null!=children&&children.size()>0){
			int size=children.size();
			for(int i=0;i<size;i++){
				ChromeNode child=children.get(i);
				if(null==child.getChildren()||child.getChildren().size()==0){
					JMenuItem jMenuItem=new JMenuItem(child.getName());
					jMenuItem.setActionCommand(child.getUrl());
					jMenuItem.addActionListener(this);
					jMenu.add(jMenuItem);
				}else{
					JMenu jSubMenu=new JMenu(child.getName());
					addMenuItem(jSubMenu, child);
					jMenu.add(jSubMenu);
				}
			}
		}
	}
	
	/**
	 * Create the frame.
	 */
	public TestJpopupMenu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(500,500));
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JButton lblNewLabel = new JButton("New label");
		lblNewLabel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(null==jpMenu){
					initJpMenu();
				}
				jpMenu.show(lblNewLabel,0,0);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 35, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 132, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblNewLabel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,e.getActionCommand());
	}
}
