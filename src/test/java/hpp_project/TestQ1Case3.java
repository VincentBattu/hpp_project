package hpp_project;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import project.Manager;

/**
 * Post commenté à l'instant de son arrivée => impossible (test passe pas)
 */

public class TestQ1Case3 {

	@SuppressWarnings("resource")
	@Test
	public void test() {
		BufferedReader buff = null;
		BufferedReader buff1 = null;

		new Manager("data/Tests/Q1Case3/posts.dat", "data/Tests/Q1Case3" + "/comments.dat",
				"data/Tests/Q1Case3/result.txt");

		try {
			buff = new BufferedReader(new FileReader("data/Tests/Q1Case3/_expectedQ1.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			buff1 = new BufferedReader(new FileReader("data/Tests/Q1Case3/result.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (true) {
			String line = "";
			try {
				line = buff.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String line1 = "";
			try {
				line1 = buff1.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			assertEquals(line, line1);
			if (line == null || line1 == null) {
				break;
			}
		}
	}
}
