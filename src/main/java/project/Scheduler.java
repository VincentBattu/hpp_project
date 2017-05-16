package project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import org.joda.time.DateTime;

public class Scheduler implements Runnable {

	private BlockingQueue<Post> postQueue;
	private BlockingQueue<Comment> commentQueue;

	private Map<Long, Post> postsStillAlive = new HashMap<>();

	/**
	 * Map qui associe les scores avec tous les posts qui ont ce score
	 */
	private Map<Integer, List<Post>> scores = new TreeMap<>(Collections.reverseOrder());

	/**
	 * Liste des trois meilleurs posts
	 */
	private List<Post> bestPosts = new Vector<>(3);

	/**
	 * Résultats envoyés au printer
	 */
	private BlockingQueue<Result> resultsQueue;

	public static final String POISON_PILL = "ça par exemple";

	public static final Result POISON_PILL_RESULT = new Result("", new ArrayList<>());

	public Scheduler(BlockingQueue<Post> postQueue, BlockingQueue<Comment> commentQueue,
			BlockingQueue<Result> resultsQueue) {
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
			List<Long> tempIdsBestPost = new ArrayList<>();
			for (Post post : bestPosts) {
				tempIdsBestPost.add(post.getId());
			}

			if (!postEnd && !commentEnd) {
				lastPostDate = lastPost.getCreationDate();
				lastCommentDate = lastComment.getcreationDate();

				if (lastCommentDate.isAfter(lastPostDate)) {
					try {
						if (lastPost != PostParser.POISON_PILL) {
							addPost(lastPost);
							updateScores(lastPost.getLastMAJDate());
							updateBestScores();
							if (!compare(tempIdsBestPost, bestPosts)) {
								Result res = new Result(lastPost.getDate(), bestPosts);
								resultsQueue.put(res);
							}

						}
						lastPost = postQueue.take();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					try {
						if (lastComment != CommentParser.POISON_PILL) {
							addComment(lastComment);
							updateScores(lastComment.getLastMAJDate());
							updateBestScores();
							if (!compare(tempIdsBestPost, bestPosts)) {
								Result res = new Result(lastComment.getDate(), bestPosts);
								resultsQueue.put(res);
							}
						}
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
						updateScores(lastPost.getLastMAJDate());
						updateBestScores();
						if (!compare(tempIdsBestPost, bestPosts)) {
							Result res = new Result(lastPost.getDate(), bestPosts);
							resultsQueue.put(res);
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
					if (lastComment != CommentParser.POISON_PILL) {
						addComment(lastComment);
						updateScores(lastComment.getLastMAJDate());
						updateBestScores();
						if (!compare(tempIdsBestPost, bestPosts)) {
							Result res = new Result(lastComment.getDate(), bestPosts);
							resultsQueue.put(res);
						}
					}
					lastComment = commentQueue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			resultsQueue.put(POISON_PILL_RESULT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ajoute un commentaire
	 */
	private void addComment(Comment comment) {

		for (Entry<Long, Post> entry : postsStillAlive.entrySet()) {
			Post post = entry.getValue();

			if (comment.getLinkPost() == -1) {
				List<Comment> comments = post.getComments();
				for (int i = 0; i < comments.size(); i++) {
					if (comments.get(i).getCommentId() == comment.getLinkCom()) {
						post.addComment(comment);
						break;
					}
				}
			} // Si le commentaire est directement relié à un post
			else {
				if (post.getId() == comment.getLinkPost()) {
					// Si on trouve on post qui a été commenté, on ajoute le
					// commentaire
					// et on sort de la boucle
					post.addComment(comment);
					break;

				}
			}

		}
	}

	/**
	 * Ajoute un post à la liste des posts en vie
	 */
	private void addPost(Post post) {
		this.postsStillAlive.put(post.getId(), post);
	}

	/**
	 * Mets à jour les map postStillAlive, scores, et bestPosts à la date donnée
	 */
	private void updateScores(DateTime date) {

		// Mise à jour de la map idPost => POST (calcul de leur score et
		// suppression des posts morts)
		List<Long> idsPostDead = new ArrayList<>();
		for (Entry<Long, Post> entry : postsStillAlive.entrySet()) {
			Long key = entry.getKey();
			Post post = entry.getValue();

			int scoreBefore = post.getScoreTotal();
			post.calculScore(date);
			int newScore = post.getScoreTotal();

			if (newScore != scoreBefore && newScore != 0) {
				if (scores.containsKey(scoreBefore)) {
					List<Post> postScore = new ArrayList<>();
					postScore = scores.get(scoreBefore);
					postScore.remove(post);
					if (postScore.size() != 0) {
						Collections.sort(postScore);
						Collections.reverse(postScore);
						scores.put(scoreBefore, postScore);
					} else {
						scores.remove(scoreBefore);
					}

				}
				if (scores.containsKey(newScore)) {
					List<Post> posts = scores.get(newScore);
					posts.add(post);
					Collections.sort(posts);
					Collections.reverse(posts);
					scores.put(newScore, posts);
				} else {
					List<Post> posts = new ArrayList<>();
					posts.add(post);
					scores.put(newScore, posts);
				}
			}

			// On met à jour le score total du post

			// Si le poste est mort, on le supprime de la map
			if (post.isDead()) {
				idsPostDead.add(key);
			}
		}

		for (int i = 0; i < idsPostDead.size(); i++) {
			postsStillAlive.remove(idsPostDead.get(i));
		}

		// updateBestScores();

	}

	/**
	 * Actualise la liste des trois meilleurs post
	 */
	public void updateBestScores() {
		int nbPostsTaken = 0;
		bestPosts.clear();
		// On parcourt la map des scores
		for (Entry<Integer, List<Post>> entry : scores.entrySet()) {
			List<Post> posts = entry.getValue();
			if (posts.size() > 1) {

				for (int i = 0; i < posts.size(); i++) {
					bestPosts.add(posts.get(i));
					nbPostsTaken++;
					if (nbPostsTaken >= 3)
						return;
				}
			} else {
				bestPosts.add(posts.get(0));
				nbPostsTaken++;
				if (nbPostsTaken >= 3)
					return;
			}
		}

	}

	private boolean compare(List<Long> l1, List<Post> l2) {

		if (l1.size() == l2.size()) {
			for (int i = 0; i < l1.size(); i++) {
				if (l1.get(i) != l2.get(i).getId()) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}

	}

}
