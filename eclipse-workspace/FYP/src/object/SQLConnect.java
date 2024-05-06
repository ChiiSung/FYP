package object;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

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
		String command = "SELECT userID, username FROM user WHERE email =? AND password =?";
		String command2 = "SELECT sucess FROM lecturerverify WHERE userId =?";
		PreparedStatement stmt;
		stmt = con.prepareStatement(command);
		stmt.setString(1,email);
		stmt.setString(2, password);
		
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
	
	
	// Admin part
	public void readLecturer(){}
	
	public void deleteLecturer() {
	}
	
	public void addLeturer() {}
	
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


