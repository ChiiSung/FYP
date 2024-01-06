package object;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Boolean loginVerification(String email, String password, User user) throws ClassNotFoundException, SQLException {
		String command = "SELECT userID, username FROM user WHERE email ='"+ email +"' AND password = '" + password + "'";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(command);
		
		boolean veri = rs.next();
		if(veri) {
			user.setUserID(rs.getInt(1));
			user.setUsername(String.valueOf(rs.getString(2)));
		}
		rs.close();
		stmt.close();
		
		return veri;
	}
	
	public void savingTask(String taskTitle, Date dueDate, String description, int userId) {
		String command = "INSERT INTO task (userId, taskTitle, dueDate, description, completed) VALUES(?,?,?,?,?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(command);
			stmt.setInt(1,userId);
			stmt.setString(2, taskTitle);
			stmt.setDate(3, new java.sql.Date(dueDate.getYear(),dueDate.getMonth(),dueDate.getDate()));
			stmt.setString(4, description);
			stmt.setBoolean(5,false);
			stmt.execute();
			
			stmt.close();
		} catch (SQLException e) {
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		} catch (SQLException e) {
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return task;
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exist;
	}
	
	public void register(User user) {
		String command = "INSERT INTO user (username, email, password) VALUES(?,?,?)";
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(command);
			stmt.setString(1,user.getUsername());
			stmt.setString(2, user.getEmail());
			stmt.setString(3,user.getPassword());
			stmt.execute();
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}


