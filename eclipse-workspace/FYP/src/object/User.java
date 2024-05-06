package object;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JLabel;

public class User implements Serializable{
	//user information
	private int userID;
	private String username;
	private String email;
	private String password;
	private String userType;

	transient private int sixDigitCode;
	
	//user task data
	private Task[] task;
	private RepeatTask[] repeatTask;
	private Assignment[] assignment;
	private Timetable[] timetable;
	
	//use to record the number in array to reduce the searching time
	private int tasknow;
	
	//timer six digit code
	Timer timer;

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Task[] getTask() {
		return task;
	}
	public void setTask(Task[] task) {
		this.task = task;
	}
	public RepeatTask[] getRepeatTask() {
		return repeatTask;
	}
	public void setRepeatTask(RepeatTask[] repeatTask) {
		this.repeatTask = repeatTask;
	}
	public Assignment[] getAssignment() {
		return assignment;
	}
	public void setAssignment(Assignment[] assignment) {
		this.assignment = assignment;
	}
	public Timetable[] getTimetable() {
		return timetable;
	}
	public void setTimetable(Timetable[] timetable) {
		this.timetable = timetable;
	}
	public int getSixDigitCode() {
		return sixDigitCode;
	}
	public void setSixDigitCode(int sixDigitCode) {
		this.sixDigitCode = sixDigitCode;
	}

	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public int getTasknow() {
		return tasknow;
	}
	public void setTasknow(int tasknow) {
		this.tasknow = tasknow;
	}
	public Boolean Login(SQLConnect sql, User user) throws ClassNotFoundException, SQLException{
		boolean x = sql.loginVerification(email, password, user);
		return x;
	}
	
	public Boolean sendsixDigitCode(String email, JLabel info) {
		int delay = 0;
	    int period = 1000; // repeat every one second
	    timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {
	    	int x = 0;
			public void run() {
				if(x == 0) {
					generateCode();
					x = 5*60;
				}
				String text = "<html>The 6-digit code is send to you email already.(Maka sure you email is correct)<br>Your Email: "+ email
				+"<br> It will expired in "+ (int)(x/60) +"min "+ x%60 +"s" +"<html>";
				info.setText(text); 
				x--;
			}
	    }, delay, period);
		return sendVerificationEmail(email);
	}
	
	private String generateCode() {
		Random random = new Random();
        sixDigitCode = 100_000 + random.nextInt(900_000);
        return String.valueOf(sixDigitCode);
	}
	
	@SuppressWarnings("finally")
	public Boolean sendVerificationEmail(String userEmail) {
        // Your email configuration
        final String username = "calendarsystemforfsktmstudent@gmail.com";
        final String password = "bxxhkfkcuehrnyxk";
		Boolean success = false;
		
        Properties props = new Properties();
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject("Email Verification Code");
            
            String htmlcode = "<!DOCTYPE html>\r\n"
            		+ "<html lang=\"en\">\r\n"
            		+ "<head>\r\n"
            		+ "    <meta charset=\"UTF-8\">\r\n"
            		+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
            		+ "    <title>Email Verification</title>\r\n"
            		+ "    <style>\r\n"
            		+ "        body {\r\n"
            		+ "            font-family: Arial, sans-serif;\r\n"
            		+ "            background-color: #f4f4f4;\r\n"
            		+ "            margin: 0;\r\n"
            		+ "            padding: 20px;\r\n"
            		+ "            text-align: center;\r\n"
            		+ "        }\r\n"
            		+ "\r\n"
            		+ "        .container {\r\n"
            		+ "            background-color: #fafafa;\r\n"
            		+ "            border-radius: 10px;\r\n"
            		+ "            padding: 20px;\r\n"
            		+ "            max-width: 600px;\r\n"
            		+ "            margin: 0 auto;\r\n"
            		+ "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\r\n"
            		+ "        }\r\n"
            		+ "\r\n"
            		+ "        h1 {\r\n"
            		+ "            color: #fff;\r\n"
            		+ "            background: #4184f3;\r\n"
            		+ "            text-align: center;\r\n"
            		+ "        }\r\n"
            		+ "\r\n"
            		+ "        p {\r\n"
            		+ "            color: #555555;\r\n"
            		+ "            line-height: 1.6;\r\n"
            		+ "        }\r\n"
            		+ "\r\n"
            		+ "        .verification-code {\r\n"
            		+ "            font-size: 24px;\r\n"
            		+ "            color: #007bff;\r\n"
            		+ "            margin-top: 20px;\r\n"
            		+ "        }\r\n"
            		+ "    </style>\r\n"
            		+ "</head>\r\n"
            		+ "<body>\r\n"
            		+ "    <div class=\"container\">\r\n"
            		+ "    	   <h1>Email Verification</h1>\r\n"
            		+ "        <p>Your verification code is:</p>\r\n"
            		+ "        <div class=\"verification-code\">" + sixDigitCode + "</div>\r\n"
            		+ "        <p>Please use this code to complete the verification process.</p>\r\n"
            		+ "    </div>\r\n"
            		+ "</body>\r\n"
            		+ "</html>";
            
            message.setContent(htmlcode, "text/html");

            Transport.send(message);

            success = true;

        } catch (MessagingException e) {
        	System.out.println(e);
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }
	
	public void findNowArrayTask(Date date) {
		int i = 0, max = task.length;
		
		
	}
}
