package java_util_concurrent.threadpoolexecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 整个流程：
 * 1、如果线程池的当前大小还没有达到基本大小(poolSize < corePoolSize)，那么就新增加一个线程处理新提交的任务；
 * 2、如果当前大小已经达到了基本大小，就将新提交的任务提交到阻塞队列排队，等候处理workQueue.offer(command)；
 * 3、如果队列容量已达上限，并且当前大小poolSize没有达到maximumPoolSize，那么就新增线程来处理任务；
 * 4、如果队列已满，并且当前线程数目也已经达到上限，那么意味着线程池的处理能力已经达到了极限，此时需要拒绝新增加的任务。至于如何拒绝处理新增的任务，取决于线程池的饱和策略RejectedExecutionHandler。
 * @author yonglu.xie
 * @date 2017/03/11
 *
 */
public class ThreadPoolExecutorTest {

	public static void main(String[] args) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));
		
		System.out.println(
				"==>线程池中线程数目：" + executor.getPoolSize() +
				",队列中等待执行的任务数目：" + executor.getQueue().size() +
				",已执行完的任务数目：" + executor.getCompletedTaskCount());
		
		for(int i=0; i<150000; i++) {
			MyTask myTask = new MyTask(i);
			executor.execute(myTask);
			System.out.println(
					"线程池中线程数目：" + executor.getPoolSize() +
					",队列中等待执行的任务数目：" + executor.getQueue().size() +
					",已执行完别的任务数目：" + executor.getCompletedTaskCount());
		}
		
		executor.shutdown();
	}

}

class MyTask implements Runnable {
    private int taskNum;

    public MyTask(int num) {
        this.taskNum = num;
    }

    @Override
    public void run() {
        System.out.println("正在执行task "+taskNum);
        try {
            Thread.currentThread().sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task "+taskNum+"执行完毕");
    }
}
