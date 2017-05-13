package project;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Printer implements Runnable {
	
	private BlockingQueue<Result> bufferQueue;
	/**
	 * Chemin du fichier de sortie
	 */
	private String path;

	public Printer(BlockingQueue<Result> bufferQueue, String path) {
		this.path = path;
		this.bufferQueue = bufferQueue;
	}
	
	public String formatResult(String date, List<Post> bestPosts) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(date);
		for (Post post : bestPosts) {
			strBuilder.append(',');
			strBuilder.append(post.getId());
			strBuilder.append(',');
			strBuilder.append(post.getUserName());
			strBuilder.append(',');
			strBuilder.append(post.getScoreTotal());
			strBuilder.append(',');
			strBuilder.append(post.getNbCommenter());
		}
		for (int i = 0; i < 3 - bestPosts.size(); i++) {
			strBuilder.append(',');
			strBuilder.append('-');
			strBuilder.append(',');
			strBuilder.append('-');
			strBuilder.append(',');
			strBuilder.append('-');
			strBuilder.append(',');
			strBuilder.append('-');
		}
		return strBuilder.toString();
	}

	@Override
	public void run() {
		
		File file =  new File(path) ;
		OutputStream os = null;
		String Newligne=System.getProperty("line.separator"); 
		
		try {
			 os = new FileOutputStream(file);
			DataOutputStream dos =  new DataOutputStream(os) ; 
			
			try {
				Result resultat = bufferQueue.take();
				while(resultat != Scheduler.POISON_PILL_RESULT){
					try {
						String line = formatResult(resultat.getTimeStamp(),resultat.getTopPosts());
						dos.write(line.getBytes(Charset.forName("UTF-8")));
						dos.write(Newligne.getBytes(Charset.forName("UTF-8")));
						resultat = bufferQueue.take();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				os.close();
				System.out.println("FIN");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
