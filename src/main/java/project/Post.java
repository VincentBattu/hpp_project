package project;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Post implements Comparable<Post> {

	private List<Comment> comments;

	private int score;

	private String date;

	private DateTime creationDate;

	private DateTime lastMAJDate;

	private long id;

	private long userID;

	private int nbCommenter;

	private String userName;

	private boolean isDead = false;

	private int nbDays = 0;

	private int scoreTotal = 0;

	private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+0000'");

	public Post(String timeStamp, long idPost, long userId, String nameUser) {
		this.date = timeStamp;
		this.creationDate = formatter.withZone(DateTimeZone.UTC).parseDateTime(timeStamp);
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
			long idUserComment = com.getUserId();
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
				Comment comment = this.comments.get(i);
				comment.updateTime(date);
				scoreTotal += comment.getScore();
			}
		}
		majScore(date);
		scoreTotal += this.score;
		
		if (scoreTotal == 0) {
			isDead = true;
		}
	}

	private void majScore(DateTime localDateTime) {

		int temp = Days.daysBetween(this.creationDate, localDateTime).getDays() - nbDays;
		
		if (temp >= 1) {
			this.score -= temp;
			nbDays += temp;
		}
		if (this.score < 0) {
			this.score = 0;
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

	public long getId() {
		return id;
	}

	public long getUserID() {
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

	public DateTime getCreationDate() {
		return creationDate;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", userName=" + userName + ", scoreTotal=" + scoreTotal + "]";
	}

	

	@Override
	public int compareTo(Post post) {
		if (this.getScoreTotal() > post.getScoreTotal()) {
			return 1;
		} else if (this.getScoreTotal() < post.getScoreTotal()) {
			return -1;
		} else {
			if (this.getCreationDate().getMillis() - post.getCreationDate().getMillis() < 0) {
				return -1;
			} else if (this.getCreationDate().getMillis() - post.getCreationDate().getMillis() > 0) {
				return 1;
			} else {
				// TODO implémenter avec les commentaires

				return 0;
			}
		}
	}

}
