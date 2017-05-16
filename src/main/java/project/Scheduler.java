package project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;

import org.joda.time.DateTime;

import model.Comment;
import model.Post;

public class Scheduler implements Runnable {

	private BlockingQueue<Object> objects;

	private Map<Long, Post> postsStillAlive = new HashMap<>();

	/**
	 * Map qui associe les scores avec tous les posts qui ont ce score
	 */
	private Map<Integer, List<Post>> scores = new TreeMap<>(Collections.reverseOrder());
	
	/**
	 * Liste des trois meilleurs posts
	 */
	private List<Post> bestPosts = new ArrayList<>(3);

	/**
	 * Résultats envoyés au printer
	 */
	private BlockingQueue<String> resultsQueue;
	
	private int compteur = 0;
	private long t1;
	private long t2;
	
	public static final String RESULT_POISON_PILL = "ça par exemple";

	public Scheduler(BlockingQueue<Object> objects,
			BlockingQueue<String> resultsQueue) {
		this.objects = objects;
		this.resultsQueue = resultsQueue;
	}

	@Override
	public void run() {
		t1 = System.currentTimeMillis();
		for (;;){
			
			if (compteur == 1000){
				compteur = 0;
				t2 = System.currentTimeMillis();
				System.out.println("Pour 1000 entités, tps traitement : " + (t2-t1) + " ms");
				t1=t2;
			}
			compteur++;
			Object object = null;
			try {
				object = objects.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			List<Long> tempsIdsBestPosts = new ArrayList<>(3);
			for (Post post : bestPosts){
				tempsIdsBestPosts.add(post.getId());
			}
			if (object == Parser.POISON_PILL){
				try {
					resultsQueue.put(RESULT_POISON_PILL);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("FIN");
				break;
			}
			else if (object instanceof Post){
				
				addPost((Post) object);
				updateScores(((Post) object).getLastMAJDate());
				if (!compare(tempsIdsBestPosts, bestPosts)){
					try {
						resultsQueue.put(formatResult(((Post) object).getDate()));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				addComment((Comment) object);
				updateScores(((Comment) object).getLastMAJDate());
				if (!compare(tempsIdsBestPosts, bestPosts)){
					try {
						resultsQueue.put(formatResult(((Comment) object).getDate()));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}
		
	}
	
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

			// On met à jour le score total du post
			post.calculScore(date);
			// Si le poste est mort, on le supprime de la map
			if (post.isDead()) {
				idsPostDead.add(key);
			}
		}
		for(int i=0 ; i<idsPostDead.size();i++){
			postsStillAlive.remove(idsPostDead.get(i));
		}

		// Mise à jour de la map score => [idPost1, idPost2]
		scores.clear();
		for (Entry<Long, Post> entry : postsStillAlive.entrySet()) {
			Long key = entry.getKey();
			Integer score = postsStillAlive.get(key).getScoreTotal();

			if (scores.containsKey(score)) {
				List<Post> posts = scores.get(score);
				posts.add(postsStillAlive.get(key));
				Collections.sort(posts);
				Collections.reverse(posts);
				scores.put(score, posts);
			} else {
				List<Post> posts = new ArrayList<>();
				posts.add(postsStillAlive.get(key));
				scores.put(score, posts);
			}

		}
		updateBestScores();

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
					if (nbPostsTaken >= 3) return;						
				}
			} else {
				bestPosts.add(posts.get(0));
				nbPostsTaken++;
				if (nbPostsTaken >= 3) return;
			}
		}

	}

	public String formatResult(String date) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(date);
		for (Post post : bestPosts) {
			strBuilder.append(',');
			strBuilder.append(post.getId());
			strBuilder.append(',');
			strBuilder.append(post.getUserName());
			strBuilder.append(',');
			strBuilder.append(post.getScoreTotal());
			strBuilder.append(',');
			strBuilder.append(post.getNbCommenter());
		}
		for (int i = 0; i < 3 - bestPosts.size(); i++) {
			strBuilder.append(',');
			strBuilder.append('-');
			strBuilder.append(',');
			strBuilder.append('-');
			strBuilder.append(',');
			strBuilder.append('-');
			strBuilder.append(',');
			strBuilder.append('-');
		}
		return strBuilder.toString();
	}
	
	private boolean compare(List<Long> l1, List<Post> l2){

		if (l1.size() == l2.size()){
			for (int i =0 ; i <l1.size(); i++){
				if (l1.get(i) != l2.get(i).getId()){
					return false;
				}
			}
			return true;
		} else{
			return false;
		}
		
	}

}
