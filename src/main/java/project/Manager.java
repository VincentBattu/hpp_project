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
	private CommentParser commentParser;

	private BlockingQueue<Post> postQueue = new ArrayBlockingQueue<Post>(20);
	private BlockingQueue<Comment> commentQueue = new ArrayBlockingQueue<Comment>(20);

	public Scheduler scheduler;

	private List<Post> alivePosts;

	public Manager(String postsPath, String commentsPath) {
		postParser = new PostParser(postsPath, postQueue);
		commentParser = new CommentParser(commentsPath, commentQueue);
		scheduler = new Scheduler(postQueue, commentQueue);
		alivePosts = new ArrayList<>();
	}

	public static void main(String[] args) {
		Manager manager = new Manager("data/Tests/Q1BigTest/posts.dat", "data/Tests/Q1BigTest/comments.dat");
		Thread t = new Thread(manager.postParser);
		Thread t2 = new Thread(manager.commentParser);
		Thread t3 = new Thread(manager.scheduler);
		t.setName("postParser");
		t.start();
		t2.setName("commentParser");
		t2.start();
		t3.setName("Scheduler");
		t3.start();
		try {
			t.join();
			t2.join();
			t3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
