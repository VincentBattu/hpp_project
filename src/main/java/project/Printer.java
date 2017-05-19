package project;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;

public class Printer implements Runnable {
	
	private BlockingQueue<String> bufferQueue;
	/**
	 * Chemin du fichier de sortie
	 */
	private String path;

	public Printer(BlockingQueue<String> bufferQueue, String path) {
		this.path = path;
		this.bufferQueue = bufferQueue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		File file =  new File(path) ;
		OutputStream os = null;
		String Newligne=System.getProperty("line.separator"); 
		
		try {
			 os = new FileOutputStream(file);
			DataOutputStream dos =  new DataOutputStream(os); 
			
			try {
				String line = bufferQueue.take();
				while(line != Scheduler.RESULT_POISON_PILL){
					try {
						dos.write(line.getBytes(Charset.forName("UTF-8")));
						dos.write(Newligne.getBytes(Charset.forName("UTF-8")));
						//System.out.println(System.nanoTime() - Scheduler.t3.get());
						line = bufferQueue.take();
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
