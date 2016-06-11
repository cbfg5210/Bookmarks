package com.hawk.bookmarks.utils;

import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.hawk.bookmarks.api.ChromeBookmarkApi;
import com.hawk.bookmarks.api.FirefoxBookmarkApi;
import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.common.RowMapper;
import com.hawk.bookmarks.utils.common.SqliteHelper;
import com.hawk.bookmarks.utils.common.Utils;
import com.hawk.bookmarks.utils.firefoxtool.FfLikeLynneGetter;
import com.hawk.bookmarks.utils.likelynnetool.LikeLynneUtils;

public class DialogUtil {
	private static final String TAG="DialogUtil";
	/**
	 * 
	 * @return
	 */
	private static List<String>getSupportedBrowsers(){
		String dbPath=LikeLynneApi.getInstance().getBookmarkPath();
		String sqlGetSupportedBrowsers="select browser from brsconfig where isSupported=1 and openStatus=1 and path is not null;";
		try {
			List<String>supportedBrowsers=SqliteHelper.getInstance(dbPath).executeQuery(sqlGetSupportedBrowsers,new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int index) throws SQLException {
					// TODO Auto-generated method stub
					return rs.getString("browser");
				}
			});
			return supportedBrowsers;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从浏览器导入数据
	 */
	public static LikeLynne showImportFromBroswer(Component parentComponent,int parentId) {
		List<String>browsers=getSupportedBrowsers();
		if(null==browsers||browsers.size()==0){
			JOptionPane.showMessageDialog(parentComponent,"请先在设置中开启浏览器支持");
			return null;
		}
		int size=browsers.size();
		Object[] browserObjs =new Object[size];
		for(int i=0;i<size;i++){
			browserObjs[i]=browsers.get(i);
		}
		String selection = (String) JOptionPane.showInputDialog(parentComponent, "请选择浏览器：", "从浏览器导入系统", JOptionPane.PLAIN_MESSAGE, null,
				browserObjs, null);
		LogUtil.i(TAG,"selection=" + selection);
		if (null != selection) {
			if (selection.equals("firefox浏览器")) {
				// 从火狐浏览器读取书签数据
				LikeLynne likelynne =new FfLikeLynneGetter().getLikeLynne();
				if(null!=likelynne){
					return importIntoLikeLynne(likelynne,parentId);
				}
			} else if (selection.equals("chrome浏览器")) {
				// 从chrome浏览器读取书签数据
				LikeLynne likelynne =new ChromeBookmarkApi().parseToLikeLynne();
				if(null!=likelynne){
					return importIntoLikeLynne(likelynne,parentId);
				}
			} else if (selection.equals("edge浏览器")) {
				JOptionPane.showMessageDialog(null, "正在建设");
			}
		}
		return null;
	}
	
	/**
	 * 从likelynne导入数据到浏览器
	 * @param parentId
	 * @return
	 */
	public static LikeLynne showImportToBrowser(Component parentComponent){
		List<String>browsers=getSupportedBrowsers();
		if(null==browsers||browsers.size()==0){
			JOptionPane.showMessageDialog(parentComponent,"请先在设置中开启浏览器支持");
			return null;
		}
		int size=browsers.size();
		Object[] browserObjs =new Object[size];
		for(int i=0;i<size;i++){
			browserObjs[i]=browsers.get(i);
		}
		String selection = (String) JOptionPane.showInputDialog(parentComponent, "请选择浏览器：", "从系统导入浏览器", JOptionPane.PLAIN_MESSAGE, null,
				browserObjs, null);
		if (null != selection) {
			if (selection.equals("firefox浏览器")) {
				int resultCode=new FirefoxBookmarkApi().insertBookmarks();
				if(resultCode==200){
					JOptionPane.showMessageDialog(parentComponent,"从如影书签导入数据到火狐浏览器成功");
				}
			} else if (selection.equals("chrome浏览器")) {
				int resultCode=new ChromeBookmarkApi().insertBookmarks();
				if(resultCode==200){
					JOptionPane.showMessageDialog(parentComponent,"从如影书签导入数据到chrome浏览器成功");
				}
			} else if (selection.equals("edge浏览器")) {
				
			}
		}
		return null;
	}

