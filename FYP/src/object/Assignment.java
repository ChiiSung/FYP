package object;

import java.util.Date;

public class Assignment extends Task{
	private Course course;

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
}
