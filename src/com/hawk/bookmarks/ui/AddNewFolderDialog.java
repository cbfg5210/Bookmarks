package com.hawk.bookmarks.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.common.RowMapper;
import com.hawk.bookmarks.utils.common.SqliteHelper;
import com.hawk.bookmarks.utils.likelynnetool.LikeLynneUtils;

public class AddNewFolderDialog extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JComboBox<LikeLynne> fli_folders;
	private LikeLynne resLikeLynne;

	/**
	 * Create the dialog.
	 */
	public AddNewFolderDialog(AddBookmarkDialog frame) {
		super(frame,true);
		this.setModal(true);//设置模式 dialog关闭后才能获取返回值
		createUI();
		setLocation(frame.getLocation());
		setVisible(true);
	}
	
	private void createUI(){
		setTitle("新建文件夹");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(300,230);
		
		getContentPane().setLayout(new BorderLayout());
		SpringLayout springLayout=new SpringLayout();
		contentPanel.setLayout(springLayout);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel folderLab=new JLabel("请选择父文件夹：");
		springLayout.putConstraint(SpringLayout.NORTH,folderLab,20,SpringLayout.NORTH,contentPanel);
		springLayout.putConstraint(SpringLayout.WEST,folderLab,25,SpringLayout.WEST,contentPanel);
		contentPanel.add(folderLab);
		
		fli_folders = new JComboBox<LikeLynne>();
		initComboFolders();
		springLayout.putConstraint(SpringLayout.NORTH, fli_folders,5, SpringLayout.SOUTH, folderLab);
		springLayout.putConstraint(SpringLayout.WEST, fli_folders, 25, SpringLayout.WEST, contentPanel);
		contentPanel.add(fli_folders);
		
		JLabel folderNameLab=new JLabel("请输入新文件夹名称：");
		springLayout.putConstraint(SpringLayout.NORTH,folderNameLab,10,SpringLayout.SOUTH,fli_folders);
		springLayout.putConstraint(SpringLayout.WEST,folderNameLab,25,SpringLayout.WEST,contentPanel);
		contentPanel.add(folderNameLab);
		
		JTextField folderNameTxt=new JTextField();
		folderNameTxt.setColumns(20);
		springLayout.putConstraint(SpringLayout.NORTH,folderNameTxt,5,SpringLayout.SOUTH,folderNameLab);
		springLayout.putConstraint(SpringLayout.WEST,folderNameTxt,25,SpringLayout.WEST,contentPanel);
		contentPanel.add(folderNameTxt);
		
		JButton submitBtn=new JButton("保存");
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String folderName=folderNameTxt.getText().trim();
				if("".equals(folderName)){
					JOptionPane.showMessageDialog(submitBtn,"请输入文件夹名字");
					return;
				}
				int lastId=new LikeLynneUtils().getMaxId();
				if(lastId<0){
					resLikeLynne=null;
					dispose();
					return;
				}
				LikeLynne parentLynne=(LikeLynne) fli_folders.getSelectedItem();
				resLikeLynne=new LikeLynne();
				resLikeLynne.setId(lastId+1);
				resLikeLynne.setParent(parentLynne.getId());
				resLikeLynne.setTitle(folderName);
				resLikeLynne.setType(2);
				resLikeLynne.setUrl(""+System.currentTimeMillis());//这一字段设置Unique约束，所以不该为空
				resLikeLynne.setDateAdded(System.currentTimeMillis());
				int res=LikeLynneApi.getInstance().addLikeLynne(resLikeLynne);
				if(res==119){
					resLikeLynne=null;
					dispose();
					return;
				}
				dispose();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH,submitBtn,20,SpringLayout.SOUTH,folderNameTxt);
		springLayout.putConstraint(SpringLayout.WEST,submitBtn,80,SpringLayout.WEST,contentPanel);
		contentPanel.add(submitBtn);
	}
	
	public void initComboFolders(){
		String sqlGetFolders="select id,title from likelynne where type=2 order by dateAdded ASC;";
		try {
			String dbPath=LikeLynneApi.getInstance().getBookmarkPath();
			List<LikeLynne>llynnes=SqliteHelper.getInstance(dbPath).executeQuery(sqlGetFolders,new RowMapper<LikeLynne>() {
				@Override
				public LikeLynne mapRow(ResultSet rs, int index) throws SQLException {
					// TODO Auto-generated method stub
					LikeLynne item=new LikeLynne();
					item.setId(rs.getInt("id"));
					item.setTitle(rs.getString("title"));
					return item;
				}
			});
			int size=llynnes.size();
			for(int i=0;i<size;i++){
				fli_folders.addItem(llynnes.get(i));
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(AddNewFolderDialog.this,"获取数据出错");
		}
	}

	public LikeLynne getResult(){
		return resLikeLynne;
	}
}
