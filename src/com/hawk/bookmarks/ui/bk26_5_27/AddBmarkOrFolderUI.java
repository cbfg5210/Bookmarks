package com.hawk.bookmarks.ui.bk26_5_27;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SpringLayout;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.event.MyMouseInputListener;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.common.Utils;
import com.hawk.bookmarks.utils.likelynnetool.LikeLynneUtils;
import com.hawk.bookmarks.widget.BmarkSimpleTree;

public class AddBmarkOrFolderUI extends JWindow implements ActionListener{
	private static final long serialVersionUID = 1L;
	private FloatUI parentFrame;
	private JTextField nameTxt,urlTxt;
	private BmarkSimpleTree bmarkSimpleTree;
//	private LikeLynne lynneNodes;
	private Point origin=new Point();
	
	public AddBmarkOrFolderUI(FloatUI parentFrame){
		super(parentFrame.floatFrame);
		this.parentFrame=parentFrame;
//		this.lynneNodes=parentFrame.llynne;
		this.parentFrame.addBmarkWindow.setVisible(false);
		
		createAndShowUI();
	}
	private void createAndShowUI(){
		setSize(new Dimension(350,500));
		JPanel contentPane=new JPanel();
		setContentPane(contentPane);
		SpringLayout springLayout=new SpringLayout();
		contentPane.setLayout(springLayout);
		
		JLabel titleLab=new JLabel("保存新书签");
		titleLab.setSize(350,20);
		MyMouseInputListener myMouseInputListener=new MyMouseInputListener(){
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				origin.x=e.getX();
				origin.y=e.getY();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				Point point=getLocation();
				setLocation(point.x + (e.getX() - origin.x), point.y + (e.getY() - origin.y));
			}
		};
		titleLab.addMouseListener(myMouseInputListener);
		titleLab.addMouseMotionListener(myMouseInputListener);
		
		springLayout.putConstraint(SpringLayout.NORTH,titleLab,10,SpringLayout.NORTH,contentPane);
		springLayout.putConstraint(SpringLayout.WEST,titleLab,150,SpringLayout.WEST,contentPane);
		contentPane.add(titleLab);
		
		JLabel nameLab = new JLabel("名字：");
		springLayout.putConstraint(SpringLayout.NORTH, nameLab, 20, SpringLayout.SOUTH, titleLab);
		springLayout.putConstraint(SpringLayout.WEST, nameLab, 20, SpringLayout.WEST, contentPane);
		contentPane.add(nameLab);

		JLabel urlLab = new JLabel("网址：");
		springLayout.putConstraint(SpringLayout.NORTH, urlLab, 20, SpringLayout.SOUTH, nameLab);
		springLayout.putConstraint(SpringLayout.WEST, urlLab, 20, SpringLayout.WEST, contentPane);
		contentPane.add(urlLab);
		
		nameTxt = new JTextField();
		nameTxt.setColumns(20);
		nameTxt.setText(parentFrame.addBmarkWindow.getNameTxt());
		springLayout.putConstraint(SpringLayout.NORTH, nameTxt, 0, SpringLayout.NORTH, nameLab);
		springLayout.putConstraint(SpringLayout.WEST, nameTxt, 5, SpringLayout.EAST, nameLab);
		contentPane.add(nameTxt);

		urlTxt = new JTextField();
		urlTxt.setColumns(20);
		urlTxt.setText(parentFrame.addBmarkWindow.getUrlTxt());
		springLayout.putConstraint(SpringLayout.NORTH, urlTxt, 0, SpringLayout.NORTH, urlLab);
		springLayout.putConstraint(SpringLayout.WEST, urlTxt, 5, SpringLayout.EAST, urlLab);
		contentPane.add(urlTxt);
		
//		bmarkSimpleTree=new BmarkSimpleTree(lynneNodes,false);
		bmarkSimpleTree=new BmarkSimpleTree(parentFrame.llynne,false);
		bmarkSimpleTree.setPreferredSize(new Dimension(300,300));
		springLayout.putConstraint(SpringLayout.NORTH, bmarkSimpleTree, 20, SpringLayout.SOUTH, urlLab);
		springLayout.putConstraint(SpringLayout.WEST, bmarkSimpleTree, 20, SpringLayout.WEST,contentPane);
		contentPane.add(bmarkSimpleTree);
		
		JButton addNewFolderBtn=new JButton("新建文件夹");
		addNewFolderBtn.setActionCommand("add");
		addNewFolderBtn.addActionListener(this);
		springLayout.putConstraint(SpringLayout.NORTH, addNewFolderBtn, 20, SpringLayout.SOUTH, bmarkSimpleTree);
		springLayout.putConstraint(SpringLayout.WEST, addNewFolderBtn, 20, SpringLayout.WEST,contentPane);		
		contentPane.add(addNewFolderBtn);
		
