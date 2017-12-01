package java_util_concurrent.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * <h1>信号量</h1>
 * @author yonglu.xie
 * @date 2017/12/02
 *
 */
public class SemaphoreTest {

	private static final Integer NUM = 20;
	
	/**
	 * 许可证
	 */
	private static final Integer PERMITS = 5;
	
	public static void main(String[] args) {
		 // 线程池 
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		// 只能5个线程同时访问 
		Semaphore semaphore = new Semaphore(PERMITS);
		
		for(int i=0; i<NUM; i++) {
			final int index = i;
			Runnable run = new Runnable() {
				@Override
				public void run() {
					try {
						// 获得许可
						semaphore.acquire();
						
						System.out.println("index----->" + index);
						
						// 访问完后，释放 ，如果屏蔽下面的语句，则在控制台只能打印5条记录，之后线程一直阻塞
						semaphore.release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			executorService.execute(run);
		}
		
		executorService.shutdown();
	}
	
}
