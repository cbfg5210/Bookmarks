package com.hawk.bookmarks.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.Timer;

public class ProgressDialog extends JDialog implements ActionListener{
	private JLabel lblNewLabel;
	private Timer timer;
	private ImageIcon[]icons;
	private int iconIndex=0;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProgressDialog frame = new ProgressDialog("正在努力下载...");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ProgressDialog(String tip) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setUndecorated(true);
		setSize(300, 300);
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		SpringLayout springLayout=new SpringLayout();
		contentPane.setLayout(springLayout);
		
		initIcons();
		lblNewLabel = new JLabel();
		lblNewLabel.setIcon(icons[0]);
		springLayout.putConstraint(SpringLayout.NORTH,lblNewLabel,30,SpringLayout.NORTH,contentPane);
		springLayout.putConstraint(SpringLayout.WEST,lblNewLabel,70,SpringLayout.WEST,contentPane);
		contentPane.add(lblNewLabel);
		
		JLabel tipLabel = new JLabel(tip);
		springLayout.putConstraint(SpringLayout.NORTH,tipLabel,5,SpringLayout.SOUTH,lblNewLabel);
		springLayout.putConstraint(SpringLayout.WEST,tipLabel,100,SpringLayout.WEST,contentPane);
		contentPane.add(tipLabel);
		
		timer=new Timer(100,this);
		timer.start();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initIcons(){
		icons=new ImageIcon[3];
		icons[0]=new ImageIcon(ProgressDialog.class.getResource("/images/loading_icon.png"));
		icons[1]=new ImageIcon(ProgressDialog.class.getResource("/images/loading_icon1.png"));
		icons[2]=new ImageIcon(ProgressDialog.class.getResource("/images/loading_icon2.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==timer){
			iconIndex++;
			if(iconIndex>2){
				iconIndex=0;
			}
			lblNewLabel.setIcon(icons[iconIndex]);
		}
	}
}
