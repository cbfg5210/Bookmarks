package com.hawk.bookmarks.ui.bk26_5_27;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SpringLayout;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.common.RowMapper;
import com.hawk.bookmarks.utils.common.SqliteHelper;
import com.hawk.bookmarks.utils.common.Utils;
import com.hawk.bookmarks.utils.likelynnetool.LikeLynneUtils;

public class AddBookmarkUI_bk extends JWindow implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextField fli_nameTxt;
	private JTextField fli_urlTxt;
	private JComboBox<LikeLynne> fli_folders;
	private FloatUI parentFrame;
	
	public void setNameTxt(String txt){
		fli_nameTxt.setText(txt);
	}
	public void setUrlTxt(String txt){
		fli_urlTxt.setText(txt);
	}
	public String getNameTxt(){
		return fli_nameTxt.getText();
	}
	public String getUrlTxt(){
		return fli_urlTxt.getText();
	}
	
	public AddBookmarkUI_bk(FloatUI parentFrame){
		super(parentFrame.floatFrame);
		this.parentFrame=parentFrame;
		createAndShowUI();
	}
	
	private void createAndShowUI(){
		setSize(350, 250);
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		SpringLayout springLayout = new SpringLayout();
		contentPane.setLayout(springLayout);

		JLabel fli_title = new JLabel("保存新书签");
		springLayout.putConstraint(SpringLayout.NORTH, fli_title, 10, SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.WEST, fli_title, 150, SpringLayout.WEST, contentPane);
		contentPane.add(fli_title);

		JLabel fli_nameLab = new JLabel("名字：");
		springLayout.putConstraint(SpringLayout.NORTH, fli_nameLab, 20, SpringLayout.SOUTH, fli_title);
		springLayout.putConstraint(SpringLayout.WEST, fli_nameLab, 20, SpringLayout.WEST, contentPane);
		contentPane.add(fli_nameLab);

		JLabel fli_urlLab = new JLabel("网址：");
		springLayout.putConstraint(SpringLayout.NORTH, fli_urlLab, 20, SpringLayout.SOUTH, fli_nameLab);
		springLayout.putConstraint(SpringLayout.WEST, fli_urlLab, 20, SpringLayout.WEST, contentPane);
		contentPane.add(fli_urlLab);

		JLabel fli_folderLab = new JLabel("文件夹：");
		springLayout.putConstraint(SpringLayout.NORTH, fli_folderLab, 20, SpringLayout.SOUTH, fli_urlLab);
		springLayout.putConstraint(SpringLayout.WEST, fli_folderLab, 20, SpringLayout.WEST, contentPane);
		contentPane.add(fli_folderLab);

		fli_nameTxt=new JTextField();
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

		JButton fli_cancel = new JButton("取消");
		springLayout.putConstraint(SpringLayout.NORTH, fli_cancel, 40, SpringLayout.SOUTH, fli_folderLab);
		springLayout.putConstraint(SpringLayout.WEST, fli_cancel, 50, SpringLayout.WEST, contentPane);
		fli_cancel.setActionCommand("cancel");
		fli_cancel.addActionListener(this);
		contentPane.add(fli_cancel);

		JButton fli_edit = new JButton("修改");
		springLayout.putConstraint(SpringLayout.NORTH, fli_edit, 0, SpringLayout.NORTH, fli_cancel);
		springLayout.putConstraint(SpringLayout.WEST, fli_edit, 20, SpringLayout.EAST, fli_cancel);
		fli_edit.setActionCommand("edit");
		fli_edit.addActionListener(this);
		contentPane.add(fli_edit);

		JButton fli_ok = new JButton("完成");
		springLayout.putConstraint(SpringLayout.NORTH, fli_ok, 0, SpringLayout.NORTH, fli_cancel);
		springLayout.putConstraint(SpringLayout.WEST, fli_ok, 20, SpringLayout.EAST, fli_edit);
		fli_ok.setActionCommand("ok");
		fli_ok.addActionListener(this);
		contentPane.add(fli_ok);
	}
	
	public void initComboFolders(){
		String sqlGetFolders="select id,title from likelynne where type=2 order by dateAdded ASC;";
		try {
			String dbPath=LikeLynneApi.getInstance().getBookmarkPath();
			List<LikeLynne>llynnes=new SqliteHelper(dbPath).executeQuery(sqlGetFolders,new RowMapper<LikeLynne>() {
				@Override
				public LikeLynne mapRow(ResultSet rs, int index) throws SQLException {
					// TODO Auto-generated method stub
					LikeLynne item=new LikeLynne();
					item.setId(rs.getInt("id"));
					item.setTitle(rs.getString("title"));
					return item;
				}
			});
			fli_folders.removeAllItems();//如果用removeAll的话，下拉箭头会消失
			int size=llynnes.size();
			for(int i=0;i<size;i++){
				fli_folders.addItem(llynnes.get(i));
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(AddBookmarkUI_bk.this,"获取数据出错");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command=e.getActionCommand();
		if ("cancel".equals(command)) {
			this.setVisible(false);
			fli_nameTxt.setText("");
			fli_urlTxt.setText("");
		} else if ("edit".equals(command)) {
			parentFrame.showAddBmarkOrFolderWin();
		} else if ("ok".equals(command)) {
			String name=fli_nameTxt.getText();
			if("".equals(name)){
				JOptionPane.showMessageDialog(AddBookmarkUI_bk.this,"请输入名字");
				return;
			}
			String url=fli_urlTxt.getText();
			if("".equals(url)){
				JOptionPane.showMessageDialog(AddBookmarkUI_bk.this,"请输入链接");
				return;
			}
			if(!Utils.isUrlFormat(url)){
				JOptionPane.showMessageDialog(AddBookmarkUI_bk.this,"链接无效,请输入有效的链接");
				return;
			}
			
			int lastId=new LikeLynneUtils().getMaxId();
			if(lastId<0){
				return;
			}
			
			LikeLynne folder=(LikeLynne) fli_folders.getSelectedItem();
			LikeLynne newLikeLynne=new LikeLynne();
			newLikeLynne.setId((lastId+1));
			newLikeLynne.setParent(folder.getId());
			newLikeLynne.setTitle(name);
			newLikeLynne.setUrl(url);
			newLikeLynne.setType(1);
			newLikeLynne.setDateAdded(System.currentTimeMillis());
			int resultCode=LikeLynneApi.getInstance().addLikeLynne(newLikeLynne);
			if(resultCode==200){
				addNewLikeLynne(parentFrame.llynne, newLikeLynne);
				parentFrame.initBmarkWindow(parentFrame.llynne);
				this.setVisible(false);
			}
			fli_nameTxt.setText("");
			fli_urlTxt.setText("");
		}
	}
	
	/**
	 * 插入新数据
	 * @param lynne
	 * @param newLikeLynne
	 */
	private void addNewLikeLynne(LikeLynne lynne, LikeLynne newLikeLynne) {
		if (newLikeLynne.getParent() == lynne.getId()) {
			lynne.getChildren().add(newLikeLynne);
		} else {
			List<LikeLynne> children = lynne.getChildren();
			if (null != children && children.size() > 0) {
				int size = children.size();
				for (int i = 0; i < size; i++) {
					LikeLynne child = children.get(i);
					if (child.getType() == 2) {
						addNewLikeLynne(child, newLikeLynne);
					}
				}
			}
		}
	}
}
