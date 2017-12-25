package java_util_concurrent.forkjoinpool_recursivetask;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * 最佳应用场景：多核、多内存、可以分割计算再合并的计算密集型任务。 
 * 1. ForkJoinTask类的几个重要方法： 
 * 2. fork()方法：将任务放入队列并安排异步执行，一个任务应该只调用一次fork()函数，除非已经执行完毕并重新初始化。 
 * 3. tryUnfork()方法：尝试把任务从队列中拿出单独处理，但不一定成功。 
 * 4. join()方法：等待计算完成并返回计算结果。
 * 5. isCompletedAbnormally()方法：用于判断任务计算是否发生异常。
 * 
 * @author yonglu.xie
 *
 */
public class ForkJoinTest {
	public static void main(String[] args) {
		long beginTime = System.nanoTime();
		System.out.println("The sum from 1 to 1000 is " + sum(1, 1000));
		System.out.println("Time consumed(nano second) By recursive algorithm : " + (System.nanoTime() - beginTime));

		//
		beginTime = System.nanoTime();
		System.out.println("The sum from 1 to 1000000000 is " + sum1(1, 1000000000));
		System.out.println("Time consumed(nano second) By loop algorithm : " + (System.nanoTime() - beginTime));
		
		//
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		CountTask task = new CountTask(1, 1000000000);
		beginTime = System.nanoTime();
		Future<Long> result = forkJoinPool.submit(task);
		try {
			System.out.println("The sum from 1 to 1000000000 is " + result.get());
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Time consumed(nano second) By ForkJoin algorithm : " + (System.nanoTime() - beginTime));
	}

	private static long sum1(long start, long end) {
		long s = 0l;

		for (long i = start; i <= end; i++) {
			s += i;
		}

		return s;
	}

	private static long sum(long start, long end) {
		if (end > start) {
			return end + sum(start, end - 1);
		} else {
			return start;
		}
	}

}
