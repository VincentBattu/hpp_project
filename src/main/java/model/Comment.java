package model;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Comment implements Entity{

	private String date;

	private DateTime creationDate;

	private DateTime lastMAJDate;

	private long commentId;

	private long userId;

	private String userName;

	private int score;

	private int nbDays = 0;
	
	private long linkCom = -1;
	
	private long linkPost = -1;

	private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+0000'");

	public Comment(String timeStamp, long idComment, long userId, String nameUser, long linkCom, long linkPost) {
		this.date = timeStamp;
		this.creationDate = formatter.withZone(DateTimeZone.UTC).parseDateTime(timeStamp);
		this.commentId = idComment;
		this.userId = userId;
		this.userName = nameUser;
		this.score = 10;
		this.lastMAJDate = this.creationDate;
		this.linkCom = linkCom;
		this.linkPost = linkPost;
	}

	public void updateTime(DateTime currentTime) {

		int temp = Days.daysBetween(creationDate, currentTime).getDays() - nbDays;
		if (temp >= 1) {
			score -= temp;
			nbDays += temp;

		} 
		if (score < 0)
			score = 0;
		this.lastMAJDate = currentTime;

	}

	public DateTime getLastMAJDate() {
		return lastMAJDate;
	}

	public long getCommentId() {
		return commentId;
	}

	public String getUserName() {
		return userName;
	}

	public int getScore() {
		return score;
	}

	public long getUserId() {
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
	
	public long getLinkCom() {
		return linkCom;
	}

	public long getLinkPost() {
		return linkPost;
	}
	
	public int getNbDays(){
		return nbDays;
	}
	
	public void incrementNbDays(){
		this.nbDays += 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (commentId != other.commentId)
			return false;
		return true;
	}
	
	

}
