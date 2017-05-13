package project;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Manager {

	private PostParser postParser;
	private CommentParser commentParser;
	private Printer printer;
	private Scheduler scheduler;

	private BlockingQueue<Post> postQueue = new ArrayBlockingQueue<Post>(100);
	private BlockingQueue<Comment> commentQueue = new ArrayBlockingQueue<Comment>(100);
	private BlockingQueue<Result> resultQueue = new ArrayBlockingQueue<Result>(100);
	
	


	public Manager(String postsPath, String commentsPath, String resultPath) {
		postParser = new PostParser(postsPath, postQueue);
		commentParser = new CommentParser(commentsPath, commentQueue);
		scheduler = new Scheduler(postQueue, commentQueue, resultQueue);
		printer = new Printer(resultQueue,resultPath);
		Thread t = new Thread(postParser);
		Thread t2 = new Thread(commentParser);
		Thread t3 = new Thread(scheduler);
		Thread t4 = new Thread(printer);
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
	
	public static void main(String[] args) {
		long t1 = System.currentTimeMillis();
		new Manager("data/postsRed.dat","data/commentsRed.dat","data/test.txt");
		
		System.out.println(System.currentTimeMillis() -t1 + "ms");
	}

}