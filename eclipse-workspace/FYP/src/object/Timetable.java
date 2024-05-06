package object;

import java.io.Serializable;
import java.util.Date;

public class Timetable implements Serializable{
	private Course[] course;
	private Date semStart;
	public Course[] getCourse() {
		return course;
	}
	public void setCourse(Course[] course) {
		this.course = course;
	}
	public Date getSemStart() {
		return semStart;
	}
	public void setSemStart(Date semStart) {
		this.semStart = semStart;
	}
	
	
}
