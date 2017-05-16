package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostParser implements Runnable {


	private static String SEPARATOR = "\\|";

	private BufferedReader br = null;
	private String line = "";
	private BlockingQueue<Post> queue;
	
	public static final Post POISON_PILL = new Post("1124-02-02T19:53:43.226+0000",0,0,"");
	
	Logger logger = LoggerFactory.getLogger(PostParser.class);
	

	public PostParser(String path, BlockingQueue<Post> queue) {
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
			
			while ((line = br.readLine()) != null){
				elements = line.split(SEPARATOR);
				try {
					queue.put(new Post(elements[0],Long.parseLong(elements[1]),
							Long.parseLong(elements[2]),elements[4]));
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
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
