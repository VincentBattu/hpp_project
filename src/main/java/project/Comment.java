package project;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Comment {

	private String date;

	private DateTime creationDate;

	private DateTime lastMAJDate;

	private int commentId;

	private int userId;

	private String userName;

	private int score;

	private int nbDays = 0;

	private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+0000'");

	public Comment(String timeStamp, int idComment, int userId, String nameUser) {
		this.date = timeStamp;
		this.creationDate = formatter.parseDateTime(timeStamp);
		this.commentId = idComment;
		this.userId = userId;
		this.userName = nameUser;
		this.score = 10;
		this.lastMAJDate = this.creationDate;
	}

	public void updateTime(DateTime currentTime) {

		int temp = Days.daysBetween(creationDate, currentTime).getDays() - nbDays;
		if (temp >= 1 && nbDays <= temp) {
			score -= temp;
			nbDays += temp;

		} else {
			if (nbDays < temp)
				score = 0;
		}
		this.lastMAJDate = currentTime;

	}

	public DateTime getLastMAJDate() {
		return lastMAJDate;
	}

	public int getCommentId() {
		return commentId;
	}

	public String getUserName() {
		return userName;
	}

	public int getScore() {
		return score;
	}

	public int getUserId() {
		return userId;
	}

	public String getDate() {
		return date;
	}

	public DateTime getcreationDate() {
		return creationDate;
	}

	public void setLastMAJDate(DateTime lastMAJDate) {
		this.lastMAJDate = lastMAJDate;
	}

}
