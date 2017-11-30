package java_util_concurrent.callable_futuretask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class CallableAndFutureTaskTest02 {

	public static void main(String[] args) {
		FutureTask<Integer> futureTask = new FutureTask<>(new CallableTask());
		
		//
		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.submit(futureTask); // submit -> Runnable
		executorService.shutdown();
		try {
			System.out.println("Result is --> " + futureTask.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		//
		ExecutorService executorService02 = Executors.newCachedThreadPool();
		Future<Integer> future = executorService02.submit(new CallableTask()); // submit -> Callable
		executorService02.shutdown();
		
		try {
			System.out.println("Result is --> " + future.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
}

