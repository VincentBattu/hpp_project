package project;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Post implements Comparable<Post>{

	private List<Comment> comments;

	private int score;

	private String date;

	private DateTime creationDate;

	private DateTime lastMAJDate;

	private int id;

	private int userID;

	private int nbCommenter;

	private String userName;

	private boolean isDead = false;

	private int nbDays = 0;

	private int scoreTotal = 0;

	private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+0000'");

	public Post(String timeStamp, int idPost, int userId, String nameUser) {
		this.date = timeStamp;
		this.creationDate = formatter.parseDateTime(timeStamp);
		this.id = idPost;
		this.userID = userId;
		this.userName = nameUser;
		this.score = 10;
		this.comments = new ArrayList<Comment>();
		this.nbCommenter = 0;
		this.lastMAJDate = this.creationDate;
	}

	public void addComment(Comment com) {
		if (this.comments.size() == 0) {
			this.nbCommenter += 1;
		} else {
			int idUserComment = com.getUserId();
			boolean exist = false;
			outerloop: for (int i = 0; i < this.comments.size(); i++) {
				if (idUserComment == this.comments.get(i).getUserId()) {
					exist = true;
					break outerloop;
				}

			}
			if (!exist)
				this.nbCommenter += 1;
		}
		this.comments.add(com);
		calculScore(com.getcreationDate());
	}

	public void calculScore(DateTime date) {
		scoreTotal = 0;
		if (this.comments.size() != 0) {
			for (int i = 0; i < this.comments.size(); i++) {
				this.comments.get(i).updateTime(date);
				scoreTotal += this.comments.get(i).getScore();
			}
		}
		majScore(date);
		scoreTotal += this.score;
		if (scoreTotal <= 0) {
			isDead = true;
			scoreTotal = 0;
		}
	}

	private void majScore(DateTime localDateTime) {

		int temp = Days.daysBetween(this.creationDate, localDateTime).getDays() - nbDays;
		if (temp >= 1) {
			this.score -= temp;
			nbDays += temp;
		}
		this.lastMAJDate = localDateTime;

	}

	public List<Comment> getComments() {
		return comments;
	}

	public int getScore() {
		return score;
	}

	public String getDate() {
		return date;
	}

	public int getId() {
		return id;
	}

	public int getUserID() {
		return userID;
	}

	public int getNbCommenter() {
		return nbCommenter;
	}

	public String getUserName() {
		return userName;
	}

	public DateTime getLastMAJDate() {
		return lastMAJDate;
	}

	public boolean isDead() {
		return isDead;
	}

	public int getScoreTotal() {
		return scoreTotal;
	}
	
	public DateTime getCreationDate(){
		return creationDate;
	}

	

	@Override
	public String toString() {
		return "Post [id=" + id + ", userName=" + userName + ", scoreTotal=" + scoreTotal + "]";
	}

	@Override
	public int compareTo(Post post) {
		if (this.getScoreTotal() > post.getScoreTotal()){
			return 1;
		} else if (this.getScoreTotal() < post.getScoreTotal()){
			return -1;
		} else {
			if (this.getCreationDate().getMillis() - post.getCreationDate().getMillis() < 0){
				return -1;
			} else if (this.getCreationDate().getMillis() - post.getCreationDate().getMillis() > 0){
				return 1;
			} else {
				// TODO implémenter avec les commentaires		
				
				
				return 0;
			}
		}
	}
	
	


	
	

}
