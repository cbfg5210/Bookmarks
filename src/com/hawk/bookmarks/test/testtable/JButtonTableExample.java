package com.hawk.bookmarks.test.testtable;

//Example from http://www.crionics.com/products/opensource/faq/swing_ex/SwingExamples.html

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * @version 1.0 11/09/98
 */
public class JButtonTableExample extends JFrame {
	public ArrayList<String>supportBrss;
	
	public JButtonTableExample() {
		super("开启/关闭对浏览器支持");

		supportBrss=new ArrayList<>();
		supportBrss.add("chrome");
		
		DefaultTableModel dm = new DefaultTableModel();
		dm.setDataVector(new Object[][] { 
			{ "chrome", "chrome" },
			{ "firefox", "firefox" },
			{ "edge", "edge" }
			},
				new Object[] { "浏览器", "开启/关闭" });

		JTable table = new JTable(dm);
		table.getColumn(table.getColumnName(1)).setCellRenderer(new ButtonRenderer());
		table.getColumn(table.getColumnName(1)).setCellEditor(new ButtonEditor(new JCheckBox()));
		JScrollPane scroll = new JScrollPane(table);
		getContentPane().add(scroll);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {

		JButtonTableExample frame = new JButtonTableExample();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
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
			String label=(value == null) ? "" : value.toString();
			setActionCommand(label);
			if(supportBrss.contains(label)){
				setText("关闭");
			}else{
				setText("开启");
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
					JOptionPane.showMessageDialog(null,button.getActionCommand());
				}
			});
		}
		
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			label = (value == null) ? "" : value.toString();
			if(supportBrss.contains(label)){
				button.setText("关闭");
			}else{
				button.setText("开启");
			}
			
			button.setActionCommand(label);
			return button;
		}
		
		// 这个方法不能去掉，不然按钮的文字会变成false
		public Object getCellEditorValue() {
			return new String(label);
		}
	}
}
