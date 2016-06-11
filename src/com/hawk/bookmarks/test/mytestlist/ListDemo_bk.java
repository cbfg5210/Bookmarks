package com.hawk.bookmarks.test.mytestlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import com.hawk.bookmarks.adapter.ConfigAdapter;
import com.hawk.bookmarks.model.BrowserConfig;

/* ListDemo.java requires no other files. */
public class ListDemo_bk extends JPanel {
	private JList<BrowserConfig> list;
	private DefaultListModel<BrowserConfig> listModel;

	public ListDemo_bk() {
		super(new BorderLayout());

		listModel = new DefaultListModel<BrowserConfig>();
		
		BrowserConfig item=new BrowserConfig();
		item.setBrowser("chrome");
		listModel.addElement(item);
		item=new BrowserConfig();
		item.setBrowser("firefox");
		listModel.addElement(item);
		item=new BrowserConfig();
		item.setBrowser("edge");
		listModel.addElement(item);
		
		// Create the list and put it in a scroll pane.
		list = new JList<BrowserConfig>(listModel);
		list.setCellRenderer(new ConfigAdapter());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		
		JScrollPane listScrollPane = new JScrollPane(list);

		add(listScrollPane, BorderLayout.CENTER);
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("ListDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		JComponent newContentPane = new ListDemo_bk();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	class myLable extends JLabel implements ListCellRenderer {
		myLable() {
			setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			setText(value.toString());
			Color background;
			
			if(isSelected){
				background=Color.BLUE;
			}else{
				background=Color.WHITE;
			}
			setBackground(background);

			return this;
		}
	}
}