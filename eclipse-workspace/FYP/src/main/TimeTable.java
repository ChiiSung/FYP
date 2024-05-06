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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.toedter.calendar.JDateChooser;

import object.SQLConnect;

public class TimeTable {
	private JFrame frame;
	private JPanel leftPanel, rightPanel;
	
	//Date format
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SQLConnect sql;
	
	
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
			timetable.add(timePl[i]);
		}
		JScrollPane scrollPane = new JScrollPane(timetable);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		rightPanel.add(scrollPane);
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
		semStartDateLb.setDate(new Date(123, 9, 10));
		semStartDateLb.setDateFormatString("yyyy-MM-dd");
		JLabel weekLb = new JLabel("Week");
		JTextField weekQuantityLb = new JTextField("14");
		
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
				
			}
		});
		JButton saveBt = new JButton("Save and Exit");
		saveBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		JButton cancelBt = new JButton("Cancel and Exit");
		cancelBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		downPl.add(addCourseBt);downPl.add(saveBt);downPl.add(cancelBt);
		
		leftPanel.add(usernameLb);
		leftPanel.add(upPl);
		leftPanel.add(downPl);
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
