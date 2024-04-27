package object;

import java.io.Serializable;
import java.util.Date;

public class Course implements Serializable{
	private String code;
	private String courseName;
	private int courseSection;
	private char courseType;
	private Date time;
	private String day;
	private int durationTime;
	public String getCode() {
		return code;
	}
	public void setCode(String courseId) {
		this.code = courseId;
	}
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
	public int getDurationTime() {
		return durationTime;
	}
	public void setDurationTime(int durationTime) {
		this.durationTime = durationTime;
	}

	public void saveCourse(SQLConnect sql ) {
		sql.addCourse(this);
	}
}
