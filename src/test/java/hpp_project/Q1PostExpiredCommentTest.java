package hpp_project;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import project.Manager;

/*
 * Le post est mort lors de l'arrivée du commentaire donc il n'existe plus
 * dans les listes, le top3 vide ne s'affiche pas.
 */

public class Q1PostExpiredCommentTest {

	@SuppressWarnings("resource")
	@Test
	public void test() {
		BufferedReader buff = null;
		BufferedReader buff1 = null;

		new Manager("data/Tests/Q1PostExpiredComment/posts.dat", "data/Tests/Q1PostExpiredComment/comments.dat",
				"data/Tests/Q1PostExpiredComment/result.txt");

		try {
			buff = new BufferedReader(new FileReader("data/Tests/Q1PostExpiredComment/_expectedQ1.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			buff1 = new BufferedReader(new FileReader("data/Tests/Q1PostExpiredComment/result.txt"));
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
