package com.hawk.bookmarks.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.event.MyMouseInputListener;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.observable.Watched;
import com.hawk.bookmarks.utils.common.SqliteHelper;
import com.hawk.bookmarks.utils.common.Utils;

public class MainUI extends JFrame implements Observer,ActionListener{
	private JPanel contentPane;
	private Point origin;
	private JPopupMenu bookmarkTreeMenu;
	private HomeUI homeUI;
	public SqliteHelper sqliteHelper;
	private final String CMD_ABOUT="cmd_about";
	private final String CMD_HOME="cmd_home";
	private final String CMD_BOOKMARK="cmd_bookmark";
	private final String CMD_ADD="cmd_add";
	
	private Watched watchedLynne;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI frame = new MainUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainUI() {
		createUIAndShow();
	}
	
	private void createUIAndShow(){
		setSize(new Dimension(150,150));
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		contentPane = new JPanel();
		contentPane.setOpaque(false);
		SpringLayout springLayout=new SpringLayout();
		contentPane.setLayout(springLayout);
		setContentPane(contentPane);
		
		//水平分割线
		JSeparator hSeparator=new JSeparator();
		hSeparator.setPreferredSize(new Dimension(150,2));
		springLayout.putConstraint(SpringLayout.NORTH,hSeparator,75,SpringLayout.NORTH,contentPane);
		contentPane.add(hSeparator);
		//垂直分割线
		JSeparator vSeparator=new JSeparator();
		vSeparator.setOrientation(SwingConstants.VERTICAL);
		vSeparator.setPreferredSize(new Dimension(2,150));
		springLayout.putConstraint(SpringLayout.WEST,vSeparator,75,SpringLayout.WEST,contentPane);
		contentPane.add(vSeparator);

		//中间
		JButton centerBtn=new JButton();
		centerBtn.setPreferredSize(new Dimension(25,25));
		centerBtn.setBorderPainted(false);
		centerBtn.setContentAreaFilled(false);
		origin=new Point();
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
				Point point =getLocation();
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
		centerBtn.addMouseListener(myMouseInputListener);
		centerBtn.addMouseMotionListener(myMouseInputListener);
		springLayout.putConstraint(SpringLayout.NORTH,centerBtn,65,SpringLayout.NORTH,contentPane);
		springLayout.putConstraint(SpringLayout.WEST,centerBtn,65,SpringLayout.WEST,contentPane);
		contentPane.add(centerBtn);
		//about button
		JButton aboutBtn=new JButton();
		aboutBtn.setBorderPainted(false);
		aboutBtn.setContentAreaFilled(false);
		ImageIcon aboutIcon = new ImageIcon(MainUI.class.getResource("/images/ic_menu_help.png"));
		aboutBtn.setIcon(aboutIcon);
		aboutBtn.setActionCommand(CMD_ABOUT);
		aboutBtn.addActionListener(this);
		springLayout.putConstraint(SpringLayout.NORTH,aboutBtn,25,SpringLayout.NORTH,contentPane);
		springLayout.putConstraint(SpringLayout.WEST,aboutBtn,15,SpringLayout.WEST,contentPane);
		contentPane.add(aboutBtn);
		//home button
		JButton homeBtn=new JButton();
		homeBtn.setBorderPainted(false);
		homeBtn.setContentAreaFilled(false);
		ImageIcon homeIcon = new ImageIcon(MainUI.class.getResource("/images/ic_menu_home.png"));
		homeBtn.setIcon(homeIcon);
		homeBtn.setActionCommand(CMD_HOME);
		homeBtn.addActionListener(this);
		springLayout.putConstraint(SpringLayout.NORTH,homeBtn,25,SpringLayout.NORTH,contentPane);
		springLayout.putConstraint(SpringLayout.EAST,homeBtn,-15,SpringLayout.EAST,contentPane);
		contentPane.add(homeBtn);
		//bookmark button
		JButton bookmarkBtn=new JButton();
		bookmarkBtn.setBorderPainted(false);
		bookmarkBtn.setContentAreaFilled(false);
		ImageIcon bookmarkIcon = new ImageIcon(MainUI.class.getResource("/images/ic_input_get.png"));
		bookmarkBtn.setIcon(bookmarkIcon);
		bookmarkBtn.setActionCommand(CMD_BOOKMARK);
		bookmarkBtn.addActionListener(this);
		springLayout.putConstraint(SpringLayout.SOUTH,bookmarkBtn,-25,SpringLayout.SOUTH,contentPane);
		springLayout.putConstraint(SpringLayout.WEST,bookmarkBtn,15,SpringLayout.WEST,contentPane);
		contentPane.add(bookmarkBtn);
		//add bookmark button
		JButton addBtn=new JButton();
		addBtn.setBorderPainted(false);
		addBtn.setContentAreaFilled(false);
		ImageIcon addIcon = new ImageIcon(MainUI.class.getResource("/images/ic_menu_star.png"));
		addBtn.setIcon(addIcon);
		addBtn.setActionCommand(CMD_ADD);
		addBtn.addActionListener(this);
		springLayout.putConstraint(SpringLayout.SOUTH,addBtn,-25,SpringLayout.SOUTH,contentPane);
		springLayout.putConstraint(SpringLayout.EAST,addBtn,-15,SpringLayout.EAST,contentPane);
		contentPane.add(addBtn);
		
		Shape circleShape = new Ellipse2D.Double(0, 0, 150, 150);
		setShape(circleShape);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setVisible(true);
		
		sqliteHelper = SqliteHelper.getInstance(LikeLynneApi.getInstance().getBookmarkPath());
		checkMenuNullInit();
	}
	
