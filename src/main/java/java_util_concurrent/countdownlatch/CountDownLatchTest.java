package java_util_concurrent.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h1>闭锁</h1>
 * @author yonglu.xie
 * @date 2017/11/30
 *
 */
public class CountDownLatchTest {
	
	private static final Integer NUM = 1000;
	
	private static AtomicInteger count = new AtomicInteger();

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(NUM); //
		
		System.out.println("==Begin==");
		for(int i=0; i<NUM; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					count.incrementAndGet();
					System.out.println("Thread==========" + count.get());
					latch.countDown(); //
				}
			}).start();
		}
		System.out.println("==Flag ...==" + count.get());
		latch.await(); //
		System.out.println("==End==" + count.get());
	}
	
}
