package project;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import model.Post;

public class Helper {
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
	              .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}
	
	
	public static void main(String[] args) {
		Post post1 = new Post("1124-02-02T19:53:43.226+0000", 0, 0, "");
		post1.setScoreTotal(80);
		Post post2 = new Post("1124-02-02T19:53:43.226+0000", 0, 0, "");
		
		Map<Long, Post> map = new HashMap<>();
		map.put(23l, post1);
		map.put(123l, post2);

		System.out.println(sortByValue(map));
	}
}