	/**
	 * 把浏览器数据导入likelynne，导入成功则更新书签树
	 * 
	 * @param likelynne
	 */
	private static LikeLynne importIntoLikeLynne(LikeLynne likelynne,int parentId) {
		if (null == likelynne) {
			JOptionPane.showMessageDialog(null, "没有要导入的数据");
			return null;
		}
		// 获取parentId
//		int parentId = maty_bmarktree.getCurrentLikeLynne().getId();
		likelynne.setParent(parentId);
		// 保存数据到likelynne表
		int resultCode= LikeLynneApi.getInstance().addLikeLynne(likelynne);
		// 数据保存不成功
		if (resultCode!= 200) {
			return null;
		}
		// 数据导入成功的话则把新增的数据添加到书签树
//		maty_bmarktree.addObject(likelynne);
		return likelynne;
	}

	/**
	 * 新建书签
	 */
	public static LikeLynne showAddNewBookmarkDialog(Component parentComponent,int parentId) {
		JLabel bookmarkNameLab= new JLabel("书签名");
		JTextField bookmarkNameTxt = new JTextField();
		bookmarkNameTxt.setColumns(20);
		JLabel bookmarkUrlLab = new JLabel("书签链接");
		JTextField bookmarkUrlTxt = new JTextField();
		bookmarkUrlTxt.setColumns(20);
		
		Object[]views = {bookmarkNameLab, bookmarkNameTxt,bookmarkUrlLab,bookmarkUrlTxt};
		bookmarkUrlTxt.setText(Utils.getSysClipboardText());
		int result = JOptionPane.showConfirmDialog(parentComponent,views, "请输入书签信息", JOptionPane.OK_CANCEL_OPTION);

		if (result == JOptionPane.OK_OPTION) {
			String bookmarkName = bookmarkNameTxt.getText().trim();
			if ("".equals(bookmarkName)) {
				JOptionPane.showMessageDialog(parentComponent, "请输入书签名称");
				return null;
			}
			String bookmarkUrl =bookmarkUrlTxt.getText().trim();
			if ("".equals(bookmarkUrl)) {
				JOptionPane.showMessageDialog(parentComponent, "请输入书签链接");
				return null;
			}
			if(!Utils.isUrlFormat(bookmarkUrl)){
				JOptionPane.showMessageDialog(parentComponent, "请输入正确的链接地址");
				return null;
			}
			try {
				int lynneId =new LikeLynneUtils().getMaxId();
				if(lynneId<0){
					return null;
				}
				lynneId++;
				LikeLynne likelynne = new LikeLynne();
				likelynne.setTitle(bookmarkName);
				likelynne.setUrl(bookmarkUrl);
				likelynne.setType(1);
				likelynne.setParent(parentId);
				likelynne.setId(lynneId);

				int resultCode= LikeLynneApi.getInstance().addLikeLynne(likelynne);
				if (resultCode!= 200) {
					JOptionPane.showMessageDialog(parentComponent, "新建书签失败");
					return null;
				}
				return likelynne;
			}catch (Exception exp) {
				JOptionPane.showMessageDialog(parentComponent, "新建书签失败:" + exp.getMessage());
			}
		}
		return null;
	}

