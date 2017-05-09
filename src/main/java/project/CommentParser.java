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

	public static final Comment POISON_PILL = new Comment("1124-02-02T19:53:43.226+0000", 0, 0, "",-1,-1);

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
						if(elements[5].equals(""))
							elements[5]="-1";
						if(elements[6].equals(""))
							elements[6]="-1";
						queue.put(new Comment(elements[0], Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
								elements[4],Integer.parseInt(elements[5]),Integer.parseInt(elements[6])));
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
