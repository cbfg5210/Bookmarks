package com.hawk.bookmarks.ui.bk26_5_27;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.observable.Watched;
import com.hawk.bookmarks.ui.AddNewFolderDialog;
import com.hawk.bookmarks.ui.MainUI;
import com.hawk.bookmarks.utils.common.RowMapper;
import com.hawk.bookmarks.utils.common.Utils;
import com.hawk.bookmarks.utils.likelynnetool.LikeLynneUtils;

public class AddBookmarkUI2 extends JFrame implements Observer,ActionListener {
	private MainUI parentUI;
	private JPanel contentPane;
	private JTextField fli_nameTxt;
	private JTextField fli_urlTxt;
	private JComboBox<LikeLynne> fli_folders;
	private final String CMD_SUBMIT="cmd_submit";
	private final String CMD_NEWFOLDER="cmd_newfolder";

	private Watched watchedLynne;
	
	/**
	 * Create the frame.
	 */
	public AddBookmarkUI2(MainUI parentUI,Watched watchedLynne) {
		this.parentUI=parentUI;
		this.watchedLynne=watchedLynne;
		this.watchedLynne.addObserver(this);
		createUIAndShow();
	}

	private void createUIAndShow() {
		setTitle("保存新书签");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				clearTextFields();
			}
		});
		setSize(350, 250);
		contentPane = new JPanel();
		SpringLayout springLayout = new SpringLayout();
		contentPane.setLayout(springLayout);
		setContentPane(contentPane);

		JLabel fli_nameLab = new JLabel("名    字：");
		springLayout.putConstraint(SpringLayout.NORTH, fli_nameLab,20, SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.WEST, fli_nameLab, 20, SpringLayout.WEST, contentPane);
		contentPane.add(fli_nameLab);

		JLabel fli_urlLab = new JLabel("网    址：");
		springLayout.putConstraint(SpringLayout.NORTH, fli_urlLab, 20, SpringLayout.SOUTH, fli_nameLab);
		springLayout.putConstraint(SpringLayout.WEST, fli_urlLab, 20, SpringLayout.WEST, contentPane);
		contentPane.add(fli_urlLab);

		JLabel fli_folderLab = new JLabel("文件夹：");
		springLayout.putConstraint(SpringLayout.NORTH, fli_folderLab, 20, SpringLayout.SOUTH, fli_urlLab);
		springLayout.putConstraint(SpringLayout.WEST, fli_folderLab, 20, SpringLayout.WEST, contentPane);
		contentPane.add(fli_folderLab);

		fli_nameTxt = new JTextField();
		fli_nameTxt.setColumns(20);
		springLayout.putConstraint(SpringLayout.NORTH, fli_nameTxt, 0, SpringLayout.NORTH, fli_nameLab);
		springLayout.putConstraint(SpringLayout.WEST, fli_nameTxt, 5, SpringLayout.EAST, fli_nameLab);
		contentPane.add(fli_nameTxt);

		fli_urlTxt = new JTextField();
		fli_urlTxt.setColumns(20);
		springLayout.putConstraint(SpringLayout.NORTH, fli_urlTxt, 0, SpringLayout.NORTH, fli_urlLab);
		springLayout.putConstraint(SpringLayout.WEST, fli_urlTxt, 5, SpringLayout.EAST, fli_urlLab);
		contentPane.add(fli_urlTxt);

		fli_folders = new JComboBox<LikeLynne>();
		initComboFolders();
		springLayout.putConstraint(SpringLayout.NORTH, fli_folders, -3, SpringLayout.NORTH, fli_folderLab);
		springLayout.putConstraint(SpringLayout.WEST, fli_folders, 5, SpringLayout.EAST, fli_folderLab);
		contentPane.add(fli_folders);

		JButton fli_edit = new JButton("新建");
		springLayout.putConstraint(SpringLayout.NORTH, fli_edit,0, SpringLayout.NORTH, fli_folders);
		springLayout.putConstraint(SpringLayout.EAST, fli_edit,-20, SpringLayout.EAST, contentPane);
		fli_edit.setActionCommand(CMD_NEWFOLDER);
		fli_edit.addActionListener(this);
		contentPane.add(fli_edit);

		JButton fli_ok = new JButton("完成");
		springLayout.putConstraint(SpringLayout.NORTH, fli_ok,30, SpringLayout.SOUTH,fli_folders);
		springLayout.putConstraint(SpringLayout.WEST, fli_ok,30, SpringLayout.WEST,fli_folders);
		fli_ok.setActionCommand(CMD_SUBMIT);
		fli_ok.addActionListener(this);
		contentPane.add(fli_ok);
	}

	public void initComboFolders() {
		String sqlGetFolders = "select id,title from likelynne where type=2 order by dateAdded ASC;";
		try {
			List<LikeLynne> llynnes =parentUI.sqliteHelper.executeQuery(sqlGetFolders, new RowMapper<LikeLynne>() {
				@Override
				public LikeLynne mapRow(ResultSet rs, int index) throws SQLException {
					// TODO Auto-generated method stub
					LikeLynne item = new LikeLynne();
					item.setId(rs.getInt("id"));
					item.setTitle(rs.getString("title"));
					return item;
				}
			});
			fli_folders.removeAllItems();// 如果用removeAll的话，下拉箭头会消失
			int size = llynnes.size();
			for (int i = 0; i < size; i++) {
				fli_folders.addItem(llynnes.get(i));
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(AddBookmarkUI2.this, "获取数据出错");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		if (CMD_NEWFOLDER.equals(command)) {
			//新建文件夹
			AddNewFolderDialog anfd=new AddNewFolderDialog(AddBookmarkUI2.this);
			LikeLynne lynne=anfd.getResult();
			if(null==lynne){
				return;
			}
			//update the folder combobox in this page
//			initComboFolders();
			//add the new bookmark to bookmark menu tree in MainUI
//			parentUI.addNewBookmark(lynne);
			watchedLynne.addNewLynneItem(lynne);
		} else if (CMD_SUBMIT.equals(command)) {
			String name = fli_nameTxt.getText();
			if ("".equals(name)) {
				JOptionPane.showMessageDialog(AddBookmarkUI2.this, "请输入名字");
				return;
			}
			String url = fli_urlTxt.getText();
			if ("".equals(url)) {
				JOptionPane.showMessageDialog(AddBookmarkUI2.this, "请输入链接");
				return;
			}
			if (!Utils.isUrlFormat(url)) {
				JOptionPane.showMessageDialog(AddBookmarkUI2.this, "链接无效,请输入有效的链接");
				return;
			}
			int lastId = new LikeLynneUtils().getMaxId();
			if (lastId < 0) {
				return;
			}
			LikeLynne folder = (LikeLynne) fli_folders.getSelectedItem();
			LikeLynne newLikeLynne = new LikeLynne();
			newLikeLynne.setId((lastId + 1));
			newLikeLynne.setParent(folder.getId());
			newLikeLynne.setTitle(name);
			newLikeLynne.setUrl(url);
			newLikeLynne.setType(1);
			newLikeLynne.setDateAdded(System.currentTimeMillis());
			int resultCode = LikeLynneApi.getInstance().addLikeLynne(newLikeLynne);
			if (resultCode == 200) {
//				parentUI.addNewBookmark(newLikeLynne);
				watchedLynne.addNewLynneItem(newLikeLynne);
			}
			this.setVisible(false);
			clearTextFields();
		}
	}
	
	public void clearTextFields(){
		fli_nameTxt.setText("");
		fli_urlTxt.setText("");
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		initComboFolders();
	}
}
