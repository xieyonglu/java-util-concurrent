package java_util_concurrent_locks.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport是用来创建锁和其他同步类的基本线程阻塞原语。 LockSupport中的park()和
 * unpark()的作用分别是阻塞线程和解除阻塞线程，而且park()和unpark()不会遇到“Thread.suspend 和
 * Thread.resume所可能引发的死锁”问题。 因为park()和 unpark()有许可的存在；调用 park() 的线程和另一个试图将其
 * unpark() 的线程之间的竞争将保持活性。
 * 
 * @author yonglu.xie
 *
 */
public class LockSupportTest {

	private static Thread mainThread;

	public static void main(String[] args) {
		ThreadA ta = new ThreadA("ta");
		// 获取主线程
		mainThread = Thread.currentThread();

		System.out.println(Thread.currentThread().getName() + " start ta");
		ta.start();

		System.out.println(Thread.currentThread().getName() + " block");
		// 主线程阻塞
		LockSupport.park(mainThread);

		System.out.println(Thread.currentThread().getName() + " continue");
	}

	static class ThreadA extends Thread {
		public ThreadA(String name) {
			super(name);
		}

		public void run() {
			System.out.println(Thread.currentThread().getName() + " wakup others");
			// 唤醒“主线程”
			LockSupport.unpark(mainThread);
		}
	}
}
