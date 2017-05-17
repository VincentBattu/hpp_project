package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import org.joda.time.DateTime;
import org.joda.time.Days;

import model.Comment;
import model.Entity;
import model.Post;

public class Schedulerv2 implements Runnable {

	/**
	 * Définit l'association entre un commentaire et le post auquel il est
	 * rapporté.
	 */
	private Map<Comment, Post> commentToPost;

	/**
	 * Définit le score de chaque poste. Est trié selon les valeur avec la
	 * méthode de Helper
	 */
	private Map<Post, Integer> postScore;

	/**
	 * Définit l'association entre un id et un post (utile pour associer un
	 * commentaire à un post).
	 */
	private Map<Long, Post> postId;

	// Les queues ci-dessous contiennent les entité de scores 10, 9,8,7 ...
	// selon leur dénomination

	private Queue<Entity> queue10;
	private Queue<Entity> queue9;
	private Queue<Entity> queue8;
	private Queue<Entity> queue7;
	private Queue<Entity> queue6;
	private Queue<Entity> queue5;
	private Queue<Entity> queue4;
	private Queue<Entity> queue3;
	private Queue<Entity> queue2;
	private Queue<Entity> queue1;

	/**
	 * Queue contenant les objets lus dans les fichiers et envoyés par le
	 * parseur
	 */
	private BlockingQueue<Entity> entities;

	/**
	 * Queue envoyant les résultats au printer.
	 */
	private BlockingQueue<String> resultsQueue;

	/**
	 * Poison pill de la queue result
	 */
	public static final String RESULT_POISON_PILL = "ça par exemple";

	public Schedulerv2(BlockingQueue<Entity> entities, BlockingQueue<String> resultsQueue) {

		this.entities = entities;
		this.resultsQueue = resultsQueue;

		commentToPost = new HashMap<>();
		postId = new HashMap<>();
		postScore = new HashMap<>();

		queue10 = new LinkedList<>();
		queue9 = new LinkedList<>();
		queue8 = new LinkedList<>();
		queue7 = new LinkedList<>();
		queue6 = new LinkedList<>();
		queue5 = new LinkedList<>();
		queue4 = new LinkedList<>();
		queue3 = new LinkedList<>();
		queue2 = new LinkedList<>();
		queue1 = new LinkedList<>();
	}

