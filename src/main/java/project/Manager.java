package project;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import model.Entity;
import model.Result;

public class Manager {

	private Parser parser;
	private Printer printer;
	private Schedulerv2 scheduler;
	private Formatter formatter;

	private BlockingQueue<Entity> objectQueue = new LinkedBlockingQueue<>(100);
	private BlockingQueue<String> resultQueue = new LinkedBlockingQueue<String>(1000);
	private BlockingQueue<Result> resultqueue = new LinkedBlockingQueue<>(100);

	public Manager(String postsPath, String commentsPath, String resultPath) {

		parser = new Parser(commentsPath, postsPath, objectQueue);

		scheduler = new Schedulerv2(objectQueue, resultQueue, resultqueue);
		printer = new Printer(resultQueue, resultPath);
		formatter = new Formatter(resultqueue);
		Thread t = new Thread(parser);
		Thread t3 = new Thread(scheduler);
		Thread t2 = new Thread(formatter);
		//Thread t4 = new Thread(printer);
		t.setName("parser");
		t.start();
		t3.setName("Scheduler");
		t3.start();
		t2.setName("formatter");
		t2.start();
		//t4.setName("printer");
		//t4.start();
		try {
			t.join();
			t3.join();
			t2.join();
		//	t4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		long t1 = System.currentTimeMillis();
		new Manager("data/postsRed.dat", "data/commentsRed.dat", "data/test.txt");

		System.out.println(System.currentTimeMillis() - t1 + "ms");
	}

}