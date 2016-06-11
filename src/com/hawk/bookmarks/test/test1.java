package com.hawk.bookmarks.test;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class test1 extends JFrame{
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				test1 t2Frame=new test1();
			}
		});
//		test2 t2Frame=new test2();
	}
	
	public test1(){
		initViews();
	}
	
	private void initViews() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new FlowLayout());
		
		JButton jbtn=new JButton("跳转到页面2");
		jbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				new test2(test1.this);
				JOptionPane.showMessageDialog(null,"asd");
//				JOptionPane.showMessageDialog(jbtn,"asd");
//				int result=new TimeDialog().showDialog(test1.this,"dialog test",8);
//				System.out.println("result="+result);
			}
		});
		
		contentPane.add(jbtn);
		
		setContentPane(contentPane);
		this.setVisible(true);
	}
}
