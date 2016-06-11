package com.hawk.bookmarks.ui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JWindow;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.hawk.bookmarks.api.LikeLynneApi;
import com.hawk.bookmarks.model.BrowserConfig;
import com.hawk.bookmarks.ui.bk26_5_27.ConfigBrowserPathDialog;
import com.hawk.bookmarks.utils.CheckUtil;
import com.hawk.bookmarks.utils.LogUtil;
import com.hawk.bookmarks.utils.common.RowMapper;
import com.hawk.bookmarks.utils.common.SqliteHelper;

public class SettingsUI extends JDialog {
	private static final String TAG = "SettingsUI";
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					new SettingsUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JTable table;
	private SqliteHelper sqliteHelper;
	private List<BrowserConfig> brsConfigs;

	public SettingsUI(JFrame parentUI) {
		super(parentUI,true);
		setTitle("开启/关闭对浏览器支持");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setSize(300, 220);

		JPanel contentPane = new JPanel();
		SpringLayout springLayout = new SpringLayout();
		contentPane.setLayout(springLayout);
		setContentPane(contentPane);

		// brsConfigs=SharedPrefUtil.getInstance().readList("brsConfigs",new
		// TypeToken<ArrayList<BrowserConfig>>(){}.getType());
		sqliteHelper = SqliteHelper.getInstance(LikeLynneApi.getInstance().getBookmarkPath());
		int reqResult = 1;
		try {
			brsConfigs = sqliteHelper.executeQuery("select * from brsconfig;", new RowMapper<BrowserConfig>() {
				@Override
				public BrowserConfig mapRow(ResultSet rs, int index) throws SQLException {
					// TODO Auto-generated method stub
					return getBrsConfig(rs);
				}
			});
//			LogUtil.i(TAG,"brsConfigs="+brsConfigs);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			JOptionPane.showMessageDialog(SettingsUI.this, "查询数据出错");
			reqResult = -1;
		}
		if (reqResult == -1) {
			return;
		}

		table = new JTable(getTableModel(brsConfigs));

		table.getColumn(table.getColumnName(1)).setCellRenderer(new ButtonRenderer());
		table.getColumn(table.getColumnName(1)).setCellEditor(new ButtonEditor(new JCheckBox()));

		springLayout.putConstraint(SpringLayout.NORTH, table, 10, SpringLayout.NORTH, contentPane);
		springLayout.putConstraint(SpringLayout.WEST, table, 0, SpringLayout.WEST, contentPane);
		springLayout.putConstraint(SpringLayout.EAST, table, 0, SpringLayout.EAST, contentPane);
		contentPane.add(table);

		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setVisible(true);
	}

	/**
	 * 生成表格模型
	 * 
	 * @param brsConfigs
	 * @return
	 */
	private TableModel getTableModel(List<BrowserConfig> brsConfigs) {
		int size = brsConfigs.size();
		String[][] brssInfos = new String[size][2];
		for (int i = 0; i < size; i++) {
			brssInfos[i][0] = brsConfigs.get(i).getBrowser();
			brssInfos[i][1] = brsConfigs.get(i).getBrowser();
		}
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setDataVector(brssInfos, new Object[] { "浏览器", "开启/关闭" });
		return tableModel;
	}

	/**
	 * 从ResultSet解析BrowserConfig数据
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private BrowserConfig getBrsConfig(ResultSet rs) throws SQLException {
		BrowserConfig browserConfig = new BrowserConfig();
		browserConfig.setBrowser(rs.getString("browser"));
		browserConfig.setPath(rs.getString("path"));
		browserConfig.setFileName(rs.getString("fileName"));
		browserConfig.setSupported(rs.getInt("isSupported"));
		browserConfig.setOpenStatus(rs.getInt("openStatus"));
		return browserConfig;
	}

	/**
	 * 添加按钮
	 * 在渲染器里边添加按钮的事件不会被触发
	 */
	class ButtonRenderer extends JButton implements TableCellRenderer {
		public ButtonRenderer() {
			setOpaque(true);
		}
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			BrowserConfig brsConfig = brsConfigs.get(row);
			if (null == brsConfig || (brsConfig.isSupported() == 0)) {
				setText("未支持");
			} else {
				if (brsConfig.getOpenStatus()==0||CheckUtil.isEmpty(brsConfig.getPath())) {
					setText("开启");
				} else {
					setText("关闭");
				}
			}
			return this;
		}
	}

	/**
	 * 添加事件
	 * 通过第一部已经为表格添加了默认的渲染器，但是还无法触发事件，
	 * 触发事件是没有反应的，因为在点击表格时，会触发表格的编辑事件，
	 * 而要想触发渲染的按钮的事件，还需要通过修改表格的默认编辑器来实现
	 */
	class ButtonEditor extends DefaultCellEditor {
		protected JButton button;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);

			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String btnTxt = button.getText();
