package com.hawk.bookmarks.ui;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class AboutDialog extends JDialog {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public AboutDialog(JFrame parentUI) {
		super(parentUI,true);
		createUIAndShow(parentUI);
	}
	
	private void createUIAndShow(JFrame parentUI) {
		setTitle("帮助");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(parentUI.getX()-65,parentUI.getY()+80,300,200);
		contentPane = new JPanel();
		SpringLayout springLayout=new SpringLayout();
		contentPane.setLayout(springLayout);
		setContentPane(contentPane);
		
		JLabel label = new JLabel("版本：1.0");
		springLayout.putConstraint(SpringLayout.NORTH, label, 30, SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.WEST, label, 53, SpringLayout.WEST, contentPane);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("作者：沉璧浮光");
		springLayout.putConstraint(SpringLayout.NORTH, label_1, 17, SpringLayout.SOUTH, label);
		springLayout.putConstraint(SpringLayout.WEST, label_1, 0, SpringLayout.WEST, label);
		contentPane.add(label_1);
		
		JLabel lblqqcom = new JLabel("邮箱：120276969@qq.com");
		springLayout.putConstraint(SpringLayout.NORTH, lblqqcom, 22, SpringLayout.SOUTH, label_1);
		springLayout.putConstraint(SpringLayout.WEST, lblqqcom, 0, SpringLayout.WEST, label);
		contentPane.add(lblqqcom);
		
		setVisible(true);
	}
}
