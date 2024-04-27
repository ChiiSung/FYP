package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.*;

import object.Course;
import object.SQLConnect;

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
            	ApprovalLecturerFrame();
            	setVisible(false);
            }
        });

        addCourseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addCourseFrame();
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
        form.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
        cmbCourseType = new JComboBox<>(new String[]{"Kuliah", "Lab"});

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
        cmbDuration = new JComboBox<>(new String[]{"2", "3"});;

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
    
    public void ApprovalLecturerFrame() {
    	form = new JFrame("Approval Lecturer Form");
        form.setSize(400, 300);
        form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        form.setLayout(new GridLayout(8, 2));

        form.setLocationRelativeTo(null);
        form.setVisible(true);
        closeFrameSetting();
    }
    
    public void closeFrameSetting() {
    	setFont(form);
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
        minuteComboBox = new JComboBox<>(new String[]{"00", "15", "30", "45"});

        add(hourComboBox);
        add(new JLabel(":"));
        add(minuteComboBox);
    }
    
    public JComboBox<String> getHour(){
    	return hourComboBox;
    }
    
    public JComboBox<String> getMinute(){
    	return minuteComboBox;    	
    }
}