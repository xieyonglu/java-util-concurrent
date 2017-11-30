package java_util_concurrent_automic.atomicinteger;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {
	
	private static AtomicInteger counter = new AtomicInteger();

	public static void main(String[] args) {
		for(int i=0; i<100; i++) {
			// i++
			//System.out.println(counter.getAndIncrement() + "-->" + counter.get());
			
			// ++i
			//System.out.println(counter.incrementAndGet() + "-->" + counter.get());
			
			// i--
			//System.out.println(counter.getAndDecrement() + "-->" + counter.get());
			
			// --i
			System.out.println(counter.decrementAndGet() + "-->" + counter.get());
		}
	}

}