	public static LikeLynne showAddNewFolderDialog(Component parentComponent,List<LikeLynne>lynnes){
		JLabel parentLab = new JLabel("请选择父文件夹:");
		JComboBox<LikeLynne>parentFolders= new JComboBox<LikeLynne>();
		int size=lynnes.size();
		for(int i=0;i<size;i++){
			parentFolders.addItem(lynnes.get(i));
		}
		JLabel folderNameLab = new JLabel("请输入文件夹名字：");
		JTextField folderNameTxt = new JTextField();
		folderNameTxt.setColumns(20);
		
		Object[]views=new Object[]{parentLab,parentFolders,folderNameLab,folderNameTxt};
		int result = JOptionPane.showConfirmDialog(parentComponent,views, "新建文件夹", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String folderName=folderNameTxt.getText().trim();
			if("".equals(folderName)){
				JOptionPane.showMessageDialog(parentComponent,"新建文件夹失败：文件夹名字为空");
				return null;
			}
			LikeLynne parentLynne=(LikeLynne) parentFolders.getSelectedItem();
			int lynneId =new LikeLynneUtils().getMaxId();
			if(lynneId<0){
				return null;
			}
			lynneId++;
			
			LikeLynne likelynne = new LikeLynne();
			likelynne.setTitle(folderName);
			likelynne.setUrl(Utils.getUuid());
			likelynne.setType(2);
			likelynne.setParent(parentLynne.getId());
			likelynne.setId(lynneId);
			
			int resultCode= LikeLynneApi.getInstance().addLikeLynne(likelynne);
			if (resultCode!= 200) {
				JOptionPane.showMessageDialog(parentComponent, "新建文件夹失败");
			}
			return likelynne;
		}
		return null;
	}
	
	/**
	 * 新建文件夹
	 */
	public static LikeLynne showAddNewFolderDialog(Component parentComponent,int parentId) {
		String folderName =JOptionPane.showInputDialog(parentComponent,"请输入文件夹名字：","新建文件夹",JOptionPane.PLAIN_MESSAGE);
		if (null == folderName){
			return null;
		}
		if("".equals(folderName.trim())) {
			JOptionPane.showMessageDialog(parentComponent,"新建文件夹失败：文件夹名字为空");
			return null;
		}
		int lynneId =new LikeLynneUtils().getMaxId();
		if(lynneId<0){
			return null;
		}
		lynneId++;

		LikeLynne likelynne = new LikeLynne();
		likelynne.setTitle(folderName);
		likelynne.setUrl(Utils.getUuid());
		likelynne.setType(2);
		likelynne.setParent(parentId);
		likelynne.setId(lynneId);
		likelynne.setDateAdded(System.currentTimeMillis());
		likelynne.setChildren(new ArrayList<>());
		
		int resultCode= LikeLynneApi.getInstance().addLikeLynne(likelynne);
		if (resultCode!= 200) {
			JOptionPane.showMessageDialog(parentComponent, "新建文件夹失败");
			return null;
		}
		return likelynne;
	}
	
	public static LikeLynne showUpdateFolderDialog(Component parentComponent,LikeLynne theBookmark){
		JLabel foldersLab=new JLabel("父文件夹：");
		List<LikeLynne>folderLynnes=getLynneFolders();
		if(null==folderLynnes){
			return null;
		}
		JComboBox<LikeLynne>parentFolders=new JComboBox<>();
		int size=folderLynnes.size();
		for(int i=0;i<size;i++){
			LikeLynne item=folderLynnes.get(i);
			parentFolders.addItem(item);
			if(item.getId()==theBookmark.getParent()){//设置当前父文件夹为选中项
				parentFolders.setSelectedIndex(i);
			}
		}
		JLabel titleLab=new JLabel("文件夹名字：");
		JTextField titleTxt=new JTextField();
		titleTxt.setColumns(20);
		titleTxt.setText(theBookmark.getTitle());
		Object[]views=new Object[]{foldersLab,parentFolders,titleLab,titleTxt};
		int result=JOptionPane.showConfirmDialog(parentComponent,views,"修改文件夹",JOptionPane.OK_CANCEL_OPTION);
		if(result==JOptionPane.OK_OPTION){
			String title=titleTxt.getText().trim();
			if("".equals(title)){
				JOptionPane.showMessageDialog(parentComponent,"更新失败：文件夹名字为空");
				return null;
			}
			LikeLynne lynne=(LikeLynne) parentFolders.getSelectedItem();
			
			theBookmark.setTitle(title);
			theBookmark.setParent(lynne.getId());
			theBookmark.setDateAdded(System.currentTimeMillis());
			
			int resultCode=LikeLynneApi.getInstance().updateLikeLynne(theBookmark);
			if(resultCode==200){
				return theBookmark;
			}
		}
		return null;
	}
	
