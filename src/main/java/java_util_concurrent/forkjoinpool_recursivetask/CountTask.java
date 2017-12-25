package java_util_concurrent.forkjoinpool_recursivetask;

import java.util.concurrent.RecursiveTask;

/**
 * <h1>递归任务</h1>
 * @author yonglu.xie
 *
 */
public class CountTask extends RecursiveTask<Long> {

	private static final long serialVersionUID = 6680974096282278116L;

	private static final int THRESHOLD = 10000;

	private int start;
	private int end;

	public CountTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	protected Long compute() {
		// System.out.println("Thread ID: " + Thread.currentThread().getId());
		Long sum = 0L;

		if ((end - start) <= THRESHOLD) {
			sum = sum1(start, end);
		} else {
			int middle = (start + end) / 2;
			CountTask leftTask = new CountTask(start, middle);
			CountTask rightTask = new CountTask(middle + 1, end);
			leftTask.fork();
			rightTask.fork();

			Long leftResult = leftTask.join();
			Long rightResult = rightTask.join();

			sum = leftResult + rightResult;
		}

		return sum;
	}

	private static long sum1(long start, long end) {
		long s = 0l;

		for (long i = start; i <= end; i++) {
			s += i;
		}

		return s;
	}
	
}
