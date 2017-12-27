package java_util_concurrent_automic.longadder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 大家对AtomicInteger的基本实现机制应该比较了解,它们是在一个死循环内,不断尝试修改目标值,知道修改成功,如果竞争不激烈,那么修改成功的概率就很高,否则,修改失败的概率就很高,在大量修改失败时,这些原子操作就会进行多次循环尝试,因此性能就会受到影响
 * 那么竞争激烈的时候,我们应该如何进一步提高系统性能呢?一种基本方案就是可以使用热点分离,将竞争的数据进行分解.
 * 基于这个思路,打击应该可以想到一种对传统AtomicInteger等原子类的改进方法,虽然在CAS操作中没有锁,但是像减少锁粒度这种分离热点的思路依然可以使用,一种可行的方案就是仿造ConcurrengHashMap,将热点数据分离,
 * 比如,可以将AtomicInteger的内部核心数据value分离成一个数组,每个线程访问时,通过哈希等算法映射到其中一个数字进行计数,而最终的计数结果,则为这个数组的求和累加,
 * 其中,热点数据value被分离成多个单元cell,每个cell独自维护内部的值,当前对象的实际值由所有的cell累计合成,这样,热点就进行了有效的分离,提高了并行度,LongAdder正是使用了这种思想.
 * 
 * 测试LongAdder,原子类以及同步锁性能测试
 * @author yonglu.xie
 *
 */
public class LongAdderDemo {
	
    private static final int MAX_THREADS = 3;
    private static final int TASK_COUNT = 3;
    private static final int TARGET_COUNT = 10000000;

    private AtomicLong acount = new AtomicLong(0L);
    private LongAdder lacount = new LongAdder();
    private long count = 0;

    private static CountDownLatch cdlsync = new CountDownLatch(TASK_COUNT);
    private static CountDownLatch cdlatomic = new CountDownLatch(TASK_COUNT);
    private static CountDownLatch cdladdr = new CountDownLatch(TASK_COUNT);

    protected synchronized long inc() {
        return ++count;
    }

    protected synchronized long getCount() {
        return count;
    }

    public class SyncThread implements Runnable {
        protected String name;
        protected long starttime;
        LongAdderDemo out;

        public SyncThread(long starttime, LongAdderDemo out) {
            this.starttime = starttime;
            this.out = out;
        }

        @Override
        public void run() {
            long v = out.getCount();
            while (v < TARGET_COUNT) {
                v = out.inc();
            }
            long endtime = System.currentTimeMillis();
            System.out.println("SyncThread spend:" + (endtime - starttime) + "ms" + " v" + v);
            cdlsync.countDown();
        }
    }

    public void testSync() throws InterruptedException {
        ExecutorService exe = Executors.newFixedThreadPool(MAX_THREADS);
        long starttime = System.currentTimeMillis();
        SyncThread sync = new SyncThread(starttime, this);
        for (int i = 0; i < TASK_COUNT; i++) {
            exe.submit(sync);
        }
        cdlsync.await();
        exe.shutdown();
    }

    public class AtomicThread implements Runnable {
        protected String name;
        protected long starttime;

        public AtomicThread(long starttime) {
            this.starttime = starttime;
        }

        @Override
        public void run() {
            long v = acount.get();
            while (v < TARGET_COUNT) {
                v = acount.incrementAndGet();
            }
            long endtime = System.currentTimeMillis();
            System.out.println("AtomicThread spend:" + (endtime - starttime) + "ms" + " v" + v);
            cdlatomic.countDown();
        }
    }

    public void testAtomic() throws InterruptedException {
        ExecutorService exe = Executors.newFixedThreadPool(MAX_THREADS);
        long starttime = System.currentTimeMillis();
        AtomicThread atomic = new AtomicThread(starttime);
        for (int i = 0; i < TASK_COUNT; i++) {
            exe.submit(atomic);
        }
        cdlatomic.await();
        exe.shutdown();
    }

    public class LongAdderThread implements Runnable {
        protected String name;
        protected long starttime;

        public LongAdderThread(long starttime) {
            this.starttime = starttime;
        }

        @Override
        public void run() {
            long v = lacount.sum();
            while (v < TARGET_COUNT) {
                lacount.increment();
                v = lacount.sum();
            }
            long endtime = System.currentTimeMillis();
            System.out.println("LongAdderThread spend:" + (endtime - starttime) + "ms" + " v" + v);
            cdladdr.countDown();
        }

    }

    public void testLongAdder() throws InterruptedException {
        ExecutorService exe = Executors.newFixedThreadPool(MAX_THREADS);
        long starttime = System.currentTimeMillis();
        LongAdderThread atomic = new LongAdderThread(starttime);
        for (int i = 0; i < TASK_COUNT; i++) {
            exe.submit(atomic);
        }
        cdladdr.await();
        exe.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        LongAdderDemo demo = new LongAdderDemo();
        demo.testSync();
        demo.testAtomic();
        demo.testLongAdder();
    }
}
