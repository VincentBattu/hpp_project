package project;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Manager {

	public PostParser postParser;
	private BlockingQueue<Post> postQueue = new ArrayBlockingQueue<Post>(20);
	private BlockingQueue<Comment> commentQueue = new ArrayBlockingQueue<Comment>(20);
	private CommentParser commentParser;


	private List<Post> alivePosts;
	


	public Manager(String postsPath, String commentsPath) {
		postParser = new PostParser(postsPath, postQueue);
		commentParser = new CommentParser(commentsPath, commentQueue);
		alivePosts = new ArrayList<>();
	}



	public static void main(String[] args) {
		Manager manager = new Manager("data/Tests/Q1Basic2/posts.dat", "data/Tests/Q1Basic2/comments.dat");
		Thread t = new Thread(manager.postParser);
		Thread t2 = new Thread(manager.commentParser);
		t.setName("postParser");
		t.start();
		t2.setName("commentParser");
		t2.start();
	}

}
