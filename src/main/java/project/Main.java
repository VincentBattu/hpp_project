package project;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Period;
import org.joda.time.Seconds;

public class Main {

	static DateTime creationDate = new DateTime(2010,03,01,20,15,10);
	static DateTime date = new DateTime(2010,03,03,20,30,10);
	
	public static void main(String[] args) {
		System.out.println(Days.daysBetween(creationDate, date));
		System.out.println(Hours.hoursBetween(creationDate, date));
		System.out.println(Seconds.secondsBetween(creationDate, date));
		System.out.println(new Period(creationDate, date));
	}
}
