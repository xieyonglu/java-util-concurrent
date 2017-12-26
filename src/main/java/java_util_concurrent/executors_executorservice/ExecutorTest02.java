package java_util_concurrent.executors_executorservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorTest02 {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
		
		for(int i=0; i<5; i++) {
			final int TaskID = i;
			fixedThreadPool.execute(new Runnable(){

				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						System.out.println("----->" + TaskID);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			});
		}
		
		fixedThreadPool.shutdown();
		
		// 等待子线程结束，再继续执行下面的代码
		fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        System.out.println("all thread complete");
	}

}
