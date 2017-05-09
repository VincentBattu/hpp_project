package project;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
		
		   File fichier =  new File("tmp/integers.bin") ;
		   OutputStream os = null ;

		    try {
		       // ouverture d'un flux de sortie sur un fichier
		      os =  new FileOutputStream(fichier);
		      DataOutputStream dos =  new DataOutputStream(os) ; 

		       for (int i : Arrays.asList(1,  2,  3,  4,  5)) {
		         dos.writeInt(i) ;
		      }

		      }  catch (IOException e) {
		          // gestion de l'erreur
		    	  
		      }  finally {
		          // fermeture du flux
		    	  os.close();
		      }
		
	}
	
	

}
