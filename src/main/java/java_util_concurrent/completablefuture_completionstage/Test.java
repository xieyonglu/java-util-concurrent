package java_util_concurrent.completablefuture_completionstage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * CompletableFuture类实现了CompletionStage和Future接口。
 * Future是Java 5添加的类，用来描述一个异步计算的结果，但是获取一个结果时方法较少,要么通过轮询isDone，确认完成后，调用get()获取值，要么调用get()设置一个超时时间。
 * 但是这个get()方法会阻塞住调用线程，这种阻塞的方式显然和我们的异步编程的初衷相违背。
 * 为了解决这个问题，JDK吸收了guava的设计思想，加入了Future的诸多扩展功能形成了CompletableFuture。
 * 
 * CompletionStage是一个接口，从命名上看得知是一个完成的阶段，它里面的方法也标明是在某个运行阶段得到了结果之后要做的事情。
 * 
 * 以Async结尾的方法都是可以异步执行的，如果指定了线程池，会在指定的线程池中执行，如果没有指定，默认会在ForkJoinPool.commonPool()中执行
 * 
 * @author yonglu.xie
 *
 */
@SuppressWarnings("unused")
public class Test {

	public static void main(String[] args) {
		thenApply();

		thenAccept();

		thenRun();
		
		thenCombine();
		
		thenAcceptBoth();
		
		runAfterBoth();
		
		applyToEither();
		
		acceptEither();
		
		runAfterEither();
		
		exceptionally();
		
		whenComplete();
		
		handle();
		
		handle02();
	}

	/**
	 * 它的入参是上一个阶段计算后的结果，返回值是经过转化后结果。
	 */
	public static void thenApply() {
		String result = CompletableFuture.supplyAsync(() -> "hello").thenApply(s -> s + " world").join();
		System.out.println(result);
	}

	/**
	 * thenAccept是针对结果进行消耗，因为他的入参是Consumer，有入参无返回值。
	 */
	public static void thenAccept() {
		CompletableFuture.supplyAsync(() -> "hello").thenAccept(s -> System.out.println(s + " world"));
	}

	/**
	 * 对上一步的计算结果不关心，执行下一个操作。
	 * thenRun它的入参是一个Runnable的实例，表示当得到上一步的结果时的操作。
	 */
	public static void thenRun() {
		CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "hello";
		}).thenRun(() -> System.out.println("hello world"));
		while (true) {
		}
		// new CountDownLatch(1).await();
	}

	/**
	 * 结合两个CompletionStage的结果，进行转化后返回
	 * 它需要原来的处理返回值，并且other代表的CompletionStage也要返回值之后，利用这两个返回值，进行转换后返回指定类型的值。
	 */
	public static void thenCombine() {
		String result = CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "hello";
		}).thenCombine(CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "world";
		}), (s1, s2) -> s1 + " " + s2).join();
		System.out.println(result);
	}

	/**
	 * 结合两个CompletionStage的结果，进行消耗
	 * 它需要原来的处理返回值，并且other代表的CompletionStage也要返回值之后，利用这两个返回值，进行消耗。
	 */
	public static void thenAcceptBoth() {
		CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "hello";
		}).thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "world";
		}), (s1, s2) -> System.out.println(s1 + " " + s2));
		while (true) {
		}
	}

	/**
	 * 在两个CompletionStage都运行完执行。
	 * 不关心这两个CompletionStage的结果，只关心这两个CompletionStage执行完毕，之后在进行操作（Runnable）。
	 */
	public static void runAfterBoth() {
		CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "s1";
		}).runAfterBothAsync(CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "s2";
		}), () -> System.out.println("hello world"));
		while (true) {
		}
	}

	/**
	 * 两个CompletionStage，谁计算的快，我就用那个CompletionStage的结果进行下一步的转化操作。
	 * 我们现实开发场景中，总会碰到有两种渠道完成同一个事情，所以就可以调用这个方法，找一个最快的结果进行处理。
	 */
	public static void applyToEither() {
		String result = CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "s1";
		}).applyToEither(CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "hello world";
		}), s -> s).join();
		System.out.println(result);
	}

	/**
	 * 两个CompletionStage，谁计算的快，我就用那个CompletionStage的结果进行下一步的消耗操作。
	 */
	public static void acceptEither() {
		CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "s1";
		}).acceptEither(CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "hello world";
		}), System.out::println);
		while (true) {
		}
	}

	/**
	 * 两个CompletionStage，任何一个完成了都会执行下一步的操作（Runnable）。
	 */
	public static void runAfterEither() {
		CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "s1";
		}).runAfterEither(CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "s2";
		}), () -> System.out.println("hello world"));
		while (true) {
		}
	}

	/**
	 * 当运行时出现了异常，可以通过exceptionally进行补偿。
	 */
	public static void exceptionally() {
		String result = CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			boolean b = 1 == 1;
			if (b) {
				throw new RuntimeException("测试一下异常情况");
			}
			return "s1";
		}).exceptionally(e -> {
			System.out.println(e.getMessage());
			return "hello world";
		}).join();
		System.out.println(result);
	}

	/**
	 * 当运行完成时，对结果的记录。这里的完成时有两种情况，一种是正常执行，返回值。另外一种是遇到异常抛出造成程序的中断。这里为什么要说成记录，因为这几个方法都会返回CompletableFuture，当Action执行完毕后它的结果返回原始的CompletableFuture的计算结果或者返回异常。所以不会对结果产生任何的作用。
	 * 
	 * 结果为：
	 * null
	 * java.lang.RuntimeException: 测试一下异常情况
	 * java.lang.RuntimeException: 测试一下异常情况
	 * hello world
	 * 
	 * 这里也可以看出，如果使用了exceptionally，就会对最终的结果产生影响，它没有口子返回如果没有异常时的正确的值，这也就引出下面我们要介绍的handle。
	 */
	public static void whenComplete() {
		String result = CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (1 == 1) {
				throw new RuntimeException("测试一下异常情况");
			}
			return "s1";
		}).whenComplete((s, t) -> {
			System.out.println(s);
			System.out.println(t.getMessage());
		}).exceptionally(e -> {
			System.out.println(e.getMessage());
			return "hello world";
		}).join();
		System.out.println(result);
	}

	/**
	 * 运行完成时，对结果的处理。这里的完成时有两种情况，一种是正常执行，返回值。另外一种是遇到异常抛出造成程序的中断。
	 * 出现异常时
	 */
	public static void handle() {
		String result = CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 出现异常
			if (1 == 1) {
				throw new RuntimeException("测试一下异常情况");
			}
			return "s1";
		}).handle((s, t) -> {
			if (t != null) {
				return "hello world";
			}
			return s;
		}).join();
		System.out.println(result);
	}

	/**
	 * 未出现异常时
	 */
	public static void handle02() {
		String result = CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "s1";
		}).handle((s, t) -> {
			if (t != null) {
				return "hello world";
			}
			return s;
		}).join();
		System.out.println(result);
	}

}
