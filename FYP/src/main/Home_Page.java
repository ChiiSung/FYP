package main;

import object.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.toedter.calendar.JDateChooser;

public class Home_Page {
	private JFrame frame;
	private JMenu menu, submenu;
	private JMenuItem i1, i2, i3;
	private JPanel leftPanel, rightPanel;
	
	//Date format
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Date pageDate;
	private Date today = new Date();
	
	//variable for calendar
	private JCheckBox taskCb;
	private JCheckBox assignmentCb;
	private JCheckBox rpTaskCb;
	
	private SQLConnect sql;
	static Login_page login;

	public Home_Page() {
		initialize();
	}

	public Home_Page(Login_page login, SQLConnect sql) {
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(new BorderLayout());
		
		//Left side panel 
		leftPanel = new JPanel();
		leftPanel();
		
		//Right side panel
        rightPanel = new JPanel();
        pageDate = new Date();
        try {
			pageDate = sdf.parse(sdf.format(pageDate));
			today = sdf.parse(sdf.format(today));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
        rightPanel(pageDate);
        
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
        
        //change the font size and type of font
        Font newFont = new Font("Verdana", Font.BOLD, 12);
        login.updateFonts(frame, newFont);
	}
	
	private void rightPanel(Date day) {
		rightPanel.setLayout(new BorderLayout());
		JPanel selectedCalendar = new JPanel();
		selectedCalendar.setLayout(new GridLayout(2,7, 10, 10));
		
	    SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
		
	    //Re-draw the calendar to previous month 
		JButton lastMonth = new JButton("Previous");
		lastMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//previous month
				pageDate.setMonth(pageDate.getMonth()-1);
				resetCalendar(rightPanel);
			}
		});
		//Re-draw the calendar to next month
		JButton nextMonth = new JButton("Next");
		nextMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//next month
				pageDate.setMonth(pageDate.getMonth()+1);
				resetCalendar(rightPanel);
			}
		});
		
		JLabel nowMonth = new JLabel(formatter.format(day));
		nowMonth.setHorizontalAlignment(SwingConstants.CENTER);
		
		String[] week = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		for(int i = 0; i < 14 ; i++) {
			if(i == 0) {
				selectedCalendar.add(lastMonth);
			}else if(i == 3) {
				selectedCalendar.add(nowMonth);
			}else if(i == 6) {
				selectedCalendar.add(nextMonth);
			}else if(i >= 6) {
				JLabel weekPl = new JLabel(week[i-7]);
				weekPl.setHorizontalAlignment(SwingConstants.CENTER);
				selectedCalendar.add(weekPl);
			}else {
				JPanel emptyPl = new JPanel();
				selectedCalendar.add(emptyPl);
			}
		}
		
		rightPanel.add(selectedCalendar, BorderLayout.NORTH);
		calendar(day);
	}
	
	private void calendar(Date day) {
        new Task().readTask(sql, login.user, login.user.getUserID());
        
		JPanel calendar = new JPanel(new GridLayout(5,7));
		JPanel[] dayPl = new JPanel[35];
		
		//Find range task for this month
		if(login.user.getTask() != null)
			login.user.findNowArrayTask(day);
		
		//Calculate first day in calendar
		Date calDay = (Date)day.clone();
		calDay.setDate(1);
		int x = calDay.getDay();//Record the day of first day(exp: monday)
		calDay.setDate(-x+1);
		
		//Build calendar panel
		for(int i = 0; i < 35 ; i++) {
			JLabel dayLb = new JLabel(""+calDay.getDate());
			dayLb.setHorizontalAlignment(SwingConstants.CENTER);
			dayLb.setMaximumSize(new Dimension(200,50));
			
			//Change today to another color
			Color cl;
			if(calDay.compareTo(today) == 0) {
				cl = new Color(0,140,255);
				dayLb.setForeground(Color.white);
			}else{
				cl = Color.white;
			}
			dayPl[i] = new JPanel();
			dayPl[i].setLayout(new BoxLayout(dayPl[i], BoxLayout.Y_AXIS));
			dayPl[i].setBackground(cl);
			dayPl[i].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			dayPl[i].add(dayLb);
			dayPl[i].setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			dayPl[i].putClientProperty("Date", calDay.clone());
			dayPl[i].addMouseListener(new MouseListener() {
				public void mouseReleased(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {
					e.getComponent().setBackground(cl);
					((JPanel)e.getComponent()).setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				}
				public void mouseEntered(MouseEvent e) {
					e.getComponent().setBackground(Color.LIGHT_GRAY);
					((JPanel)e.getComponent()).setBorder(BorderFactory.createLineBorder(Color.black));
				}
				public void mouseClicked(MouseEvent e) {
					Date day = (Date)((JPanel)e.getComponent()).getClientProperty("Date");
					addTaskInformation(day);
				}
			});
			
			//add task into calendar
			int a = 0, b = 0;
			while(a < login.user.getTask().length && taskCb.isSelected()){
				if(calDay.compareTo(login.user.getTask()[a].getDueDate()) == 0) {
					Color taskCl;
					if(login.user.getTask()[a].getCompleted()) {
						taskCl = null;
					}else {
						taskCl = Color.GREEN;
					}
					JLabel task = new JLabel(login.user.getTask()[a].getTaskTitle());
					task.setHorizontalAlignment(SwingConstants.CENTER);
					task.setPreferredSize(new Dimension(dayPl[i].getWidth()-10,10));
					task.setBackground(taskCl);
					task.setOpaque(true);
					task.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
					task.putClientProperty("TaskArray", a);
					task.addMouseListener(new MouseListener() {
						public void mouseReleased(MouseEvent e) {
						}
						public void mousePressed(MouseEvent e) {
						}
						public void mouseExited(MouseEvent e) {
							task.setBackground(taskCl);
						}
						public void mouseEntered(MouseEvent e) {
							task.setBackground(Color.PINK);
						}
						public void mouseClicked(MouseEvent e) {
							editTaskInformation(login.user.getTask()[(int)((JLabel)e.getComponent()).getClientProperty("TaskArray")]);
						}
					});
					dayPl[i].add(task);
					b++;
				}
				a++;
			}
			//if task in a day more than 3, the scrolling function will insert
			if(b > 3) {
				dayPl[i].setPreferredSize(new Dimension(rightPanel.getWidth()/5, (a*8)+50));
				JScrollPane scrollPane = new JScrollPane(dayPl[i]);
	            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//	            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				calendar.add(scrollPane);
			}else {
				calendar.add(dayPl[i]);
			}
			calDay.setDate(calDay.getDate()+1);
		}
		
		rightPanel.add(calendar, BorderLayout.CENTER);
	}
	
	
	private void leftPanel() {
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		JLabel usernameLb = new JLabel(login.user.getUsername());
		usernameLb.setMaximumSize(new Dimension(1000,60));
		
		//Upside panel
		JPanel upPl = new JPanel();
		upPl.setLayout(new GridBagLayout());
		upPl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		JTextField searchTF = new JTextField();
		searchTF.setPreferredSize(new Dimension((int)(1280*0.15), 20));
		JPanel displaySearchPl = new JPanel();
		displaySearchPl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		displaySearchPl.setLayout(new BoxLayout(displaySearchPl, BoxLayout.Y_AXIS));
		JLabel nothing = new JLabel("Nothing found...");
		displaySearchPl.add(nothing);
		searchTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {
				displaySearchTask(displaySearchPl, nothing, searchTF);
			}
			public void keyPressed(KeyEvent e) {}
		});
		searchTF.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				 if (searchTF.getText().isEmpty()) {
					 searchTF.setText("Search by typing title");
					 searchTF.setForeground(Color.GRAY);
				 }
			}
			public void focusGained(FocusEvent e) {
				if (searchTF.getText().equals("Search by typing title")) {
					searchTF.setText("");
					searchTF.setForeground(Color.BLACK);
                }
			}
		});
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
		upPl.add(searchTF, gbc);
		
		gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
		upPl.add(displaySearchPl, gbc);
		
		//Down side panel
		JPanel downPl = new JPanel();
		downPl.setLayout(new BoxLayout(downPl, BoxLayout.Y_AXIS));
		taskCb = new JCheckBox("Task");
		taskCb.setSelected(true);
		taskCb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetCalendar(rightPanel);
			}
		});
		assignmentCb = new JCheckBox("Assignment");
		assignmentCb.setSelected(true);
		assignmentCb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetCalendar(rightPanel);
			}
		});
		rpTaskCb = new JCheckBox("Repeating task");
		rpTaskCb.setSelected(true);
		rpTaskCb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetCalendar(rightPanel);
			}
		});
		downPl.add(taskCb);downPl.add(assignmentCb);downPl.add(rpTaskCb);
		JButton addTaskBt = new JButton("Add Task");
		addTaskBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addTaskInformation(pageDate);
			}
		});
		JButton editTimetableBt = new JButton("Edit Timetable");
		editTimetableBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TimeTable tt = new TimeTable(login, sql);
			}
		});
		JButton viewHistoryBt = new JButton("View History");
		downPl.add(addTaskBt);downPl.add(editTimetableBt);downPl.add(viewHistoryBt);
		
		leftPanel.add(usernameLb);
		leftPanel.add(upPl);
		leftPanel.add(downPl);
	}
	
	//build a task information page for use to add task
	private void addTaskInformation(Date date) {
		Date endRepeatDate = (Date)date.clone();
		endRepeatDate.setDate(endRepeatDate.getDate()+1);
		float align = Component.LEFT_ALIGNMENT;
		
		//container of component of task information form
		JDialog ti = new JDialog();
		ti.setTitle("Task Information");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		
		//component in task information form
		JLabel titleLb = new JLabel("Task Information", SwingConstants.CENTER);
		JLabel taskTypeLb = new JLabel("Task Type:");
		taskTypeLb.setAlignmentX(align);
		JComboBox<String> taskTypeCb = new JComboBox<>(new String[] {"Task", "Assignment"});
		taskTypeCb.setMaximumSize(new Dimension(500, 50));
		JLabel taskTitleLb = new JLabel("Task Title");
		taskTitleLb.setAlignmentX(align);
		JTextField tasktitleTF = new JTextField();
		tasktitleTF.setMaximumSize(new Dimension(500, 50));
		JLabel duedateLb = new JLabel("Due Date:");
		duedateLb.setAlignmentX(align);
		JDateChooser duedateDC = new JDateChooser();
		duedateDC.setDateFormatString("yyyy-MM-dd");
		duedateDC.setDate(date);
		duedateDC.setMaximumSize(new Dimension(500, 50));
		JLabel repeatLb = new JLabel("Repeat Task:");
		repeatLb.setAlignmentX(align);
		JComboBox<String> repeatTypeCb = new JComboBox<>(new String[] {"None", "Daily", "Weekly", "Monthly"});
		repeatTypeCb.setMaximumSize(new Dimension(500, 50));
		
		//repeat task component
		JLabel repeatFrequancyLb = new JLabel("Repeat Frequancy");
		repeatFrequancyLb.setAlignmentX(align);
		JTextField repeatFrequancyTF = new JTextField();
		repeatFrequancyTF.setMaximumSize(new Dimension(500, 50));
		JLabel enddateLb = new JLabel("End Repeat Date:");
		enddateLb.setAlignmentX(align);
		JDateChooser enddateDC = new JDateChooser();
		enddateDC.setDateFormatString("yyyy-MM-dd");
		enddateDC.setDate(endRepeatDate);
		enddateDC.setMaximumSize(new Dimension(500, 50));
		repeatFrequancyLb.setVisible(false);
		repeatFrequancyTF.setVisible(false);
		enddateLb.setVisible(false);
		enddateDC.setVisible(false);
		
		//assignment component
		JLabel courseLb = new JLabel("Course Name:");
		courseLb.setAlignmentX(align);
		JComboBox<String> courseCb = new JComboBox<>(new String[] {"Data Science - BIT314302"});
		courseCb.setMaximumSize(new Dimension(500, 50));
		courseLb.setVisible(false);
		courseCb.setVisible(false);
		
		JLabel descriptionLb = new JLabel("Description:");
		descriptionLb.setAlignmentX(align);
		JTextArea descriptionTF = new JTextArea();
		JButton addBt = new JButton("Add");
		
		panel.add(titleLb);
		panel.add(taskTypeLb);
		panel.add(taskTypeCb);
		panel.add(taskTitleLb);
		panel.add(tasktitleTF);
		panel.add(duedateLb);
		panel.add(duedateDC);
		panel.add(repeatLb);
		panel.add(repeatTypeCb);
		panel.add(courseLb);
		panel.add(courseCb);
		panel.add(repeatFrequancyLb);
		panel.add(repeatFrequancyTF);
		panel.add(enddateLb);
		panel.add(enddateDC);
		panel.add(descriptionLb);
		panel.add(descriptionTF);
		panel.add(addBt);

		//change the table when user select the task type
		taskTypeCb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(taskTypeCb.getSelectedItem().equals("Assignment")) {
					repeatTypeCb.setSelectedIndex(0);
					repeatFrequancyLb.setVisible(false);
					repeatFrequancyTF.setVisible(false);
					enddateLb.setVisible(false);
					enddateDC.setVisible(false);
					courseLb.setVisible(true);
					courseCb.setVisible(true);
					repeatLb.setVisible(false);
					repeatTypeCb.setVisible(false);
				}else if(taskTypeCb.getSelectedItem().equals("Task")){
					courseLb.setVisible(false);
					courseCb.setVisible(false);
					repeatLb.setVisible(true);
					repeatTypeCb.setVisible(true);
				}
			}
		});
		//change the table when user select the repeat type
		repeatTypeCb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String content = (String)repeatTypeCb.getSelectedItem();
				System.out.println(content);
				if(content.equals("None")) {
					repeatFrequancyLb.setVisible(false);
					repeatFrequancyTF.setVisible(false);
					enddateLb.setVisible(false);
					enddateDC.setVisible(false);
				}else{
					repeatFrequancyLb.setVisible(true);
					repeatFrequancyTF.setVisible(true);
					enddateLb.setVisible(true);
					enddateDC.setVisible(true);
				}
			}
		});
		//save task information
		addBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Task task;
				//if is task, else assignment
				if(taskTypeCb.getSelectedIndex()== 0) {
					if(repeatTypeCb.getSelectedIndex() == 0) {
						//none repeat task
						task = new Task();
						task.setTaskTitle(tasktitleTF.getText());
						task.setDueDate(date);
						task.setDescription(descriptionTF.getText());
						task.saveTask(sql, login.user.getUserID());
					}else if(repeatTypeCb.getSelectedIndex() == 1) {
						//daily task
						task = new RepeatTask();
						
					}else if(repeatTypeCb.getSelectedIndex() == 2) {
						//weekly task
						task = new RepeatTask();
						
					}else if(repeatTypeCb.getSelectedIndex() == 3) {
						//monthly task
						task = new RepeatTask();
						
					}
				}else {
					task = new Assignment();
					
				}
				
				//close this pop-up form
				ti.dispose();
				resetCalendar(rightPanel);
			}
		});
		
		ti.getContentPane().add(panel, BorderLayout.CENTER);
        ti.setSize(330, 600);
        ti.setLocationRelativeTo(null); // Center the frame on the screen
        ti.setVisible(true);
	}
	
	
	
	//Display the task information entered
	
	private void editTaskInformation(Task task) {
		float align = Component.LEFT_ALIGNMENT;
		Date endRepeatDate = ((Date)task.getDueDate().clone());
		endRepeatDate.setDate(endRepeatDate.getDate()+1);
		
		//container of component of task information form
		JDialog ti = new JDialog();
		ti.setTitle("Task Information");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		
		//component in task information form
		JLabel titleLb = new JLabel("Task Information", SwingConstants.CENTER);
		
		//Parent class task information
		JLabel taskTypeLb = new JLabel("Task Type:");
		taskTypeLb.setAlignmentX(align);
		JComboBox<String> taskTypeCb = new JComboBox<>(new String[] {"Task", "Assignment"});
		taskTypeCb.setMaximumSize(new Dimension(500, 50));
		
		JLabel taskTitleLb = new JLabel("Task Title");
		taskTitleLb.setAlignmentX(align);
		JTextField tasktitleTF = new JTextField();
		tasktitleTF.setMaximumSize(new Dimension(500, 50));
		tasktitleTF.setText(task.getTaskTitle());
		
		JLabel duedateLb = new JLabel("Due Date:");
		duedateLb.setAlignmentX(align);
		JDateChooser duedateDC = new JDateChooser();
		duedateDC.setDateFormatString("yyyy-MM-dd");
		duedateDC.setDate(task.getDueDate());
		duedateDC.setMaximumSize(new Dimension(500, 50));
		
		JLabel repeatLb = new JLabel("Repeat Task:");
		repeatLb.setAlignmentX(align);
		JComboBox<String> repeatTypeCb = new JComboBox<>(new String[] {"None", "Daily", "Weekly", "Monthly"});
		repeatTypeCb.setMaximumSize(new Dimension(500, 50));
		
		//repeat task component
		JLabel repeatFrequancyLb = new JLabel("Repeat Frequancy");
		repeatFrequancyLb.setAlignmentX(align);
		JTextField repeatFrequancyTF = new JTextField();
		repeatFrequancyTF.setMaximumSize(new Dimension(500, 50));
		JLabel enddateLb = new JLabel("End Repeat Date:");
		enddateLb.setAlignmentX(align);
		JDateChooser enddateDC = new JDateChooser();
		enddateDC.setDateFormatString("yyyy-MM-dd");
		enddateDC.setDate(endRepeatDate);
		enddateDC.setMaximumSize(new Dimension(500, 50));

		//assignment component
		JLabel courseLb = new JLabel("Course Name:");
		courseLb.setAlignmentX(align);
		JComboBox<String> courseCb = new JComboBox<>(new String[] {"Data Science - BIT314302"});
		courseCb.setMaximumSize(new Dimension(500, 50));
		
		JLabel descriptionLb = new JLabel("Description:");
		descriptionLb.setAlignmentX(align);
		JTextArea descriptionTF = new JTextArea();
		descriptionTF.setText(task.getDescription());
		JCheckBox completedCb = new JCheckBox("Completed");
		completedCb.setSelected(task.getCompleted());
		JPanel buttonPl = new JPanel();
		JButton saveBt = new JButton("Save");
		JButton deleteBt = new JButton("Delete");
		buttonPl.add(saveBt);
		buttonPl.add(deleteBt);
		
		if(!(task instanceof Assignment)){
			//if task is task
			taskTypeCb.setSelectedIndex(0);
			courseLb.setVisible(false);
			courseCb.setVisible(false);
		}else {
			//if task is assignment
			taskTypeCb.setSelectedIndex(1);
		}

		if(!(task instanceof RepeatTask)) {
			repeatFrequancyLb.setVisible(false);
			repeatFrequancyTF.setVisible(false);
			enddateLb.setVisible(false);
			enddateDC.setVisible(false);
		}else {
			
		}
		
		panel.add(titleLb);
		panel.add(taskTypeLb);
		panel.add(taskTypeCb);
		panel.add(taskTitleLb);
		panel.add(tasktitleTF);
		panel.add(duedateLb);
		panel.add(duedateDC);
		panel.add(repeatLb);
		panel.add(repeatTypeCb);
		panel.add(courseLb);
		panel.add(courseCb);
		panel.add(repeatFrequancyLb);
		panel.add(repeatFrequancyTF);
		panel.add(enddateLb);
		panel.add(enddateDC);
		panel.add(descriptionLb);
		panel.add(descriptionTF);
		panel.add(completedCb);
		panel.add(buttonPl);

		//change the table when user select the task type
		taskTypeCb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(taskTypeCb.getSelectedItem().equals("Assignment")) {
					repeatTypeCb.setSelectedIndex(0);
					repeatFrequancyLb.setVisible(false);
					repeatFrequancyTF.setVisible(false);
					enddateLb.setVisible(false);
					enddateDC.setVisible(false);
					courseLb.setVisible(true);
					courseCb.setVisible(true);
					repeatLb.setVisible(false);
					repeatTypeCb.setVisible(false);
				}else if(taskTypeCb.getSelectedItem().equals("Task")){
					courseLb.setVisible(false);
					courseCb.setVisible(false);
					repeatLb.setVisible(true);
					repeatTypeCb.setVisible(true);
				}
			}
		});
		//change the table when user select the repeat type
		repeatTypeCb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String content = (String)repeatTypeCb.getSelectedItem();
				System.out.println(content);
				if(content.equals("None")) {
					repeatFrequancyLb.setVisible(false);
					repeatFrequancyTF.setVisible(false);
					enddateLb.setVisible(false);
					enddateDC.setVisible(false);
				}else{
					repeatFrequancyLb.setVisible(true);
					repeatFrequancyTF.setVisible(true);
					enddateLb.setVisible(true);
					enddateDC.setVisible(true);
				}
			}
		});
		//save task information
		saveBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Task tempTask = null;
				//if is task, else assignment
				if(taskTypeCb.getSelectedIndex()== 0) {
					if(repeatTypeCb.getSelectedIndex() == 0) {
						//none repeat task
						task.setTaskTitle(tasktitleTF.getText());
						task.setDueDate(duedateDC.getDate());
						task.setDescription(descriptionTF.getText());
						task.setCompleted(completedCb.isSelected());
						sql.editTask(task);
					}else{
						//repeat task
						tempTask = new RepeatTask();
						if(repeatTypeCb.getSelectedIndex() == 1) {
							
						}
					}
				}else {
					tempTask = new Assignment();
					
				}
				
				//close this pop-up form
				ti.dispose();
				resetCalendar(rightPanel);
			}
		});
		
		//delete the task
		deleteBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sql.deleteTask(task.getTaskId());
				//close this pop-up form
				ti.dispose();
				resetCalendar(rightPanel);
			}
		});
		
		ti.getContentPane().add(panel, BorderLayout.CENTER);
        ti.setSize(330, 600);
        ti.setLocationRelativeTo(null); // Center the frame on the screen
        ti.setVisible(true);
	}
	
	
	
	private void displaySearchTask(JPanel displaySearchPl, JLabel nothing, JTextField searchTF) {
		clearPanel(displaySearchPl);
		//search task and save at here
		Task[] searchTask = searchTF.getText().isEmpty()? new Task[0]:sql.searchTask(login.user, searchTF.getText());
		if(searchTask.length == 0) {
			displaySearchPl.add(nothing);
			nothing.setVisible(true);
			return;
		}
		//add searchTask
		JLabel[] task = new JLabel[20];
		for(int j=0; j<20; j++) {
			if(searchTask[j] == null) {
				return;
			}
			task[j] = new JLabel(searchTask[j].getTaskTitle());
			task[j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			task[j].putClientProperty("Task", searchTask[j]);
			task[j].setOpaque(true); 
			task[j].setEnabled(true); 
			task[j].addMouseListener(new MouseListener() {
				public void mouseReleased(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {
					e.getComponent().setBackground(null);
				}
				public void mouseEntered(MouseEvent e){
					e.getComponent().setBackground(Color.lightGray);
				}
				public void mouseClicked(MouseEvent e) {
					editTaskInformation((Task)((JLabel)e.getComponent()).getClientProperty("Task"));
				}
			});
			displaySearchPl.add(task[j]);
		}
	}
	
	
	private void resetCalendar(JPanel rightPanel) {
		clearPanel(rightPanel);
		rightPanel.setVisible(false);
		rightPanel(pageDate);
		rightPanel.setVisible(true);
	}
	
	
	
	
	private void clearPanel(JPanel panel) {
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
	}
	
}
