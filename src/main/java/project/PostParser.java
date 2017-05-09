package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class PostParser implements Runnable {


	private static String SEPARATOR = "\\|";

	private BufferedReader br = null;
	private String line = "";
	private BlockingQueue<Post> queue;
	
	public static final Post POISON_PILL = new Post("1124-02-02T19:53:43.226+0000",0,0,"");
	

	public PostParser(String path, BlockingQueue<Post> queue) {
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
			while ((line = br.readLine()) != null){
				elements = line.split(SEPARATOR);
				try {
					queue.put(new Post(elements[0],Integer.parseInt(elements[1]),
							Integer.parseInt(elements[2]),elements[4]));
				} catch (InterruptedException e) {
					e.printStackTrace();
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
