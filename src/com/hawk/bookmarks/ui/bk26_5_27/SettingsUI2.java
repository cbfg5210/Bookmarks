package com.hawk.bookmarks.ui.bk26_5_27;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.model.BrowserConfig;
import com.hawk.bookmarks.model.LikeLynne;
import com.hawk.bookmarks.utils.LogUtil;
import com.hawk.bookmarks.utils.common.RowMapper;
import com.hawk.bookmarks.utils.common.SqliteHelper;

public class SettingsUI2 extends JFrame {
	private static final String TAG="SettingsUI";
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new SettingsUI2();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private JTable table;
	private SqliteHelper sqliteHelper;
	private List<BrowserConfig>brsConfigs;
	
	public SettingsUI2() {
		super("开启/关闭对浏览器支持");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setSize(300,220);
		
		JPanel contentPane=new JPanel();
		SpringLayout springLayout=new SpringLayout();
		contentPane.setLayout(springLayout);
		setContentPane(contentPane);

//		brsConfigs=SharedPrefUtil.getInstance().readList("brsConfigs",new TypeToken<ArrayList<BrowserConfig>>(){}.getType());
		sqliteHelper=SqliteHelper.getInstance(LikeLynneApi.getInstance().getBookmarkPath());
		int reqResult=1;
		try {
			brsConfigs=sqliteHelper.executeQuery("select * from brsconfig;",new RowMapper<BrowserConfig>() {
				@Override
				public BrowserConfig mapRow(ResultSet rs, int index) throws SQLException {
					// TODO Auto-generated method stub
					return getBrsConfig(rs);
				}
			});
//			System.out.println("brsConfigs="+brsConfigs);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null,"查询数据出错");
			reqResult=-1;
		}
		if(reqResult==-1){
			return;
		}
		
		table = new JTable(getTableModel(brsConfigs));
		
		table.getColumn(table.getColumnName(1)).setCellRenderer(new ButtonRenderer());
		table.getColumn(table.getColumnName(1)).setCellEditor(new ButtonEditor(new JCheckBox()));
		
		springLayout.putConstraint(SpringLayout.NORTH,table,10,SpringLayout.NORTH,contentPane);
		springLayout.putConstraint(SpringLayout.WEST,table,0,SpringLayout.WEST,contentPane);
		springLayout.putConstraint(SpringLayout.EAST,table,0,SpringLayout.EAST,contentPane);
//		springLayout.putConstraint(SpringLayout.SOUTH,table,0,SpringLayout.SOUTH,contentPane);
		contentPane.add(table);
		
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setVisible(true);
	}
	
	/**
	 * 生成表格模型
	 * @param brsConfigs
	 * @return
	 */
	private TableModel getTableModel(List<BrowserConfig>brsConfigs){
		int size=brsConfigs.size();
		String[][]brssInfos=new String[size][2];
		for(int i=0;i<size;i++){
			brssInfos[i][0]=brsConfigs.get(i).getBrowser();
			brssInfos[i][1]=brsConfigs.get(i).getBrowser();
		}
		DefaultTableModel tableModel=new DefaultTableModel(); 
		tableModel.setDataVector(brssInfos,new Object[] { "浏览器", "开启/关闭" });
		return tableModel;
	}
	
	/**
	 * 查找获取对应的BrowserConfig
	 * @param brsName
	 * @return
	 */
	private int getBrsConfigIndxByName(String brsName){
		int size=brsConfigs.size();
		for(int i=0;i<size;i++){
			if(brsConfigs.get(i).getBrowser().equals(brsName)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 从ResultSet解析BrowserConfig数据
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private BrowserConfig getBrsConfig(ResultSet rs) throws SQLException{
		BrowserConfig browserConfig=new BrowserConfig();
		browserConfig.setBrowser(rs.getString("browser"));
		browserConfig.setPath(rs.getString("path"));
		browserConfig.setFileName(rs.getString("fileName"));
		browserConfig.setSupported(rs.getInt("isSupported"));
		return browserConfig;
	}
	
	/**
	 * @version 1.0 11/09/98
	 */

	class ButtonRenderer extends JButton implements TableCellRenderer {
		public ButtonRenderer() {
			setOpaque(true);
		}
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			String label = (value == null) ? "" : value.toString();
			setActionCommand(label);
			
			int brsConfigIndx=getBrsConfigIndxByName(label);
			BrowserConfig brsConfig=null;
			if(brsConfigIndx!=-1){
				brsConfig=brsConfigs.get(brsConfigIndx);
			}
			if(null==brsConfig||(brsConfig.isSupported()==0)){
				setText("未支持");
			}else {
				if(null==brsConfig.getPath()||brsConfig.getPath().equals(""))setText("开启");
				else setText("关闭");
			}
			return this;
		}
	}

	/**
	 * @version 1.0 11/09/98
	 */

	class ButtonEditor extends DefaultCellEditor {
		protected JButton button;
		private String label;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String btnTxt=button.getText();
					if(btnTxt.equals("未支持"))return;
					if(btnTxt.equals("开启")){
						ConfigBrowserPathDialog cbpd=new ConfigBrowserPathDialog(SettingsUI2.this,button.getActionCommand());
						String dbPath=cbpd.getConfigResult();
						if(null==dbPath){
							return;
						}
						
						
//						openSupportForBrowser(button.getActionCommand(), repPath);
					}else if(btnTxt.equals("关闭")){
						closeSupportForBrowser(button.getActionCommand());
					}
				}
			});
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			label = (value == null) ? "" : value.toString();
			
			int brsConfigIndx=getBrsConfigIndxByName(label);
			BrowserConfig brsConfig=null;
			if(brsConfigIndx!=-1){
				brsConfig=brsConfigs.get(brsConfigIndx);
			}
			if(null==brsConfig||(brsConfig.isSupported()==0)){
				button.setText("未支持");
			}else {
				if(null==brsConfig.getPath()||brsConfig.getPath().equals("")){
					button.setText("开启");
				}else{
					button.setText("关闭");
				}
			}

			button.setActionCommand(label);
			return button;
		}

		// 这个方法不能去掉，不然按钮的文字会变成false
		public Object getCellEditorValue() {
			return new String(label);
		}
	}
	
	private void openSupportForBrowser(String brs,String path){
		int brsConfigIndx=getBrsConfigIndxByName(brs);
		BrowserConfig browserConfig=brsConfigs.get(brsConfigIndx);
		if(!path.contains(browserConfig.getFileName())){
			JOptionPane.showMessageDialog(null,"书签数据库路径不正确");
			return;
		}
		String sqlUpdateBrsConfig="update brsconfig set path='"+path+"' where browser='"+brs+"';";
		int result=0;
		try {
			result=sqliteHelper.executeUpdate(sqlUpdateBrsConfig);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"更新数据出错,无法开启对该浏览器的支持");
			System.out.println(e.getMessage());
		}
		if(result==0){
			return;
		}
		brsConfigs.get(brsConfigIndx).setPath(path);
	}
	
	private void closeSupportForBrowser(String brs){
		int brsConfigIndx=getBrsConfigIndxByName(brs);
		String sqlUpdateBrsConfig="update brsconfig set path='' where browser='"+brs+"';";
		int reqResult=0;
		try {
			reqResult=sqliteHelper.executeUpdate(sqlUpdateBrsConfig);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"更新数据出错,无法关闭对该浏览器的支持");
		}
		System.out.println("reqResult="+reqResult);
		if(reqResult==0){
			return;
		}
		brsConfigs.get(brsConfigIndx).setPath("");
	}
}
