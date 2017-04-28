package project;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Main {

	static DateTime creationDate = new DateTime(2010,03,01,20,15,10);
	static DateTime date = new DateTime(2010,03,03,20,30,10);
	
	public static void main(String[] args) {
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+0000'");
		
		DateTime dat = formatter.parseDateTime("2010-02-01T05:12:32.921+0000");
		System.out.println(dat);
		System.out.println(Days.daysBetween(creationDate, date));
		System.out.println(Hours.hoursBetween(creationDate, date));
		System.out.println(Seconds.secondsBetween(creationDate, date));
		System.out.println(new Period(creationDate, date));
	}
}
