package com.hawk.bookmarks.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class test2 extends JFrame{
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
			}
		});
//		test2 t2Frame=new test2();
	}
	
	public test2(test1 t1){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new FlowLayout());
		
		JButton jbtn=new JButton("跳转到页面1");
		jbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new test1();
				dispose();
			}
		});
		
		contentPane.add(jbtn);
		
		setContentPane(contentPane);
		this.setVisible(true);
	}
	
}
