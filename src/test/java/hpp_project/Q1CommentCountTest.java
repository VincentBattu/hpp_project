package hpp_project;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import project.Manager;

/*
 * Normal de pas passer : le top 3 n'est pas modifié par l'ajout 
 * de commentaire au seul post créé donc on n'écrit pas le top3 dans le fichier
 */

public class Q1CommentCountTest {

	@SuppressWarnings("resource")
	@Test
	public void test() {
		BufferedReader buff = null;
		BufferedReader buff1 = null;

		new Manager("data/Tests/Q1CommentCount/posts.dat", "data/Tests/Q1CommentCount/comments.dat",
				"data/Tests/Q1CommentCount/result.txt");

		try {
			buff = new BufferedReader(new FileReader("data/Tests/Q1CommentCount/_expectedQ1.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			buff1 = new BufferedReader(new FileReader("data/Tests/Q1CommentCount/result.txt"));
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
