package java_util_concurrent.delayed_delayqueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class MessageTest {
	public static void main(String[] args) {
		DelayQueue<Message> queue = new DelayQueue<>();
		new Thread(new Producer(queue)).start();
		new Thread(new Consumer(queue)).start();
	}
}

class Message implements Delayed {
	private String id;
	private String name;
	private long activeTime;// 执行时间

	public Message() {

	}

	public Message(String id, String name, long activeTime) {
		super();
		this.id = id;
		this.name = name;
		this.activeTime = TimeUnit.NANOSECONDS.convert(activeTime, TimeUnit.MILLISECONDS) + System.nanoTime();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Delayed delayed) {
		Message msg = (Message) delayed;
		return Integer.valueOf(this.id) > Integer.valueOf(msg.id) ? 1 : (Integer.valueOf(this.id) < Integer.valueOf(msg.id) ? -1 : 0);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(this.activeTime - System.nanoTime(), TimeUnit.NANOSECONDS);
	}

}

class Producer implements Runnable {
	private DelayQueue<Message> queue;

	public Producer(DelayQueue<Message> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		// 5秒后发送消息
		Message m2 = new Message("2", "Tom", 5000);
		queue.offer(m2);
		System.out.println("消息生产者往消息队列放置消息：" + m2.getId() + ":" + m2.getName());
		
		// 3秒后发送消息
		Message m1 = new Message("1", "Tom", 3000);
		queue.offer(m1);
		System.out.println("消息生产者往消息队列放置消息：" + m1.getId() + ":" + m1.getName());

	}
}

class Consumer implements Runnable {
	private DelayQueue<Message> queue;

	public Consumer(DelayQueue<Message> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Message take = queue.take();
				System.out.println("消息需求者获取消息：" + take.getId() + ":" + take.getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
