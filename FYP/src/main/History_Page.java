package main;
import object.Task;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import object.SQLConnect;

public class History_Page {
    private JFrame frame;
    private JTextField searchTextField;
    private JComboBox<String> categoryComboBox;
    private JTable taskTable;

    private SQLConnect sql;
    static Login_page login;

    public History_Page(Login_page login, SQLConnect sql) {
        this.login = login;
        this.sql = sql;
        initialize();
    }

    private void initialize() {

        frame = new JFrame();
        frame.setForeground(Color.RED);
        frame.getContentPane().setBackground(new Color(240, 240, 240));
        frame.setResizable(true);
        frame.setSize(1280,720);
        frame.setTitle("Calendar System for FSKTM Student (History Page)");
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent arg0) {
                login.insertToFile();
            }
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        // Search Bar
        searchTextField = new JTextField(20);
        searchPanel.add(searchTextField);

        // Category Dropdown
        String[] categories = {"All", "Task", "Repeat Task", "Assignment"};
        categoryComboBox = new JComboBox<>(categories);
        searchPanel.add(categoryComboBox);

        // Search Button
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Table
        taskTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(taskTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Table Headers
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Title");
        model.addColumn("Due Date");
        model.addColumn("Description");
        taskTable.setModel(model);

        // Set preferred column widths based on a ratio
        int totalColumns = model.getColumnCount();
        int[] columnWidthRatios = {3, 2, 5};

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performSearch(model);
            }
        });

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);

        // Set column widths based on ratio (after frame is visible)
        setColumnWidths(taskTable, model, columnWidthRatios);

        // Make the table uneditable
        taskTable.setEnabled(false);

        // Change font size and type of font
        Font newFont = new Font("Verdana", Font.BOLD, 24);
        updateFonts(frame, newFont);
    }

    private void performSearch(DefaultTableModel model) {
        Task[] temptask = null;
        Boolean[] category = {false,false,false};
        // Category separation
        if(categoryComboBox.getSelectedIndex() == 0) {
            category[0] = true;
            category[1] = true;
            category[2] = true;
        } else {
            category[categoryComboBox.getSelectedIndex()-1] = true;
        }
        temptask = sql.searchHistoryTask(login.user.getUserID(), category, searchTextField.getText());

        // Clear existing rows from the table
        model.setRowCount(0);
        taskTable.setRowHeight(30);
        for (Task task : temptask) {
            Object[] rowData = {task.getTaskTitle(), task.getDueDate(), task.getDescription()};
            model.addRow(rowData);
        }
    }

    // Set preferred column widths based on a ratio
    private void setColumnWidths(JTable table, DefaultTableModel model, int[] columnWidthRatios) {
        int totalColumns = model.getColumnCount();
        int totalRatio = Arrays.stream(columnWidthRatios).sum();
        TableColumnModel columnModel = table.getColumnModel();

        for (int i = 0; i < totalColumns; i++) {
            TableColumn column = columnModel.getColumn(i);
            int width = (int) ((double) columnWidthRatios[i] / totalRatio * table.getWidth());
            column.setPreferredWidth(width);
        }
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
