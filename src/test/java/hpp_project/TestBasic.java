package hpp_project;

import static org.junit.Assert.*;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.tz.Provider;
import org.junit.Before;
import org.junit.Test;

import project.Comment;
import project.Post;

public class TestBasic {
	
	private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+0000'");
	
	private Post post1 = new Post("2010-02-01T05:12:32.921+0000", 529360, 2608, "Wei Zhu");
	/*
	 * private Post post2 = new Post("2010-02-09T16:21:23.008+0000", 1076792,
	 * 3825,"Richard Richter"); private Post post3 = new
	 * Post("2010-02-11T12:01:34.646+0000", 1301393, 4555,"Wei Zhou"); private
	 * Post post4 = new Post("2010-02-11T19:23:25.459+0000", 299390,
	 * 4661,"Michael Wang"); private Post post5 = new
	 * Post("2010-02-11T19:26:26.459+0000", 299391, 4661,"Michael Wang");
	 * private Post post6 = new Post("2010-02-11T19:26:27.459+0000", 299392,
	 * 4661,"Michael Wang"); private Post post7 = new
	 * Post("2010-02-11T19:26:28.459+0000", 299393, 4661,"Michael Wang");
	 * private Post post8 = new Post("2010-02-11T19:26:29.459+0000", 299394,
	 * 4661,"Michael Wang"); private Post post9 = new
	 * Post("2010-02-11T19:26:30.459+0000", 299395, 4661,"Michael Wang");
	 * private Post post10 = new Post("2010-02-12T10:37:40.873+0000", 798675,
	 * 2788,"Petter Schmidt");
	 */
	Comment com5 = new Comment("2010-02-17T13:24:08.859+0000", 702750, 3825, "great", -1, -1);
	private Post post2 = new Post("2010-02-07T05:12:32.921+0000", 1039993, 3981, "Lei Liu");
	Comment com6 = new Comment("2010-02-10T05:12:32.921+0000", 702750, 3825, "great", -1, -1);

	@Before
	public void executedBeforeEach() {

		/*
		 * static DateTime creationDate = new DateTime(2010,03,01,20,15,10);
		 * static DateTime date = new DateTime(2010,03,03,20,30,10);
		 */
		Comment com1 = new Comment("2010-02-09T04:05:20.777+0000", 529590, 2886, "LOL", -1, -1);
		Comment com2 = new Comment("2010-02-12T18:19:57.527+0000", 702748, 143, "cool", -1, -1);
		Comment com3 = new Comment("2010-02-13T13:18:00.586+0000", 702749, 3825, "great", -1, -1);
		Comment com4 = new Comment("2010-02-17T13:24:08.859+0000", 702750, 3825, "great", -1, -1);

		/*
		 * Comment com1 = new Comment("2010-02-17T13:24:08.859+0000", 702750,
		 * 3825, "great",-1,2608); Comment com2 = new
		 * Comment("2010-02-17T13:24:08.859+0000", 702750, 3825,
		 * "great",-1,2608); Comment com3 = new
		 * Comment("2010-02-17T13:24:08.859+0000", 702750, 3825,
		 * "great",-1,2608); Comment com4 = new
		 * Comment("2010-02-17T13:24:08.859+0000", 702750, 3825,
		 * "great",-1,2608); Comment com5 = new
		 * Comment("2010-02-17T13:24:08.859+0000", 702750, 3825, "great", -1,
		 * 2608); Comment com6 = new Comment("2010-02-17T13:24:08.859+0000",
		 * 702750, 3825, "great",-1,2608); Comment com7 = new
		 * Comment("2010-02-17T13:24:08.859+0000", 702750, 3825,
		 * "great",-1,2608);
		 */

		post1.addComment(com1);
		post1.addComment(com2);
		post1.addComment(com3);
		post1.addComment(com4);
	}

	@Test
	public void testScore() throws IOException {

		int ScoreObtenu = post1.getScoreTotal();
		int ScoreAttendu = 24;
		assertEquals(ScoreAttendu, ScoreObtenu);
	}

	@Test
	public void testCommenteurs() throws IOException {
		int NbCommenteurObtenu = post1.getNbCommenter();
		int NbCommenteurAttendu = 3;

		assertEquals(NbCommenteurAttendu, NbCommenteurObtenu);
	}

	@Test
	public void testMajdate() throws IOException {
		DateTime DateMAJobtenu = post1.getLastMAJDate();
		//DateTime DateMAJAttendu = new DateTime(2010, 02, 17, 13, 24, 8, 859);
		DateTime DateMAJAttendu = formatter.withZone(DateTimeZone.UTC).parseDateTime("2010-02-17T13:24:08.859+0000");
		assertEquals(DateMAJAttendu, DateMAJobtenu);

	}

	@Test
	public void testDateCrea() throws IOException {
		DateTime datecreaObtenu = com5.getcreationDate();
		//DateTime datecreaAttendu = new DateTime(2010, 02, 17, 13, 24, 8, 859);
		DateTime datecreaAttendu = formatter.withZone(DateTimeZone.UTC).parseDateTime("2010-02-17T13:24:08.859+0000");
		assertEquals(datecreaAttendu, datecreaObtenu);
	}

	@Test
	public void testisDead() throws IOException {
		post2.getLastMAJDate();
		DateTime date = new DateTime(2010, 03, 03, 20, 30, 10);
		post2.calculScore(date);
		boolean assertionObtenu = post2.isDead();
		boolean assertionAttendu = true;
		assertEquals(assertionAttendu, assertionObtenu);

	}

}
