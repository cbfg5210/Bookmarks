package com.hawk.bookmarks.test.mytestlist;

/*
Java Swing, 2nd Edition
By Marc Loy, Robert Eckstein, Dave Wood, James Elliott, Brian Cole
ISBN: 0-596-00408-7
Publisher: O'Reilly 
*/
// SwingListExample.java
//An example of the JList component in action. This program uses a custom
//renderer (BookCellRenderer.java) to show a list of books with icons of their
//covers.
//

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

public class SwingListExample extends JPanel {

  private BookEntry books[] = {
      new BookEntry("Ant: The Definitive Guide", "/images/camera_camera_background_highlighted.png"),
      new BookEntry("Database Programming with JDBC and Java",
          "/images/camera_camera_background.png"),
      new BookEntry("Developing Java Beans", "/images/msp_switch_2x_off.png"),
      new BookEntry("Developing JSP Custom Tag Libraries",
          "/images/camera_camera_background_highlighted.png"),
      new BookEntry("Java 2D Graphics", "/images/msp_switch_2x_on.png"),
      new BookEntry("Java and XML", "/images/music_more_highlighted.png"),
      new BookEntry("Java and XSLT", "/images/camera_camera_background_highlighted.png"),
      new BookEntry("Java and SOAP", "/images/camera_camera_background.png"),
      new BookEntry("Learning Java", "/images/msp_switch_2x_off.png") };

  private JList booklist = new JList(books);

  public SwingListExample() {
    setLayout(new BorderLayout());
    JButton button = new JButton("Print");
    button.addActionListener(new PrintListener());

    booklist = new JList(books);
    booklist.setCellRenderer(new BookCellRenderer());
    booklist.setVisibleRowCount(4);
    JScrollPane pane = new JScrollPane(booklist);

    add(pane, BorderLayout.NORTH);
    add(button, BorderLayout.SOUTH);
  }

  public static void main(String s[]) {
    JFrame frame = new JFrame("List Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new SwingListExample());
    frame.pack();
    frame.setVisible(true);
  }

  // An inner class to respond to clicks on the Print button
  class PrintListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      int selected[] = booklist.getSelectedIndices();
      System.out.println("Selected Elements:  ");

      for (int i = 0; i < selected.length; i++) {
        BookEntry element = (BookEntry) booklist.getModel()
            .getElementAt(selected[i]);
        System.out.println("  " + element.getTitle());
      }
    }
  }
}

class BookEntry {
  private final String title;

  private final String imagePath;

  private ImageIcon image;

  public BookEntry(String title, String imagePath) {
    this.title = title;
    this.imagePath = imagePath;
  }

  public String getTitle() {
    return title;
  }

  public ImageIcon getImage() {
    if (image == null) {
      image = new ImageIcon(SwingListExample.class.getResource(imagePath));
    }
    return image;
  }

  // Override standard toString method to give a useful result
  public String toString() {
    return title;
  }
}

class BookCellRenderer extends JLabel implements ListCellRenderer {
  private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

  public BookCellRenderer() {
    setOpaque(true);
    setIconTextGap(12);
  }

  public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {
    BookEntry entry = (BookEntry) value;
    setText(entry.getTitle());
    setIcon(entry.getImage());
    if (isSelected) {
      setBackground(HIGHLIGHT_COLOR);
      setForeground(Color.white);
    } else {
      setBackground(Color.white);
      setForeground(Color.black);
    }
    return this;
  }
}
