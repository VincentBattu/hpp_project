package project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {

	private List<Comment> comments;

	private int score;
	
	private String date;

	private LocalDateTime creationDate;
	
	private LocalDateTime lastMAJDate;

	private int id;

	private int userID;

	private int nbCommenter;

	private String userName;

	public Post(String timeStamp, int idPost, int userId, String nameUser) {
		//annee,moi,jour,heure,minute,seconde,nanosecond
		this.date = timeStamp;
		//this.creationDate = new LocalDateTime(timeStamp.substring(beginIndex, endIndex),,,,);
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

	public int calculScore(LocalDateTime date) {
		int scoreTotal = 0;
		if (this.comments.size() != 0) {

		} else {
			majScore(date);
			scoreTotal = this.score;
		}
		return scoreTotal;
	}

	private void majScore(LocalDateTime localDateTime) {
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

	public LocalDateTime getLastMAJDate() {
		return lastMAJDate;
	}

}
