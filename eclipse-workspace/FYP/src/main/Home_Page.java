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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import com.toedter.calendar.JDateChooser;

public class Home_Page {
	private JFrame frame;
	private JMenu menu, submenu;
	private JMenuItem i1, i2, i3;
	private JPanel leftPanel, rightPanel;
	
	//Color of task
	private Color taskColor = Color.CYAN;
	private Color repeatColor = Color.PINK;
	private Color dayColor = Color.WHITE;
	private Color todayColor = new Color(0,140,255);
	
	//Date format
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Date pageDate;
	private Date today = new Date();
	
	//variable for calendar
	private JCheckBox taskCb;
	private JCheckBox assignmentCb;
	private JCheckBox rpTaskCb;
	private Timer timer;	
	
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
		frame.addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent arg0) {
				login.insertToFile();
			}
		});
		
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
        
        resetFont();
	}
	
	private void rightPanel(Date day) {
		rightPanel.setLayout(new BorderLayout());
		JPanel selectedCalendar = new JPanel();
		selectedCalendar.setLayout(new GridLayout(2,7, 10, 10));
		
		 timer = new Timer(50, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	if(timer.getActionCommand() == null) { 
	            		return; 
	            	}
	            	if(Integer.valueOf(timer.getActionCommand()) > 0) {
						//next month
						pageDate.setMonth(pageDate.getMonth()+1);
						resetCalendar(rightPanel);
					}else {
						//previous month
						pageDate.setMonth(pageDate.getMonth()-1);
						resetCalendar(rightPanel);
					}
	            }
	     });
		
		rightPanel.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				timer.setActionCommand(String.valueOf(e.getWheelRotation()));
				timer.restart();
			}
		});
		
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
        new RepeatTask().readTask(sql,login.user, login.user.getUserID());
        
        day.setDate(1);
		int calendarMonthDays;
		if(day.getDay() > 5 && day.getMonth()!= 1) {
			calendarMonthDays = 42;
		}else {
			calendarMonthDays = 35;
		}
        
		JPanel calendar = new JPanel(new GridLayout(calendarMonthDays/7,7));
		JPanel[] dayPl= new JPanel[(calendarMonthDays)];
		
		//Find range task for this month
		if(login.user.getTask() != null)
			login.user.findNowArrayTask(day);
		
		//Calculate first day in calendar
		Date calDay = (Date)day.clone();
		int x = calDay.getDay();//Record the day of first day(exp: monday)
		calDay.setDate(-x+1);
		
		//Build calendar panel
		for(int i = 0; i < calendarMonthDays ; i++) {
			JLabel dayLb = new JLabel(""+calDay.getDate());
			dayLb.setHorizontalAlignment(SwingConstants.CENTER);
			dayLb.setMaximumSize(new Dimension(200,50));
			
			//Change today to another color
			Color cl;
			if(calDay.compareTo(today) == 0) {
				cl = todayColor;
			}else{
				cl = dayColor;
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
			Color taskCl;
			int a = 0, b = 0;
			while(a < login.user.getTask().length){
				//same date
				if(calDay.compareTo(login.user.getTask()[a].getDueDate()) == 0 && taskCb.isSelected()) {
					if(login.user.getTask()[a].getCompleted()) {
						taskCl = null;
					}else {
						taskCl = taskColor;
					}
					JLabel task = labelTask(a, taskCl, dayPl[i], 1, login.user.getTask()[a]);
					dayPl[i].add(task);
					b++;
				}
				a++;
			}
			a = 0;
			while (a < login.user.getRepeatTask().length) {
				//add repeat task into calendar
				if(a<login.user.getRepeatTask().length && rpTaskCb.isSelected() && Arrays.asList(login.user.getRepeatTask()[a].getInDate()).contains(calDay)) {
					if(login.user.getRepeatTask()[a].getCompleted()) {
						taskCl = null;
					}else {
						taskCl = repeatColor;
					}
					JLabel task = labelTask(a, taskCl, dayPl[i], 2, login.user.getRepeatTask()[a]);
					dayPl[i].add(task);
					b++;
				}
				a++;
			}
			
			//if task in a day more than 3, the scrolling function will insert
			if(b > 3) {
				dayPl[i].setPreferredSize(new Dimension(rightPanel.getWidth()/5, (b*8)+50));
				JScrollPane scrollPane = new JScrollPane(dayPl[i]);
	            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            	scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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
		viewHistoryBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				History_Page hp = new History_Page(login, sql);
			}
		});
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
		JLabel repeatfrequencyLb = new JLabel("Repeat frequency");
		repeatfrequencyLb.setAlignmentX(align);
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(2, 1, 1000, 1);
		JSpinner repeatfrequencyTF = new JSpinner(spinnerModel);
		repeatfrequencyTF.setMaximumSize(new Dimension(500, 50));
		JComponent editor = repeatfrequencyTF.getEditor();
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)editor;
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);
		JFormattedTextField txt = ((JSpinner.NumberEditor) repeatfrequencyTF.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
		JLabel enddateLb = new JLabel("End Repeat Date:");
		enddateLb.setAlignmentX(align);
		JDateChooser enddateDC = new JDateChooser();
		enddateDC.setDateFormatString("yyyy-MM-dd");
		enddateDC.setDate(endRepeatDate);
		enddateDC.setMaximumSize(new Dimension(500, 50));
		repeatfrequencyLb.setVisible(false);
		repeatfrequencyTF.setVisible(false);
		enddateLb.setVisible(false);
		enddateDC.setVisible(false);
		
		//change date when frequency change
		repeatfrequencyTF.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				frequencyChangeToDate(repeatTypeCb, repeatfrequencyTF, duedateDC, enddateDC);;
			}
		});
		
		 enddateDC.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
            	dateChangeToFrequency(repeatTypeCb, repeatfrequencyTF, duedateDC, enddateDC);;
            }
        });
		
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
		panel.add(repeatfrequencyLb);
		panel.add(repeatfrequencyTF);
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
					repeatfrequencyLb.setVisible(false);
					repeatfrequencyTF.setVisible(false);
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
				if(content.equals("None")) {
					repeatfrequencyLb.setVisible(false);
					repeatfrequencyTF.setVisible(false);
					enddateLb.setVisible(false);
					enddateDC.setVisible(false);
					duedateLb.setText("Due Date");
				}else{
					repeatfrequencyLb.setVisible(true);
					repeatfrequencyTF.setVisible(true);
					enddateLb.setVisible(true);
					enddateDC.setVisible(true);
					duedateLb.setText("Start Date");
				}
			}
		});
		//save task information
		addBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(repeatTypeCb.getSelectedIndex() == 0) {
					//task
					Task task;
					task = new Task();
					task.setTaskTitle(tasktitleTF.getText());
					task.setDueDate(date);
					task.setDescription(descriptionTF.getText());
					task.saveTask(sql, login.user.getUserID());
				}else {
					//repeat task
					RepeatTask repeatTask;
					repeatTask = new RepeatTask();
					repeatTask.setTaskTitle(tasktitleTF.getText());
					repeatTask.setDueDate(date);
					repeatTask.setDescription(descriptionTF.getText());
					repeatTask.setRepeatType((String)repeatTypeCb.getSelectedItem());
					repeatTask.setFrequencyRepeat((int)repeatfrequencyTF.getValue());
					repeatTask.setEndDate(enddateDC.getDate());
					repeatTask.saveTask(sql, login.user.getUserID());
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
		JLabel repeatfrequencyLb = new JLabel("Repeat frequency");
		repeatfrequencyLb.setAlignmentX(align);
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(2, 1, 1000, 1);
		JSpinner repeatfrequencyTF = new JSpinner(spinnerModel);
		repeatfrequencyTF.setMaximumSize(new Dimension(500, 50));
		JComponent editor = repeatfrequencyTF.getEditor();
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)editor;
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);
		JFormattedTextField txt = ((JSpinner.NumberEditor) repeatfrequencyTF.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
		JLabel enddateLb = new JLabel("End Repeat Date:");
		enddateLb.setAlignmentX(align);
		JDateChooser enddateDC = new JDateChooser();
		enddateDC.setDateFormatString("yyyy-MM-dd");
		enddateDC.setDate(endRepeatDate);
		enddateDC.setMaximumSize(new Dimension(500, 50));
		repeatfrequencyLb.setVisible(false);
		repeatfrequencyTF.setVisible(false);
		enddateLb.setVisible(false);
		enddateDC.setVisible(false);
		
		//change date when frequency change
		repeatfrequencyTF.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				frequencyChangeToDate(repeatTypeCb, repeatfrequencyTF, duedateDC, enddateDC);;
			}
		});
		
		 enddateDC.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
            	dateChangeToFrequency(repeatTypeCb, repeatfrequencyTF, duedateDC, enddateDC);;
            }
        });
		

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

		if(task instanceof RepeatTask) {
			//if is a repeat task
			repeatfrequencyLb.setVisible(true);
			repeatfrequencyTF.setVisible(true);
			enddateLb.setVisible(true);
			enddateDC.setVisible(true);
			RepeatTask tempTask = (RepeatTask)task;
			repeatTypeCb.setSelectedItem(tempTask.getRepeatType());
			repeatfrequencyTF.setValue(tempTask.getFrequencyRepeat());
			enddateDC.setDate(tempTask.getEndDate());
			
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
		panel.add(repeatfrequencyLb);
		panel.add(repeatfrequencyTF);
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
					repeatfrequencyLb.setVisible(false);
					repeatfrequencyTF.setVisible(false);
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
				if(content.equals("None")) {
					repeatfrequencyLb.setVisible(false);
					repeatfrequencyTF.setVisible(false);
					enddateLb.setVisible(false);
					enddateDC.setVisible(false);
				}else{
					repeatfrequencyLb.setVisible(true);
					repeatfrequencyTF.setVisible(true);
					enddateLb.setVisible(true);
					enddateDC.setVisible(true);
				}
			}
		});
		//save task information
		saveBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				task.setTaskTitle(tasktitleTF.getText());
				task.setDueDate(duedateDC.getDate());
				task.setDescription(descriptionTF.getText());
				task.setCompleted(completedCb.isSelected());
				
				if(task instanceof RepeatTask) {
					RepeatTask tempTask = new RepeatTask(task);
					tempTask.setRepeatType((String)repeatTypeCb.getSelectedItem());
					tempTask.setFrequencyRepeat((int)repeatfrequencyTF.getValue());
					tempTask.setEndDate(enddateDC.getDate());
					tempTask.editTask(sql, login.user.getUserID());
				}else if(task instanceof Task && !repeatTypeCb.getSelectedItem().equals("None")){
					RepeatTask tempTask = new RepeatTask(task);
					task.deleteTask(sql);
					tempTask.setRepeatType((String)repeatTypeCb.getSelectedItem());
					tempTask.setFrequencyRepeat((int)repeatfrequencyTF.getValue());
					tempTask.setEndDate(enddateDC.getDate());
					tempTask.saveTask(sql, login.user.getUserID());
				}else {
					task.editTask(sql);
				}
				//close this pop-up form
				ti.dispose();
				resetCalendar(rightPanel);
			}
		});
		
		//delete the task
		deleteBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (task instanceof RepeatTask) {
					sql.deleteRepeatTask(task.getTaskId());
				}else if(task instanceof Task) {
					sql.deleteTask(task.getTaskId());
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
	

	
	private JLabel labelTask(int a, Color taskCl, JPanel dayPl, int tasktype, Task task) {
		JLabel tasklb = new JLabel(task.getTaskTitle());
		tasklb.setHorizontalAlignment(SwingConstants.CENTER);
		tasklb.setPreferredSize(new Dimension(dayPl.getWidth()-10,10));
		tasklb.setBackground(taskCl);
		tasklb.setOpaque(true);
		tasklb.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
		tasklb.putClientProperty("TaskArray", a);
		tasklb.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
				tasklb.setBackground(taskCl);
			}
			public void mouseEntered(MouseEvent e) {
				tasklb.setBackground(Color.LIGHT_GRAY);
			}
			public void mouseClicked(MouseEvent e) {
				if(tasktype == 1) {
					editTaskInformation(login.user.getTask()[(int)((JLabel)e.getComponent()).getClientProperty("TaskArray")]);
				}else if(tasktype == 2) {
					editTaskInformation(login.user.getRepeatTask()[(int)((JLabel)e.getComponent()).getClientProperty("TaskArray")]);
				}else if(tasktype == 3) {
					editTaskInformation(login.user.getAssignment()[(int)((JLabel)e.getComponent()).getClientProperty("TaskArray")]);
				}
			}
		});
		return tasklb;
	}
	
	private void frequencyChangeToDate(JComboBox <String> repeatTypeCb, JSpinner repeatfrequencyTF, JDateChooser duedateDC, JDateChooser enddateDC) {
		int y = 0;
		if (repeatTypeCb.getSelectedIndex() == 1) {
			y = 1;
		}else if (repeatTypeCb.getSelectedIndex() == 2) {
			y = 7;
		}else if (repeatTypeCb.getSelectedIndex() == 3) {
			int x = (int) repeatfrequencyTF.getValue();
		    Date startDate = duedateDC.getDate();
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(startDate);
		    calendar.add(Calendar.MONTH, x);
		    Date endRepeatDate = calendar.getTime();
		    enddateDC.setDate(endRepeatDate);
			return;
		}
		int x = (int) repeatfrequencyTF.getValue() * y;
		Date startDate = (Date) duedateDC.getDate().clone();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.DATE, x);
		Date endRepeatDate = calendar.getTime();
		enddateDC.setDate(endRepeatDate);
	}
	
	private void dateChangeToFrequency(JComboBox <String> repeatTypeCb, JSpinner repeatfrequencyTF, JDateChooser duedateDC, JDateChooser enddateDC) {
		Date startDate = duedateDC.getDate();
		Date endRepeatDate = enddateDC.getDate();
		
		if(startDate.compareTo(endRepeatDate) > 0) {
			System.out.println(startDate.compareTo(endRepeatDate));
			frequencyChangeToDate(repeatTypeCb, repeatfrequencyTF, duedateDC, enddateDC);
			return;
		}
		
		long frequencybetween = 1;
		if (repeatTypeCb.getSelectedIndex() == 1) {
			long differenceInMilliseconds = Math.abs(endRepeatDate.getTime() - startDate.getTime());
			frequencybetween = differenceInMilliseconds / (1000 * 60 * 60 * 24);
		}else if (repeatTypeCb.getSelectedIndex() == 2) {
			long differenceInMilliseconds = Math.abs(endRepeatDate.getTime() - startDate.getTime());
			long daysBetween = differenceInMilliseconds / (1000 * 60 * 60 * 24);
			frequencybetween = daysBetween / 7;
		}else if (repeatTypeCb.getSelectedIndex() == 3) {
			Calendar startCalendar = Calendar.getInstance();
	        startCalendar.setTime(startDate);
	        Calendar endCalendar = Calendar.getInstance();
	        endCalendar.setTime(endRepeatDate);

	        int startYear = startCalendar.get(Calendar.YEAR);
	        int startMonth = startCalendar.get(Calendar.MONTH);
	        int endYear = endCalendar.get(Calendar.YEAR);
	        int endMonth = endCalendar.get(Calendar.MONTH);
	        frequencybetween = (endYear - startYear) * 12 + (endMonth - startMonth);
		}
		repeatfrequencyTF.setValue((int)frequencybetween);
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
		resetFont();
	}
	
	private void clearPanel(JPanel panel) {
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
	}

	private void resetFont() {
		//change the font size and type of font
		Font newFont = new Font("Verdana", Font.BOLD, 12);
		login.updateFonts(frame, newFont);
	}
}

