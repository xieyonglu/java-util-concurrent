package java_util_concurrent.exchanger;

import java.util.concurrent.Exchanger;

class Car extends Thread {
	
	private Exchanger<String> exchanger;
	
	public Car(Exchanger<String> exchanger) {
		this.exchanger = exchanger;
	}
	
	@Override
	public void run() {
		try {
			System.out.println(Thread.currentThread().getName() + "--Car->" + this.exchanger.exchange("Car"));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

class Bike extends Thread {
	
	private Exchanger<String> exchanger;
	
	public Bike(Exchanger<String> exchanger) {
		this.exchanger = exchanger;
	}
	
	@Override
	public void run() {
		try {
			System.out.println(Thread.currentThread().getName() + "--Bike->" + this.exchanger.exchange("Bike"));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

/**
 * Exchanger类源于java.util.concurrent包，它可以在两个线程之间传输数据，
 * Exchanger中的public V exchange(V x)方法被调用后等待另一个线程到达交换点（如果当前线程没有被中断），然后将已知的对象传给它，返回接收的对象。
 * 如果另外一个线程已经在交换点等待，那么恢复线程计划并接收通过当前线程传给的对象：
 * @author yonglu.xie
 * @date 2017/12/01
 *
 */
public class ExchangerTest {

	public static void main(String[] args) {
		Exchanger<String> exchanger = new Exchanger<>();
		
		Thread carThread = new Car(exchanger);
		Thread bikeThread = new Bike(exchanger);
		
		carThread.start();
		bikeThread.start();
	}
	
}
