package object;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

public class SQLConnect {
	private String host = "localhost:3306";
	private String databaseName = "calendar_system";
	private String username = "root";
	private String password = "";
	
	Connection con ;
	
	public SQLConnect(){
		try {
			con = DriverManager.getConnection("jdbc:mysql://"+ host +"/" + databaseName, this.username, this.password);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public Boolean loginVerification(String email, String password, User user) throws ClassNotFoundException, SQLException {
		String command = "SELECT userID, username FROM user WHERE email =? AND password =? AND userType =?";
		String command2 = "SELECT sucess FROM lecturerverify WHERE userId =?";
		PreparedStatement stmt;
		stmt = con.prepareStatement(command);
		stmt.setString(1,email);
		stmt.setString(2, password);
		stmt.setString(3, user.getUserType());
		
		ResultSet rs = stmt.executeQuery();
		boolean veri = rs.next();
		if(veri) {
			user.setUserID(rs.getInt("userID"));
			user.setUsername(rs.getString("username"));
			if(user.getUserType().equals("Lecturer")) {
				stmt = con.prepareStatement(command2);
				stmt.setInt(1,user.getUserID());
				rs = stmt.executeQuery();
				rs.next();
				veri = rs.getBoolean("sucess");
				if(!veri) {
					JOptionPane.showMessageDialog(
			            null,
			            "Login failed. Needed approval from admin",
			            "Login Failed",
			            JOptionPane.ERROR_MESSAGE);
				}
			}
		}else{
			JOptionPane.showMessageDialog(
	            null,
	            "Login failed. Please make sure you enter it correctly and try again.",
	            "Login Failed",
	            JOptionPane.ERROR_MESSAGE);
		}

		rs.close();
		stmt.close();
		
		return veri;
	}
	
	public void savingTask(String taskTitle, Date dueDate, String description, int userId, Boolean completed) {
		String command = "INSERT INTO task (userId, taskTitle, dueDate, description, completed) VALUES(?,?,?,?,?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(command);
			stmt.setInt(1,userId);
			stmt.setString(2, taskTitle);
			stmt.setDate(3, new java.sql.Date(dueDate.getYear(),dueDate.getMonth(),dueDate.getDate()));
			stmt.setString(4, description);
			stmt.setBoolean(5,completed);
			stmt.execute();
			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readTask(User user, int userId) {
		Task temp;
		Task[] temparr;
		
		String command = "SELECT taskId, taskTitle, dueDate, description, completed FROM task WHERE userId ='"+ userId + "' ORDER BY dueDate";
		
		try {
			String count = "SELECT COUNT(*) FROM task WHERE userId ='"+ userId + "'";
			Statement countstmt = con.createStatement();
			ResultSet countrs = countstmt.executeQuery(count);
			countrs.next();
			temparr = new Task[countrs.getInt(1)];
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			int i = 0;
			while(rs.next()) {
				temp = new Task();
				temp.setTaskId(rs.getInt(1));
				temp.setTaskTitle(rs.getString(2));
				temp.setDueDate(rs.getDate(3));
				temp.setDescription(rs.getString(4));
				temp.setCompleted(rs.getBoolean(5));
				temparr[i] = temp;
				i++;
			}
			
			user.setTask(temparr);
			countstmt.close();
			countrs.close();
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void editTask(Task task) {
		String command = "UPDATE task SET taskTitle = ?, dueDate = ?, description = ?, completed = ? WHERE taskId = '" + task.getTaskId() + "'";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(command);
			stmt.setString(1,task.getTaskTitle());
			stmt.setDate(2,new java.sql.Date(task.getDueDate().getYear(),task.getDueDate().getMonth(),task.getDueDate().getDate()));
			stmt.setString(3,task.getDescription());
			stmt.setBoolean(4,task.getCompleted());
			
			stmt.execute();
			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteTask(int taskId){
		String command = "DELETE FROM task WHERE taskId = '" + taskId + "'";
		try {
			Statement stmt = con.createStatement();
			stmt.execute(command);
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Task[] searchTask(User user, String taskTitle) {
		String command = "SELECT taskId, taskTitle, dueDate, description, completed FROM task WHERE userId ='" + user.getUserID() + "' AND taskTitle like '%" + taskTitle + "%'" ;
		//limit the search's task in the search container
		Task[] task = new Task[20];
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			int i = 0;
			while(rs.next() && i<20) {
				task[i] = new Task();
				task[i].setTaskId(rs.getInt(1));
				task[i].setTaskTitle(rs.getString(2));
				task[i].setDueDate(rs.getDate(3));
				task[i].setDescription(rs.getString(4));
				task[i].setCompleted(rs.getBoolean(5));
				i++;
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return task;
	}
	
	public void savingRepeatTask(String taskTitle, Date dueDate, String description, int userId, String repeatType, int frequency, Date endDate, Boolean completed) {
		String command = "INSERT INTO repeattask (userId, taskTitle, dueDate, description, completed, repeatType, frequancyRepeat, endDate) VALUES(?,?,?,?,?,?,?,?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(command);
			stmt.setInt(1,userId);
			stmt.setString(2, taskTitle);
			stmt.setDate(3, new java.sql.Date(dueDate.getYear(),dueDate.getMonth(),dueDate.getDate()));
			stmt.setString(4, description);
			stmt.setBoolean(5,completed);
			stmt.setString(6, repeatType);
			stmt.setInt(7, frequency);
			stmt.setDate(8, new java.sql.Date(endDate.getYear(),endDate.getMonth(),endDate.getDate()));
			stmt.execute();
			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readRepeatTask(User user, int userId){
		RepeatTask temp;
		RepeatTask[] temparr;
		
		String command = "SELECT repeatId, taskTitle, dueDate, description, completed, repeatType, frequancyRepeat, endDate FROM repeattask WHERE userId ='"+ userId + "' ORDER BY dueDate";
		
		try {
			String count = "SELECT COUNT(*) FROM repeattask WHERE userId ='"+ userId + "'";
			Statement countstmt = con.createStatement();
			ResultSet countrs = countstmt.executeQuery(count);
			countrs.next();
			temparr = new RepeatTask[countrs.getInt(1)];
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			int i = 0;
			while(rs.next()) {
				temp = new RepeatTask();
				temp.setTaskId(rs.getInt(1));
				temp.setTaskTitle(rs.getString(2));
				temp.setDueDate(rs.getDate(3));
				temp.setDescription(rs.getString(4));
				temp.setCompleted(rs.getBoolean(5));
				temp.setRepeatType(rs.getString(6));
				temp.setFrequencyRepeat(rs.getInt(7));
				temp.setEndDate(rs.getDate(8));
				temparr[i] = temp;
				i++;
			}
			
			user.setRepeatTask(temparr);
			countstmt.close();
			countrs.close();
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void editRepeatTask(RepeatTask task) {
		String command = "UPDATE repeattask SET taskTitle = ?, dueDate = ?, description = ?, completed = ?, repeatType = ?, frequancyRepeat = ?, endDate = ? WHERE repeatId = '" + task.getTaskId() + "'";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(command);
			stmt.setString(1,task.getTaskTitle());
			stmt.setDate(2,new java.sql.Date(task.getDueDate().getYear(),task.getDueDate().getMonth(),task.getDueDate().getDate()));
			stmt.setString(3,task.getDescription());
			stmt.setBoolean(4,task.getCompleted());
			stmt.setString(5, task.getRepeatType());
			stmt.setInt(6, task.getFrequencyRepeat());
			stmt.setDate(7, new java.sql.Date(task.getEndDate().getYear(),task.getEndDate().getMonth(),task.getEndDate().getDate()));

			stmt.execute();
			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteRepeatTask(int taskId){
		String command = "DELETE FROM repeattask WHERE repeatId = '" + taskId + "'";
		try {
			Statement stmt = con.createStatement();
			stmt.execute(command);
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Boolean existEmail(String email) {
		String command = "SELECT * FROM user WHERE email ='"+ email + "'";
		Statement stmt;
		boolean exist = true;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			exist = rs.next();
			
			stmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exist;
	}
	
	public void register(User user) {
		String command = "INSERT INTO user (username, email, password, userType) VALUES(?,?,?,?)";
		String command2 = "INSERT INTO lecturerverify (userId, sucess) VALUES(?,?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);;
			stmt.setString(1,user.getUsername());
			stmt.setString(2, user.getEmail());
			stmt.setString(3,user.getPassword());
			stmt.setString(4,user.getUserType());
			// Execute first SQL statement
			stmt.executeUpdate();

		    // Get the generated user ID
		    ResultSet generatedKeys = stmt.getGeneratedKeys();
		    int userId = -1;
		    if (generatedKeys.next()) {
		        userId = generatedKeys.getInt(1);
		    }
		    PreparedStatement stmt2 = con.prepareStatement(command2);
		    stmt2.setInt(1, userId);
		    stmt2.setBoolean(2, false); 
		    stmt2.execute();
		    
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getUserEmail(int userID) {
		String command = "SELECT email FROM user WHERE userID = '"+ userID +"'";
		Statement stmt;
		String email = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			rs.next();
			email = rs.getString("email");
			
			stmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return email;
	}
	
	public void forgetPassword(User user) {
		String command = "UPDATE user SET password = ? WHERE email = '" + user.getEmail() + "'";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(command);
			stmt.setString(1,user.getPassword());
			stmt.execute();
			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Task[] searchHistoryTask(int userId, Boolean[] category, String taskTitle) {
		Task[] task = null;
		String condition = "userId = '" + userId + "' AND taskTitle LIKE '%" + taskTitle + "%' AND ";
		String command = "SELECT taskTitle, duedate, description FROM task WHERE " + condition + category[0] + "\r\n"
				+ "UNION\r\n"
				+ "SELECT taskTitle, duedate, description FROM repeatTask WHERE " + condition + category[1] + "\r\n"
				+ "UNION\r\n"
				+ "SELECT taskTitle, duedate, description FROM assignment WHERE " + condition + category[2] ;
		
		String count = "SELECT COUNT(*) FROM (\r\n"
				+ "SELECT taskTitle, duedate, description FROM task WHERE " + condition + category[0] + "\r\n"
				+ "UNION\r\n"
				+ "SELECT taskTitle, duedate, description FROM repeatTask WHERE " + condition + category[1] + "\r\n"
				+ "UNION\r\n"
				+ "SELECT taskTitle, duedate, description FROM assignment WHERE " + condition + category[2] + ") AS total";
		try {
			Statement countstmt = con.createStatement();
			ResultSet countrs = countstmt.executeQuery(count);
			countrs.next();
			task = new Task[countrs.getInt(1)];
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			int i = 0;
			while(rs.next()) {
				task[i] = new Task();
				task[i].setTaskTitle(rs.getString(1));
				task[i].setDueDate(rs.getDate(2));
				task[i].setDescription(rs.getString(3));
				i++;
			}
			
			countstmt.close();
			countrs.close();
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return task;
	}
	
	public void addStudentCourse(User user, String courseCode, int section) {
        String command = "INSERT INTO timetableCourse (timetableId, courseId) " +
                         "VALUES ((SELECT timetableId FROM timetable WHERE userId = ?), " +
                         "(SELECT courseId FROM course WHERE code = ? AND courseSection = ?))";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(command);
            stmt.setInt(1, user.getUserID());
            stmt.setString(2, courseCode);
            stmt.setInt(3, section);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean userHasTimeTable(int userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM timetable WHERE userId = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private void createTimeTableForUser(User user) throws SQLException {
        String query = "INSERT INTO timetable (userId, semStart) VALUES (?, ?)";
        
        // Set the current date as the semester start date
        user.setTimetable(new Timetable());
        java.util.Date currentDate = new java.util.Date();
        user.getTimetable().setSemStart(currentDate);
        
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setInt(1, user.getUserID());
            statement.setDate(2, new java.sql.Date(currentDate.getTime())); // Correctly convert to java.sql.Date
            statement.executeUpdate();
        }
    }
    
    public void readTimetable(User user) {
        String command1 = "SELECT * FROM timetable WHERE userId = ?";//timetable read
        String command2 = "SELECT * FROM course WHERE courseId IN "
        		+ "(SELECT courseId FROM timetablecourse "
        		+ "WHERE timetableId = (SELECT timetableId FROM timetable WHERE userId = ?))";//course read
		PreparedStatement stmt;
    	try {
			if (!userHasTimeTable(user.getUserID())) {
			    createTimeTableForUser(user);
			}else {
				//read timetable
				stmt = con.prepareStatement(command1);
				stmt.setInt(1, user.getUserID());
				ResultSet rs = stmt.executeQuery();
				rs.next();
				user.setTimetable(new Timetable());
				user.getTimetable().setSemStart(rs.getDate("semStart"));
				
				//read course
				stmt = con.prepareStatement(command2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				stmt.setInt(1, user.getUserID());
				rs = stmt.executeQuery();
				// Move to the last row
	            rs.last();
	            // Get the row number which is the count of rows
	            int rowCount = rs.getRow();
	            // Move the cursor back to the start
	            rs.beforeFirst();
	            Course[] temp = new Course[rowCount];
	            rowCount = 0;
				while(rs.next()) {
					temp[rowCount] = new Course();
					temp[rowCount].setCode(rs.getString("code"));
					temp[rowCount].setCourseName(rs.getString("courseName"));
					temp[rowCount].setCourseSection(rs.getInt("courseSection"));
					temp[rowCount].setCourseType(rs.getString("courseType").charAt(0));
					temp[rowCount].setDay(rs.getString("day"));
					temp[rowCount].setDurationTime(rs.getInt("durationTime"));
					temp[rowCount].setTime(rs.getTime("time"));
					temp[rowCount].setCourseId(rs.getInt("courseId"));
					rowCount++;
				}
				user.getTimetable().setCourse(temp);
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void readStudentCourse(String keyword, List<Course> course, int userId) {
		String command = "SELECT * FROM course WHERE (courseName LIKE ? OR code LIKE ?) AND courseId NOT IN (\r\n"
				+ "    SELECT courseId \r\n"
				+ "    FROM timetablecourse \r\n"
				+ "    WHERE timetableId = (\r\n"
				+ "        SELECT timetableId \r\n"
				+ "        FROM timetable \r\n"
				+ "        WHERE userId = ?\r\n"
				+ "    )\r\n"
				+ ")";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(command);
			stmt.setString(1,"%" + keyword + "%");
			stmt.setString(2,"%" + keyword + "%");
			stmt.setInt(3, userId);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Course temp = new Course();
				temp.setCode(rs.getString("code"));
				temp.setCourseName(rs.getString("courseName"));
				temp.setCourseSection(rs.getInt("courseSection"));
				temp.setCourseType(rs.getString("courseType").charAt(0));
				temp.setDay(rs.getString("day"));
				temp.setDurationTime(rs.getInt("durationTime"));
				temp.setTime(rs.getTime("time"));
				temp.setCourseId(rs.getInt("courseId"));
				course.add(temp);
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void removeStudentCourse(int userId, Course course) {
    	String command = "DELETE FROM timetablecourse WHERE courseId = ? AND timetableId = (SELECT timetableId FROM timetable WHERE userId = ?)";
		try {
			PreparedStatement stmt = con.prepareStatement(command);
			stmt.setInt(1, course.getCourseId());
			stmt.setInt(2, userId);
			stmt.execute();
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void removeAllStudentCourse(int userId) {
    	String command = "DELETE FROM timetablecourse WHERE timetableId = (SELECT timetableId FROM timetable WHERE userId = ?)";
		try {
			PreparedStatement stmt = con.prepareStatement(command);
			stmt.setInt(1, userId);
			stmt.execute();
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void updateSemesterStartDate(int userId, Date semesterStart) {
    	String command = "UPDATE timetable SET semStart = ? WHERE timetableId = (SELECT timetableId FROM timetable WHERE userId = ?)";
		try {
			PreparedStatement stmt = con.prepareStatement(command);
			stmt.setDate(1, new java.sql.Date(semesterStart.getYear(),semesterStart.getMonth(),semesterStart.getDate()));
			stmt.setInt(2, userId);
			stmt.execute();
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void readAssignment(User user) {
    	String command = null;
    	PreparedStatement stmt = null;
    	if(user.getUserType().equals("student")) {
    		command = "SELECT * FROM assignment "
    				+ "WHERE courseId IN (SELECT courseId FROM timetablecourse WHERE timetableId = (SELECT timetableId FROM timetable WHERE userId = ?))";
    	}else {
    		command = "SELECT * FROM assignment WHERE userId = ?";
    	}
    	try {
			//read assignment
			stmt = con.prepareStatement(command, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.setInt(1, user.getUserID());
			ResultSet rs = stmt.executeQuery();
			// Move to the last row
            rs.last();
            // Get the row number which is the count of rows
            int rowCount = rs.getRow();
            Assignment[] temp = new Assignment[rowCount];
            rs.beforeFirst();
            rowCount = 0;
			while(rs.next()) {
				temp[rowCount] = new Assignment();
				temp[rowCount].setTaskId(rs.getInt("assignmentId"));
				temp[rowCount].setTaskTitle(rs.getString("taskTitle"));
				temp[rowCount].setDueDate(rs.getDate("dueDate"));
				
				rowCount++;
			}
			user.setAssignment(temp);
			
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
    }
    
    public void saveAssignment(int userId, Assignment asg) {
    	String command = "INSERT INTO assignment(userId, courseId, taskTitle, dueDate, description) VALUES(?,(SELECT courseId FROM course WHERE code = ? AND courseSection = ?),?,?,?)";
		try {
			PreparedStatement stmt = con.prepareStatement(command);
			stmt.setInt(1, userId);
			stmt.setString(2, asg.getCourse().getCode());
			stmt.setInt(3, asg.getCourse().getCourseSection());
			stmt.setString(4, asg.getTaskTitle());
			stmt.setDate(5, new java.sql.Date(asg.getDueDate().getYear(),asg.getDueDate().getMonth(),asg.getDueDate().getDate()));
			stmt.setString(6, asg.getDescription());
			stmt.execute();
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    public void editAssignment(Assignment asg) {
    	
    }
	
	//Administrator part
	public User[] readLecturer(){
		String command = "SELECT * FROM user \r\n"
				+ "INNER JOIN lecturerverify ON lecturerverify.userId = user.userID\r\n"
				+ "WHERE user.userType = 'lecturer' AND lecturerverify.sucess = '1'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			
			User[] lecturer = new User[getLecturerApprovedCount()];
			int a = 0;
			while(rs.next()) {
				lecturer[a] = new User();
                lecturer[a].setUserID(rs.getInt("userId"));
                lecturer[a].setUsername(rs.getString("username"));
                lecturer[a].setEmail(rs.getString("email"));
                a++;
			}
			rs.close();
			stmt.close();
            
            return lecturer;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void deleteLecturer(int lecturerId) {
		//send email to removed lecturer for notification
        final String systemEmail = "calendarsystemforfsktmstudent@gmail.com";
        final String password = "bxxhkfkcuehrnyxk";
        final String lecturerEmail = getUserEmail(lecturerId);
        if(lecturerEmail.equals(null)) {
        	JOptionPane.showMessageDialog(null,
                    "Error: Email is undefined.",
                    "Email Error",
                    JOptionPane.ERROR_MESSAGE);
        }
		Boolean success = false;
		String error = "";
		
        Properties props = new Properties();
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(systemEmail, password);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(systemEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(lecturerEmail));
            message.setSubject("Email Verification Code");
            
            String htmlcode = "<!DOCTYPE html>\r\n"
                    + "<html lang=\"en\">\r\n"
                    + "<head>\r\n"
                    + "    <meta charset=\"UTF-8\">\r\n"
                    + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                    + "    <title>Account Removal Notification</title>\r\n"
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
                    + "            background: #d9534f;\r\n"
                    + "            padding: 10px;\r\n"
                    + "            border-radius: 5px;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        p {\r\n"
                    + "            color: #555555;\r\n"
                    + "            line-height: 1.6;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        .notice {\r\n"
                    + "            font-size: 18px;\r\n"
                    + "            color: #d9534f;\r\n"
                    + "            margin-top: 20px;\r\n"
                    + "        }\r\n"
                    + "    </style>\r\n"
                    + "</head>\r\n"
                    + "<body>\r\n"
                    + "    <div class=\"container\">\r\n"
                    + "        <h1>Account Removal Notification</h1>\r\n"
                    + "        <p>Dear Lecturer,</p>\r\n"
                    + "        <p>We regret to inform you that your account has been removed from our system.</p>\r\n"
                    + "        <p>If you have any questions or believe this is a mistake, please contact our support team.</p>\r\n"
                    + "        <div class=\"notice\">This is an automated message. Please do not reply to this email.</div>\r\n"
                    + "    </div>\r\n"
                    + "</body>\r\n"
                    + "</html>";
            
            message.setContent(htmlcode, "text/html");

            Transport.send(message);

            success = true;

        } catch (MessagingException e) {
        	error = e.getMessage();
        	JOptionPane.showMessageDialog(null, "Email error: " + error + "\nThe delete process fail...", "Delete Failed", JOptionPane.WARNING_MESSAGE);
        }
        
		//remove lecturer in user table
		//remove lecturer in verify table
        if(success) {
			String command1 = "DELETE FROM user WHERE userId = '" + lecturerId + "'";
			String command2 = "DELETE FROM lecturerverify WHERE userId = '" + lecturerId + "'";
			try {
				Statement stmt = con.createStatement();
				stmt.execute(command1);
				stmt.execute(command2);
				stmt.close();
				JOptionPane.showMessageDialog(null, "The lecturer have been deleted, and already informed by email.", "Delete Completed", JOptionPane.INFORMATION_MESSAGE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
	}
	
	public User[] addLecturer() {
		String command = "SELECT * FROM user \r\n"
				+ "INNER JOIN lecturerverify ON lecturerverify.userId = user.userID\r\n"
				+ "WHERE user.userType = 'lecturer' AND lecturerverify.sucess = '0'";
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			
			User[] lecturer = new User[getLecturerApprovalCount()];
			int a = 0;
			while(rs.next()) {
				lecturer[a] = new User();
                lecturer[a].setUserID(rs.getInt("userId"));
                lecturer[a].setUsername(rs.getString("username"));
                lecturer[a].setEmail(rs.getString("email"));
                a++;
			}
			rs.close();
			stmt.close();
            
            return lecturer;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private int getLecturerApprovalCount() {
        int count = 0;
        String countQuery = "SELECT COUNT(*) FROM user INNER JOIN lecturerverify ON lecturerverify.userId = user.userID WHERE user.userType = 'lecturer' AND lecturerverify.sucess = '0'";
        try {
            Statement countStatement = con.createStatement();
            ResultSet countResultSet = countStatement.executeQuery(countQuery);
            if (countResultSet.next()) {
                count = countResultSet.getInt(1);
            }
            countResultSet.close();
            countStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
	
	public int getLecturerApprovedCount() {
		int count = 0;
        String countQuery = "SELECT COUNT(*) FROM user INNER JOIN lecturerverify ON lecturerverify.userId = user.userID WHERE user.userType = 'lecturer' AND lecturerverify.sucess = '1'";
        try {
            Statement countStatement = con.createStatement();
            ResultSet countResultSet = countStatement.executeQuery(countQuery);
            if (countResultSet.next()) {
                count = countResultSet.getInt(1);
            }
            countResultSet.close();
            countStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
	}
	
	public void lecturerApproval(boolean approve, int userId) {
		String command = "UPDATE lecturerverify SET sucess = ? WHERE userId = ?";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(command);
			stmt.setBoolean(1, approve);
			stmt.setInt(2, userId);
			stmt.execute();
			stmt.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readCourse(String keyword, List<Course> course) {
		String command = "SELECT * FROM course WHERE courseName LIKE ? OR code LIKE ?";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(command);
			stmt.setString(1,"%" + keyword + "%");
			stmt.setString(2,"%" + keyword + "%");
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Course temp = new Course();
				temp.setCode(rs.getString("code"));
				temp.setCourseName(rs.getString("courseName"));
				temp.setCourseSection(rs.getInt("courseSection"));
				temp.setCourseType(rs.getString("courseType").charAt(0));
				temp.setDay(rs.getString("day"));
				temp.setDurationTime(rs.getInt("durationTime"));
				temp.setTime(rs.getTime("time"));
				temp.setCourseId(rs.getInt("courseId"));
				course.add(temp);
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void editCourse(Course course) {
		String command = "UPDATE course SET courseName = ?,courseSection = ?,courseType = ?,time = ?,day = ?,durationTime = ?,code = ? WHERE courseId = ?";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(command);
			stmt.setString(1,course.getCourseName());
			stmt.setInt(2, course.getCourseSection());
			stmt.setString(3, (String.valueOf(course.getCourseType())));
			stmt.setTime(4, new java.sql.Time(course.getTime().getHours(), course.getTime().getMinutes(), 0));
			stmt.setString(5,course.getDay());
			stmt.setString(6, String.valueOf(course.getDurationTime()));
			stmt.setString(7, course.getCode());
			stmt.setInt(8, course.getCourseId());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteCourse(int courseId) {
	    String deleteCommand = "DELETE FROM course WHERE courseId = ?";
	    String resetIDCommand = "SET @num := 0;";
	    String updateCommand = "UPDATE course SET courseId = @num := (@num + 1);";
	    String alterTableCommand = "ALTER TABLE course AUTO_INCREMENT = 1;";
	    
	    try {
	        // Prepare and execute the DELETE statement
	        PreparedStatement deleteStmt = con.prepareStatement(deleteCommand);
	        deleteStmt.setInt(1, courseId);
	        deleteStmt.executeUpdate();
	        
	        // Prepare and execute the SET command
	        Statement resetStmt = con.createStatement();
	        resetStmt.executeUpdate(resetIDCommand);
	        
	        // Prepare and execute the UPDATE statement
	        Statement updateStmt = con.createStatement();
	        updateStmt.executeUpdate(updateCommand);
	        
	        // Prepare and execute the ALTER TABLE statement
	        Statement alterTableStmt = con.createStatement();
	        alterTableStmt.executeUpdate(alterTableCommand);
	        
	        // Close the statements
	        deleteStmt.close();
	        resetStmt.close();
	        updateStmt.close();
	        alterTableStmt.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	
	public void addCourse(Course course) {
		String command = "INSERT INTO course(courseName,courseSection,courseType,time,day,durationTime,code) VALUES(?,?,?,?,?,?,?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(command);
			stmt.setString(1,course.getCourseName());
			stmt.setInt(2, course.getCourseSection());
			stmt.setString(3, (String.valueOf(course.getCourseType())));
			stmt.setTime(4, new java.sql.Time(course.getTime().getHours(), course.getTime().getMinutes(), 0));
			stmt.setString(5,course.getDay());
			stmt.setString(6, String.valueOf(course.getDurationTime()));
			stmt.setString(7, course.getCode());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