	@Override
	public void run() {
		for (;;) {
			// On retire la dernière entity envoyée par le parseur.
			Entity entity = null;
			try {
				entity = entities.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Si l'on arrive à la fin du traitement (on rencontre la
			// POISON_PILL)
			if (entity == Parser.POISON_PILL) {
				// On ajoute la poison pill à la queue result et on sort de la
				// bouche
				try {
					resultsQueue.put(RESULT_POISON_PILL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}

			// Sinon, si on rencontre une entité, on l'ajoute à la queue10, puis
			// on met
			// à jour les queues et à la map postScore avec un score de 10
			queue10.add(entity);

			// Si l'entité est un post, on l'ajoutee à postScore et à postId
			// Sinon on l'ajoute à commentToPost
			if (entity instanceof Post) {
				postScore.put((Post) entity, 10);
				postId.put(((Post) entity).getId(), (Post) entity);
			} else {

				long linkPost = ((Comment) entity).getLinkPost();
				// Si le commentaire est directement relié à un post, on peut
				// faire la correspondance
				// facilement
				if (linkPost != -1) {
					commentToPost.put((Comment) entity, postId.get(linkPost));
				}
				// Sinon on doit parcourir notre map de commentaires pour
				// trouver le commentaire parent
				else {
					long linkCom = ((Comment) entity).getLinkCom();

					for (Entry<Comment, Post> entry : commentToPost.entrySet()) {
						Comment comment = entry.getKey();
						// Si on trouve le commentaire parent, on définit la
						// relation
						// commentaire fils -> post du commentaire parent
						if (comment.getCommentId() == linkCom) {
							commentToPost.put(comment, postId.get(comment.getLinkPost()));
						}
					}
				}
			}

			updateQueues(entity.getLastMAJDate());

			// On trie les la map des scores
			Helper.sortByValue(postScore);
			
			// On récupère les meilleurs posts
			int cpt = 0;
			List<Post> bestPosts= new ArrayList<Post>();
			for(Entry<Post, Integer> entry : postScore.entrySet()){
				if (cpt >= 3)
					break;
				cpt++;
				Post post = entry.getKey();
				post.setScoreTotal(entry.getValue());
				bestPosts.add(post);
			}
			String result = formatResult(entity.getDate(), bestPosts);
			
			// On push enfin le résultat dans la queue pour qu'il soit transmis au printer
			try {
				resultsQueue.put(result);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Met à jour les queues pour une date donnée
	 * 
	 * @param date
	 */
	private void updateQueues(DateTime date) {

		updateQueue(queue10, queue9, date, 10);
		updateQueue(queue9, queue8, date, 9);
		updateQueue(queue8, queue7, date, 8);
		updateQueue(queue7, queue6, date, 7);
		updateQueue(queue6, queue5, date, 6);
		updateQueue(queue5, queue4, date, 5);
		updateQueue(queue4, queue3, date, 4);
		updateQueue(queue3, queue2, date, 3);
		updateQueue(queue2, queue1, date, 2);

		// Cas de la queue 1 qui ne peut se vider dans une autre mais dont les
		// éléments doivent
		// être supprimés.
		for (;;) {
			Entity nextElement = queue1.peek();
			if (nextElement == null)
				return;
			DateTime currentEntityDate = nextElement.getLastMAJDate();
			if (Days.daysBetween(currentEntityDate, date).getDays() > 24) {
				queue1.remove();

			} else {
				return;
			}
		}

	}

	private void updateQueue(Queue<Entity> currentQueue, Queue<Entity> nextQueue, DateTime date,
			int scoreCurrentQueue) {
		for (;;) {
			// Si la queue est vide, on sort de la fonction
			Entity nextElement = currentQueue.peek();
			if (nextElement == null)
				return;
			DateTime currentEntityDate = nextElement.getLastMAJDate();

			// Si plus de 24 heures se sont écoulées entre al date de l'entité
			// et la date
			// passée en paramètre, on retire cette entité de la queue pour la
			// placer à la
			// tête de la suivante.
			if (Days.daysBetween(currentEntityDate, date).getDays() > 24) {
				currentQueue.remove();
				// Si l'objet nextElement n'est pas mort (ou sur le point de
				// mourir),

				if (updateScore(nextElement)) {
					nextQueue.add(nextElement);
				}

			}
			// Sinon, ça veut dire que tous les éléments sont plus récentes que
			// 24h, on sort donc de la fonction
			else {
				return;
			}
		}
	}

	/**
	 *  Retourne true s'il faut continuer à traiter l'objet dans les queues.
	 *  Faux sinon.
	 * @param entity
	 * @return
	 */
	private boolean updateScore(Entity entity) {
		// Si l'entité est un post, on l'ajoute à la liste des posts en
		// décrémentant son score de 1
		if (entity instanceof Post) {
			// Si le post à une valeur de 1, ça veut dire quil doit mourir, on le supprime.
			// Sinon, on décrémente son score de 1
			if (postScore.get((Post) entity) == 1){
				postScore.remove((Post) entity);
				return false;
			} else {
				postScore.put((Post) entity, postScore.get(entity) - 1);
				return true;
			}
			
		}
		// Si l'entité est un commentaire, on va chercher le post correspondant
		// au commentaire et on décrémente le post de 1
		else {
			Post post = commentToPost.get((Comment) entity);
			// Si le post à une valeur de 1, il va mourir, on le supprime ainsi que son commentaire.
			if (postScore.get(post) == 1){
				postScore.remove(post);
				return false;
			}
			
			// Si la map ne contient pas le post, c'est qu'il est déjà mort, on supprime donc le commentaire
			else if (postScore.get(post) == null){
				commentToPost.remove((Comment) entity);
				return false;
			}
			// Sinon, on décrémente la valeur du post de 1
			else {
				postScore.put(post,  postScore.get(post) -1);
				return true;
			}
		}
	}
	
	public String formatResult(String date, List<Post> bestPosts) {
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
		System.out.println(strBuilder.toString());
		return strBuilder.toString();
	}
	

}
