package project;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Printer implements Runnable {
	
	private BlockingQueue<String> bufferQueue;

	public Printer(BlockingQueue<String> bufferQueue) {
		super();
		this.bufferQueue = bufferQueue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		File file =  new File("result.txt") ;
		OutputStream os = null;
		String Newligne=System.getProperty("line.separator"); 
		
		try {
			 os = new FileOutputStream(file);
			DataOutputStream dos =  new DataOutputStream(os) ; 
			
			while(!bufferQueue.isEmpty()){
				try {
					dos.write(bufferQueue.take().getBytes(Charset.forName("UTF-8")));
					dos.write(Newligne.getBytes(Charset.forName("UTF-8")));
					System.out.println("WRITTEN");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
