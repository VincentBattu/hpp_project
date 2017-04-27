package project;

public class Comment {

	private String date;
	private String lastMAJDate;

	private int commentId;

	private int userId;

	private String userName;

	private int score;

	private int postCommentedId;

	// PROB = SI chaine de comment : a quel moment recup de l'id du post !!!!

	public Comment(String timeStamp, int idComment, int userId, String nameUser, int postID, int commentedID) {
		this.date = timeStamp;
		this.commentId = idComment;
		this.userId = userId;
		this.userName = nameUser;
		this.score = 10;
		this.lastMAJDate = date;
		if (commentedID == -1) {
			this.postCommentedId = postID;
		} else {
			//TODO
		}
	}

	public String getLastMAJDate() {
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

	public void setLastMAJDate(String lastMAJDate) {
		this.lastMAJDate = lastMAJDate;
	}

}
