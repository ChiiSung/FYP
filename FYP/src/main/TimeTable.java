package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import object.SQLConnect;

public class TimeTable {
	private JFrame frame;
	private JMenu menu, submenu;
	private JMenuItem i1, i2, i3;
	private JPanel leftPanel, rightPanel;
	
	//Date format
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SQLConnect sql;
	
	
	static Login_page login;

	public TimeTable() {
		initialize();
	}

	public TimeTable(Login_page login, SQLConnect sql) {
		this.login = login;
		this.sql = sql;
		initialize();
	}
	
	public JFrame getFrame(){	
		return frame;
	}
	
	private void initialize() {
		
		frame = new JFrame();
		frame.setForeground(Color.RED);
		frame.getContentPane().setBackground(new Color(240, 240, 240));
		frame.setResizable(true);
		frame.setSize(1280,720);
		frame.setTitle("Calendar System for FSKTM Student");
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(new BorderLayout());
		
		//Left side panel 
		leftPanel = new JPanel();	
		leftPanel();
		
		//Right side panel
        rightPanel = new JPanel();
        rightPanel();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        
        //Menu
  		JMenuBar mb = new JMenuBar();
  		menu = new JMenu("Menu");
  		submenu = new JMenu("SubMenu");
  		i1 = new JMenuItem("Setting");
  		i1.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				if(Color.black == leftPanel.getBackground()) {
  					leftPanel.setBackground(Color.white);
  				}else {
  					leftPanel.setBackground(Color.black);
  				}
  			}
  		});
  		i2 = new JMenuItem("Refresh");
  		i2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				initialize();
			}
		});
  		i3 = new JMenuItem("Logout");
  		i3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login.logout();
			}
		});
  		menu.add(i1);menu.add(i2);menu.add(i3);
  		mb.add(menu);menu.add(submenu);
  		frame.setJMenuBar(mb);
        
  		
        frame.add(splitPane, BorderLayout.CENTER);
        frame.setVisible(true);
		
        splitPane.setDividerLocation(0.2);
	}
	
	private void rightPanel() {
	}
	
	private void leftPanel() {
	}
	
}
