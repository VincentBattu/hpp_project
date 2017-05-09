package hpp_project;

import static org.junit.Assert.*;

import java.io.IOException;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import project.Comment;
import project.Post;

public class TestBasic {
	private Post post1 = new Post("2010-02-01T05:12:32.921+0000", 1039993, 3981,"Lei Liu");
	@Before
	public void executedBeforeEach() {

	/*static DateTime creationDate = new DateTime(2010,03,01,20,15,10);
	static DateTime date = new DateTime(2010,03,03,20,30,10);*/
	Comment com1 = new Comment("2010-02-09T04:05:20.777+0000", 529590, 2886, "LOL",-1,-1);
	Comment com2 = new Comment("2010-02-12T18:19:57.527+0000", 702748, 143, "cool",-1,-1);
	Comment com3 = new Comment("2010-02-13T13:18:00.586+0000", 702749, 3825, "great",-1,-1);
	Comment com4 = new Comment("2010-02-17T13:24:08.859+0000", 702750, 3825, "great",-1,-1); 
	//Post post1 = new Post("2010-02-01T05:12:32.921+0000", 1039993, 3981,"Lei Liu");
	post1.addComment(com1);
	post1.addComment(com2);
	post1.addComment(com3);
	post1.addComment(com4);
	}

	@Test
	public void testScore() throws IOException {
		
		int ScoreObtenu= post1.getScoreTotal();
		int ScoreAttendu= 18;
		assertEquals(ScoreAttendu,ScoreObtenu);
	}
	@Test
	public void testCommenteurs() throws IOException{
			int NbCommenteurObtenu=post1.getNbCommenter();
			int NbCommenteurAttendu=3;
			
			assertEquals(NbCommenteurObtenu,NbCommenteurAttendu);
	}
			
}
	


