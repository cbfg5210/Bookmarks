package com.hawk.bookmarks.ui.bk26_5_27;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import com.google.gson.reflect.TypeToken;
import com.hawk.bookmarks.model.BrowserConfig;
import com.hawk.bookmarks.utils.SharedPrefUtil;

public class ConfigBrowserPath{

	private JDialog dialog=null;
	private JPanel contentPane;
	private JTextField coh_path;
	private JButton coh_ok;
	private String result;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					new ConfigBrowserPath();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public String showDialog(JFrame father,String brs){
		ifNullInit(father, brs);
		dialog.setVisible(true);
		
		return result;
	}
	
	private void ifNullInit(JFrame father,String brs){
		if(null==dialog){
			dialog=new JDialog(father,true);
			String title="配置"+brs+"书签数据库路径";
			dialog.setTitle(title);
			dialog.setBounds(100, 100, 450, 300);
			contentPane = new JPanel();
			dialog.setContentPane(contentPane);
			SpringLayout sl_contentPane = new SpringLayout();
			contentPane.setLayout(sl_contentPane);
			
			coh_path = new JTextField();
			sl_contentPane.putConstraint(SpringLayout.NORTH, coh_path, 93, SpringLayout.NORTH, contentPane);
			sl_contentPane.putConstraint(SpringLayout.WEST, coh_path, 33, SpringLayout.WEST, contentPane);
			contentPane.add(coh_path);
			coh_path.setColumns(10);
			
			JButton coh_open = new JButton("");
			sl_contentPane.putConstraint(SpringLayout.NORTH, coh_open, 93, SpringLayout.NORTH, contentPane);
			sl_contentPane.putConstraint(SpringLayout.WEST, coh_open, 359, SpringLayout.WEST, contentPane);
			sl_contentPane.putConstraint(SpringLayout.SOUTH, coh_open, -137, SpringLayout.SOUTH, contentPane);
			sl_contentPane.putConstraint(SpringLayout.EAST, coh_open, -10, SpringLayout.EAST, contentPane);
			sl_contentPane.putConstraint(SpringLayout.EAST, coh_path, -6, SpringLayout.WEST, coh_open);
			coh_open.setIcon(new ImageIcon(ConfigBrowserPath.class.getResource("/images/music_more_highlighted.png")));
			coh_open.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser jFileChooser=new JFileChooser();
					int option=jFileChooser.showOpenDialog(null);
					if(option==JFileChooser.APPROVE_OPTION){
						coh_path.setText(jFileChooser.getSelectedFile().getAbsolutePath());
					}
				}
			});
			contentPane.add(coh_open);
			
			JLabel label = new JLabel(brs+"书签数据库路径:");
			sl_contentPane.putConstraint(SpringLayout.NORTH, label, 65, SpringLayout.NORTH, contentPane);
			sl_contentPane.putConstraint(SpringLayout.WEST, label, 33, SpringLayout.WEST, contentPane);
			contentPane.add(label);
			
			coh_ok = new JButton("确定");
			coh_ok.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					result=coh_path.getText().trim();
					if(result.equals("")){
						result=null;
					}
					ConfigBrowserPath.this.dialog.dispose();
				}
			});
			sl_contentPane.putConstraint(SpringLayout.NORTH, coh_ok, 52, SpringLayout.SOUTH, coh_path);
			sl_contentPane.putConstraint(SpringLayout.WEST, coh_ok, 166, SpringLayout.WEST, contentPane);
			contentPane.add(coh_ok);
		}
	}
}
