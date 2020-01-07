package java_util_concurrent.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * <h1>栅栏</h1>
 * 
 * @author yonglu.xie
 * @date 2017/11/30
 *
 */
public class CyclicBarrierTest {

	private static final int NUM = 100;
	
	public static void main(String[] args) {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM, new Runnable() {
			@Override
			public void run() {
				System.out.println("==Let's go...===");
			}
		}); //
		
		System.out.println("==Begin==");
		for(int i=0; i<NUM; i++) {
			final int index = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println(index + "---Reach--");
					try {
						cyclicBarrier.await(); //
//						System.out.println("==End End==");
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		System.out.println("==End==");
	}
	
}

