package _03_java_util_concurrent_locks.ReentrantLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReenrantLockTest {
	
	private int count = 0;
	
	private Lock lock = new ReentrantLock(); // 参数默认false，不公平锁
	
	private void read() {
		try {
			if (lock.tryLock(5, TimeUnit.SECONDS)) {
				lock.lock();
				try {
					System.out.println("count----->" + count);
				} catch (Exception e) {

				} finally {
					lock.unlock();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void write() {
		try {
			if (lock.tryLock(5, TimeUnit.SECONDS)) {
				lock.lock();
				try {
					count++;
				} catch (Exception e) {

				} finally {
					lock.unlock();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ReenrantLockTest test = new ReenrantLockTest();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<10; i++) {
					test.write();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<10; i++) {
					test.read();
				}
			}
		}).start();
	}
	
}
