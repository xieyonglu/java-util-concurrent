package java_util_concurrent.delayed_delayqueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class MLDNTestDemo2 {
    public static void main(String[] args) throws Exception {
        System.out.println("准备聚会。。。。。。");
        // 设置延迟队列
        DelayQueue<Member> queue = new DelayQueue<>() ;
        queue.add(new Member("张三", 3, TimeUnit.SECONDS));
        queue.add(new Member("李四", 5, TimeUnit.SECONDS));
        // 如果聚会还有人在呢
        while(!queue.isEmpty()) {
            // 从里面取出数据内容
            Delayed dyd = queue.poll() ;
            // 如果通过队列里面可以获取数据，就表示当前的用户已经离开了，满足了延迟的条件了
            System.out.println("【poll = ｛" + dyd + "｝】" + System.currentTimeMillis());
            // 延迟500毫秒
            TimeUnit.MILLISECONDS.sleep(500);
        }
    }
}

// 如果成员要想离开一定要实现Delayed接口
class Member implements Delayed {
	
    // 聚会人员的名单
    private String name;
    
    // 失效时间，人员离开的时间，毫秒单位
    private long expire;
    
    // 设置的延迟时间，毫秒单位
    private long delay;
    
    /**
     * 设置参与到队列之中的用户信息
     * @param name 用户的姓名
     * @param delay 延迟时间
     * @param unit 时间处理单位
     */
    public Member(String name,long delay,TimeUnit unit) {
        this.name = name ;
        // 保存延迟的时间
        this.delay = TimeUnit.MILLISECONDS.convert(delay, unit) ;
        // 当前时间加上延迟时间
        this.expire = System.currentTimeMillis() + this.delay ;
    }
    
    @Override  // 决定了你优先级队列的弹出操作
    public int compareTo(Delayed o) {
        return (int) (this.delay - this.getDelay(TimeUnit.MILLISECONDS));
    }
    
    @Override    // 计算延迟时间是否到达
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
    
    @Override
    public String toString() {
        return this.name + "预计" + this.delay + "离开，现在已经到点了。";
    }
}

