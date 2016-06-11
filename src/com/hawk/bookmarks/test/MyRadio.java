package com.hawk.bookmarks.test;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

class MyRadio{
    private JFrame f=new JFrame("welcome to use!");
    private Container cont=f.getContentPane();
    private JComboBox jcb1=null;
//    private JComboBox jcb2=null;
    public MyRadio(){
        this.f.setLayout(new GridLayout(2,2));
        String nations[]={"China","Brazil","America","Korean","Italy","France"
    };
        this.jcb1=new JComboBox(nations);
        jcb1.setMaximumRowCount(3);
        cont.add(this.jcb1);
        this.f.setSize(300,150);
        this.f.setVisible(true);
        this.f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent arg0){
                System.exit(1);
            }
        });
    }
    public static void main(String[] args) {
        new MyRadio();
        
    }
}