package object;

import java.util.Date;

public class Course {
	private String courseName;
	private int courseSection;
	private char courseType;
	private Date time;
	private String day;
	private Date durationTime;
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public int getCourseSection() {
		return courseSection;
	}
	public void setCourseSection(int courseSection) {
		this.courseSection = courseSection;
	}
	public char getCourseType() {
		return courseType;
	}
	public void setCourseType(char courseType) {
		this.courseType = courseType;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public Date getDurationTime() {
		return durationTime;
	}
	public void setDurationTime(Date durationTime) {
		this.durationTime = durationTime;
	}
	
	
}
