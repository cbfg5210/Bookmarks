package com.hawk.bookmarks.widget;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.hawk.bookmarks.event.MyMouseInputListener;
import com.hawk.bookmarks.model.LikeLynne;

public class BmarkSimpleTree extends JPanel {
	private static final long serialVersionUID = 1L;
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel treeModel;
	private JTree tree;
	private JScrollPane scrollPane;

	public BmarkSimpleTree(LikeLynne item) {
		rootNode = new DefaultMutableTreeNode(item);
		rootNode = addLiklynneNode(rootNode, item);
		treeModel = new DefaultTreeModel(rootNode);
		tree = new JTree(treeModel);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.setSelectionRow(0);

		scrollPane = new JScrollPane(tree);
		add(scrollPane);
	}
	
	public DefaultTreeModel getTreeModel(){
		return treeModel;
	}

	/**
	 * 重新初始化树
	 * @param lynne
	 */
	public void initTree(LikeLynne lynne) {
		tree.removeAll();
		rootNode = new DefaultMutableTreeNode(lynne);
		rootNode = addLiklynneNode(rootNode, lynne);
		treeModel.setRoot(rootNode);
		treeModel.reload();
	}
	
	/**
	 * 
	 * @param actionListener
	 */
	public void setRightKeyMenu(ActionListener actionListener) {
		JPopupMenu rightKeyMenu = new JPopupMenu();
		JMenuItem addBmarkItem = new JMenuItem("添加书签");
		addBmarkItem.setActionCommand("addbmark");
		addBmarkItem.addActionListener(actionListener);
		rightKeyMenu.add(addBmarkItem);
		rightKeyMenu.addSeparator();

		JMenuItem addFolderItem = new JMenuItem("添加文件夹");
		addFolderItem.setActionCommand("addfolder");
		addFolderItem.addActionListener(actionListener);
		rightKeyMenu.add(addFolderItem);
		rightKeyMenu.addSeparator();

		JMenuItem editItem = new JMenuItem("修改");
		editItem.setActionCommand("edit");
		editItem.addActionListener(actionListener);
		rightKeyMenu.add(editItem);
		rightKeyMenu.addSeparator();

		JMenuItem deleteItem = new JMenuItem("删除");
		deleteItem.setActionCommand("delete");
		deleteItem.addActionListener(actionListener);
		rightKeyMenu.add(deleteItem);

		tree.addMouseListener(new MyMouseInputListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (SwingUtilities.isRightMouseButton(e)) {
					TreePath path = tree.getPathForLocation(e.getX(), e.getY()); // 关键是这个方法的使用
					if (path == null)
						return;
					tree.setSelectionPath(path);
					rightKeyMenu.show(tree, e.getX(), e.getY());
				}
			}
		});
	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		// TODO Auto-generated method stub
		scrollPane.setPreferredSize(preferredSize);
	}

	/**
	 * 添加树节点
	 * 
	 * @param mainNode
	 * @param llynne
	 * @return
	 */
	private DefaultMutableTreeNode addLiklynneNode(DefaultMutableTreeNode mainNode, LikeLynne llynne) {
		List<LikeLynne> children = llynne.getChildren();
		if (null != children && children.size() > 0) {
			int size = children.size();
			for (int i = 0; i < size; i++) {
				LikeLynne child = children.get(i);
				DefaultMutableTreeNode treeChildNode = new DefaultMutableTreeNode(child);
				addLiklynneNode(treeChildNode, child);// !!!!
				mainNode.add(treeChildNode);
			}
		}
		return mainNode;
	}

	/**
	 * 获取当前节点
	 * 
	 * @return
	 */
	public DefaultMutableTreeNode getCurrentTreeNode() {
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = tree.getSelectionPath();

		if (parentPath == null) {
			parentNode = rootNode;
		} else {
			parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		}
		return parentNode;
	}

	/**
	 * Add child to the currently selected node.
	 * 
	 * @param child
	 * @return
	 */
	public DefaultMutableTreeNode addObject(Object child) {
		DefaultMutableTreeNode parentNode = null;
		parentNode = getCurrentTreeNode();

		LikeLynne newLlynne = (LikeLynne) child;

		DefaultMutableTreeNode newParentNode = addObject(parentNode, newLlynne);
		if (newLlynne.getType() == 2 && null != newLlynne.getChildren() && newLlynne.getChildren().size() > 0) {
			DefaultMutableTreeNode childrenNodes = addLiklynneNode(newParentNode, newLlynne);
			addObject(newParentNode, childrenNodes);
		}
		return parentNode;
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 * @return
	 */
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
		if (parent == null) {
			parent = rootNode;
		}
		// It is key to invoke this on the TreeModel, and NOT
		// DefaultMutableTreeNode
		treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

		return childNode;
	}
}