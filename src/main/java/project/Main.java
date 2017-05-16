package project;

import org.joda.time.DateTime;

import model.Comment;
import model.Post;

public class Main {

	static DateTime creationDate = new DateTime(2010, 03, 01, 20, 15, 10);
	static DateTime date = new DateTime(2010, 03, 03, 20, 30, 10);
	

public static void main(String[] args) {

		Post post1 = new Post("2010-02-01T05:12:32.921+0000", 1039993, 3981, "Lei Liu");

		Comment com1 = new Comment("2010-02-09T04:05:20.777+0000", 529590, 2886, "LOL", -1, -1);
		Comment com2 = new Comment("2010-02-12T18:19:57.527+0000", 702748, 143, "cool", -1, -1);
		Comment com3 = new Comment("2010-02-13T13:18:00.586+0000", 702749, 3825, "great", -1, -1);
		Comment com4 = new Comment("2010-02-17T13:24:08.859+0000", 702750, 3825, "great", -1, -1);

		post1.calculScore(creationDate);
	}

	/*
	 * Post post1 = new Post("2010-02-01T05:12:32.921+0000", 1039993,
	 * 3981,"Lei Liu");
	 * 
	 * Comment com1 = new Comment("2010-02-09T04:05:20.777+0000", 529590, 2886,
	 * "LOL", 0, 0); Comment com2 = new Comment("2010-02-12T18:19:57.527+0000",
	 * 702748, 143, "cool", 0, 0); Comment com3 = new
	 * Comment("2010-02-13T13:18:00.586+0000", 702749, 3825, "great", 0, 0);
	 * Comment com4 = new Comment("2010-02-17T13:24:08.859+0000", 702750, 3825,
	 * "great", 0, 0);
	 * 
	 * //System.out.println(post1.calculScore(creationDate));
	 * 
	 * post1.addComment(com1); post1.addComment(com2); post1.addComment(com3);
	 * post1.addComment(com4); System.out.println(" Score total: " +
	 * post1.getScoreTotal()); // System.out.println("Commenteurs: " +
	 * post1.getNbCommenter());
	 * 
	 */

}

