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

import com.hawk.bookmarks.model.LikeLynne;

public class TestDrag2 extends JFrame {
    private static final long serialVersionUID = 1L;
    JScrollPane jScrollPane1 = new JScrollPane();
    private JTree jtr;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;

    public TestDrag2() {
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
        
        jScrollPane1.getViewport().add(jtr);
        add(jScrollPane1, BorderLayout.CENTER);

        DragSource dragSource = DragSource.getDefaultDragSource(); // 创建拖拽源
        dragSource.createDefaultDragGestureRecognizer(jtr,
                DnDConstants.ACTION_COPY_OR_MOVE, new MyDragGestureListener()); // 建立拖拽源和事件的联系
        new DropTarget(jtr, new MyDropTargetListener() {
			@Override
			public void drop(DropTargetDropEvent dtde) {
				// TODO Auto-generated method stub
				Transferable tr = dtde.getTransferable();// 使用该函数从Transferable对象中获取有用的数据
		        String str = "";
		        try {
		            if (tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
		                str = tr.getTransferData(DataFlavor.stringFlavor).toString();
		            }
		        } catch (IOException ex) {
		        } catch (UnsupportedFlavorException ex) {
		        }
		        System.out.println(str);
		        DropTarget target = (DropTarget) dtde.getSource();
		        
		        DefaultMutableTreeNode dd=getCurrentTreeNode();
		        String ll=(String) dd.getUserObject();
		        JOptionPane.showMessageDialog(null,""+ll);
			}
		});
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
        TestDrag2 dad = new TestDrag2();
        dad.setTitle("拖拽演示");
        dad.setSize(400, 300);
        dad.setVisible(true);

    }
}

class MyDragGestureListener implements DragGestureListener {
    public void dragGestureRecognized(DragGestureEvent dge) {
        // 将数据存储到Transferable中，然后通知组件开始调用startDrag()初始化
        JTree tree = (JTree) dge.getComponent();
        TreePath path = tree.getSelectionPath();
        if (path != null) {
            DefaultMutableTreeNode selection = (DefaultMutableTreeNode) path
                    .getLastPathComponent();
            MyTransferable dragAndDropTransferable = new MyTransferable(
                    selection);
            dge.startDrag(DragSource.DefaultCopyDrop, dragAndDropTransferable,
                    new MySourceListener());
        }
    }
}

class MyTransferable implements Transferable {
    private DefaultMutableTreeNode treeNode;

    MyTransferable(DefaultMutableTreeNode treeNode) {
        this.treeNode = treeNode;
    }

    static DataFlavor flavors[] = { DataFlavor.stringFlavor };

    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        // if (treeNode.getChildCount() == 0) {
        // return true;
        // }
        return true;
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {

        return treeNode;

    }

}

class MySourceListener implements DragSourceListener {
    public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
        if (dragSourceDropEvent.getDropSuccess()) {
            // 拖拽动作结束的时候打印出移动节点的字符串
            int dropAction = dragSourceDropEvent.getDropAction();
            if (dropAction == DnDConstants.ACTION_MOVE) {
                System.out.println("MOVE: remove node");
            }
        }
    }

    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
        DragSourceContext context = dragSourceDragEvent.getDragSourceContext();
        int dropAction = dragSourceDragEvent.getDropAction();
        if ((dropAction & DnDConstants.ACTION_COPY) != 0) {
            context.setCursor(DragSource.DefaultCopyDrop);
        } else if ((dropAction & DnDConstants.ACTION_MOVE) != 0) {
            context.setCursor(DragSource.DefaultMoveDrop);
        } else {
            context.setCursor(DragSource.DefaultCopyNoDrop);
        }
    }

    public void dragExit(DragSourceEvent dragSourceEvent) {
    }

    public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
    }

    public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {
    }
}