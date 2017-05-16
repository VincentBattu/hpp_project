package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.joda.time.DateTime;

import model.Comment;
import model.Post;

public class Parser implements Runnable {
	private static String SEPARATOR = "\\|";

	private BufferedReader brPost = null;
	private BufferedReader brComment = null;
	private String line = "";
	private BlockingQueue<Object> queue;
	private List<Object> listPost = new ArrayList<>(1);
	private List<Object> listComment = new ArrayList<>(1);

	public static Object POISON_PILL = new Object();

	private static Object POISON_PILL_COMMENT = new Comment("1124-02-02T19:53:43.226+0000", 0, 0, "", -1, -1);
	private static Object POISON_PILL_POST = new Post("1124-02-02T19:53:43.226+0000", 0, 0, "");

	Parser(String commentsPath, String postsPath, BlockingQueue<Object> queue) {
		this.queue = queue;
		try {
			brPost = new BufferedReader(new FileReader(postsPath));
			brComment = new BufferedReader(new FileReader(commentsPath));
		} catch (FileNotFoundException e) {
			e.getMessage();
		}
	}

	@Override
	public void run() {
		for (;;) {
			if (listComment.size() == 0) {
				String[] elements = null;
				try {
					if ((line = brComment.readLine()) != null) {
						elements = line.split(SEPARATOR);
						long userId;
						if (elements[5].equals(""))
							elements[5] = "-1";
						if (elements.length == 6)
							userId = -1;
						else
							userId = Long.parseLong(elements[6]);
						Object comment = new Comment(elements[0], Long.parseLong(elements[1]),
								Long.parseLong(elements[2]), elements[4], Long.parseLong(elements[5]), userId);
						listComment.add(comment);
					} else {
						listComment.add(POISON_PILL_COMMENT);
					}
				} catch (NumberFormatException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (listPost.size() == 0) {
				String[] elements = null;
				try {
					if ((line = brPost.readLine()) != null) {
						elements = line.split(SEPARATOR);
						Object post = new Post(elements[0], Long.parseLong(elements[1]), Long.parseLong(elements[2]),
								elements[4]);
						listPost.add(post);
					} else {

						listPost.add(POISON_PILL_POST);
					}
				} catch (NumberFormatException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			DateTime lastPostDate = ((Post) listPost.get(0)).getCreationDate();
			DateTime lastCommentDate = ((Comment) listComment.get(0)).getcreationDate();

			if (listPost.get(0) == POISON_PILL_POST && listComment.get(0) == POISON_PILL_COMMENT) {
				try {
					queue.put(POISON_PILL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Fin parseur");
				break;
			}

			if (lastCommentDate.isAfter(lastPostDate)) {
				if (listPost.get(0) != POISON_PILL_POST) {
					Object post = listPost.remove(0);
					try {
						queue.put(post);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Object comment = listComment.remove(0);
					try {
						queue.put(comment);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			if (lastPostDate.isAfter(lastCommentDate)) {
				if (listComment.get(0) != POISON_PILL_COMMENT) {
					Object comment = listComment.remove(0);
					try {
						queue.put(comment);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Object post = listPost.remove(0);
					try {
						queue.put(post);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

	}

}
