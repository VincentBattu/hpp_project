package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class CommentParser implements Runnable {

	private static String SEPARATOR = "\\|";

	private BufferedReader br = null;
	private String line = "";
	private BlockingQueue<Comment> queue;

	public static final Comment POISON_PILL = new Comment("1124-02-02T19:53:43.226+0000", 0, 0, "",-1,-1);
	
	Logger logger = LoggerFactory.getLogger(CommentParser.class);
	
	public CommentParser(String path, BlockingQueue<Comment> queue) {
		this.queue = queue;
		try {
			br = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void run() {
		String[] elements = null;
		
		try {
			
			while ((line = br.readLine()) != null) {
				elements = line.split(SEPARATOR);
				if (elements.length != 1) {
					try {
						long userId;
						if(elements[5].equals(""))
							elements[5]="-1";
						if(elements.length == 6)
							userId = -1;
						else
							userId = Long.parseLong(elements[6]);
						queue.put(new Comment(elements[0], Long.parseLong(elements[1]),  Long.parseLong(elements[2]),
								elements[4],Long.parseLong(elements[5]),userId));
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
					}
				}

			}
			try {
				queue.put(POISON_PILL);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}

		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}