		JButton cancelBtn=new JButton("取消");
		cancelBtn.setActionCommand("cancel");
		cancelBtn.addActionListener(this);
		springLayout.putConstraint(SpringLayout.NORTH, cancelBtn,0, SpringLayout.NORTH, addNewFolderBtn);
		springLayout.putConstraint(SpringLayout.EAST, cancelBtn, -20, SpringLayout.EAST,contentPane);
		contentPane.add(cancelBtn);
		
		JButton saveBtn=new JButton("保存");
		saveBtn.setActionCommand("save");
		saveBtn.addActionListener(this);
		springLayout.putConstraint(SpringLayout.NORTH, saveBtn,0, SpringLayout.NORTH, addNewFolderBtn);
		springLayout.putConstraint(SpringLayout.EAST, saveBtn, -20, SpringLayout.WEST,cancelBtn);
		contentPane.add(saveBtn);
	}
	
	public static void main(String[] args) {
//		LikeLynne lynne=LikeLynneApi.getInstance().getLikeLynne();
//		AddBmarkOrFolderUI abofu=new AddBmarkOrFolderUI(lynne);
//		abofu.setAlwaysOnTop(true);
//		abofu.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command=e.getActionCommand();
		if(command.equals("cancel")){
			this.setVisible(false);
		}else if(command.equals("add")){
			String newFolderName=JOptionPane.showInputDialog(bmarkSimpleTree,"请输入新文件夹名字:",""+System.currentTimeMillis());
//			System.out.println("newFolderName="+newFolderName);
			if(null==newFolderName||"".equals(newFolderName)){
				return;
			}
			int lastId=new LikeLynneUtils().getMaxId();
			if(lastId<0){
				return;
			}
			LikeLynne parentNode=(LikeLynne) bmarkSimpleTree.getCurrentTreeNode().getUserObject();
			LikeLynne newFolder=new LikeLynne();
			newFolder.setId((lastId+1));
			newFolder.setParent(parentNode.getId());
			newFolder.setTitle(newFolderName);
			newFolder.setType(2);
			newFolder.setChildren(new ArrayList<>());
			newFolder.setDateAdded(System.currentTimeMillis());
			int resultCode=LikeLynneApi.getInstance().addLikeLynne(newFolder);
			if(resultCode==200){
				addNewLikeLynne(parentFrame.llynne, newFolder);
				bmarkSimpleTree.initTree(parentFrame.llynne,false);
				parentFrame.llynne=parentFrame.llynne;
				parentFrame.initBmarkWindow(parentFrame.llynne);
				parentFrame.addBmarkWindow.initComboFolders();
			}
		}else if(command.equals("save")){
			String name=nameTxt.getText();
			if("".equals(name)){
				JOptionPane.showMessageDialog(bmarkSimpleTree,"请输入书签名字");
				return;
			}
			String url=urlTxt.getText();
			if("".equals(url)){
				JOptionPane.showMessageDialog(AddBmarkOrFolderUI.this,"请输入书签链接");
				return;
			}
			if(!Utils.isUrlFormat(url)){
				JOptionPane.showMessageDialog(bmarkSimpleTree,"请输入有效书签链接");
				return;
			}
//			JOptionPane.showMessageDialog(null,"url="+url);
			int lastId=new LikeLynneUtils().getMaxId();
			if(lastId<0){
				return;
			}
			LikeLynne parentNode=(LikeLynne) bmarkSimpleTree.getCurrentTreeNode().getUserObject();
			LikeLynne newLikeLynne=new LikeLynne();
			newLikeLynne.setId((lastId+1));
			newLikeLynne.setParent(parentNode.getId());
			newLikeLynne.setTitle(name);
			newLikeLynne.setUrl(url);
			newLikeLynne.setType(1);
			newLikeLynne.setDateAdded(System.currentTimeMillis());
			int resultCode=LikeLynneApi.getInstance().addLikeLynne(newLikeLynne);
			if(resultCode==200){
				addNewLikeLynne(parentFrame.llynne,newLikeLynne);
				this.setVisible(false);
				parentFrame.llynne=parentFrame.llynne;
				parentFrame.initBmarkWindow(parentFrame.llynne);
			}
		}
	}
	
	/**
	 * 插入新数据
	 * @param lynne
	 * @param newLikeLynne
	 */
	private void addNewLikeLynne(LikeLynne lynne,LikeLynne newLikeLynne){
		if(newLikeLynne.getParent()==lynne.getId()){
			lynne.getChildren().add(newLikeLynne);
		}else{
			List<LikeLynne>children=lynne.getChildren();
			if(null!=children&&children.size()>0){
				int size=children.size();
				for(int i=0;i<size;i++){
					LikeLynne child=children.get(i);
					if(child.getType()==2){
						addNewLikeLynne(child,newLikeLynne);
					}
				}
			}
		}
	}
}
