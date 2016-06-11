package com.hawk.bookmarks.ui.bk26_5_27;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class ConfigBrowserPathDialog extends JDialog{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					new ConfigBrowserPathDialog("asdf");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private String brsDbPath;
	
	public String getConfigResult(){
		return brsDbPath;
	}
	
	public ConfigBrowserPathDialog(JDialog parentUI,String brsName){
		super(parentUI,true);
		createAndShowUI(brsName);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void createAndShowUI(String brsName){
		String title="配置"+brsName+"书签数据库路径";
		setTitle(title);
		setSize(350,150);
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		JLabel label = new JLabel(brsName+"书签数据库路径:");
		sl_contentPane.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, contentPane);
		contentPane.add(label);
		
		JTextField coh_path = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, coh_path, 10, SpringLayout.SOUTH, label);
		sl_contentPane.putConstraint(SpringLayout.WEST, coh_path, 10, SpringLayout.WEST, contentPane);
		contentPane.add(coh_path);
		coh_path.setColumns(20);
		
		JButton coh_open = new JButton("");
		coh_open.setPreferredSize(new Dimension(40,20));
		coh_open.setIcon(new ImageIcon(ConfigBrowserPathDialog.class.getResource("/images/music_more_highlighted.png")));
		coh_open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser=new JFileChooser();
				int option=jFileChooser.showOpenDialog(ConfigBrowserPathDialog.this);
				if(option==JFileChooser.APPROVE_OPTION){
					coh_path.setText(jFileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, coh_open, 0, SpringLayout.NORTH, coh_path);
		sl_contentPane.putConstraint(SpringLayout.WEST, coh_open,10, SpringLayout.EAST, coh_path);
		contentPane.add(coh_open);
		
		JButton coh_ok = new JButton("确定");
		coh_ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				brsDbPath=coh_path.getText().trim();
				if("".equals(brsDbPath)){
					JOptionPane.showMessageDialog(null,"路径不能为空");
					brsDbPath=null;
				}
				dispose();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, coh_ok, 20, SpringLayout.SOUTH, coh_path);
		sl_contentPane.putConstraint(SpringLayout.WEST, coh_ok, 120, SpringLayout.WEST, contentPane);
		contentPane.add(coh_ok);
	}
}
