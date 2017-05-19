package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class Post implements Comparable<Post>, Entity {

	private String date;

	private DateTime creationDate;

	private DateTime lastMAJDate;

	private long id;

	private long userID;

	private int nbCommenter;
	
	/**
	 * Liste des ids des commentaires du post, utile lors de la mort
	 * du post pour supprimer ces commentaires
	 */
	private List<Long> idsComments;

	/**
	 * Map qui contient l'id des personnes ayant commenté ce post. La valeur est
	 * factice (int =0), uniquement là pour profiter du temps d'accès constant
	 * d'une map.
	 */
	private Map<Long, Integer> idsCommenters;

	private String userName;


	private int nbDays = 0;

	private int scoreTotal = 10;

	private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+0000'");

	public Post(String timeStamp, long idPost, long userId, String nameUser) {
		this.idsCommenters = new HashMap<>();
		this.date = timeStamp;
		this.creationDate = formatter.withZone(DateTimeZone.UTC).parseDateTime(timeStamp);
		this.id = idPost;
		this.userID = userId;
		this.userName = nameUser;
		this.nbCommenter = 0;
		this.lastMAJDate = this.creationDate;
		this.idsComments = new ArrayList<>();
	}

	public void addCommenter(long idCommenters) {
		if (idsCommenters.get(idCommenters) == null) {
			idsCommenters.put(idCommenters, 0);
			nbCommenter++;
		}
		scoreTotal += 10;
	}

	public void decrementScore() {
		this.scoreTotal -= 1;
	}

	public void incrementNbDays() {
		this.nbDays += 1;
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


	public int getScoreTotal() {
		return scoreTotal;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setScoreTotal(int scoreTotal) {
		this.scoreTotal = scoreTotal;
	}

	public int getNbDays() {
		return nbDays;
	}
	
	public void addComments(long idComment){
		idsComments.add(idComment);
	}
	
	public List<Long> getIdsComments(){
		return idsComments;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
