package com.hawk.bookmarks.ui.bk26_5_27;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
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
import com.hawk.bookmarks.event.MyMouseInputListener;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.DialogUtil;
import com.hawk.bookmarks.widget.BmarkSimpleTree;

public class ManageUI extends JWindow implements ActionListener {
	private static final long serialVersionUID = 1L;
	private BmarkSimpleTree bmarkSimpleTree;
	private Point origin;
//	private LikeLynne lynneNodes;
	private FloatUI floatUI;

	public ManageUI(FloatUI floatUI,JFrame owner){
		super(owner);
		this.floatUI=floatUI;
//		this.lynneNodes=parentFrame.llynne;
		createAndShowUI();
	}
	
//	public ManageUI(LikeLynne lynneNodes) {
//		// TODO Auto-generated constructor stub
//		this.lynneNodes = lynneNodes;
//		createAndShowUI();
//	}

	private void createAndShowUI() {
		this.setSize(new Dimension(350, 500));
		JPanel contentPane = new JPanel();
		this.setContentPane(contentPane);
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

		JLabel userNameLab = new JLabel("未登录");
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
		JMenuItem fromBrowser = new JMenuItem("从浏览器同步到系统");
		fromBrowser.setActionCommand("fromBrowser");
		fromBrowser.addActionListener(this);
		browserMenu.add(fromBrowser);
//		browserMenu.add(new JSeparator());或
		browserMenu.addSeparator();
		JMenuItem toBrowser = new JMenuItem("从系统同步到浏览器");
		toBrowser.setActionCommand("toBrowser");
		toBrowser.addActionListener(this);
		browserMenu.add(toBrowser);

		JButton browserBtn = new JButton("浏览器同步");
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
//		serverMenu.add(new JSeparator());
		browserMenu.addSeparator();
		JMenuItem toServer = new JMenuItem("从系统同步到服务器");
		toServer.setActionCommand("toServer");
		toServer.addActionListener(this);
		serverMenu.add(toServer);
		JButton serverBtn = new JButton("服务器同步");
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

		bmarkSimpleTree = new BmarkSimpleTree(floatUI.llynne, false);
		bmarkSimpleTree.setPreferredSize(new Dimension(300, 300));
		bmarkSimpleTree.setBorder(BorderFactory.createTitledBorder("系统书签"));
		bmarkSimpleTree.setRightKeyMenu(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String command=e.getActionCommand();
//				System.out.println("command="+command);
				JOptionPane.showMessageDialog(null,"command="+command);
				if(command.equals("addbmark")){
					
				}else if(command.equals("addfolder")){
					
				}else if(command.equals("edit")){
					
				}else if(command.equals("delete")){
					
				}
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, bmarkSimpleTree, 20, SpringLayout.SOUTH, browserBtn);
		springLayout.putConstraint(SpringLayout.WEST, bmarkSimpleTree, 20, SpringLayout.WEST, contentPane);
		contentPane.add(bmarkSimpleTree);

		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		if (command.equals("close")) {
//			dispose();
			setVisible(false);
		} else if (command.equals("editAvatar")) {
			JOptionPane.showMessageDialog(ManageUI.this, "暂时不能修改头像");
		} else if (command.equals("editNick")) {
			JOptionPane.showMessageDialog(ManageUI.this, "暂时不能修改昵称");
		} else if (command.equals("fromBrowser")) {
			DefaultMutableTreeNode dmt=bmarkSimpleTree.getCurrentTreeNode();
			LikeLynne ll=(LikeLynne) dmt.getUserObject();
			DialogUtil.showImportFromBroswer(bmarkSimpleTree,ll.getId());
		} else if (command.equals("toBrowser")) {
			DialogUtil.showImportToBrowser(bmarkSimpleTree);
		} else if (command.equals("fromServer")) {

		} else if (command.equals("toServer")) {

		} else if (command.equals("settings")) {
			new SettingsUI();
		}
	}

	public static void main(String[] args) {
		LikeLynne lynneNodes = LikeLynneApi.getInstance().getLikeLynne();
//		new ManageUI(lynneNodes);
	}
}
