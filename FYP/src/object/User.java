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
				String text = "<html>The 6-digit code is send to you email already.(Maka sure you email is correct)<br>Your Email:"+ email
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
            message.setText("Your verification code is: " + sixDigitCode);

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
