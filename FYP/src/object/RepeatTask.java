package object;

import java.util.Date;

public class RepeatTask extends Task{
	private String repeatType;
	private int frequencyRepeat;
	private Date endDate;
	
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
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
