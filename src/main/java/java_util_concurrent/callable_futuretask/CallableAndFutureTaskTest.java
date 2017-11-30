package java_util_concurrent.callable_futuretask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableAndFutureTaskTest {

	public static void main(String[] args) {
		//
		Callable<Integer> callable = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return 120;
			}
		};
		
		// 继承 -> RunnableFuture -> Runnable & Future
		FutureTask<Integer> future = new FutureTask<Integer>(callable);
		
		//
		new Thread(future).start();
		
		try {
			System.out.println("Result is --> " + future.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
}
