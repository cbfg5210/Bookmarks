package com.hawk.bookmarks.adapter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;

import com.hawk.bookmarks.model.BrowserConfig;
import com.hawk.bookmarks.test.TestUi;
import com.hawk.bookmarks.utils.ScreenUtil;

import jdk.nashorn.internal.scripts.JO;

public class ConfigAdapter extends JPanel implements ListCellRenderer<BrowserConfig>{
	private JLabel label;
	private JToggleButton tglbtnNewToggleButton;
	
	public ConfigAdapter() {
		// TODO Auto-generated constructor stub
		this.setMaximumSize(new Dimension(ScreenUtil.getScreeWidth(),40));
		this.setPreferredSize(new Dimension(300, 40));
		this.setOpaque(true);//如果为false的话设置背景色不生效
		this.setLayout(null);
		
		label=new JLabel();
		label.setBounds(10,0,360,30);

		tglbtnNewToggleButton = new JToggleButton();
		tglbtnNewToggleButton.setBounds(100,0,100,37);
		tglbtnNewToggleButton.setIcon(new ImageIcon(TestUi.class.getResource("/images/msp_switch_2x_off.png")));
		
		tglbtnNewToggleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null,"clicked");
				if(tglbtnNewToggleButton.isSelected()){
					tglbtnNewToggleButton.setIcon(new ImageIcon(TestUi.class.getResource("/images/msp_switch_2x_on.png")));
				}else{
					tglbtnNewToggleButton.setIcon(new ImageIcon(TestUi.class.getResource("/images/msp_switch_2x_off.png")));
				}
			}
		});
		
		this.add(label);
		this.add(tglbtnNewToggleButton);
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends BrowserConfig> list, BrowserConfig value, int index,
			boolean isSelected, boolean cellHasFocus) {
		// TODO Auto-generated method stub
		
		label.setText(value.getBrowser());
		
		if(isSelected){
			this.setBackground(Color.lightGray);
		}else{
			this.setBackground(Color.white);
		}
		
		return this;
	}
}
