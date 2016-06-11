package com.hawk.bookmarks.test.testtable;

//Example from http://www.crionics.com/products/opensource/faq/swing_ex/SwingExamples.html

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
public class JButtonTableExample_bk extends JFrame {

	public JButtonTableExample_bk() {
		super("JButtonTable Example");

		DefaultTableModel dm = new DefaultTableModel();
		dm.setDataVector(new Object[][] { { "foo", "button 1" }, { "bar", "button 2" } },
				new Object[] { "浏览器", "开启/关闭" });

		JTable table = new JTable(dm);
		table.getColumn(table.getColumnName(1)).setCellRenderer(new ButtonRenderer());
		table.getColumn(table.getColumnName(1)).setCellEditor(new ButtonEditor(new JCheckBox()));
		JScrollPane scroll = new JScrollPane(table);
		getContentPane().add(scroll);
		setSize(400, 100);
		setVisible(true);
	}

	public static void main(String[] args) {
		JButtonTableExample_bk frame = new JButtonTableExample_bk();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}

/**
 * @version 1.0 11/09/98
 */

class ButtonRenderer extends JButton implements TableCellRenderer {

	public ButtonRenderer() {
		// setOpaque(true);
		setOpaque(false);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// if (isSelected) {
		// setForeground(table.getSelectionForeground());
		// setBackground(table.getSelectionBackground());
		// } else {
		// setForeground(table.getForeground());
		// setBackground(UIManager.getColor("Button.background"));
		// }
		String label = (value == null) ? "" : value.toString();
		setText(label);
		System.out.println("getTableCellRendererComponent,value=" + value.toString());
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
		// button.setOpaque(true);
		button.setOpaque(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// fireEditingStopped();
				JOptionPane.showMessageDialog(null, "btn");
			}
		});
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// if (isSelected) {
		// button.setForeground(table.getSelectionForeground());
		// button.setBackground(table.getSelectionBackground());
		// } else {
		// button.setForeground(table.getForeground());
		// button.setBackground(table.getBackground());
		// }
		label = (value == null) ? "" : value.toString();
		System.out.println("getTableCellEditorComponent,label=" + label);
		button.setText(label);
		return button;
	}
}
