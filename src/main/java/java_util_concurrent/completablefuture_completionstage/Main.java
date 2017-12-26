package java_util_concurrent.completablefuture_completionstage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CompletableFuture类实现了CompletionStage和Future接口。
 * Future是Java 5添加的类，用来描述一个异步计算的结果，但是获取一个结果时方法较少,要么通过轮询isDone，确认完成后，调用get()获取值，要么调用get()设置一个超时时间。
 * 但是这个get()方法会阻塞住调用线程，这种阻塞的方式显然和我们的异步编程的初衷相违背。
 * 为了解决这个问题，JDK吸收了guava的设计思想，加入了Future的诸多扩展功能形成了CompletableFuture。
 * CompletionStage是一个接口，从命名上看得知是一个完成的阶段，它里面的方法也标明是在某个运行阶段得到了结果之后要做的事情。
 * 
 * @author yonglu.xie
 *
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("main thread: " + Thread.currentThread());

		new Thread(Main::test1) {
			{
				setName("my-new-thread");
			}
		}.start();

		test2();
	}

	private static void test1() {
		CompletionStage<Void> futurePrice = CompletableFuture.runAsync(() -> {
			sleep(1000);
			System.out.println("test1:1 - runAsync(runnable), job thread: " + Thread.currentThread());
		});

		System.out.println("test1:flag1");

		futurePrice.thenRun(() -> {
			System.out.println("test1:2 - thenRun(runnable)), action thread: " + Thread.currentThread());
		});

		System.out.println("test1:flag2");

		futurePrice.thenRunAsync(() -> {
			System.out.println("test1:3 - thenRunAsync(runnable), action thread: " + Thread.currentThread());
		});

	}

	private static void test2() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		CompletionStage<Void> futurePrice = CompletableFuture.runAsync(() -> {
			sleep(1000);
			System.out.println("test2:1 - runAsync(runnable, executor), job thread: " + Thread.currentThread());
		}, executorService);

		System.out.println("test2:flag1");

		futurePrice.thenRunAsync(() -> {
			System.out.println("test2:2 - thenRunAsync(runnable), action thread: " + Thread.currentThread());
		});

		System.out.println("test2:flag2");

		futurePrice.thenRun(() -> {
			System.out.println("test2:3 - thenRun(runnable), action thread: " + Thread.currentThread());
		});

		futurePrice.thenRunAsync(() -> {
			System.out.println("test2:4 - thenRunAsync(runnable, executor), action thread: " + Thread.currentThread());
		}, executorService);

		executorService.shutdown();
	}

	private static void sleep(long time) {
		// if(true) return;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
}
