package object;

import java.util.Calendar;
import java.util.Date;

public class RepeatTask extends Task {
	private String repeatType;
	private int frequencyRepeat;
	private Date endDate;
	
	private int remainfrequancy;
	private Date inDate[];
	
	public RepeatTask() {
	}
	
	public RepeatTask(Task task){
		taskId = task.getTaskId();
		taskTitle = task.getTaskTitle();
		dueDate = task.getDueDate();
		description = task.getDescription();
		completed = task.getCompleted();
	}
	
	public String getRepeatType() {
		return repeatType;
	}
	public void setRepeatType(String repeatType) {
		this.repeatType = repeatType;
	}
	public int getFrequencyRepeat() {
		return frequencyRepeat;
	}
	public void setFrequencyRepeat(int frequencyRepeat) {
		this.frequencyRepeat = frequencyRepeat;
		this.remainfrequancy = frequencyRepeat;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		this.inDate = new Date[remainfrequancy];
		inDate[0] = dueDate;
		for(int i = 1; i<remainfrequancy; i++) {
			int y = 0;
			if (repeatType.equals("Daily")) {
				y = 1;
				int x = i * y;
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dueDate);
				calendar.add(Calendar.DATE, x);
				Date endRepeatDate = calendar.getTime();
				inDate[i] = endRepeatDate;
			}else if (repeatType.equals("Weekly")) {
				y = 7;
				int x = i * y;
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dueDate);
				calendar.add(Calendar.DATE, x);
				Date endRepeatDate = calendar.getTime();
				inDate[i] = endRepeatDate;
			}else if (repeatType.equals("Monthly")) {
			    Calendar calendar = Calendar.getInstance();
			    calendar.setTime(dueDate);
			    calendar.add(Calendar.MONTH, i);
			    Date endRepeatDate = calendar.getTime();
			    inDate[i] = endRepeatDate;
			}
		}
	}
	
	public void saveTask(SQLConnect sql, int userId){
		sql.savingRepeatTask(taskTitle, dueDate, description, userId, repeatType, frequencyRepeat, endDate, completed);
	}
	public void readTask(SQLConnect sql, User user, int userId) {
		sql.readRepeatTask(user, userId);
	}
	public void deleteRepeatTask(SQLConnect sql) {
		sql.deleteRepeatTask(taskId);
	}
	public void editTask(SQLConnect sql, int userId) {
		if(!repeatType.equals("None")) {
			sql.editRepeatTask(this);
		}else {
			sql.savingTask(taskTitle, dueDate, description, userId, completed);
			sql.deleteRepeatTask(taskId);
		}
	}
	public int getRemainfrequancy() {
		return remainfrequancy;
	}
	public void setRemainfrequancy(int remainfrequancy) {
		this.remainfrequancy = remainfrequancy;
	}
	public Date[] getInDate() {
		return inDate;
	}
}
