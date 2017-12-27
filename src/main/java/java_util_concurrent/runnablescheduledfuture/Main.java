package java_util_concurrent.runnablescheduledfuture;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Main class of the example. Creates a MyScheduledThreadPoolExecutor and
 * executes a delayed task and a periodic task in it.
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		/*
		 * Create a MyScheduledThreadPool object
		 */
		MyScheduledThreadPoolExecutor executor = new MyScheduledThreadPoolExecutor(2);

		/*
		 * Create a task object
		 */
		Task task = new Task();

		/*
		 * Write the start date of the execution
		 */
		System.out.printf("Main: %s\n", new Date());

		/*
		 * Send to the executor a delayed task. It will be executed after 1 second of delay
		 */
		executor.schedule(task, 1, TimeUnit.SECONDS);

		/*
		 * Sleeps the thread three seconds
		 */
		TimeUnit.SECONDS.sleep(3);

		/*
		 * Create another task
		 */
		task = new Task();

		/*
		 * Write the actual date again
		 */
		System.out.printf("Main: %s\n", new Date());

		/*
		 * Send to the executor a delayed task. It will begin its execution after 1
		 * second of dealy and then it will be executed every three seconds
		 */
		executor.scheduleAtFixedRate(task, 1, 3, TimeUnit.SECONDS);

		/*
		 * Sleep the thread during ten seconds
		 */
		TimeUnit.SECONDS.sleep(10);

		/*
		 * Shutdown the executor
		 */
		executor.shutdown();

		/*
		 * Wait for the finalization of the executor
		 */
		executor.awaitTermination(1, TimeUnit.DAYS);

		/*
		 * Write a message indicating the end of the program
		 */
		System.out.printf("Main: End of the program.\n");
	}

}

/**

计划的线程池是 Executor 框架的基本线程池的扩展，允许你定制一个计划来执行一段时间后需要被执行的任务。 它通过 ScheduledThreadPoolExecutor 类来实现，并允许运行以下这两种任务：

Delayed 任务：这种任务在一段时间后仅执行一次。
Periodic 任务：这种任务在延迟后执行，然后通常周期性运行
Delayed 任务可以执行 Callable 和 Runnable 对象，但是 periodic任务只能执行 Runnable 对象。全部任务通过计划池执行的都必须实现 RunnableScheduledFuture 接口。在这个指南，你将学习如何实现你自己的 RunnableScheduledFuture 接口来执行延迟和周期性任务。
结果

有了 Task 类例子总是完成了，它实现 Runnable 接口，也是在计划的执行者中运行的任务。这个例子的主类创建了 MyScheduledThreadPoolExecutor 执行者，然后给他们发送了以下2个任务：

一个延迟任务，在当前时间过一秒后运行 
一个周期任务，在当前时间过一秒后运行，接着每隔3秒运行 
以下结果展示了这个例子的运行的一部分。你可以检查2种任务运行正常
*/
