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

public class TestQBasic {

	@SuppressWarnings("resource")
	@Test
	public void test() {
		BufferedReader buff=null;
		BufferedReader buff1 =null;
		
		try {
			buff=new BufferedReader(new FileReader("_expectedQ1.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
		buff1=new BufferedReader(new FileReader("_Q1Bidon.txt"));
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