//					LogUtil.i(TAG,"btnTxt="+btnTxt);
					
					if (btnTxt.equals("未支持")) {
						return;
					}
					if ("开启".equals(btnTxt)) {
						int result=onOpenSupportClick();
						if(result==1){
							button.setText("关闭");
						}
					} else if ("关闭".equals(btnTxt)) {
						 int result=onCloseSupportClick();
						 if(result==1){
							 button.setText("开启");
						 }
					}
				}
			});
		}
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			BrowserConfig brsConfig = brsConfigs.get(row);
			if (null == brsConfig || (brsConfig.isSupported() == 0)) {
				button.setText("未支持");
			} else {
				if (brsConfig.getOpenStatus()==0||CheckUtil.isEmpty(brsConfig.getPath())) {
					button.setText("开启");
				} else {
					button.setText("关闭");
				}
			}
			return button;
		}
	}

	/**
	 * 点击开启浏览器支持
	 * @return -1:not ok,1:ok
	 */
	private int onOpenSupportClick() {
		int selectedRow=table.getSelectedRow();
		String selectValue = (String) table.getValueAt(selectedRow, 0);
		ConfigBrowserPathDialog cbpd = new ConfigBrowserPathDialog(SettingsUI.this, selectValue);
		String dbPath = cbpd.getConfigResult();
		if (CheckUtil.isEmpty(dbPath)) {
			return -1;
		}
		LogUtil.i(TAG, "selectValue=" + selectValue + ";dpPath=" + dbPath);
		
		BrowserConfig browserConfig = brsConfigs.get(selectedRow);
		if (!dbPath.contains(browserConfig.getFileName())) {
			JOptionPane.showMessageDialog(SettingsUI.this, "书签数据库路径不正确");
			return -1;
		}
		String sqlUpdateBrsConfig = "update brsconfig set openStatus=1,path='" + dbPath + "' where browser='"
				+ browserConfig.getBrowser() + "';";
		int result = 0;
		try {
			result = sqliteHelper.executeUpdate(sqlUpdateBrsConfig);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(SettingsUI.this, "更新数据出错,无法开启对该浏览器的支持");
			System.out.println(e.getMessage());
		}
		LogUtil.i(TAG,"onOpenSupportClick,result="+result);
		if (result == 0) {
			return -1;
		}
//		table.getCellRenderer(selectedRow,1).
		brsConfigs.get(selectedRow).setPath(dbPath);
		return 1;
	}

	/**
	 * 关闭浏览器支持
	 * @return -1:not ok,1:ok
	 */
	private int onCloseSupportClick() {
		int selectedRow=table.getSelectedRow();
		BrowserConfig brsConfig = brsConfigs.get(selectedRow);
		String sqlUpdateBrsConfig = "update brsconfig set openStatus=0 where browser='" + brsConfig.getBrowser() + "';";
		int reqResult = 0;
		try {
			reqResult = sqliteHelper.executeUpdate(sqlUpdateBrsConfig);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(SettingsUI.this, "更新数据出错,无法关闭对该浏览器的支持");
		}
		LogUtil.i(TAG,"onCloseSupportClick,reqResult="+reqResult);
		if (reqResult == 0) {
			return -1;
		}
		brsConfigs.get(selectedRow).setPath("");
		return 1;
	}
}
