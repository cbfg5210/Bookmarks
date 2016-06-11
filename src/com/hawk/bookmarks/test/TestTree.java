package com.hawk.bookmarks.test;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.event.MouseEvent;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComboBox;

import javax.swing.JFrame;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import javax.swing.JScrollPane;

import javax.swing.JTree;

import javax.swing.tree.DefaultMutableTreeNode;

import javax.swing.tree.DefaultTreeCellEditor;

import javax.swing.tree.DefaultTreeCellRenderer;

import javax.swing.tree.DefaultTreeModel;

import javax.swing.tree.TreePath;

import javax.swing.tree.TreeSelectionModel;

public class TestTree extends JFrame implements MouseListener, ActionListener, MouseMotionListener {

	JTree tree;

	JPopupMenu popMenu; // 菜单
	JMenuItem addItem; // 各个菜单项
	JMenuItem delItem;
	JMenuItem editItem;

	public TestTree() {

		tree = new JTree();

		tree.setEditable(true);

		tree.getSelectionModel().setSelectionMode(

				TreeSelectionModel.SINGLE_TREE_SELECTION);

		tree.addMouseListener(this);
		tree.addMouseMotionListener(this);

		tree.setCellEditor(new DefaultTreeCellEditor(tree,

				new DefaultTreeCellRenderer()));

		getContentPane().add(tree);

		setSize(200, 200);

		// 添加菜单项以及为菜单项添加事件
		popMenu = new JPopupMenu();
		addItem = new JMenuItem("添加");
		addItem.addActionListener(this);
		delItem = new JMenuItem("删除");
		delItem.addActionListener(this);
		editItem = new JMenuItem("修改");
		editItem.addActionListener(this);
		popMenu.add(addItem);
		popMenu.add(delItem);
		popMenu.add(editItem);

		getContentPane().add(new JScrollPane(tree));

	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {

		// TreePath path = tree.getPathForLocation(e.getX(), e.getY()); //
		// 关键是这个方法的使用
		//
		// if (path == null) { // JTree上没有任何项被选中
		//
		// return;
		//
		// }
		//
		// tree.setSelectionPath(path);
		//
		// if (e.getButton() == 3) {
		//
		// popMenu.show(tree, e.getX(), e.getY());
		//
		// }

	}

	public void mouseReleased(MouseEvent e) {

	}

	// 弹出菜单的事件处理程序（需要实现ActionListener接口)

	public void actionPerformed(ActionEvent e) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree

				.getLastSelectedPathComponent(); // 获得右键选中的节点

		if (e.getSource() == addItem) {

			((DefaultTreeModel) tree.getModel()).insertNodeInto(

					new DefaultMutableTreeNode("TestTree"), node, node

							.getChildCount());

			tree.expandPath(tree.getSelectionPath());

		} else if (e.getSource() == delItem) {

			if (node.isRoot()) {

				return;

			}

			((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);

		} else if (e.getSource() == editItem) {

			tree.startEditingAtPath(tree.getSelectionPath());

		}

	}

	public static void main(String[] args) {

		TestTree frame = new TestTree();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		TreePath path = tree.getPathForLocation(e.getX(), e.getY()); // 关键是这个方法的使用

		if (path == null) { // JTree上没有任何项被选中
			if(popMenu.isShowing())popMenu.setVisible(false);
			return;
		}

		tree.setSelectionPath(path);

		popMenu.show(tree, e.getX(), e.getY());
	}

}