	public static LikeLynne showUpdateBookmarkDialog(Component parentComponent,LikeLynne theBookmark){
		JLabel foldersLab=new JLabel("父文件夹：");
		List<LikeLynne>folderLynnes=getLynneFolders();
		if(null==folderLynnes){
			return null;
		}
		JComboBox<LikeLynne>parentFolders=new JComboBox<>();
		int size=folderLynnes.size();
		for(int i=0;i<size;i++){
			LikeLynne item=folderLynnes.get(i);
			parentFolders.addItem(item);
			if(item.getId()==theBookmark.getParent()){//设置当前父文件夹为选中项
				parentFolders.setSelectedIndex(i);
			}
		}
		JLabel titleLab=new JLabel("书签名字：");
		JTextField titleTxt=new JTextField();
		titleTxt.setColumns(20);
		titleTxt.setText(theBookmark.getTitle());
		JLabel urlLab=new JLabel("书签链接：");
		JTextField urlTxt=new JTextField();
		urlTxt.setColumns(20);
		urlTxt.setText(theBookmark.getUrl());
		Object[]views=new Object[]{foldersLab,parentFolders,titleLab,titleTxt,urlLab,urlTxt};
		int result=JOptionPane.showConfirmDialog(parentComponent,views,"修改书签",JOptionPane.OK_CANCEL_OPTION);
		if(result==JOptionPane.OK_OPTION){
			String title=titleTxt.getText().trim();
			if("".equals(title)){
				JOptionPane.showMessageDialog(parentComponent,"更新失败：书签名字为空");
				return null;
			}
			String url=urlTxt.getText().trim();
			if("".equals(url)){
				JOptionPane.showMessageDialog(parentComponent,"更新失败：书签链接为空");
				return null;
			}
			if(!Utils.isUrlFormat(url)){
				JOptionPane.showMessageDialog(parentComponent,"更新失败：书签链接格式不正确");
				return null;
			}
			LikeLynne lynne=(LikeLynne) parentFolders.getSelectedItem();
			
			theBookmark.setTitle(title);
			theBookmark.setUrl(url);
			theBookmark.setParent(lynne.getId());
			theBookmark.setDateAdded(System.currentTimeMillis());
			
			int resultCode=LikeLynneApi.getInstance().updateLikeLynne(theBookmark);
			if(resultCode==200){
				return theBookmark;
			}
		}
		return null;
	}
	
	public static boolean showUpdateFolderDialog(LikeLynne theFolder){
		return false;
	}

	public static boolean showDeleteDialog(Component parentComponent,LikeLynne theLynne){
		int resultCode=JOptionPane.showConfirmDialog(parentComponent,"确定要删除‘"+theLynne.getTitle()+"’吗？","删除书签/文件夹",JOptionPane.OK_CANCEL_OPTION);
		if(resultCode==JOptionPane.OK_OPTION){
			int result=LikeLynneApi.getInstance().removeLikeLynne(theLynne);
			if(result!=200){
				return false;
			}
			return true;
		}
		return false;
	}
	
	public static List<LikeLynne>getLynneFolders(){
		String sqlGetFolders = "select id,title from likelynne where type=2 order by dateAdded ASC;";
		try {
			SqliteHelper sqliteHelper=SqliteHelper.getInstance(LikeLynneApi.getInstance().getBookmarkPath());
			List<LikeLynne>lynneFolders=sqliteHelper.executeQuery(sqlGetFolders, new RowMapper<LikeLynne>() {
				@Override
				public LikeLynne mapRow(ResultSet rs, int index) throws SQLException {
					// TODO Auto-generated method stub
					LikeLynne item = new LikeLynne();
					item.setId(rs.getInt("id"));
					item.setTitle(rs.getString("title"));
					return item;
				}
			});
			return lynneFolders;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "获取数据出错");
		}
		return null;
	}
}
