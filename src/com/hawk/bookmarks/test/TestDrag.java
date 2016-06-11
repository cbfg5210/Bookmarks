//package com.hawk.bookmarks.test;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.datatransfer.DataFlavor;
//import java.awt.datatransfer.Transferable;
//import java.awt.datatransfer.UnsupportedFlavorException;
//import java.awt.dnd.DnDConstants;
//import java.awt.dnd.DragGestureEvent;
//import java.awt.dnd.DragGestureListener;
//import java.awt.dnd.DragSource;
//import java.awt.dnd.DragSourceContext;
//import java.awt.dnd.DragSourceDragEvent;
//import java.awt.dnd.DragSourceDropEvent;
//import java.awt.dnd.DragSourceEvent;
//import java.awt.dnd.DragSourceListener;
//import java.awt.dnd.DropTarget;
//import java.awt.dnd.DropTargetDragEvent;
//import java.awt.dnd.DropTargetDropEvent;
//import java.awt.dnd.DropTargetEvent;
//import java.awt.dnd.DropTargetListener;
//import java.io.IOException;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextField;
//import javax.swing.JTree;
//import javax.swing.tree.DefaultMutableTreeNode;
//import javax.swing.tree.TreePath;
//
//public class TestDrag extends JFrame {
//    private static final long serialVersionUID = 1L;
//    JScrollPane jScrollPane1 = new JScrollPane();
//    JTextField jtf = new JTextField();
//
//    public TestDrag() {
//        getContentPane().setLayout(new BorderLayout());
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        jtf.setBackground(Color.yellow);
//        jtf.setSize(100, 80);
//        JPanel panel1 = new JPanel(new BorderLayout());
//        panel1.add(jtf, BorderLayout.CENTER);
//        JTree jtr = new JTree();
//        jScrollPane1.getViewport().add(jtr);
//        add(panel1, BorderLayout.SOUTH);
//        add(jScrollPane1, BorderLayout.CENTER);
//
//        DragSource dragSource = DragSource.getDefaultDragSource(); // 创建拖拽源
//        dragSource.createDefaultDragGestureRecognizer(jtr,
//                DnDConstants.ACTION_COPY_OR_MOVE, new MyDragGestureListener()); // 建立拖拽源和事件的联系
//        new DropTarget(jtf, new MyTargetListener());
//
//    }
//
//    public static void main(String[] args) {
//        TestDrag dad = new TestDrag();
//        dad.setTitle("拖拽演示");
//        dad.setSize(400, 300);
//        dad.setVisible(true);
//
//    }
//}
//
//class MyDragGestureListener implements DragGestureListener {
//    public void dragGestureRecognized(DragGestureEvent dge) {
//        // 将数据存储到Transferable中，然后通知组件开始调用startDrag()初始化
//        JTree tree = (JTree) dge.getComponent();
//        TreePath path = tree.getSelectionPath();
//        if (path != null) {
//            DefaultMutableTreeNode selection = (DefaultMutableTreeNode) path
//                    .getLastPathComponent();
//            MyTransferable dragAndDropTransferable = new MyTransferable(
//                    selection);
//            dge.startDrag(DragSource.DefaultCopyDrop, dragAndDropTransferable,
//                    new MySourceListener());
//        }
//    }
//
//}
//
//class MyTransferable implements Transferable {
//    private DefaultMutableTreeNode treeNode;
//
//    MyTransferable(DefaultMutableTreeNode treeNode) {
//        this.treeNode = treeNode;
//    }
//
//    static DataFlavor flavors[] = { DataFlavor.stringFlavor };
//
//    public DataFlavor[] getTransferDataFlavors() {
//        return flavors;
//    }
//
//    public boolean isDataFlavorSupported(DataFlavor flavor) {
//        // if (treeNode.getChildCount() == 0) {
//        // return true;
//        // }
//        return true;
//    }
//
//    public Object getTransferData(DataFlavor flavor)
//            throws UnsupportedFlavorException, IOException {
//
//        return treeNode;
//
//    }
//
//}
//
//class MySourceListener implements DragSourceListener {
//    public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
//        if (dragSourceDropEvent.getDropSuccess()) {
//            // 拖拽动作结束的时候打印出移动节点的字符串
//            int dropAction = dragSourceDropEvent.getDropAction();
//            if (dropAction == DnDConstants.ACTION_MOVE) {
//                System.out.println("MOVE: remove node");
//            }
//        }
//    }
//
//    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
//        DragSourceContext context = dragSourceDragEvent.getDragSourceContext();
//        int dropAction = dragSourceDragEvent.getDropAction();
//        if ((dropAction & DnDConstants.ACTION_COPY) != 0) {
//            context.setCursor(DragSource.DefaultCopyDrop);
//        } else if ((dropAction & DnDConstants.ACTION_MOVE) != 0) {
//            context.setCursor(DragSource.DefaultMoveDrop);
//        } else {
//            context.setCursor(DragSource.DefaultCopyNoDrop);
//        }
//    }
//
//    public void dragExit(DragSourceEvent dragSourceEvent) {
//    }
//
//    public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
//    }
//
//    public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {
//    }
//}
//
//class MyTargetListener implements DropTargetListener {
//    public void dragEnter(DropTargetDragEvent dtde) {
//
//    }
//
//    public void dragOver(DropTargetDragEvent dtde) {
//    }
//
//    public void dropActionChanged(DropTargetDragEvent dtde) {
//    }
//
//    public void dragExit(DropTargetEvent dte) {
//    }
//
//    public void drop(DropTargetDropEvent dtde) {
//        Transferable tr = dtde.getTransferable();// 使用该函数从Transferable对象中获取有用的数据
//        String str = "";
//        try {
//            if (tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
//                str = tr.getTransferData(DataFlavor.stringFlavor).toString();
//            }
//        } catch (IOException ex) {
//        } catch (UnsupportedFlavorException ex) {
//        }
//        System.out.println(str);
//        DropTarget target = (DropTarget) dtde.getSource();
//        JTextField filed = (JTextField) target.getComponent();
//        if (str != null && str != "") {
//            filed.setText(filed.getText() + str);
//        }
//    }
//}