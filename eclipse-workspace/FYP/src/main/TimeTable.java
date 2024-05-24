package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.toedter.calendar.JDateChooser;

import object.Course;
import object.SQLConnect;

public class TimeTable {
	private JFrame frame;
	private JPanel leftPanel, rightPanel;
	
	//Date format
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SQLConnect sql;
	
	private Color courseCl = Color.CYAN;
	
	static Login_page login;

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
		frame.setTitle("Calendar System for FSKTM Student (Timetable)");
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(new BorderLayout());frame.addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent arg0) {
				login.insertToFile();
			}
		});
		
		sql.readTimetable(login.user);
		
		//Left side panel 
		leftPanel = new JPanel();	
		leftPanel();
		
		//Right side panel
        rightPanel = new JPanel();
        rightPanel();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        
  		
        frame.add(splitPane, BorderLayout.CENTER);
        frame.setVisible(true);
		
        splitPane.setDividerLocation(0.2);
        
        // Change font size and type of font
        Font newFont = new Font("Verdana", Font.BOLD, 12);
        updateFonts(frame, newFont);
	}
	
	private void rightPanel() {
		rightPanel.setLayout(new BorderLayout());
		JPanel timetable = new JPanel(new GridLayout(6,16));
		JPanel[] timePl = new JPanel[96];
		
		for(int i=0; i<96 ; i++) {
			timePl[i] = new JPanel();
			if(i == 0) {
				timePl[i].add(new JLabel("Day"));
			}else if(i < 16) {
				timePl[i].add(new JLabel((i+7)+".00 -\n"+(i+8)+".00"));
			}else if(i%16 == 0) {
				DayOfWeek dayOfWeek;
				if(i/16 != 1) {
					dayOfWeek = DayOfWeek.of(i/16-1);
				}else {
					dayOfWeek = DayOfWeek.of(7);
				}
				timePl[i].add(new JLabel(String.valueOf(dayOfWeek)));
			}
			timePl[i].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			timePl[i].setBackground(Color.WHITE);
			timetable.add(timePl[i]);
		}
		
		if(login.user.getTimetable().getCourse()!= null) {
			for(Course course : login.user.getTimetable().getCourse()){
				int position = ((getDayPosition(course.getDay()))*(16)+getTimePosition(course.getTime()));
				for(int i=0; i<course.getDurationTime(); i++) {
					courseLabel(timePl[position+i], course);
				}
			}
		}
		
		JScrollPane scrollPane = new JScrollPane(timetable);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		rightPanel.add(scrollPane);
	}
	
	private void courseLabel(JPanel timePl, Course course) {
	    // Create the label with course details
	    JLabel tasklb = new JLabel("<html><div style='text-align: center; vertical-align: middle;'>" +
	            course.getCode() + "<br>" + course.getCourseName() + "<br>" + course.getCourseType() +
	            "</div></html>");
	    tasklb.setHorizontalAlignment(SwingConstants.CENTER);
	    tasklb.setBackground(courseCl);
	    tasklb.setOpaque(true);
	    tasklb.setPreferredSize(new Dimension(timePl.getWidth() - 10, 50));
	    tasklb.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
	    tasklb.addMouseListener(new MouseListener() {
	        public void mouseReleased(MouseEvent e) {}
	        public void mousePressed(MouseEvent e) {}
	        public void mouseExited(MouseEvent e) {
	        	tasklb.setBackground(courseCl);
	        }
	        public void mouseEntered(MouseEvent e) {
	        	tasklb.setBackground(Color.LIGHT_GRAY);
	        }
	        public void mouseClicked(MouseEvent e) {
	        	if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(frame, "Are you sure want to remove " + course.getCode() + " in timetable?", "Remove course", JOptionPane.WARNING_MESSAGE)) {
	        		sql.removeStudentCourse(login.user.getUserID(), course);
	        		resetTimetable(rightPanel);
	        	}
	        }
	    });
	    timePl.setLayout(new BorderLayout());
	    timePl.add(tasklb, BorderLayout.CENTER);
	    
	    timePl.revalidate();
	    timePl.repaint();
	}

	
	private int getTimePosition(Date time) {
		if(!time.equals(null)) {
			return time.getHours()-8+1;
		}
		return -1;
	}
	
	private int getDayPosition(String day) {
	    String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
	    for (int i = 0; i < daysOfWeek.length; i++) {
	        if (daysOfWeek[i].equalsIgnoreCase(day)) {
	            return i + 1;
	        }
	    }
	    return -1; // Return -1 if no match is found
	}
	
	private void leftPanel() {
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		JLabel usernameLb = new JLabel(login.user.getUsername());
		usernameLb.setMaximumSize(new Dimension(1000,60));
		
		//Upside panel
		JPanel upPl = new JPanel();
		upPl.setLayout(new GridBagLayout());
		upPl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		JLabel infoLb = new JLabel("Information");
		JLabel semStartLb = new JLabel("Semester Start:");
		JDateChooser semStartDateLb = new JDateChooser();
		semStartDateLb.setDateFormatString("yyyy-MM-dd");
		semStartDateLb.setDate(login.user.getTimetable().getSemStart());
		
		JLabel weekLb = new JLabel("Now Weeks :");
		JTextField weekQuantityLb = new JTextField();
		weekQuantityLb.setEditable(false);
		//calculate the week from semester start until now
		weekQuantityLb.setText(String.valueOf(getWeeksDifference(login.user.getTimetable().getSemStart())+1));
		
		semStartDateLb.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
            	sql.updateSemesterStartDate(login.user.getUserID(), semStartDateLb.getDate());
            	weekQuantityLb.setText(String.valueOf(getWeeksDifference(semStartDateLb.getDate())+1));
            }
        });
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        upPl.add(infoLb, gbc);

        gbc.gridy = 1;
        upPl.add(semStartLb, gbc);

        gbc.gridy = 2;
        upPl.add(semStartDateLb, gbc);

        gbc.gridy = 3;
        upPl.add(weekLb, gbc);

        gbc.gridy = 4;
        upPl.add(weekQuantityLb, gbc);
		
		//Down side panel
		JPanel downPl = new JPanel();
		downPl.setLayout(new BoxLayout(downPl, BoxLayout.Y_AXIS));
		
		JButton addCourseBt = new JButton("Add Course");
		addCourseBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addCourseForm();
			}
		});
		JButton deleteAllBt = new JButton("Delete All Course");
		deleteAllBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(frame, "Are you sure want to remove all course in timetable?", "Remove course", JOptionPane.WARNING_MESSAGE)) {					
					sql.removeAllStudentCourse(login.user.getUserID());
					resetTimetable(rightPanel);
				}
			}
		});
		JButton exitBt = new JButton("Exit");
		exitBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		downPl.add(addCourseBt);downPl.add(deleteAllBt);downPl.add(exitBt);
		
		leftPanel.add(usernameLb);
		leftPanel.add(upPl);
		leftPanel.add(downPl);
	}
	
	private static long getWeeksDifference(Date semStart) {
	    // Convert java.util.Date to LocalDate using Calendar
	    LocalDate semStartDate = convertToLocalDate(semStart);
	    // Get current date
	    LocalDate currentDate = LocalDate.now();
	    // Calculate the difference in weeks
	    long weeksBetween = ChronoUnit.WEEKS.between(semStartDate, currentDate);
	    return weeksBetween;
	}

	private static LocalDate convertToLocalDate(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return LocalDate.of(
	        calendar.get(Calendar.YEAR),
	        calendar.get(Calendar.MONTH) + 1,
	        calendar.get(Calendar.DAY_OF_MONTH)
	    );
	}
	
	public void addCourseForm() {
        float align = Component.LEFT_ALIGNMENT;

        // Container of component of course information form
        JDialog ci = new JDialog();
        ci.setTitle("Course Information");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        List<Course> course = new ArrayList<>();
        sql.readStudentCourse("", course, login.user.getUserID());

        JLabel courseNameSearchLb = new JLabel("Search Course Name:");
        courseNameSearchLb.setAlignmentX(align);
        JTextField courseNameSearchTf = new JTextField();
        courseNameSearchTf.setMaximumSize(new Dimension(600, 50));

        JLabel courseCodeLb = new JLabel("Course Code:");
        courseCodeLb.setAlignmentX(align);
        JComboBox<String> courseCodeCb = new JComboBox<>();
        courseCodeCb.setMaximumSize(new Dimension(600, 50));

        JLabel courseNameLb = new JLabel("Course Name:");
        courseNameLb.setAlignmentX(align);
        JTextField courseNameTf = new JTextField();
        courseNameTf.setEditable(false);
        courseNameTf.setMaximumSize(new Dimension(600, 50));

        JLabel sectionLb = new JLabel("Section:");
        sectionLb.setAlignmentX(align);
        JComboBox<String> sectionCb = new JComboBox<>();
        sectionCb.setMaximumSize(new Dimension(600, 50));

        JPanel buttonPl = new JPanel();
        JButton addBt = new JButton("Add");
        buttonPl.add(addBt);

        panel.add(courseNameSearchLb);
        panel.add(courseNameSearchTf);
        panel.add(courseCodeLb);
        panel.add(courseCodeCb);
        panel.add(courseNameLb);
        panel.add(courseNameTf);
        panel.add(sectionLb);
        panel.add(sectionCb);
        panel.add(buttonPl);

        // Add course information
        addBt.addActionListener(e -> {
        	sql.addStudentCourse(login.user,String.valueOf(courseCodeCb.getSelectedItem()), Integer.valueOf((String) sectionCb.getSelectedItem()));
			resetTimetable(rightPanel);
            ci.dispose();
        });

        // Add DocumentListener for the course name text field
        courseNameSearchTf.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateComboBoxes();
            }

            public void removeUpdate(DocumentEvent e) {
                updateComboBoxes();
            }

            public void changedUpdate(DocumentEvent e) {
                updateComboBoxes();
            }

            private void updateComboBoxes() {
                String input = courseNameSearchTf.getText();
                List<Course> filteredCourses = new ArrayList<>();
                sql.readStudentCourse(input, filteredCourses, login.user.getUserID());

                if (!filteredCourses.isEmpty()) {
                    updateCourseCodeAndSection(filteredCourses, courseCodeCb, courseNameTf, sectionCb);
                } else {
                    courseCodeCb.removeAllItems();
                    courseNameTf.setText("");
                    sectionCb.removeAllItems();
                }
            }
        });

        // Add ItemListener for the course code combo box
        courseCodeCb.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedCourseCode = (String) e.getItem();
                List<Course> filteredCourses = new ArrayList<>();
                sql.readStudentCourse(courseNameSearchTf.getText(), filteredCourses, login.user.getUserID());
                filteredCourses.stream()
                        .filter(c -> c.getCode().equals(selectedCourseCode))
                        .findFirst()
                        .ifPresent(c -> {
                            courseNameTf.setText(c.getCourseName());
                            updateSections(c.getCode(), filteredCourses, sectionCb);
                        });
            }
        });

        ci.getContentPane().add(panel, BorderLayout.CENTER);
        ci.setSize(400, 250);
        ci.setLocationRelativeTo(null); // Center the frame on the screen
        ci.setVisible(true);
    }

	private void updateCourseCodeAndSection(List<Course> filteredCourses, JComboBox<String> courseCodeCb, JTextField courseNameTf, JComboBox<String> courseSectionCb) {
	    courseCodeCb.removeAllItems();
	    courseSectionCb.removeAllItems();

	    Set<String> uniqueCourseCodes = new HashSet<>();
	    filteredCourses.forEach(c -> uniqueCourseCodes.add(c.getCode()));

	    uniqueCourseCodes.forEach(courseCodeCb::addItem);

	    if (!filteredCourses.isEmpty()) {
	        Course firstCourse = filteredCourses.get(0);
	        courseNameTf.setText(firstCourse.getCourseName());
	    }
	}


    private void updateSections(String selectedCourseCode, List<Course> filteredCourses, JComboBox<String> sectionCb) {
        sectionCb.removeAllItems();
        filteredCourses.stream()
                .filter(c -> c.getCode().equals(selectedCourseCode))
                .forEach(c -> sectionCb.addItem(String.valueOf(c.getCourseSection())));
    }
    
    private void resetTimetable(JPanel rightPanel) {
    	sql.readTimetable(login.user);
		clearPanel(rightPanel);
		rightPanel.setVisible(false);
		rightPanel();
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
	
	// Update font for a single component
    public void updateFont(Component component, Font newFont) {
        component.setFont(newFont);
    }

    // Recursively update font for all components in a container
    void updateFonts(Container container, Font newFont) {
        for (Component component : container.getComponents()) {
            updateFont(component, newFont);

            if (component instanceof Container) {
                updateFonts((Container) component, newFont);
            }
        }
    }
}
