package project;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import model.Post;
import model.Result;

public class Formatter implements Runnable {

	private BlockingQueue<Result> top3Queue;
	private StringBuilder strBuilder;

	public Formatter(BlockingQueue<Result> top3Queue) {
		this.top3Queue = top3Queue;
		strBuilder = new StringBuilder();
	}

	@Override
	public void run() {
		Result result = null;
		for (;;) {
			try {
				result = top3Queue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (result == Schedulerv2.RESULT_POISON_PILL2) {
				break;
			}

			strBuilder.setLength(0);
			strBuilder.trimToSize();
			strBuilder.append(result.getDate());
			List<Post> top3 = result.getTop3();
			synchronized (top3) {
				for (Post post : top3) {
					strBuilder.append(',');
					strBuilder.append(post.getId());
					strBuilder.append(',');
					strBuilder.append(post.getUserName());
					strBuilder.append(',');
					strBuilder.append(post.getScoreTotal());
					strBuilder.append(',');
					strBuilder.append(post.getNbCommenter());

				}
				for (int i = 0; i < 3 - top3.size(); i++) {
					strBuilder.append(",-,-,-,-");
				}
				System.out.println(strBuilder.toString());
			}

		}

	}

}
