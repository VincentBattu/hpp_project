package project;

import java.util.concurrent.BlockingQueue;

import org.joda.time.DateTime;

public class Scheduler implements Runnable {

	private BlockingQueue<Post> postQueue;
	private BlockingQueue<Comment> commentQueue;

	public Scheduler(BlockingQueue<Post> postQueue, BlockingQueue<Comment> commentQueue) {
		this.postQueue = postQueue;
		this.commentQueue = commentQueue;
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
			if (postEnd && commentEnd){
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
				
			} // Si on est à la fin de la queue comment, on ne retire que dans post
			else if (!postEnd) {
				try {
					lastPost = postQueue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} // Si on est à la fin de la queue post, on ne retirer que dans comment
			else if (!commentEnd) {
				try {
					lastComment = commentQueue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void addComment() {

	}

}
