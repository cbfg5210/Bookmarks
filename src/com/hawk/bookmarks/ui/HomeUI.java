package com.hawk.bookmarks.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JWindow;
import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.bmob.bson.BSONObject;
import com.hawk.bookmarks.bmob.custom.Downloader;
import com.hawk.bookmarks.bmob.custom.MyBmob;
import com.hawk.bookmarks.bmob.custom.OnBmobListener;
import com.hawk.bookmarks.event.MyMouseInputListener;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.observable.Watched;
import com.hawk.bookmarks.ui.bk26_5_27.ManageUI;
import com.hawk.bookmarks.utils.CheckUtil;
import com.hawk.bookmarks.utils.DialogUtil;
import com.hawk.bookmarks.utils.SharedPrefUtil;
import com.hawk.bookmarks.widget.BmarkSimpleTree;

public class HomeUI extends JWindow implements Observer,ActionListener{
	private static final long serialVersionUID = 1L;
	private static final String TAG="HomeUI";
	private BmarkSimpleTree bmarkSimpleTree;
	private Point origin;
	private Watched watchedLynne;
	private MainUI parentUI;

	public HomeUI(MainUI parentUI,Watched watchedLynne){
		super(parentUI);
		this.parentUI=parentUI;
		this.watchedLynne=watchedLynne;
		this.watchedLynne.addObserver(this);
		createUI();
	}
	
