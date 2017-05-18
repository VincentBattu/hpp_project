package project;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import model.Entity;
import model.Result;

public class Manager {

	private Parser parser;
	private Printer printer;
	private Scheduler scheduler;

	private BlockingQueue<Entity> objectQueue = new LinkedBlockingQueue<>(100);
	private BlockingQueue<String> resultQueue = new LinkedBlockingQueue<String>(1000);
	private BlockingQueue<Result> resultqueue = new LinkedBlockingQueue<>(100);

	public Manager(String postsPath, String commentsPath, String resultPath) {

		parser = new Parser(commentsPath, postsPath, objectQueue);

		scheduler = new Scheduler(objectQueue, resultQueue, resultqueue);
		printer = new Printer(resultQueue, resultPath);
		Thread t = new Thread(parser);
		Thread t2 = new Thread(scheduler);
		Thread t3 = new Thread(printer);
		t.setName("parser");
		t.start();
		t2.setName("Scheduler");
		t2.start();

		t3.setName("printer");
		t3.start();
		try {
			t.join();
			t2.join();
			t3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		long t1 = System.currentTimeMillis();
		new Manager("data/postsRed.dat", "data/commentsRed.dat", "data/result.txt");		
		

		System.out.println("Temps exécution : " + (System.currentTimeMillis() - t1) + "ms");
	}

}