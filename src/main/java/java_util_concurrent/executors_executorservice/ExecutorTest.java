package java_util_concurrent.executors_executorservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorTest {

	public static void main(String[] args) {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

		ExecutorService singleThread = Executors.newSingleThreadExecutor();

		// ScheduledExecutorService
		ExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);

		run(fixedThreadPool);
	}

	private static void run(ExecutorService threadPool) {
		for (int i = 1; i < 5; i++) {
			final int taskID = i;
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					for (int i = 1; i < 5; i++) {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("第" + taskID + "次任务的第" + i + "次执行");
					}
				}
			});
		}
		threadPool.shutdown();// 任务执行完毕，关闭线程池
	}

}
