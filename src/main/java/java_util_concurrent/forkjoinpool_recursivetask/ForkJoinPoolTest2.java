package java_util_concurrent.forkjoinpool_recursivetask;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
/**
 * 
 * @author 李社河-2015年6月19日
 *RecursiveTask代表没有返回值的任务。
 */
class SumTask extends RecursiveTask<Integer> {  
	
	private static final long serialVersionUID = 3017758128645214851L;
	
	// 每个"小任务"最多只打印70个数  
    private static final int MAX = 70;  
    private int arr[];  
    private int start;  
    private int end;  
  
    SumTask(int arr[], int start, int end) {  
        this.arr = arr;  
        this.start = start;  
        this.end = end;  
    }  
  
    @Override  
    protected Integer compute() {  
        int sum = 0;  
        // 当end-start的值小于MAX时候，开始打印  
        if ((end - start) < MAX) {  
            for (int i = start; i < end; i++) {  
                sum += arr[i];  
            }  
            return sum;  
        } else {  
            System.err.println("=====任务分解======");  
            // 将大任务分解成两个小任务  
            int middle = (start + end) / 2;  
            SumTask left = new SumTask(arr, start, middle);  
            SumTask right = new SumTask(arr, middle, end);  
            // 并行执行两个小任务  
            left.fork();  
            right.fork();  
            // 把两个小任务累加的结果合并起来  
            return left.join() + right.join();  
        }  
    }  
  
}  
  
public class ForkJoinPoolTest2 {  
    /** 
     * @param args 
     * @throws Exception 
     */  
    public static void main(String[] args) throws Exception {  
        int arr[] = new int[1000];  
        Random random = new Random();  
        int total = 0;  
        // 初始化100个数字元素  
        for (int i = 0; i < arr.length; i++) {  
            int temp = random.nextInt(100);  
            // 对数组元素赋值,并将数组元素的值添加到total总和中  
            total += (arr[i] = temp);  
        }
        System.out.println("初始化时的总和=" + total);
        // 创建包含Runtime.getRuntime().availableProcessors()返回值作为个数的并行线程的ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        // 提交可分解的PrintTask任务
        Future<Integer> future = forkJoinPool.submit(new SumTask(arr, 0, arr.length));
        System.out.println("计算出来的总和=" + future.get());
        // 关闭线程池
        forkJoinPool.shutdown();
    }  
  
}  
