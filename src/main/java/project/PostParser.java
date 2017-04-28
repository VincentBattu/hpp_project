package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PostParser {
	private static String POST_PATH = "data/Tests/Q1Basic/posts.dat";

	private static String SEPARATOR = "\\|";

	private BufferedReader br = null;
	private String line = "";
	
	private static Map<String, Integer> columns = new HashMap<>();
	
	static{
		columns.put("ts", 0);
		columns.put("post_id", 1);
		columns.put("user_id", 2);
		columns.put("post", 3);
		columns.put("user", 4);
	}

	public PostParser() {
		try {
			br = new BufferedReader(new FileReader(POST_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void readNextLine() {
		try {
			if ((line = br.readLine()) != null) {

				String[] elements = line.split(SEPARATOR);

				System.out.println(elements[columns.get("post_id")]);

			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		PostParser postParser = new PostParser();
		postParser.readNextLine();
		postParser.readNextLine();
		postParser.readNextLine();
	}
}
