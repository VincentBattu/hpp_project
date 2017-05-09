package project;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
		
		try {
			 os = new FileOutputStream(file);
			DataOutputStream dos =  new DataOutputStream(os) ; 
			
			while(!bufferQueue.isEmpty()){
				try {
					dos.writeChars(bufferQueue.take());
					dos.writeChars("\n");
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
	
	public static void main(String[] args) {
	
		BlockingQueue<String> bufferQueue = new ArrayBlockingQueue<>(5);
		bufferQueue.add("Coucou AZERTY");
		bufferQueue.add("LaRobeDeDELPHINE");
		
		Printer pr = new Printer(bufferQueue);
		Thread t = new Thread(pr);
		t.start();
	}
	

}
