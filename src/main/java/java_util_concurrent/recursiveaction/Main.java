package java_util_concurrent.recursiveaction;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author lishehe-2015年6月19日 RecursiveAction代表没有返回值的任务。
 * RecursiveAction 无返回值任务。
 * RecursiveTask有返回值类型。
 */
class PrintTask extends RecursiveAction {
	
	private static final long serialVersionUID = -8949269031799230088L;

	// 每个"小任务"最多只打印20个数
	private static final int MAX = 20;

	private int start;
	private int end;

	PrintTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {
		// 当end-start的值小于MAX时候，开始打印
		if ((end - start) < MAX) {
			for (int i = start; i < end; i++) {
				System.out.println(Thread.currentThread().getName() + "的i值:" + i);
			}
		} else {
			// 将大任务分解成两个小任务
			int middle = (start + end) / 2;
			PrintTask left = new PrintTask(start, middle);
			PrintTask right = new PrintTask(middle, end);
			// 并行执行两个小任务
			left.fork();
			right.fork();
			
			// 方式02
//			invokeAll(left, right);
		}
	}
}

public class Main {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// 创建包含Runtime.getRuntime().availableProcessors()返回值作为个数的并行线程的ForkJoinPool
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		// 提交可分解的PrintTask任务
		forkJoinPool.submit(new PrintTask(0, 1000));
		forkJoinPool.awaitTermination(2, TimeUnit.SECONDS);// 阻塞当前线程直到 ForkJoinPool 中所有的任务都执行结束
		// 关闭线程池
		forkJoinPool.shutdown();
		
		// 方式02
//		forkJoinPool.invoke(new PrintTask(0, 1000));
	}

}
