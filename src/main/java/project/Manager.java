package project;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import model.Entity;

public class Manager {

	private Parser parser;
	private Printer printer;
	private Schedulerv2 scheduler;

	private BlockingQueue<Entity> objectQueue = new LinkedBlockingQueue<>(100);
	private BlockingQueue<String> resultQueue = new LinkedBlockingQueue<String>(100);

	public Manager(String postsPath, String commentsPath, String resultPath) {

		parser = new Parser(commentsPath, postsPath, objectQueue);

		scheduler = new Schedulerv2(objectQueue, resultQueue);
		printer = new Printer(resultQueue, resultPath);
		Thread t = new Thread(parser);
		Thread t3 = new Thread(scheduler);
		Thread t4 = new Thread(printer);
		t.setName("parser");
		t.start();
		t3.setName("Scheduler");
		t3.start();
		t4.setName("printer");
		t4.start();
		try {
			t.join();
			t3.join();
			t4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		long t1 = System.currentTimeMillis();
		new Manager("data/posts.dat", "data/comments.dat", "data/test.txt");

		System.out.println(System.currentTimeMillis() - t1 + "ms");
	}

}