	private void showHomeUI(){
		if(null==homeUI){
			homeUI=new HomeUI(MainUI.this,watchedLynne);
		}
		if(!homeUI.isVisible()){
			homeUI.setLocation(getX()-65,getY()+80);
			homeUI.setVisible(true);
		}
		// Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	/**
	 * 书签菜单为空的话初始化
	 */
	private void checkMenuNullInit(){
		if (null == bookmarkTreeMenu) {
			LikeLynne llynne = LikeLynneApi.getInstance().getLikeLynne();
			if (null == llynne) {
				return;
			}
			bookmarkTreeMenu = new JPopupMenu(llynne.getTitle());
			initBookmarkTreeMenu(llynne);
			watchedLynne=new Watched(llynne);
			watchedLynne.addObserver(MainUI.this);
		}
	}
	
	public void initBookmarkTreeMenu(LikeLynne lynne) {
		bookmarkTreeMenu.removeAll();
		JMenu rootMenu=new JMenu(lynne.getTitle());
		bookmarkTreeMenu.add(rootMenu);
		List<LikeLynne> children = lynne.getChildren();
		if (null != children && children.size() != 0) {
			int size = children.size();
			for (int i = 0; i < size; i++) {
				LikeLynne child = children.get(i);
				if (null == child.getChildren() || child.getChildren().size() == 0) {
					JMenuItem jMenuItem = new JMenuItem(child.getTitle());
					jMenuItem.setActionCommand(child.getUrl());
					jMenuItem.addActionListener(this);
					rootMenu.add(jMenuItem);
				} else {
					JMenu jMenu = new JMenu(child.getTitle());
					addMenuItem(jMenu, child);
					rootMenu.add(jMenu);
				}
			}
		}
	}
	
	private void addMenuItem(JMenu jMenu, LikeLynne lynne) {
		List<LikeLynne> children = lynne.getChildren();
		if (null != children && children.size() > 0) {
			int size = children.size();
			for (int i = 0; i < size; i++) {
				LikeLynne child = children.get(i);
				if (null == child.getChildren() || child.getChildren().size() == 0) {
					JMenuItem jMenuItem = new JMenuItem(child.getTitle());
					jMenuItem.setActionCommand(child.getUrl());
					jMenuItem.addActionListener(this);
					jMenu.add(jMenuItem);
				} else {
					JMenu jSubMenu = new JMenu(child.getTitle());
					addMenuItem(jSubMenu, child);
					jMenu.add(jSubMenu);
				}
			}
		}
	}
	
	public void showAddBookmarkUI() {
		checkMenuNullInit();
		new AddBookmarkDialog(MainUI.this, watchedLynne);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command=e.getActionCommand();
		if(CMD_ABOUT.equals(command)){
			new AboutDialog(MainUI.this);
		}else if(CMD_HOME.equals(command)){
			showHomeUI();
		}else if(CMD_BOOKMARK.equals(command)){
			bookmarkTreeMenu.show(this,0,130);
		}else if(CMD_ADD.equals(command)){
			showAddBookmarkUI();
		}else{
			Utils.openUrl(command);
		}
	}

	@Override
	public void update(java.util.Observable o, Object arg) {
		// TODO Auto-generated method stub
		LikeLynne newLikeLynne=((com.hawk.bookmarks.observable.Watched)o).getTheLynne();
		initBookmarkTreeMenu(newLikeLynne);
	}
}
