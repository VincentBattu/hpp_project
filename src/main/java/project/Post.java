package project;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class Post {

	private List<Comment> comments;

	private int score;

	private String date;

	private DateTime creationDate;

	private DateTime lastMAJDate;

	private int id;

	private int userID;

	private int nbCommenter;

	private String userName;

	public Post(String timeStamp, int idPost, int userId, String nameUser) {
		this.date = timeStamp;
		this.creationDate = new DateTime(Integer.parseInt(timeStamp.substring(0, 4)),
				Integer.parseInt(timeStamp.substring(5, 7)), Integer.parseInt(timeStamp.substring(8, 10)),
				Integer.parseInt(timeStamp.substring(11, 13)), Integer.parseInt(timeStamp.substring(14, 16)),
				Integer.parseInt(timeStamp.substring(17, 19)), Integer.parseInt(timeStamp.substring(20, 23)));
		this.id = idPost;
		this.userID = userId;
		this.userName = nameUser;
		this.score = 10;
		this.comments = new ArrayList<Comment>();
		this.nbCommenter = 0;
		this.lastMAJDate = this.creationDate;
	}

	public void addComment(Comment com) {
		this.comments.add(com);
		this.score = calculScore(com.getcreationDate());
		if (this.comments.size() == 0) {
			this.nbCommenter += 1;
		} else {
			int idUserComment = com.getUserId();
			boolean exist = false;
			for (int i = 0; i < this.comments.size(); i++) {
				if (idUserComment == this.comments.get(i).getUserId()) {
					exist = true;
					break;
				}

			}
			if (!exist)
				this.nbCommenter += 1;
		}
	}

	public int calculScore(DateTime date) {
		int scoreTotal = 0;
		if (this.comments.size() != 0) {

		} else {
			majScore(date);
			scoreTotal = this.score;
		}
		return scoreTotal;
	}

	private void majScore(DateTime localDateTime) {
		// TODO Auto-generated method stub

		// com.setLastMAJDate(currentDate);
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

}
