package project;

import org.joda.time.DateTime;

public class Comment {

	private String date;

	private DateTime creationDate;

	private DateTime lastMAJDate;

	private int commentId;

	private int userId;

	private String userName;

	private int score;

	private int postCommentedId;

	public Comment(String timeStamp, int idComment, int userId, String nameUser, int postID, int commentedID) {
		this.date = timeStamp;
		this.creationDate = new DateTime(Integer.parseInt(timeStamp.substring(0, 4)),
				Integer.parseInt(timeStamp.substring(5, 7)), Integer.parseInt(timeStamp.substring(8, 10)),
				Integer.parseInt(timeStamp.substring(11, 13)), Integer.parseInt(timeStamp.substring(14, 16)),
				Integer.parseInt(timeStamp.substring(17, 19)), Integer.parseInt(timeStamp.substring(20, 23)));
		this.commentId = idComment;
		this.userId = userId;
		this.userName = nameUser;
		this.score = 10;
		this.lastMAJDate = this.creationDate;
		if (commentedID == -1) {
			this.postCommentedId = postID;
		} else {
			// TODO
			// PROB = SI chaine de comment : a quel moment recup de l'id du post
			// !!!!
		}
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

	public int getPostCommentedId() {
		return postCommentedId;
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
