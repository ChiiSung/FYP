package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

//"calendarsystemfsktm@gmail.com","calendarsystem123++"

public class Test {
	
	private JFrame frame;
    private JTextField searchTextField;
    private JComboBox<String> categoryComboBox;
    private JTable taskTable;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Test().initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initialize() {
        frame = new JFrame("Task Search App");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        // Search Bar
        searchTextField = new JTextField(20);
        searchPanel.add(searchTextField);

        // Category Dropdown
        String[] categories = {"Task", "Repeat Task", "Assignment"};
        categoryComboBox = new JComboBox<>(categories);
        searchPanel.add(categoryComboBox);

        // Search Button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform search based on the search criteria
                performSearch();
            }
        });
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

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    private void performSearch() {
        // TODO: Implement the search logic based on searchTextField and categoryComboBox values
        // Update the table model with the search results
        // For now, we'll just display a message
        JOptionPane.showMessageDialog(frame, "Performing search...");
    }
}