package com.hawk.bookmarks.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.hawk.bookmarks.bmob.bson.BSONObject;
import com.hawk.bookmarks.bmob.custom.MyBmob;
import com.hawk.bookmarks.bmob.custom.OnBmobListener;
import com.hawk.bookmarks.utils.CheckUtil;

public class LoginUI extends JFrame {

	private JPanel contentPane;
	private JTextField loi_account;
	private JTextField loi_password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginUI frame = new LoginUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginUI() {
		initViews();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initViews(){
		setTitle("登录");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(380,250));
		contentPane = new JPanel();
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JLabel label = new JLabel("账号：");
		sl_contentPane.putConstraint(SpringLayout.NORTH, label, 30, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, label, 30, SpringLayout.WEST, contentPane);
		contentPane.add(label);
		
		loi_account = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, loi_account, 0, SpringLayout.NORTH, label);
		sl_contentPane.putConstraint(SpringLayout.WEST, loi_account,10, SpringLayout.EAST, label);
		loi_account.setColumns(20);
		contentPane.add(loi_account);
		
		JLabel label_1 = new JLabel("密码：");
		sl_contentPane.putConstraint(SpringLayout.NORTH, label_1, 20, SpringLayout.SOUTH, loi_account);
		sl_contentPane.putConstraint(SpringLayout.WEST, label_1, 30, SpringLayout.WEST, contentPane);
		contentPane.add(label_1);
		
		
		loi_password = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, loi_password, 0, SpringLayout.NORTH, label_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, loi_password, 10, SpringLayout.EAST, label_1);
		loi_password.setColumns(20);
		contentPane.add(loi_password);
		
		JButton loi_login = new JButton("登录");
		loi_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String account=loi_account.getText().trim();
				if(CheckUtil.isEmpty(account)){
					JOptionPane.showMessageDialog(LoginUI.this,"请输入账号");
					return;
				}
				String password=loi_password.getText().trim();
				if(CheckUtil.isEmpty(password)){
					JOptionPane.showMessageDialog(LoginUI.this,"请输入密码");
					return;
				}
				login(account, password);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, loi_login, 40, SpringLayout.SOUTH, loi_password);
		sl_contentPane.putConstraint(SpringLayout.WEST, loi_login, 150, SpringLayout.WEST, contentPane);
		contentPane.add(loi_login);
		
		JButton loi_register = new JButton("还没账号，前往注册>>>");
		loi_register.setOpaque(false);
		loi_register.setBorder(null);
		loi_register.setContentAreaFilled(false);
		loi_register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new RegisterUI();
				dispose();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.SOUTH, loi_register, -10, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, loi_register, -35, SpringLayout.EAST, contentPane);
		contentPane.add(loi_register);
	}
	
	private void login(String account,String password){
		String bSql="select * from UeUser where username='"+account+"' and password='"+password+"' limit 1;";
		MyBmob.findBQL(bSql,new OnBmobListener() {
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
//				System.out.println("result="+result);
//				result={"results":[{"createdAt":"2016-05-31 19:37:40","objectId":"02557229c7","password":"123","updatedAt":"2016-05-31 19:37:40","username":"123"}]}
				BSONObject bsonResult=new BSONObject(result);
				if(bsonResult.getBSONArray("results").length>0){
					new MainUI();
					dispose();
				}
			}
			@Override
			public void onError(String msg) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(LoginUI.this,"登录失败："+msg);
			}
		});
	}
}
