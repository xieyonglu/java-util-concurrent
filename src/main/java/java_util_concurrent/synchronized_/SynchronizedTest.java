package java_util_concurrent.synchronized_;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Synchronized {
	
	public synchronized void method01() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("=====method01=====");
	}
	
	public synchronized void method02() {
		System.out.println("=====method02=====");
	}
}

/**
 * <p>
 * 假如：
 * class A {
 * 	synchronized void a01(){}
 *  synchronized void a02(){}
 * }
 * 
 * A a = new A();
 * a.a01(); --> x
 * a.a02(); --> y
 * 如果执行x时，y会马上执行吗？
 * 
 * 这里不会的，他会a进行上锁
 * </p>
 * @author yonglu.xie
 *
 */
public class SynchronizedTest {

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		
		System.out.println("1----->");
		Synchronized sync01 = new Synchronized();
		for(int i=0; i<10; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sync01.method01();
				}
			}).start();
		}
		
		System.out.println("2----->");
		Synchronized sync02 = new Synchronized();
		for(int i=0; i<10; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sync01.method02();
				}
			}).start();
		}
		
		System.out.println("3----->");
		executorService.shutdown();
	}

}
