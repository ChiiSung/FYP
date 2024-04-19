package object;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;

public class Task implements Serializable{
	protected int taskId;
	protected String taskTitle; 
	protected Date dueDate;
	protected String description;
	protected Boolean completed = false;

	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getCompleted() {
		return completed;
	}
	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
	
	public void saveTask(SQLConnect sql, int userId){
		sql.savingTask(taskTitle, dueDate, description, userId, completed);
	}
	
	public void readTask(SQLConnect sql, User user, int userId) {
		sql.readTask(user, userId);
	}
	
	public void deleteTask(SQLConnect sql) {
		sql.deleteTask(taskId);
	}
	public void editTask(SQLConnect sql) {
		sql.editTask(this);
	}
	
	public String toString() {
		return taskTitle + description + dueDate ;
	}
}
