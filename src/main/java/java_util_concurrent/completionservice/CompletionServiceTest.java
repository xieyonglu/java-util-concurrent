package java_util_concurrent.completionservice;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletionServiceTest {
	
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        
        //
        CompletionService<Integer> cs = new ExecutorCompletionService<>(threadPool);
        
        for(int i = 1; i < 5; i++) {
            final int taskID = i;
            cs.submit(new Callable<Integer>() {
                public Integer call() throws Exception {
                    return taskID;
                }
            });
        }
        
        // 可能做一些事情
        for(int i = 1; i < 5; i++) {
        	try {
				System.out.println(cs.take().get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}  
        }
    }
} 
