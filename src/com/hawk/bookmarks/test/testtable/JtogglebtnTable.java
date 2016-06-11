package com.hawk.bookmarks.test.testtable;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class JtogglebtnTable extends JFrame {
	public JtogglebtnTable() {
		// TODO Auto-generated constructor stub
		JTable table = new JTable(new DefaultTableModel(new Object[0][0],new Object[]{"",})) {
			// set all cells except first non-editable
			public boolean isCellEditable(int row, int column) {
				if (column == COLUMN_PREVIEW) {
					return true;
				} else if (column == COLUMN_SELECT) {
					return true;
				} else if (column == COLUMN_MAIN_CATEGORIE) {
					return super.isCellEditable(row, column);
				} else if (column == COLUMN_SUB_CATEGORIE) {
					return super.isCellEditable(row, column);
				}

				return false;
			}

			// set checkbox in the first column
			public Class getColumnClass(int col) {
				if (col == COLUMN_PREVIEW || col == COLUMN_SELECT) {
					return Boolean.class;
				} else {
					return super.getColumnClass(col);
				}
			}
		};

		ButtonEditor btnE = new ButtonEditor(new JCheckBox());
		table.getColumnModel().getColumn(0).setCellRenderer(btnE);
		table.getColumnModel().getColumn(0).setCellEditor(btnE);
	}
	
	class ButtonEditor extends DefaultCellEditor implements TableCellRenderer{
		  protected JToggleButton button;
		  
		  private int m_selectedRow = -1;
		  
		  public ButtonEditor(JCheckBox checkBox) {
		    super(checkBox);
		    button = new JToggleButton("");
		    button.setOpaque(true);
		    button.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        fireEditingStopped();
		      }
		    });
		  }
		  
		  /////////////////
		  public Component getTableCellRendererComponent(JTable table, Object value,
		      boolean isSelected, boolean hasFocus, int row, int column) {
		    if(m_selectedRow == row){
		      System.out.println("ButtonEditor.getTableCellRendererComponent() selected true ,row = " + row);
		      button.setSelected(true);  
		    }else{
		      System.out.println("ButtonEditor.getTableCellRendererComponent() selected false ,row = " + row);
		      button.setSelected(false);
		    }
		    button.revalidate();
		    button.repaint();
		    
		    return button;
		  }
		  
		  ////////////////////////
		  


		  public Component getTableCellEditorComponent(JTable table, Object value,
		      boolean isSelected, int row, int column) {
		      m_selectedRow = row;
		      
		      System.out.println("ButtonEditor.getTableCellEditorComponent() set row to " + row);
		    return button;
		  }


		  public boolean stopCellEditing() {
		    return super.stopCellEditing();
		  }

		  protected void fireEditingStopped() {
		    super.fireEditingStopped();
		  }
		}
}
