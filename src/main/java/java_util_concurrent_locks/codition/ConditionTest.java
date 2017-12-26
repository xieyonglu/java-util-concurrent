package java_util_concurrent_locks.codition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者、消费者示例
 */
public class ConditionTest {
	private int storage;
	private int putCounter;
	private int getCounter;
	private Lock lock = new ReentrantLock();
	private Condition putCondition = lock.newCondition();
	private Condition getCondition = lock.newCondition();

	public void put() throws InterruptedException {
		try {
			lock.lock();
			if (storage > 0) {
				putCondition.await();
			}
			storage++;
			System.out.println("put => " + ++putCounter);
			getCondition.signal();
		} finally {
			lock.unlock();
		}
	}

	public void get() throws InterruptedException {
		try {
			lock.lock();
			lock.lock();
			if (storage <= 0) {
				getCondition.await();
			}
			storage--;
			System.out.println("get  => " + ++getCounter);
			putCondition.signal();
		} finally {
			lock.unlock();
			lock.unlock();
		}
	}

	public class PutThread extends Thread {
		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				try {
					put();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public class GetThread extends Thread {
		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				try {
					get();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public static void main(String[] args) {
		final ConditionTest test = new ConditionTest();
		Thread put = test.new PutThread();
		Thread get = test.new GetThread();
		put.start();
		get.start();
	}
}
