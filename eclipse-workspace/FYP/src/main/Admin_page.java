package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import object.*;

public class Admin_page extends JFrame {
	JFrame form = new JFrame();
	SQLConnect sql = new SQLConnect();
	
    public Admin_page() {
        // Set up the JFrame
        setTitle("Admin Panel");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create a panel for holding components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        add(panel);
        
        // Create and add components to the panel
        JLabel titleLabel = new JLabel("Admin Panel");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Buttons for managing users
        JButton addLecturerButton = new JButton("Approval Lecturer Request");
        JButton viewLecturerButton = new JButton("View Lecturer");
        
        // Buttons for managing courses
        JButton addCourseButton = new JButton("Add Course");
        JButton viewCoursesButton = new JButton("View Courses");
        
        // Add action listeners to buttons
        addLecturerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	approvalLecturerFrame();
            	setVisible(false);
            }
        });
        
        viewLecturerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	viewLecturerFrame();
            	setVisible(false);
            }
        });

        addCourseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addCourseFrame();
            	setVisible(false);
			}
		});
        
        viewCoursesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewCourseForm();
				setVisible(false);
			}
		});
        
        // Create panel for user actions
        JPanel userButtonPanel = new JPanel();
        userButtonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        userButtonPanel.add(addLecturerButton);
        userButtonPanel.add(viewLecturerButton);
        
        // Create panel for course actions
        JPanel courseButtonPanel = new JPanel();
        courseButtonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        courseButtonPanel.add(addCourseButton);
        courseButtonPanel.add(viewCoursesButton);
        
        // Create panel for organizing user and course buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(userButtonPanel);
        buttonPanel.add(courseButtonPanel);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        // Set the frame visible
        setVisible(true);
        setFont(this);
    }

    public static void main(String[] args) {
        // Create an instance of AdminGUI
        SwingUtilities.invokeLater(() -> new Admin_page());
    }
    
    public void addCourseFrame() {
    	form = new JFrame("Add User");
        form.setSize(400, 300);
        form.setLayout(new GridLayout(8, 2));
        
    	JLabel lblCourseID, lblCourseName, lblCourseType, lblCourseSection, lblTime, lblDay, lblDuration;
        JTextField txtCourseID, txtCourseName;
        JSpinner txtCourseSection;
        JComboBox<String> cmbCourseType, cmbDay, cmbDuration;
        TimeSelector timeSelector;
        JButton btnSave;

    	lblCourseID = new JLabel("Course ID:");
        txtCourseID = new JTextField();

        lblCourseName = new JLabel("Course Name:");
        txtCourseName = new JTextField();

        lblCourseType = new JLabel("Course Type:");
        cmbCourseType = new JComboBox<>(new String[]{"Kuliah", "Tutorial"});

        lblCourseSection = new JLabel("Course Section:");
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        txtCourseSection = new JSpinner(spinnerModel);
        JComponent editor = txtCourseSection.getEditor();
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)editor;
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);
		
        lblTime = new JLabel("Time:");
        timeSelector = new TimeSelector();

        lblDay = new JLabel("Day:");
        cmbDay = new JComboBox<>(new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"});

        lblDuration = new JLabel("Duration Time:");
        cmbDuration = new JComboBox<>(new String[]{"2", "3"});

        btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a new Course object and populate its fields
                Course course = new Course();
                course.setCode(txtCourseID.getText());
                course.setCourseName(txtCourseName.getText());
                course.setCourseSection((int)txtCourseSection.getValue());
                course.setCourseType(cmbCourseType.getSelectedItem().toString().charAt(0)); 
                course.setTime(new Date(0,0,0,Integer.valueOf(timeSelector.getHour().getSelectedItem().toString()),Integer.valueOf(timeSelector.getMinute().getSelectedItem().toString())));
                course.setDay(cmbDay.getSelectedItem().toString());
                course.setDurationTime(Integer.valueOf(cmbDuration.getSelectedItem().toString()));
                course.saveCourse(sql);

                form.dispose();
                setVisible(true);
            }
        });

        // Add components to the frame
        form.add(lblCourseID);
        form.add(txtCourseID);
        form.add(lblCourseName);
        form.add(txtCourseName);
        form.add(lblCourseType);
        form.add(cmbCourseType);
        form.add(lblCourseSection);
        form.add(txtCourseSection);
        form.add(lblTime);
        form.add(timeSelector);
        form.add(lblDay);
        form.add(cmbDay);
        form.add(lblDuration);
        form.add(cmbDuration);
        form.add(new JLabel()); // Empty label for alignment
        form.add(btnSave);

        form.setLocationRelativeTo(null);
        form.setVisible(true);
        closeFrameSetting();
    }
    
    public void viewCourseForm() {
    	JButton editButton, deleteButton;
    	JTextField searchTF;
    	JPanel funcPl;
	    JTable courseTable;
	    DefaultTableModel tableModel;
	    List<Course> courseList;
    	form = new JFrame("Course Management");
    	final int[] idSelected = {0};
    	
        // Initialize course list
        courseList = new ArrayList<Course>();
        sql.readCourse("", courseList);
    	
        // Create table model with column names
        tableModel = new DefaultTableModel();
        tableModel.addColumn("No.");
        tableModel.addColumn("Course ID");
        tableModel.addColumn("Course Name");
        tableModel.addColumn("Type");
        tableModel.addColumn("Section");
        tableModel.addColumn("Time");
        tableModel.addColumn("Duration(Hour)");
        tableModel.addColumn("Day");
        insertDataIntoTable(courseList, tableModel);

    	funcPl = new JPanel();
    	editButton = new JButton("Edit");
    	deleteButton = new JButton("Delete");
    	searchTF = new JTextField("Search course id or course name");
    	
    	// Function for button and search bar
    	editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editCourseForm(courseList.get(idSelected[0]), tableModel, courseList);
			}
		});
    	deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(form, "Are you sure you want to delete the "+ (idSelected[0]+1) + ": " + courseList.get(idSelected[0]).getCourseName() +" ?", "Confirm delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {					
					courseList.get(idSelected[0]).deleteCourse(sql);
					tableRefresh(courseList, tableModel, searchTF.getText());
				}
			}
		});
    	searchTF.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
			}
			public void keyReleased(KeyEvent arg0) {
				tableRefresh(courseList, tableModel, searchTF.getText());
			}
			public void keyTyped(KeyEvent arg0) {
			}
			
		});
    	searchTF.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				searchTF.setForeground(Color.LIGHT_GRAY);
				if(searchTF.getText().contentEquals("") ) {
					searchTF.setText("Search course id or course name");
				}
			}
			public void focusGained(FocusEvent e) {
				searchTF.setForeground(Color.BLACK);
				if(searchTF.getText().contentEquals("Search course id or course name") ) {
					searchTF.setText("");
				}
			}
		});
    	
    	funcPl.add(searchTF);
    	funcPl.add(editButton);
    	funcPl.add(deleteButton);

        // Create table with custom table model
        courseTable = new JTable(tableModel){
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };;
        courseTable.getColumnModel().getColumn(0).setPreferredWidth(8); // No
        courseTable.getColumnModel().getColumn(1).setPreferredWidth(100);  // Id
        courseTable.getColumnModel().getColumn(2).setPreferredWidth(200);  // Name
        courseTable.getColumnModel().getColumn(3).setPreferredWidth(50); // type
        courseTable.getColumnModel().getColumn(4).setPreferredWidth(50);  // Section
        courseTable.getColumnModel().getColumn(5).setPreferredWidth(50);  // time
        courseTable.getColumnModel().getColumn(6).setPreferredWidth(50); // duration
        courseTable.getColumnModel().getColumn(7).setPreferredWidth(50);  // day
        JScrollPane scrollPane = new JScrollPane(courseTable);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Get selected course from table
        ListSelectionModel listSelectionModel = courseTable.getSelectionModel();
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				idSelected[0] = lsm.getMinSelectionIndex();
			}
		});
        
        // Add table to the frame
        form.add(funcPl,BorderLayout.NORTH);
        form.add(scrollPane, BorderLayout.CENTER);

        // Set frame size and visibility
        form.setSize(800, 600);
        form.setLocationRelativeTo(null);
        form.setVisible(true);
        closeFrameSetting();
    }
    
    private void insertDataIntoTable(List<Course> courseList, DefaultTableModel tableModel) {
    	tableModel.setRowCount(0);//remove all data in table
    	// Insert data from courseList into the table
        for (Course course : courseList) {
            Object[] rowData = {
            	course.getCourseId(),
                course.getCode(),
                course.getCourseName(),
                course.getCourseType(),
                course.getCourseSection(),
                String.format("%02d:%02d", course.getTime().getHours(), course.getTime().getMinutes()),
                course.getDurationTime(),
                course.getDay(),
            };
            tableModel.addRow(rowData);
        }
    }
    
    public void editCourseForm(Course course, DefaultTableModel tableModel, List<Course> courseList) {
    	JFrame form = new JFrame("Edit User");
        form.setSize(400, 300);
        form.setLayout(new GridLayout(8, 2));
        
    	JLabel lblCourseID, lblCourseName, lblCourseType, lblCourseSection, lblTime, lblDay, lblDuration;
        JTextField txtCourseID, txtCourseName;
        JSpinner txtCourseSection;
        JComboBox<String> cmbCourseType, cmbDay, cmbDuration;
        TimeSelector timeSelector;
        JButton btnSave;

    	lblCourseID = new JLabel("Course ID:");
        txtCourseID = new JTextField();

        lblCourseName = new JLabel("Course Name:");
        txtCourseName = new JTextField();

        lblCourseType = new JLabel("Course Type:");
        cmbCourseType = new JComboBox<>(new String[]{"Kuliah", "Tutorial"});

        lblCourseSection = new JLabel("Course Section:");
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        txtCourseSection = new JSpinner(spinnerModel);
        JComponent editor = txtCourseSection.getEditor();
		JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)editor;
		spinnerEditor.getTextField().setHorizontalAlignment(JTextField.LEFT);
		
        lblTime = new JLabel("Time:");
        timeSelector = new TimeSelector();

        lblDay = new JLabel("Day:");
        cmbDay = new JComboBox<>(new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"});

        lblDuration = new JLabel("Duration Time:");
        cmbDuration = new JComboBox<>(new String[]{"2", "3"});

        btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a new Course object and populate its fields
                course.setCode(txtCourseID.getText());
                course.setCourseName(txtCourseName.getText());
                course.setCourseSection((int)txtCourseSection.getValue());
                course.setCourseType(cmbCourseType.getSelectedItem().toString().charAt(0)); 
                course.setTime(new Date(0,0,0,Integer.valueOf(timeSelector.getHour().getSelectedItem().toString()),Integer.valueOf(timeSelector.getMinute().getSelectedItem().toString())));
                course.setDay(cmbDay.getSelectedItem().toString());
                course.setDurationTime(Integer.valueOf(cmbDuration.getSelectedItem().toString()));
                course.editCourse(sql);
                
                form.dispose();
                tableRefresh(courseList, tableModel, "");
            }
        });
        
        // Set date into text field
        txtCourseID.setText(course.getCode());
        txtCourseName.setText(course.getCourseName());
        cmbCourseType.setSelectedItem((course.getCourseType()=='K') ? "Kuliah": "Tutorial");
        txtCourseSection.setValue(course.getCourseSection());
        timeSelector.setHour(String.valueOf(course.getTime().getHours()));
        timeSelector.setMinute(String.valueOf(course.getTime().getMinutes()));
        cmbDay.setSelectedItem(course.getDay());
        cmbDuration.setSelectedItem(String.valueOf(course.getDurationTime()));

        // Add components to the frame
        form.add(lblCourseID);
        form.add(txtCourseID);
        form.add(lblCourseName);
        form.add(txtCourseName);
        form.add(lblCourseType);
        form.add(cmbCourseType);
        form.add(lblCourseSection);
        form.add(txtCourseSection);
        form.add(lblTime);
        form.add(timeSelector);
        form.add(lblDay);
        form.add(cmbDay);
        form.add(lblDuration);
        form.add(cmbDuration);
        form.add(new JLabel()); // Empty label for alignment
        form.add(btnSave);

        form.setLocationRelativeTo(null);
        form.setVisible(true);
    }
    
    public void tableRefresh(List<Course> courseList, DefaultTableModel tableModel, String keyword) {
    	keyword = (keyword.equals("Search course id or course name") ? "": keyword);
    	courseList.removeAll(courseList);
		sql.readCourse(keyword, courseList);
		insertDataIntoTable(courseList, tableModel);
    }
    
    public void approvalLecturerFrame() {
        form = new JFrame("Approval Lecturer Form");
        form.setSize(800, 600);
        form.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        approvalListPanel(mainPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        form.add(scrollPane, BorderLayout.CENTER);

        form.setLocationRelativeTo(null);
        form.setVisible(true);
        closeFrameSetting();
    }

    public void approvalListPanel(JPanel mainPanel) {
        mainPanel.removeAll(); // Clear the panel before adding new components

        User[] lecturer = sql.addLecturer();

        // Create title row panel
        JPanel titlePanel = new JPanel(new GridLayout(1, 3));
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        titlePanel.setMaximumSize(new Dimension(1000, 80));
        JLabel nameLabel = new JLabel("Name");
        JLabel emailLabel = new JLabel("Email");
        JLabel approvalLabel = new JLabel("Approval");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
        approvalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(nameLabel);
        titlePanel.add(emailLabel);
        titlePanel.add(approvalLabel);
        mainPanel.add(titlePanel);

        for (int i = 0; i < lecturer.length; i++) {
            // Create JPanel for each row
            JPanel rowPanel = new JPanel(new GridLayout(1, 3));
            rowPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            rowPanel.setMaximumSize(new Dimension(1000, 80));
            rowPanel.setMinimumSize(new Dimension(100, 60));

            // Create JLabels for name and email
            JLabel nameLabelRow = new JLabel(lecturer[i].getUsername());
            JLabel emailLabelRow = new JLabel(lecturer[i].getEmail());
            JPanel buttonPanel = new JPanel();
            JButton approveButton = new JButton("Approve");
            approveButton.setActionCommand(String.valueOf(i));
            JButton rejectButton = new JButton("Reject");
            rejectButton.setActionCommand(String.valueOf(i));

            approveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int a = Integer.valueOf(e.getActionCommand());
                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(mainPanel,
                            "Are you sure to approve Dr." + lecturer[a].getUsername() + "?",
                            "Approval lecturer registration",
                            JOptionPane.YES_NO_OPTION)) {
                        sql.lecturerApproval(true, lecturer[a].getUserID());
                        clearPanel(mainPanel);
                        approvalListPanel(mainPanel);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                }
            });
            
            rejectButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int a = Integer.valueOf(e.getActionCommand());
                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(mainPanel,
                            "Are you sure to reject Dr." + lecturer[a].getUsername() + "?\n The registration will be removed!",
                            "Reject lecturer registration",
                            JOptionPane.YES_NO_OPTION)) {
                        sql.deleteLecturer(lecturer[a].getUserID());
                        clearPanel(mainPanel);
                        approvalListPanel(mainPanel);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                }
            });

            // Set horizontal alignment for labels
            nameLabelRow.setHorizontalAlignment(SwingConstants.CENTER);
            emailLabelRow.setHorizontalAlignment(SwingConstants.CENTER);

            // Add components to row panel
            rowPanel.add(nameLabelRow);
            rowPanel.add(emailLabelRow);
            buttonPanel.add(approveButton);
            buttonPanel.add(rejectButton);
            rowPanel.add(buttonPanel);

            // Add row panel to main panel
            mainPanel.add(rowPanel);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
        setFont(form);
    }
    
    public void viewLecturerFrame() {
        form = new JFrame("View Lecturer Information");
        form.setSize(800, 600);
        form.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        viewListPanel(mainPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        form.add(scrollPane, BorderLayout.CENTER);

        form.setLocationRelativeTo(null);
        form.setVisible(true);
        closeFrameSetting();
    }
    
    public void viewListPanel(JPanel mainPanel) {
    	mainPanel.removeAll(); // Clear the panel before adding new components

        User[] lecturer = sql.readLecturer();

        // Create title row panel
        JPanel titlePanel = new JPanel(new GridLayout(1, 3));
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        titlePanel.setMaximumSize(new Dimension(1000, 80));
        JLabel nameLabel = new JLabel("Name");
        JLabel emailLabel = new JLabel("Email");
        JLabel approvalLabel = new JLabel("Delete");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
        approvalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(nameLabel);
        titlePanel.add(emailLabel);
        titlePanel.add(approvalLabel);
        mainPanel.add(titlePanel);
        
        //if there is not lecturer
        if(lecturer == null) {
        	
        }

        for (int i = 0; i < lecturer.length; i++) {
            // Create JPanel for each row
            JPanel rowPanel = new JPanel(new GridLayout(1, 3));
            rowPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            rowPanel.setMaximumSize(new Dimension(1000, 80));
            rowPanel.setMinimumSize(new Dimension(100, 60));

            // Create JLabels for name and email
            JLabel nameLabelRow = new JLabel(lecturer[i].getUsername());
            JLabel emailLabelRow = new JLabel(lecturer[i].getEmail());
            JPanel buttonPanel = new JPanel();
            JButton deleteButton = new JButton("Delete");
            deleteButton.setActionCommand(String.valueOf(i));

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int a = Integer.valueOf(e.getActionCommand());
                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(mainPanel,
                            "Are you sure to delete Dr." + lecturer[a].getUsername() + "?",
                            "Delete lecturer",
                            JOptionPane.YES_NO_OPTION)) {
                        sql.deleteLecturer(lecturer[a].getUserID());
                        clearPanel(mainPanel);
                        viewListPanel(mainPanel);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                }
            });

            // Set horizontal alignment for labels
            nameLabelRow.setHorizontalAlignment(SwingConstants.CENTER);
            emailLabelRow.setHorizontalAlignment(SwingConstants.CENTER);

            // Add components to row panel
            rowPanel.add(nameLabelRow);
            rowPanel.add(emailLabelRow);
            buttonPanel.add(deleteButton);
            rowPanel.add(buttonPanel);

            // Add row panel to main panel
            mainPanel.add(rowPanel);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
        setFont(form);
    }

    public void clearPanel(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }
    
    public void closeFrameSetting() {
    	setFont(form);
    	form.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	form.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(form, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // Close the frame
                	form.dispose();
                	form.removeAll();
                	setVisible(true);
                }
            }
        });
    }
	
    public void setFont(JFrame frame) {
		//change the font size and type of font
		Font newFont = new Font("Verdana", Font.BOLD, 12);
		updateFonts(frame, newFont);
	}
    
    // Update font for a single component
    public void updateFont(Component component, Font newFont) {
        component.setFont(newFont);
    }

    // Recursively update font for all components in a container
    public void updateFonts(Container container, Font newFont) {
        for (Component component : container.getComponents()) {
            updateFont(component, newFont);

            if (component instanceof Container) {
                updateFonts((Container) component, newFont);
            }
        }
    }
}

class TimeSelector extends JPanel {
    private JComboBox<String> hourComboBox;
    private JComboBox<String> minuteComboBox;

    public TimeSelector() {
        setLayout(new FlowLayout());

        hourComboBox = new JComboBox<>(new String[]{"8", "9", "10", "11", "12","13","14","15","16","17","18","19","20","21","22","23"});
        minuteComboBox = new JComboBox<>(new String[]{"00"});
        minuteComboBox.setEditable(false);

        add(hourComboBox);
        add(new JLabel(":"));
        add(minuteComboBox);
    }
    
    public void setHour(String hour) {
    	hourComboBox.setSelectedItem(hour);
    }
    
    public void setMinute(String minute){
    	minuteComboBox.setSelectedItem(minute);
    }
    
    public JComboBox<String> getHour(){
    	return hourComboBox;
    }
    
    public JComboBox<String> getMinute(){
    	return minuteComboBox;    	
    }
}