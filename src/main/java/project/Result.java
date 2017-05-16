package project;

import java.util.List;
import java.util.Vector;

public class Result {

	private String timeStamp = "";

	private List<Post> topPosts = new Vector<>(3);

	public Result(String date, List<Post> posts) {
		timeStamp = date;
		topPosts = posts;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public List<Post> getTopPosts() {
		return topPosts;
	}

}
