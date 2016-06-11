package com.hawk.bookmarks.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.hawk.bookmarks.event.MyMouseListener;
import com.hawk.bookmarks.model.LikeLynne;

public class Jtree_Float extends JFrame {
    private static final long serialVersionUID = 1L;
    JScrollPane jScrollPane1 = new JScrollPane();
    private JTree jtr;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;

    public Jtree_Float() {
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        rootNode=new DefaultMutableTreeNode("root");
        DefaultMutableTreeNode child1=new DefaultMutableTreeNode("child1");
        DefaultMutableTreeNode subchild11=new DefaultMutableTreeNode("subchild11");
        DefaultMutableTreeNode subchild12=new DefaultMutableTreeNode("subchild12");
        child1.add(subchild11);
        child1.add(subchild12);
        
        DefaultMutableTreeNode child2=new DefaultMutableTreeNode("child2");
        DefaultMutableTreeNode subchild21=new DefaultMutableTreeNode("subchild21");
        DefaultMutableTreeNode subchild22=new DefaultMutableTreeNode("subchild22");
        child2.add(subchild21);
        child2.add(subchild22);
        
        rootNode.add(child1);
        rootNode.add(child2);
        
        treeModel=new DefaultTreeModel(rootNode);
        jtr = new JTree(treeModel);
        
        jtr.addMouseListener(new MyMouseListener(){
        	
        });
        
        jScrollPane1.getViewport().add(jtr);
        add(jScrollPane1, BorderLayout.CENTER);
    }
    
    /**
	 * 获取当前节点
	 * 
	 * @return
	 */
	public DefaultMutableTreeNode getCurrentTreeNode() {
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = jtr.getSelectionPath();

		if (parentPath == null) {
			parentNode = rootNode;
		} else {
			parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		}
		return parentNode;
	}

    public static void main(String[] args) {
        Jtree_Float dad = new Jtree_Float();
        dad.setTitle("拖拽演示");
        dad.setSize(400, 300);
        dad.setVisible(true);
    }
}
