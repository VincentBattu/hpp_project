package hpp_project;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import project.Manager;

public class TestQ1Case1 {

	@SuppressWarnings("resource")
	@Test
	public void test() {
		BufferedReader buff=null;
		BufferedReader buff1 =null;
		
		Manager manager = new Manager("data/Tests/Q1Case1/posts.dat", "data/Tests/Q1Case1"
				+ "/comments.dat", "data/Tests/Q1Case1/result.txt");		
		
		try {
			buff=new BufferedReader(new FileReader("data/Tests/Q1Case1/_expectedQ1.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
		buff1=new BufferedReader(new FileReader("data/Tests/Q1Case1/result.txt"));
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
