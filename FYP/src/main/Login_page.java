package main;
import object.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class Login_page {
	private JFrame frame;
	private JTextField emailField;
	private JTextField userField;
    private JPasswordField passwordField;
	private JTextField emailFieldR;
    private JPasswordField passwordFieldR;
    private JPasswordField confirmPasswordField;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel loginpanel;
    private JPanel registerpanel;
    Home_Page hp = null;
	
	static Login_page window;
	
	SQLConnect sql = new SQLConnect();
	User user;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Login_page();
					FileInputStream filein;
					try {
						filein = new FileInputStream("C:\\Users\\MSI Prestige\\eclipse-workspace\\FYP\\src\\data\\user_info");
						ObjectInputStream in = new ObjectInputStream(filein);
						window.user =(User)in.readObject();
						in.close();
						filein.close();
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
					
					if(window.user == null) {
						window.frame.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(window.frame,
					            "Welcome " + window.user.getUsername(),
					            "Login Successful", JOptionPane.INFORMATION_MESSAGE);
						window.hp = new Home_Page(window, window.sql);
						window.hp.getFrame().setVisible(true);
						window.frame.setVisible(false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 * @throws FileNotFoundException 
	 */
	public Login_page() {
		initialize();
	}
	
	private void initialize() {

		ImageIcon imageIcon =  new ImageIcon("src\\pic\\calendarBackground.png");
		Image image = imageIcon.getImage();
		Image newimg = image.getScaledInstance(720, 600, java.awt.Image.SCALE_SMOOTH);
        ImageIcon tf = new ImageIcon(newimg);
        ImageIcon kl = new ImageIcon("src\\pic\\klee.jpg");
        
		frame = new JFrame();
		frame.setForeground(Color.RED);
		frame.getContentPane().setBackground(new Color(240, 240, 240));
		frame.setResizable(true);
		frame.setBounds(400, 150, 720, 550);
		frame.setTitle("Calendar System for FSKTM Student");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		frame.setIconImage(kl.getImage());
		
		loginpanel = new JPanel();
		loginpanel.setBounds(112, 75, 460, 335);
		loginpanel.setLayout(new GridBagLayout());
		loginpanel.setBackground(new Color(255,255,255,200));
		
		registerpanel = new JPanel();
		registerpanel.setBounds(112, 75, 460, 335);
		registerpanel.setLayout(new GridBagLayout());
		registerpanel.setBackground(new Color(255,255,255,200));
		
		login(loginpanel);
		register(registerpanel);
		
		 // Create the card panel with CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBounds(112, 75, 460, 335);
        cardPanel.setBackground(new Color(255,255,255,200));
        
		// Add panels to card panel
        cardPanel.add(loginpanel, "login");
        cardPanel.add(registerpanel, "register");
        
        // Add the cardPanel to frame
		frame.add(cardPanel);

        //Background Image
        JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(0, 0, 696, 503);
		frame.getContentPane().add(lblNewLabel);
		lblNewLabel.setIcon(tf);
		
        // Make the frame visible
        frame.setVisible(true);
        
        //change font size and type of font
        Font newFont = new Font("Verdana", Font.BOLD, 12);
        updateFonts(frame, newFont);
	}

    private void login(JPanel panel) {
        // Components
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        emailField.setColumns(12);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        passwordField.setColumns(12);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        
        JLabel newUser = new JLabel("New User");
        JLabel line = new JLabel("---------------------------------------------------------------");

        // Hyperlink style "Forgot Password?" label
        JLabel forgotPasswordLabel = new JLabel("<html><u>Forgot Password?</u></html>");
        forgotPasswordLabel.setForeground(Color.BLUE);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showForgetPasswordDialog();
            }
        });

        // Add components to the panel with GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 0;
        panel.add(forgotPasswordLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);
        
//        gbc.gridx = 1;
//        gbc.gridy = 3;
//        panel.add(cancelButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(newUser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(line, gbc);

        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	user = new User();
                String email = emailField.getText();
                char[] passwordC = passwordField.getPassword();
                String password = String.valueOf(passwordC);
                user.setEmail(email);
                user.setPassword(password);
                
                try {
					if(user.Login(sql, user)) {
						JOptionPane.showMessageDialog(frame,
					            "Welcome " + user.getUsername(),
					            "Login Successful", JOptionPane.INFORMATION_MESSAGE);
						hp = new Home_Page(window, sql);
						hp.getFrame().setVisible(true);
						frame.setVisible(false);
		                //update the auto login about user information into file
		                insertToFile();
					}else {
						JOptionPane.showMessageDialog(
					            null,
					            "Login failed. Please make sure you enter it correctly and try again.",
					            "Login Failed",
					            JOptionPane.ERROR_MESSAGE);
					}
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
           }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement your registration logic here
            	cardPanel.setVisible(false);
                cardLayout.show(cardPanel, "register");
                cardPanel.setVisible(true);
            }
        });
    }
	
    private void register(JPanel panel) {
    	// Components
        JLabel emailLabel = new JLabel("Email:");
        emailFieldR = new JTextField();
        emailFieldR.setColumns(12);
        
        JLabel userLabel = new JLabel("Username:");
        userField = new JTextField();
        userField.setColumns(12);

        JLabel passwordLabel = new JLabel("Password:");
        passwordFieldR = new JPasswordField();
        passwordFieldR.setColumns(12);
        
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setColumns(12);

        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        
        JLabel oldUser = new JLabel("Have account already ??");
        JLabel line = new JLabel("---------------------------------------------------------------");

        // Add components to the panel with GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        panel.add(emailFieldR, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        panel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordFieldR, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(line, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        panel.add(oldUser, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardPanel.setVisible(false);
                cardLayout.show(cardPanel, "login");
                cardPanel.setVisible(true);
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(checkValidInput(emailFieldR, passwordFieldR, confirmPasswordField, userField)) {
            		frame.setVisible(false);
                    user = new User();
                    user.setEmail(emailFieldR.getText());
                    user.setPassword(String.valueOf(passwordFieldR.getPassword()));
                    user.setUsername(userField.getText());
            		verificationPage(frame, "register");
            	}
            }
        });
    }
	
    //forget password
	public void showForgetPasswordDialog() {
		JDialog dialog = new JDialog();
		dialog.setTitle("Forget Password");
		dialog.setSize(300, 150);
		dialog.setLayout(new GridLayout(4, 2, 10, 10));
		
		JLabel emailLabel = new JLabel("Email:");
		JTextField emailTextField = new JTextField();
		
		JLabel newPasswordLabel = new JLabel("New Password:");
		JPasswordField newPasswordField = new JPasswordField();
		
		JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
		JPasswordField confirmPasswordField = new JPasswordField();
		
		JButton submitButton = new JButton("Submit");
		JButton cancelButton = new JButton("Cancel");
		
		submitButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	if(checkValidInput(emailTextField, newPasswordField, confirmPasswordField, new JTextField("temp"))){
		            user = new User();
		            user.setEmail(emailTextField.getText());
		            user.setPassword(String.valueOf(newPasswordField.getPassword()));
		    		verificationPage(frame, "forget");
		    		dialog.dispose();
		    	}
		    }
		});
		
		cancelButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        dialog.dispose(); 
		    }
		});
	
	    dialog.add(emailLabel);
	    dialog.add(emailTextField);
	    dialog.add(newPasswordLabel);
	    dialog.add(newPasswordField);
	    dialog.add(confirmPasswordLabel);
	    dialog.add(confirmPasswordField);
	    dialog.add(submitButton);
	    dialog.add(cancelButton);
	
	    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    dialog.setLocationRelativeTo(frame);
	    dialog.setVisible(true);
	}
	    
    //when user click the register or forget password will use this
	private void verificationPage(JFrame frame, String registerOrForget) {
		
        JDialog vp = new JDialog();
        vp.setTitle("Verification email");
        vp.setLayout(new BorderLayout());
        
        JLabel info = new JLabel("<html>The 6-digit code is send to you email already."
        		+ "<br>(Maka sure you email is correct) <br> It will expired in 5 min.<html>");
        
        JTextField code = new JTextField();
        code.setColumns(24);
        
        JButton verifyButton = new JButton("Verify");
        JButton resendButton = new JButton("Resend");
        JPanel buttonPl = new JPanel();
        buttonPl.add(verifyButton);
        buttonPl.add(resendButton);

        verifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//if user enter correct verification code
                if (user.getSixDigitCode() == Integer.parseInt(code.getText())) {
                    JOptionPane.showMessageDialog(vp, "Verification successful!\nTry login in login page");
                    //switch user to login page
                    cardLayout.show(cardPanel, "login");
                    emailField.setText(user.getEmail());
                    passwordField.setText(user.getPassword());
                    
                    //to know the type of verification 
                    if(registerOrForget.equals("register")) {
                    	afterRegister();
                    }else if(registerOrForget.equals("forget")) {
                    	afterForget();
                    }
                    
                    vp.dispose(); // Close the pop-up after successful verification
                } else {
                    JOptionPane.showMessageDialog(vp, "Invalid verification code. Please try again.");
                }
            }
        });
        //re-send the verification code
        resendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(vp, "Resending verification code...");
                user.sendVerificationEmail(emailFieldR.getText());
            }
        });
        
        //when user click x button at pop-up window
        vp.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		frame.setVisible(true);
        	}
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(info, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("<html>Enter 6-digit verification code:<html>"), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(code, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(buttonPl, gbc);

        vp.getContentPane().add(panel, BorderLayout.CENTER);
        vp.setSize(600, 300);
        vp.setLocationRelativeTo(null); // Center the frame on the screen
        vp.setVisible(true);
        
		//sending verification code to user email with a multitasking
		VC vc = new VC(user, frame, vp, info);
		vc.start();
		
    }
    
	//when user click logout in home page, this will be run and clear the home page
    public void logout() {
    	hp.getFrame().removeAll();
    	hp.getFrame().setVisible(false);
    	hp = null;
    	user = null;
    	insertToFile();
    	frame.setVisible(true);
    	user = null;
    }
    
    public void afterRegister() {
    	//clear the register page field
        emailFieldR.setText(null);
        passwordFieldR.setText(null);
        userField.setText(null);
        confirmPasswordField.setText(null);
        sql.register(user);//record registered user into database
        frame.setVisible(true);
    }
    
    public void afterForget() {
    	sql.forgetPassword(user);//record registered user into database
        frame.setVisible(true);
    }
    
    public Boolean checkValidInput(JTextField emailFieldR, JPasswordField passwordFieldR, JPasswordField confirmPasswordField, JTextField userField) {
        String password = String.valueOf(passwordFieldR.getPassword());
        String Cpassword = String.valueOf(confirmPasswordField.getPassword());
        //Check the empty input
     	if(emailFieldR.getText().equals("") || password.equals("") || userField.getText().equals("") || Cpassword.equals("") ) {
     		JOptionPane.showMessageDialog(frame, "Please input all information", "Error", JOptionPane.ERROR_MESSAGE);
             return false;
     	}
     	//Check if email error
     	if (!isValidEmail(emailFieldR.getText())) {
             JOptionPane.showMessageDialog(frame, "Invalid email address", "Error", JOptionPane.ERROR_MESSAGE);
             return false;
         }
         //Check if passwords match
         if (!password.equals(Cpassword)) {
             JOptionPane.showMessageDialog(frame, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
             return false;
         }
         // Check if passwords match
         if (!userField.getText().equals("temp") && sql.existEmail(emailFieldR.getText())) {
             JOptionPane.showMessageDialog(frame, "Email have registered", "Error", JOptionPane.ERROR_MESSAGE);
             return false;
         }
         return true;
    }
    
    //check the user email correct or not
    public Boolean isValidEmail(String email) {
    	String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@" 
    	        + "[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    	Pattern pat = Pattern.compile(regexPattern); 
    	return pat.matcher(email).matches();
    }
    
    //insert the user information into file
    public void insertToFile() {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream("src\\data\\user_info");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(user);
			out.close();
			fileOut.close();
		} catch (IOException e1) {
			e1.printStackTrace();
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

class VC extends Thread{
	User user;
	JFrame frame;
	JLabel info;
	JDialog vp;
	
	VC(User user, JFrame frame, JDialog vp, JLabel info){
		this.user = user;
		this.frame = frame;
		this.vp = vp;
		this.info = info;
	}
	public void run() {
		if(!user.sendsixDigitCode(user.getEmail(), info)) {
			JOptionPane.showMessageDialog(frame, "Error in sending the verification code.\nPlease check your internet connection.", "Error", JOptionPane.WARNING_MESSAGE);
			vp.dispose();
			frame.setVisible(true);
		}
	}
}

