package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class CommentParser implements Runnable {

	private static String SEPARATOR = "\\|";

	private BufferedReader br = null;
	private String line = "";
	private BlockingQueue<Comment> queue;

	public static final Comment POISON_PILL = new Comment("1124-02-02T19:53:43.226+0000", 0, 0, "");

	public CommentParser(String path, BlockingQueue<Comment> queue) {
		this.queue = queue;
		try {
			br = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String[] elements = null;
		try {
			line = br.readLine();
			while (line != null) {
				elements = line.split(SEPARATOR);
				if (elements.length != 1) {
					try {
						queue.put(new Comment(elements[0], Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
								elements[4]));
						System.out.println(elements[0] + " " + Thread.currentThread().getName());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
			try {
				queue.put(POISON_PILL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
