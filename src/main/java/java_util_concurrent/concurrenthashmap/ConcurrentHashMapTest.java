package java_util_concurrent.concurrenthashmap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentHashMapTest {

	private static Map<String, AtomicInteger> map = new ConcurrentHashMap<>();
	
	private static AtomicInteger count = new AtomicInteger();
	
	public static void main(String[] args) {
		for(int i=0; i<100; i++) {
			count.incrementAndGet();
			map.putIfAbsent("KEY_" + i, count);
		}
		
		System.out.println(map.get("KEY_60"));
		
		System.out.println();
	}
	
	
	
}
