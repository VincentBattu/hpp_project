package project;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Manager {

	public PostParser postParser;
	private CommentParser commentParser;

	private BlockingQueue<Post> postQueue = new ArrayBlockingQueue<Post>(20);
	private BlockingQueue<Comment> commentQueue = new ArrayBlockingQueue<Comment>(20);
	private BlockingQueue<String> resultQueue = new ArrayBlockingQueue<String>(20);
	
	private Printer printer;

	public Scheduler scheduler;


	public Manager(String postsPath, String commentsPath, String path) {
		postParser = new PostParser(postsPath, postQueue);
		commentParser = new CommentParser(commentsPath, commentQueue);
		scheduler = new Scheduler(postQueue, commentQueue, resultQueue);
		printer = new Printer(resultQueue,path);
	}

	public static void main(String[] args) {

		
		Manager manager = new Manager("data/Tests/Q1Basic2/posts.dat", "data/Tests/Q1Basic2"
				+ "/comments.dat", "result.txt");
		Thread t = new Thread(manager.postParser);
		Thread t2 = new Thread(manager.commentParser);
		Thread t3 = new Thread(manager.scheduler);
		Thread t4 = new Thread(manager.printer);
		t.setName("postParser");
		t.start();
		t2.setName("commentParser");
		t2.start();
		t3.setName("Scheduler");
		t3.start();
		t4.setName("printer");
		t4.start();
		try {
			t.join();
			t2.join();
			t3.join();
			t4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}