	private void createUI() {
		setSize(new Dimension(350, 500));
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		SpringLayout springLayout = new SpringLayout();
		contentPane.setLayout(springLayout);

		JLabel topBarLab = new JLabel();
		topBarLab.setPreferredSize(new Dimension(315, 20));
		springLayout.putConstraint(SpringLayout.NORTH, topBarLab, 0, SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.WEST, topBarLab, 0, SpringLayout.WEST, contentPane);

		origin = new Point();
		MyMouseInputListener myMouseInputListener = new MyMouseInputListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				origin.x = e.getX();
				origin.y = e.getY();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				Point point = getLocation();
				setLocation(point.x + (e.getX() - origin.x), point.y + (e.getY() - origin.y));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.MOVE_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
			}
		};
		topBarLab.addMouseListener(myMouseInputListener);
		topBarLab.addMouseMotionListener(myMouseInputListener);
		contentPane.add(topBarLab);

		JButton closeBtn = new JButton();
		closeBtn.setPreferredSize(new Dimension(35, 35));
		closeBtn.setOpaque(false);
		closeBtn.setBorder(null);
		closeBtn.setContentAreaFilled(false);
		ImageIcon closeIcon = new ImageIcon(ManageUI.class.getResource("/images/ad_close_icon.png"));
		closeBtn.setIcon(closeIcon);
		springLayout.putConstraint(SpringLayout.NORTH, closeBtn, 0, SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.EAST, closeBtn, 0, SpringLayout.EAST, contentPane);
		closeBtn.setActionCommand("close");
		closeBtn.addActionListener(this);
		contentPane.add(closeBtn);

		JButton avatarBtn = new JButton();
		avatarBtn.setPreferredSize(new Dimension(60, 60));
		avatarBtn.setOpaque(false);
		avatarBtn.setBorder(null);
		avatarBtn.setContentAreaFilled(false);
		ImageIcon avatarIcon = new ImageIcon(ManageUI.class.getResource("/images/f_hufen.png"));
		avatarBtn.setIcon(avatarIcon);
		springLayout.putConstraint(SpringLayout.NORTH, avatarBtn, 10, SpringLayout.SOUTH, topBarLab);
		springLayout.putConstraint(SpringLayout.WEST, avatarBtn, 20, SpringLayout.WEST, contentPane);
		avatarBtn.setActionCommand("editAvatar");
		avatarBtn.addActionListener(this);
		contentPane.add(avatarBtn);

		String username=SharedPrefUtil.getInstance().readString(SharedPrefUtil.USERNAME);
		JLabel userNameLab = new JLabel(username);
		springLayout.putConstraint(SpringLayout.NORTH, userNameLab, 30, SpringLayout.NORTH, avatarBtn);
		springLayout.putConstraint(SpringLayout.WEST, userNameLab, 20, SpringLayout.EAST, avatarBtn);
		contentPane.add(userNameLab);

		JButton editBtn = new JButton();
		editBtn.setPreferredSize(new Dimension(35, 35));
		editBtn.setOpaque(false);
		editBtn.setBorder(null);
		editBtn.setContentAreaFilled(false);
		ImageIcon editIcon = new ImageIcon(ManageUI.class.getResource("/images/activity_card_write.png"));
		editBtn.setIcon(editIcon);
		springLayout.putConstraint(SpringLayout.NORTH, editBtn, -8, SpringLayout.NORTH, userNameLab);
		springLayout.putConstraint(SpringLayout.EAST, editBtn, -10, SpringLayout.EAST, contentPane);
		editBtn.setActionCommand("editNick");
		editBtn.addActionListener(this);
		contentPane.add(editBtn);

		JSeparator speratorLab = new JSeparator();
		speratorLab.setPreferredSize(new Dimension(350, 2));
		springLayout.putConstraint(SpringLayout.NORTH, speratorLab, 10, SpringLayout.SOUTH, avatarBtn);
		springLayout.putConstraint(SpringLayout.WEST, speratorLab, 0, SpringLayout.WEST, contentPane);
		contentPane.add(speratorLab);

		JPopupMenu browserMenu = new JPopupMenu();
		JMenuItem fromBrowser = new JMenuItem("从浏览器导入系统");
		fromBrowser.setActionCommand("fromBrowser");
		fromBrowser.addActionListener(this);
		browserMenu.add(fromBrowser);
		browserMenu.addSeparator();
		JMenuItem toBrowser = new JMenuItem("从系统导入浏览器");
		toBrowser.setActionCommand("toBrowser");
		toBrowser.addActionListener(this);
		browserMenu.add(toBrowser);

		JButton browserBtn = new JButton("导入");
		browserBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				browserMenu.show(browserBtn, 0, 20);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, browserBtn, 10, SpringLayout.SOUTH, speratorLab);
		springLayout.putConstraint(SpringLayout.WEST, browserBtn, 20, SpringLayout.WEST, contentPane);
		contentPane.add(browserBtn);

		JPopupMenu serverMenu = new JPopupMenu();
		JMenuItem fromServer = new JMenuItem("从服务器同步到系统");
		fromServer.setActionCommand("fromServer");
		fromServer.addActionListener(this);
		serverMenu.add(fromServer);
		browserMenu.addSeparator();
		JMenuItem toServer = new JMenuItem("从系统同步到服务器");
		toServer.setActionCommand("toServer");
		toServer.addActionListener(this);
		serverMenu.add(toServer);
		JButton serverBtn = new JButton("云同步");
		serverBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				serverMenu.show(serverBtn, 0, 20);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, serverBtn, 0, SpringLayout.NORTH, browserBtn);
		springLayout.putConstraint(SpringLayout.WEST, serverBtn, 40, SpringLayout.EAST, browserBtn);
		contentPane.add(serverBtn);

		JButton settingsBtn = new JButton();
		settingsBtn.setPreferredSize(new Dimension(35, 35));
		settingsBtn.setOpaque(false);
		settingsBtn.setBorder(null);
		settingsBtn.setContentAreaFilled(false);
		ImageIcon settingsIcon = new ImageIcon(ManageUI.class.getResource("/images/perm_group_system_tools.png"));
		settingsBtn.setIcon(settingsIcon);
		springLayout.putConstraint(SpringLayout.NORTH, settingsBtn, -5, SpringLayout.NORTH, browserBtn);
		springLayout.putConstraint(SpringLayout.EAST, settingsBtn, -10, SpringLayout.EAST, contentPane);
		settingsBtn.setActionCommand("settings");
		settingsBtn.addActionListener(this);
		contentPane.add(settingsBtn);

		bmarkSimpleTree = new BmarkSimpleTree(watchedLynne.getTheLynne());
		bmarkSimpleTree.setPreferredSize(new Dimension(300, 300));
		bmarkSimpleTree.setBorder(BorderFactory.createTitledBorder("系统书签"));
		bmarkSimpleTree.setRightKeyMenu(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String command=e.getActionCommand();
				DefaultMutableTreeNode currentNode=bmarkSimpleTree.getCurrentTreeNode();
				LikeLynne currentLynne=(LikeLynne) currentNode.getUserObject();
				
				if(command.equals("addbmark")){
					if(currentLynne.getType()==1){
						return;
					}
//					new AddBookmarkDialog(parentUI, watchedLynne);
					LikeLynne newBookmark=DialogUtil.showAddNewBookmarkDialog(HomeUI.this,currentLynne.getId());
					if(null==newBookmark){
						return;
					}
					bmarkSimpleTree.addObject(newBookmark);
					watchedLynne.deleteObserver(HomeUI.this);
					watchedLynne.addNewLynneItem(newBookmark);
					watchedLynne.addObserver(HomeUI.this);
				}else if(command.equals("addfolder")){
					if(currentLynne.getType()==1){
						return;
					}
					//新建文件夹
					LikeLynne newFolder=DialogUtil.showAddNewFolderDialog(HomeUI.this,currentLynne.getId());
					if(null==newFolder){
						return;
					}
					bmarkSimpleTree.addObject(newFolder);
					watchedLynne.deleteObserver(HomeUI.this);
					watchedLynne.addNewLynneItem(newFolder);
					watchedLynne.addObserver(HomeUI.this);
				}else if(command.equals("edit")){
					if(currentLynne.getType()==1){//更新书签
						LikeLynne updatedBookmark=DialogUtil.showUpdateBookmarkDialog(HomeUI.this,currentLynne);
						if(null==updatedBookmark){
							return;
						}
						watchedLynne.updateLynneItem(updatedBookmark);
					}else{//更新文件夹
						//如果是根目录的话不能修改
//						LogUtil.i(TAG,"level="+currentNode.getLevel());
						if(currentNode.getLevel()==0){
							JOptionPane.showMessageDialog(HomeUI.this,"不能修改根目录");
							return;
						}
						LikeLynne updatedBookmark=DialogUtil.showUpdateFolderDialog(HomeUI.this,currentLynne);
						if(null==updatedBookmark){
							return;
						}
						watchedLynne.updateLynneItem(updatedBookmark);
					}
				}else if(command.equals("delete")){
					//如果是根目录的话不能修改
					if(currentNode.getLevel()==0){
						JOptionPane.showMessageDialog(HomeUI.this,"不能删除根目录");
						return;
					}
					boolean result=DialogUtil.showDeleteDialog(HomeUI.this,currentLynne);
					if(result){
						watchedLynne.deleteLynneItem(currentLynne,true);
					}
				}
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, bmarkSimpleTree, 20, SpringLayout.SOUTH, browserBtn);
		springLayout.putConstraint(SpringLayout.WEST, bmarkSimpleTree, 20, SpringLayout.WEST, contentPane);
		contentPane.add(bmarkSimpleTree);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		if (command.equals("close")) {
//			dispose();
			setVisible(false);
		} else if (command.equals("editAvatar")) {
			JOptionPane.showMessageDialog(HomeUI.this, "暂时不能修改头像");
		} else if (command.equals("editNick")) {
			JOptionPane.showMessageDialog(HomeUI.this, "暂时不能修改昵称");
		} else if (command.equals("fromBrowser")) {
			DefaultMutableTreeNode dmt=bmarkSimpleTree.getCurrentTreeNode();
			LikeLynne ll=(LikeLynne) dmt.getUserObject();
			LikeLynne importLynne=DialogUtil.showImportFromBroswer(bmarkSimpleTree,ll.getId());
			if(null!=importLynne){
				watchedLynne.addNewLynneItem(importLynne);
			}
		} else if (command.equals("toBrowser")) {
			DialogUtil.showImportToBrowser(bmarkSimpleTree);
		} else if (command.equals("fromServer")) {
			downFile();
		} else if (command.equals("toServer")) {
			uploadFile();
		} else if (command.equals("settings")) {
			new SettingsUI(parentUI);
		}
	}
	
	private void downFile(){
		String dbFileUrl=SharedPrefUtil.getInstance().readString(SharedPrefUtil.DBFILEURL);
		if(CheckUtil.isEmpty(dbFileUrl)){
			JOptionPane.showMessageDialog(HomeUI.this,"没有同步过文件");
			return;
		}
		String dbPath=System.getProperty("user.dir");
		ProgressDialog progressDialog=new ProgressDialog("正在快速下载,请稍等...");
		
		boolean result=Downloader.downLoad(dbFileUrl,dbPath,"likelynne.sqlite");
		if(result){
			LikeLynne llynne = LikeLynneApi.getInstance().getLikeLynne();
			if (null!= llynne) {
				watchedLynne.setTheLynne(llynne);
			}
		}
		progressDialog.dispose();
	}
	
	private void uploadFile(){
		ProgressDialog progressDialog=new ProgressDialog("正在快速上传,请稍等...");
		MyBmob.uploadFile2(LikeLynneApi.getInstance().getBookmarkPath(),new OnBmobListener() {
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
//				{"cdn":"upyun","filename":"likelynne.sqlite","url":"http://bmob-cdn-635.b0.upaiyun.com/2016/06/01/2d8e61aa40855d058046e761efa8a828.sqlite"}
				BSONObject resultBson=new BSONObject(result);
				String url=resultBson.getString("url");
				SharedPrefUtil.getInstance().save(SharedPrefUtil.DBFILEURL,url);
				
				BSONObject bson = new BSONObject();
				bson.put("dbFileUrl",url);
				String objectId=SharedPrefUtil.getInstance().readString(SharedPrefUtil.OBJECTID);
				MyBmob.update("UeUser",objectId, bson.toString(),new OnBmobListener() {
					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						progressDialog.dispose();
						JOptionPane.showMessageDialog(HomeUI.this,"文件同步成功");
					}
					@Override
					public void onError(String msg) {
						// TODO Auto-generated method stub
						progressDialog.dispose();
						JOptionPane.showMessageDialog(HomeUI.this,"同步文件失败："+msg);
					}
				});
			}
			@Override
			public void onError(String msg) {
				// TODO Auto-generated method stub
				progressDialog.dispose();
				JOptionPane.showMessageDialog(HomeUI.this,"上传文件失败："+msg);
			}
		});
	}
	
	public static void main(String[] args) {
		LikeLynne lynneNodes = LikeLynneApi.getInstance().getLikeLynne();
//		new ManageUI(lynneNodes);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		LikeLynne newLikeLynne=((Watched)o).getTheLynne();
		bmarkSimpleTree.initTree(newLikeLynne);
	}
}
