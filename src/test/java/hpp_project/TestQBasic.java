package hpp_project;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.junit.Test;

import project.Manager;

public class TestQBasic {

	@SuppressWarnings("resource")
	@Test
	public void test() {
		BufferedReader buff=null;
		BufferedReader buff1 =null;
		
		Manager manager = new Manager("data/Tests/Q1Basic2/posts.dat", "data/Tests/Q1Basic2"
				+ "/comments.dat", "data/Tests/Q1Basic2/result.txt");
		Thread t = new Thread(manager.postParser);
		Thread t2 = new Thread(manager.commentParser);
		Thread t3 = new Thread(manager.scheduler);
		Thread t4 = new Thread(manager.printer);
		t.setName("postParser");
		t.start();
		t2.setName("commentParser");
		t2.start();
		t3.setName("Scheduler");
		t3.start();
		t4.setName("printer");
		t4.start();
		try {
			t.join();
			t2.join();
			t3.join();
			t4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		
		try {
			buff=new BufferedReader(new FileReader("data/Tests/Q1Basic2/_expectedQ1.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
		buff1=new BufferedReader(new FileReader("data/Tests/Q1Basic2/result.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true){
			String line="";
			try {
				 line= buff.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String line1="";
			try {
				 line1= buff1.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			assertEquals(line,line1);
			if(line==null || line1==null){
				break;
			}
		}	
	}

}
