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
import com.hawk.bookmarks.utils.SharedPrefUtil;

public class RegisterUI extends JFrame {

	private JPanel contentPane;
	private JTextField loi_account;
	private JTextField loi_password;
	private JLabel label_2;
	private JTextField loi_email;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterUI frame = new RegisterUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RegisterUI() {
		setTitle("注册账号");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(350, 250);
		contentPane = new JPanel();
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		JLabel label = new JLabel("账号：");
		sl_contentPane.putConstraint(SpringLayout.NORTH, label, 30, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, label, 20, SpringLayout.WEST, contentPane);
		contentPane.add(label);

		loi_account = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, loi_account, 0, SpringLayout.NORTH, label);
		sl_contentPane.putConstraint(SpringLayout.WEST, loi_account, 10, SpringLayout.EAST, label);
		loi_account.setColumns(20);
		contentPane.add(loi_account);
		
		JLabel label_1 = new JLabel("密码：");
		sl_contentPane.putConstraint(SpringLayout.NORTH, label_1, 20, SpringLayout.SOUTH, label);
		sl_contentPane.putConstraint(SpringLayout.WEST, label_1, 0, SpringLayout.WEST, label);
		contentPane.add(label_1);

		loi_password = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, loi_password, 0, SpringLayout.NORTH, label_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, loi_password, 10, SpringLayout.EAST, label_1);
		loi_password.setColumns(20);
		contentPane.add(loi_password);
		
		label_2 = new JLabel("邮箱：");
		sl_contentPane.putConstraint(SpringLayout.NORTH, label_2, 20, SpringLayout.SOUTH, label_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, label_2, 0, SpringLayout.WEST, label_1);
		contentPane.add(label_2);
		
		loi_email = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, loi_email, 0, SpringLayout.NORTH, label_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, loi_email, 10, SpringLayout.EAST, label_2);
		loi_email.setColumns(20);
		contentPane.add(loi_email);

		JButton loi_login = new JButton("注册");
		loi_login.setPreferredSize(new Dimension(100,30));
		loi_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String account = loi_account.getText().trim();
				if (CheckUtil.isEmpty(account)) {
					JOptionPane.showMessageDialog(RegisterUI.this, "请输入账号");
					return;
				}
				String password = loi_password.getText().trim();
				if (CheckUtil.isEmpty(password)) {
					JOptionPane.showMessageDialog(RegisterUI.this, "请输入密码");
					return;
				}
				String email = loi_email.getText().trim();
				if (CheckUtil.isEmpty(email)) {
					JOptionPane.showMessageDialog(RegisterUI.this, "请输入邮箱");
					return;
				}
				login(account, password, email);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, loi_login, 62, SpringLayout.SOUTH, loi_password);
		sl_contentPane.putConstraint(SpringLayout.WEST, loi_login, 60, SpringLayout.WEST, loi_email);
		contentPane.add(loi_login);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void login(String account,String password,String email){
		BSONObject bson = new BSONObject();
		bson.put("username",account);
		bson.put("password",password);
		bson.put("email",email);
		MyBmob.insert("UeUser", bson.toString(),new OnBmobListener() {
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				BSONObject resultBson=new BSONObject(result);
				SharedPrefUtil.getInstance().save(SharedPrefUtil.OBJECTID,resultBson.getString("objectId"));
				SharedPrefUtil.getInstance().save(SharedPrefUtil.USERNAME,account);
				SharedPrefUtil.getInstance().save(SharedPrefUtil.PASSWORD,password);
				new MainUI();
				dispose();
			}
			@Override
			public void onError(String msg) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(RegisterUI.this,"注册失败："+msg);
			}
		});
	}
	
}
