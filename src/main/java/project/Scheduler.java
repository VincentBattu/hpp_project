package project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.joda.time.DateTime;

public class Scheduler implements Runnable {

	private BlockingQueue<Post> postQueue;
	private BlockingQueue<Comment> commentQueue;

	private Map<Integer, Post> postsStillAlive = new HashMap<>();
	private Map<Integer, Integer> scores = new HashMap<>();

	private List<Post> bestPosts = new ArrayList<>(3);

	/**
	 * Résultats envoyés au printer
	 */
	private BlockingQueue<String> resultsQueue;

	public Scheduler(BlockingQueue<Post> postQueue, BlockingQueue<Comment> commentQueue,
			BlockingQueue<String> resultsQueue) {
		this.postQueue = postQueue;
		this.commentQueue = commentQueue;
		this.resultsQueue = resultsQueue;
	}

	@Override
	public void run() {

		DateTime lastPostDate;
		DateTime lastCommentDate;
		Post lastPost = null;
		Comment lastComment = null;
		boolean postEnd = false;
		boolean commentEnd = false;
		try {
			lastPost = postQueue.take();
			lastComment = commentQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (;;) {
			if (postEnd && commentEnd) {
				break;
			}
			if (lastPost == PostParser.POISON_PILL) {
				postEnd = true;
			}
			if (lastComment == CommentParser.POISON_PILL) {
				commentEnd = true;
			}

			// Si on n'est pas à la fin d'une des deux queues.
			if (!postEnd && !commentEnd) {
				lastPostDate = lastPost.getCreationDate();
				lastCommentDate = lastComment.getcreationDate();

				if (lastCommentDate.isAfter(lastPostDate)) {
					try {
						lastPost = postQueue.take();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					try {
						lastComment = commentQueue.take();

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			} // Si on est à la fin de la queue comment, on ne retire que dans
				// post
			else if (!postEnd) {
				try {
					if (lastPost != PostParser.POISON_PILL) {
						addPost(lastPost);
						update(lastPost.getLastMAJDate());
						System.out.println("Changement");
						for (Post post : bestPosts) {
							System.out.println(post.getDate());
						}

					}
					lastPost = postQueue.take();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} // Si on est à la fin de la queue post, on ne retirer que dans
				// comment
			else if (!commentEnd) {
				try {
					lastComment = commentQueue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Ajoute un commentaire
	 */
	private void addComment() {

	}

	/**
	 * Ajoute un post à la liste des posts en vie
	 */
	private void addPost(Post post) {

		this.postsStillAlive.put(post.getId(), post);
		scores.put(post.getId(), post.getScoreTotal());

	}

	/**
	 * Mets à jour les map postStillAlive, scores, et bestPosts à la date donnée
	 */
	private void update(DateTime date) {
		// Mise à jour de la map idPost => POST
		for (Entry<Integer, Post> entry : postsStillAlive.entrySet()) {
			Integer key = entry.getKey();
			Post post = entry.getValue();

			// On met à jour le score total du post
			post.calculScore(date);
			// Si le poste est mort, on le supprime de la map
			if (post.isDead()) {
				postsStillAlive.remove(key);
			}
		}

		// Mise à jour de la map idPost => Score
		for (Entry<Integer, Integer> entry : scores.entrySet()) {
			Integer key = entry.getKey();
			Integer score = postsStillAlive.get(key).getScoreTotal();
			scores.put(key, score);
		}
		sortMap(scores);

	}

	/**
	 * Actualise la liste des trois meilleurs post
	 */
	public void sortMap(Map<Integer, Integer> unsortedMap) {
		List<Entry<Integer, Integer>> list = new LinkedList<Map.Entry<Integer, Integer>>(unsortedMap.entrySet());

		// sorting the list with a comparator
		Collections.sort(list, new Comparator<Entry<Integer, Integer>>() {
			public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue()) * (-1);
			}
		});

		// convert sortedMap back to Map
		Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
		for (Entry<Integer, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		Set<Integer> ids = sortedMap.keySet();
		int cpt = 0;
		bestPosts.clear();
		for (Integer id : ids) {
			if (cpt < 3) {
				bestPosts.add(postsStillAlive.get(id));
				cpt++;
			} else {
				break;
			}
		}

	}

}
