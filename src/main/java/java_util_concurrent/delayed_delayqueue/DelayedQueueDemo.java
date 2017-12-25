package java_util_concurrent.delayed_delayqueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * DelayedTask线程要实现Delayed接口的getDelay()和compareTo()方法，放入DelayQueue队列后，通过take()方法取出时，可根据compareTo方法制定的顺序来优先取出线程执行
 * 静态类EndSentinel负责遍历所有的线程，其设置的trigger最大，最后被执行时调用ExecutorService.shutdownNow()，来结束线程的执行
 * 
 * @author yonglu.xie
 *
 */
public class DelayedQueueDemo {

	public static void main(String[] args) {
		Random rand = new Random(47);
		DelayQueue<DelayedTask> queue = new DelayQueue<>();
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 20; i++) {
			queue.put(new DelayedTask(rand.nextInt(5000)));
		}
		queue.add(new DelayedTask.EndSentinel(5000, exec));
		exec.execute(new DelayedTaskConsumer(queue));
	}

}

class DelayedTask implements Delayed, Runnable {
	private static int counter = 0;
	private final int id = counter++;
	private final int delta;
	private final long trigger;
	protected static List<DelayedTask> sequence = new ArrayList<DelayedTask>();

	public DelayedTask(int delayInMilliseconds) {
		delta = delayInMilliseconds;
		trigger = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delta, TimeUnit.MILLISECONDS);
		sequence.add(this);
	}

	@Override
	public int compareTo(Delayed o) {
		// TODO Auto-generated method stub
		DelayedTask that = (DelayedTask) o;
		if (this.trigger > that.trigger)
			return 1;
		if (this.trigger < that.trigger)
			return -1;
		return 0;
	}

	@Override
	public void run() {
		System.out.println(this + "  is running");
	}

	public String toString() {
		return "  Task:" + id + " delta:" + delta;
	}

	public String summary() {
		return " id:" + id + "  delta:" + delta;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(trigger - System.nanoTime(), TimeUnit.NANOSECONDS);
	}

	public static class EndSentinel extends DelayedTask {
		private ExecutorService exec;

		public EndSentinel(int delayInMilliseconds, ExecutorService exec) {
			super(delayInMilliseconds);
			this.exec = exec;
		}

		public void run() {
			for (DelayedTask dt : sequence) {
				System.out.println(dt.summary() + "  ");
			}
			System.out.print(' ');
			System.out.println(this + " Calling ShutdownNow()");
			exec.shutdownNow();
		}
	}
}

class DelayedTaskConsumer implements Runnable {
	private DelayQueue<DelayedTask> dq;

	public DelayedTaskConsumer(DelayQueue<DelayedTask> q) {
		dq = q;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				dq.take().run();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("finished delayedtask consume!!!!!");
	}
}
