package com.hawk.bookmarks.ui.bk26_5_27;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.event.MyMouseInputListener;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.common.SqliteHelper;
import com.hawk.bookmarks.utils.common.Utils;

public class FloatUI implements ActionListener {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new FloatUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// private JPanel contentPane;
	public JFrame floatFrame;
	private Point origin;
	public AddBookmarkUI_bk addBmarkWindow;
	public JPopupMenu bmarkWindow;
	private AddBmarkOrFolderUI addBmarkOrFolderWin;
	public LikeLynne llynne;
	public SqliteHelper sqliteHelper;

	/**
	 * Create the frame.
	 */
	public FloatUI() {
		createUIAndShow();
	}

	private void createUIAndShow() {
		floatFrame = new JFrame();
		floatFrame.setUndecorated(true);
		floatFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

		floatFrame.setSize(100, 100);

		ImageIcon bk = new ImageIcon(FloatUI.class.getResource("/images/camera_camera_background_highlighted.png"));

		JPanel contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				g.drawImage(bk.getImage(), 0, 0, getWidth(), getHeight(), null);
				super.paintComponent(g);
			}
		};
		contentPane.setOpaque(false);
		floatFrame.setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout());

		origin = new Point();
		MyMouseInputListener myMouseInputListener = new MyMouseInputListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				origin.x = e.getX();
				origin.y = e.getY();
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (SwingUtilities.isRightMouseButton(e)) {
					new ManageUI(FloatUI.this,floatFrame);
					return;
				}
				if (null != addBmarkWindow && addBmarkWindow.isVisible()) {
					addBmarkWindow.setVisible(false);
					addBmarkWindow.setNameTxt("");
					addBmarkWindow.setUrlTxt("");
				} else {
					showAddBmarkWindow();
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				Point point = floatFrame.getLocation();
				floatFrame.setLocation(point.x + (e.getX() - origin.x), point.y + (e.getY() - origin.y));
				if (null != addBmarkWindow) {
					if (addBmarkWindow.isVisible()) {
						addBmarkWindow.setVisible(false);
						addBmarkWindow.setNameTxt("");
						addBmarkWindow.setUrlTxt("");
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				showBmarkWindow();
			}
		};

		contentPane.addMouseListener(myMouseInputListener);
		contentPane.addMouseMotionListener(myMouseInputListener);

		Shape circle1 = new Ellipse2D.Double(0, 0, 100, 100);
		floatFrame.setShape(circle1);

		sqliteHelper = new SqliteHelper(LikeLynneApi.getInstance().getBookmarkPath());

		floatFrame.setLocationRelativeTo(null);
		floatFrame.setAlwaysOnTop(true);
		floatFrame.setVisible(true);
	}

	/**
	 * 显示添加书签窗口
	 */
	private void showAddBmarkWindow() {
		if (null == addBmarkWindow) {
			addBmarkWindow = new AddBookmarkUI_bk(FloatUI.this);
		}
		if(null!=addBmarkOrFolderWin&&addBmarkOrFolderWin.isShowing()){
			return;
		}
		Point point = floatFrame.getLocation();
		addBmarkWindow.setLocation((point.x - 120), (point.y + 100));
		addBmarkWindow.setVisible(true);
	}

	/**
	 * 显示JpopupMenu书签树
	 */
	private void showBmarkWindow() {
		if (null == bmarkWindow) {
			llynne = LikeLynneApi.getInstance().getLikeLynne();
			if (null == llynne) {
				return;
			}
			bmarkWindow = new JPopupMenu(llynne.getTitle());
			initBmarkWindow(llynne);
		}
		bmarkWindow.show(floatFrame,0,100);
	}
	
	/**
	 * 
	 */
	public void showAddBmarkOrFolderWin() {
		if (null == addBmarkOrFolderWin) {
			addBmarkOrFolderWin = new AddBmarkOrFolderUI(FloatUI.this);
		}
		if (addBmarkOrFolderWin.isShowing()) {
			return;
		}
		addBmarkOrFolderWin.setAlwaysOnTop(true);
		Point point = floatFrame.getLocation();
		addBmarkOrFolderWin.setLocation(point.x + 100, point.y - 100);
		addBmarkOrFolderWin.setVisible(true);
	}
	
	/**
	 * 
	 * @param lynne
	 */
	public void initBmarkWindow(LikeLynne lynne) {
		bmarkWindow.removeAll();
		JMenu rootMenu=new JMenu(lynne.getTitle());
		bmarkWindow.add(rootMenu);
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

	/**
	 * 
	 * @param jMenu
	 * @param lynne
	 */
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		Utils.openUrl(command);
	}
}
