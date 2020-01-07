package java_util_concurrent.delayed_delayqueue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
public class MLDNTestDemo {
    public static void main(String[] args) throws Exception {
        Cache0<String,News> cache = new Cache0<String,News>() ;
        cache.put("小岳岳",new News("小岳岳","跑路了"),3,TimeUnit.SECONDS) ;
        cache.put("小疯冯",new News("小疯冯","吃屎了"),3,TimeUnit.SECONDS) ;
        System.out.println(cache.get("小岳岳"));
        TimeUnit.SECONDS.sleep(5);
        System.out.println(cache.get("小岳岳"));
        System.out.println(cache.get("小疯冯"));
    }
}
// // 定义缓存的操作类，该类之中需要用户设置保存的key类型与value类型
class Cache0<K,V> {
    // 如果要想实现多个线程的并发访问操作，必须要考虑使用ConcurrentHashMap子类
    private ConcurrentMap<K, V> cacheObjectMap = new ConcurrentHashMap<K, V>();
    private DelayQueue<DelayItem<Pair>> delayQueue = new DelayQueue<DelayItem<Pair>>() ;
    private class Pair {   // 定义一个内部类，该类可以保存队列之中的K与V类型
        private K key ;
        private V value ;
        public Pair(K key,V value) {
            this.key = key ;
            this.value = value ;
        }
    }
    // 如果要想清空不需要的缓冲数据，则需要守护线程
    public Cache0() {
        Runnable daemonTask = () -> {
            // 守护线程要一直进行执行，当已经超时之后可以取出数据
            while(true) {
                // 通过延迟队列获取数据
                DelayItem<Pair> item = Cache0.this.delayQueue.poll() ;
                // 已经有数据超时了
                if (item != null) {
                    Pair pair = item.getItem() ;
                    Cache0.this.cacheObjectMap.remove(pair.key, pair.value) ;
                }
            }
        };
        Thread thread = new Thread(daemonTask,"缓存守护线程") ;
        // 设置守护线程
        thread.setDaemon(true);
        // 启动守护线程
        thread.start();
    }
    /**
     * 表示将要保存的数据写入到缓存之中，如果一个对象重复被保存了，则应该重置它的超时时间
     * @param key 要写入的K的内容
     * @param value 要写入的对象
     * @param time 保存的时间
     * @param unit 保存的时间单位
     */
    public void put(K key, V value, long time, TimeUnit unit) {
        // put()方法如果发现原本的key存在，则会用新的value替换掉旧的内容，同时返回旧的内容
        // 将数据保存进去
        V oldValue = this.cacheObjectMap.put(key, value) ;
        // 原本已经存储过此内容了
        if (oldValue != null) {
            this.delayQueue.remove(key) ;
        }
        this.delayQueue.put(new DelayItem<Pair>(new Pair(key,value), time, unit));
    }
    
    // 根据key获取内容
    public V get(K key) {
        // Map负责查询
        return this.cacheObjectMap.get(key) ;
    }
}
 
class DelayItem<T> implements Delayed {
    private T item ;   // 设置要保存的数据内容
    private long delay ;   // 保存缓存的时间
    private long expire ;  // 设置缓存数据的失效时间
    public DelayItem(T item,long delay,TimeUnit unit) {
        this.item = item ;
        this.delay = TimeUnit.MILLISECONDS.convert(delay, unit) ;
        this.expire = System.currentTimeMillis() + this.delay ;
    }
    @Override
    public int compareTo(Delayed o) {
        return (int) (this.delay - this.getDelay(TimeUnit.MILLISECONDS));
    }
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
    public T getItem() {
        return item;
    }
}
 
class News {   // 定义一个新闻类
    private String title ;
    private String note ;
    public News(String title,String note) {
        this.title = title ;
        this.note = note ;
    }
    public String toString() {
        return "【新闻数据】title = " + this.title + "、note = " + this.note ;
    }
}

