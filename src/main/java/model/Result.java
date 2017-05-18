package model;

import java.util.List;

public class Result {

	private String date;
	private List<Post> top3;

	public Result(String date, List<Post> top3) {
		this.date = date;
		this.top3 = top3;
	}

	public String getDate() {
		return date;
	}

	public List<Post> getTop3() {
		return top3;
	}
	

}
