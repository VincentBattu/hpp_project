package project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import org.joda.time.DateTime;
import org.joda.time.Days;

import model.Comment;
import model.Entity;
import model.Post;
import model.Result;

public class Schedulerv2 implements Runnable {

	/**
	 * Définit l'association entre un commentaire et le post auquel il est
	 * rapporté. Les association sont faites à l'aide des id
	 */
	private Map<Long, Long> commentToPost;

	/**
	 * Définit le score de chaque poste à l'aide de son id. Est trié selon les
	 * valeur avec la méthode de Helper
	 */
	private Map<Long, Integer> postScore;

	/**
	 * Définit l'association entre un id et un post
	 */
	private Map<Long, Post> postIdMap;

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
	
	private BlockingQueue<Result> resultsqueue;

	/**
	 * Liste qui contient les 3 meilleurs posts (id => post)
	 */
	private List<Post> top3;

	/**
	 * Le plus petit score du top 3
	 */
	private int scoreMin = 0;

	/**
	 * Poison pill de la queue result
	 */
	public static final String RESULT_POISON_PILL = "ça par exemple";
	
	public static final Result RESULT_POISON_PILL2 = new Result("", new ArrayList<>(1));

	public Schedulerv2(BlockingQueue<Entity> entities, BlockingQueue<String> resultsQueue, BlockingQueue<Result> resultqueue) {

		this.entities = entities;
		this.resultsQueue = resultsQueue;
		this.resultsqueue = resultqueue;

		commentToPost = new HashMap<>();
		postIdMap = new HashMap<>();
		postScore = new HashMap<>();
		top3 = new Vector<>(3);

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
		int cpt = 0;
		long t1  = System.currentTimeMillis();
		for (;;) {
			// On retire la dernière entity envoyée par le parseur.
			Entity entity = null;
			try {
 			entity = entities.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			cpt++;
			if (cpt == 1000){
				cpt = 0;
				long t2 = System.currentTimeMillis();
				System.out.println((t2-t1));
				t1 = t2;
			}
			// Si l'on arrive à la fin du traitement (on rencontre la
			// POISON_PILL)
			if (entity == Parser.POISON_PILL) {
				// On ajoute la poison pill à la queue result et on sort de la
				// bouche
				try {
					resultsqueue.put(RESULT_POISON_PILL2);
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
				Post post = (Post) entity;
				postIdMap.put(post.getId(), post);
				if (top3.size() != 3) {
					top3.add(post);
					updateQueues(entity.getLastMAJDate());
					Collections.sort(top3, Collections.reverseOrder());
					//formatResult(entity.getDate(),top3);
					
				} else if (post.getScoreTotal() > scoreMin) {
					updateTop3(post);
					updateQueues(entity.getLastMAJDate());

					Collections.sort(top3, Collections.reverseOrder());
					
					//formatResult(entity.getDate(),top3);
				} else if (post.getScoreTotal() == scoreMin) {
					if (post.compareTo(top3.get(2)) == 1) {
						updateTop3(post);
						updateQueues(entity.getLastMAJDate());

						Collections.sort(top3, Collections.reverseOrder());
						
						//formatResult(entity.getDate(),top3);
					}
				}
				scoreMin = top3.get(top3.size()-1).getScoreTotal();
			} else {

				long linkPost = ((Comment) entity).getLinkPost();
				// Si le commentaire est directement relié à un post, on peut
				// faire la correspondance
				// facilement
				if (linkPost != -1) {
					long commentId = ((Comment) entity).getCommentId();
					commentToPost.put(commentId, linkPost);
					Post post = postIdMap.get(linkPost);
					post.addCommenter(((Comment) entity).getUserId());
					postIdMap.put(linkPost, post);
					if (top3.size() != 3) {
						top3.add(post);
						updateQueues(entity.getLastMAJDate());
						Collections.sort(top3, Collections.reverseOrder());
						formatResult(entity.getDate(),top3);
					} else if (post.getScoreTotal() > scoreMin) {
						updateTop3(post);
						updateQueues(entity.getLastMAJDate());

						Collections.sort(top3, Collections.reverseOrder());
						
						//formatResult(entity.getDate(),top3);
					} else if (post.getScoreTotal() == scoreMin) {
						if (post.compareTo(top3.get(2)) == 1) {
							updateTop3(post);
							updateQueues(entity.getLastMAJDate());

							Collections.sort(top3, Collections.reverseOrder());
							
							//formatResult(entity.getDate(),top3);
						}
					}
					scoreMin = top3.get(top3.size()-1).getScoreTotal();				}
				// Sinon on doit parcourir notre map de commentaires pour
				// trouver le commentaire parent
				else {
					long linkCom = ((Comment) entity).getLinkCom();

					for (Entry<Long, Long> entry : commentToPost.entrySet()) {
						Long commentId = entry.getKey();
						// Si on trouve le commentaire parent, on définit la
						// relation
						// commentaire fils -> post du commentaire parent
						if (commentId == linkCom) {
							long postId = entry.getValue();
							commentToPost.put(commentId, postId);
							Post post = postIdMap.get(postId);
							post.addCommenter(((Comment) entity).getUserId());
							postIdMap.put(postId, post);
							if (top3.size() != 3) {
								top3.add(post);
								updateQueues(entity.getLastMAJDate());

								Collections.sort(top3, Collections.reverseOrder());
								
								//formatResult(entity.getDate(),top3);
							} else if (post.getScoreTotal() > scoreMin) {
								updateTop3(post);
								updateQueues(entity.getLastMAJDate());

								Collections.sort(top3, Collections.reverseOrder());
								
								//formatResult(entity.getDate(),top3);
							} else if (post.getScoreTotal() == scoreMin) {
								if (post.compareTo(top3.get(2)) == 1) {
									updateTop3(post);
									updateQueues(entity.getLastMAJDate());

									Collections.sort(top3, Collections.reverseOrder());
									
									//formatResult(entity.getDate(),top3);
								}
							}
							scoreMin = top3.get(top3.size()-1).getScoreTotal();
							break;
						}
					}
				}
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
			if (Days.daysBetween(currentEntityDate, date).getDays() - nextElement.getNbDays() >= 1) {

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
	 * Retourne true s'il faut continuer à traiter l'objet dans les queues. Faux
	 * sinon.
	 * 
	 * @param entity
	 * @return
	 */
	private boolean updateScore(Entity entity) {
		// Si l'entité est un post, on l'ajoute à la liste des posts en
		// décrémentant son score de 1
		if (entity instanceof Post) {
			// Si le post à une valeur de 1, ça veut dire quil doit mourir, on
			// le supprime.
			// Sinon, on décrémente son score de 1
			long postId = ((Post) entity).getId();
			if (postIdMap.get(postId).getScoreTotal() == 1) {
				postIdMap.remove(postId);
				return false;
			} else {
				Post postUpdated = postIdMap.get(postId);
				postUpdated.decrementScore();
				postUpdated.incrementNbDays();
				postIdMap.put(postId, postUpdated);
				return true;
			}

		}
		// Si l'entité est un commentaire, on va chercher le post correspondant
		// au commentaire et on décrémente le post de 1
		else {
			Long postId = commentToPost.get(((Comment) entity).getCommentId());
			// Si la map ne contient pas le post, c'est qu'il est déjà mort, on
			// supprime donc le commentaire
			if (postIdMap.get(postId) == null) {
				commentToPost.remove(((Comment) entity).getCommentId());
				return false;
			}

			// Si le post à une valeur de 1, il va mourir, on le supprime ainsi
			// que son commentaire.

			else if (postIdMap.get(postId).getScoreTotal() == 1) {
				postIdMap.remove(postId);
				return false;

			}
			// Sinon, on décrémente la valeur du post de 1
			else {
				entity.incrementNbDays();
				Post updatedPost = postIdMap.get(postId);
				updatedPost.decrementScore();
				postIdMap.put(postId, updatedPost);
				return true;
			}
		}
	}

	private void updateTop3(Post post) {
		long idPost = post.getId();

		if (idPost == top3.get(0).getId()) {
			top3.add(0, post);
		} else if (idPost == top3.get(1).getId()) {
			top3.add(1, post);
		} else if (idPost == top3.get(2).getId()) {
			top3.add(2, post);
		} else {
			top3.remove(2);
			top3.add(post);
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
			strBuilder.append(",-,-,-,-");
		}
		return strBuilder.toString(); 
	}

}
