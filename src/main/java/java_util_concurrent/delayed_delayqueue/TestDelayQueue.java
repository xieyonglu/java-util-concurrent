package java_util_concurrent.delayed_delayqueue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 第一周作业：模拟一个使用DelayQueue的场景
 * 这里模拟的是订单下达之后，如果一直都还没支付，也就是停留在创建状态的话，就将其改成取消状态。
 *
 * @author Owen
 * @date 2018/8/27 21:20
 */
public class TestDelayQueue {
	
    //是否开启自动取消功能
    int isStarted = 1;
    
    //延迟队列，用来存放订单对象
    DelayQueue<Order> queue = new DelayQueue<>();

    public static void main(String[] args) {
        TestDelayQueue testDelayQueue = new TestDelayQueue();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        //新建一个线程，用来模拟定时取消订单job
        Thread t1 = new Thread(() -> {
            System.out.println("开启自动取消订单job,当前时间：" + LocalDateTime.now().format(formatter));
            while (testDelayQueue.isStarted == 1) {
                try {
                    Order order = testDelayQueue.queue.take();
                    order.setStatus("CANCELED");

                    System.out.println("订单：" + order.getOrderNo() + "付款超时，自动取消，当前时间：" + LocalDateTime.now().format(formatter));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();

        //新建一个线程，模拟提交订单
        Thread t2 = new Thread(() -> {
            //定义最早的订单的创建时间
            long beginTime = System.currentTimeMillis();
            //下面模拟6个订单，每个订单的创建时间依次延后3秒
            testDelayQueue.queue.add(new Order("SO001", "A", 100, "CREATED", new Date(beginTime), new Date(beginTime + 3000)));
            beginTime += 3000L;
            testDelayQueue.queue.add(new Order("SO002", "B", 100, "CREATED", new Date(beginTime), new Date(beginTime + 3000)));
            beginTime += 3000L;
            testDelayQueue.queue.add(new Order("SO003", "C", 100, "CREATED", new Date(beginTime), new Date(beginTime + 3000)));
            beginTime += 3000L;
            testDelayQueue.queue.add(new Order("SO004", "D", 100, "CREATED", new Date(beginTime), new Date(beginTime + 3000)));
            beginTime += 3000L;
            testDelayQueue.queue.add(new Order("SO005", "E", 100, "CREATED", new Date(beginTime), new Date(beginTime + 3000)));
            beginTime += 3000L;
            testDelayQueue.queue.add(new Order("SO006", "F", 100, "CREATED", new Date(beginTime), new Date(beginTime + 3000)));
        });
        t2.start();
    }

}

/**
 * 订单类，用于存放订单头信息
 *
 * @author Owen
 * @date 2018/9/3 14:10
 */
class Order implements Delayed {
    String orderNo;
    String receiveName;
    int cost;
    String status;
    Date createTime;
    Date cancelTime;

    public Order(String orderNo, String receiveName, int cost, String status, Date createTime, Date cancelTime) {
        this.orderNo = orderNo;
        this.receiveName = receiveName;
        this.cost = cost;
        this.status = status;
        this.createTime = createTime;
        this.cancelTime = cancelTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        //下面用到unit.convert()方法，其实在这个小场景不需要用到，只是学习一下如何使用罢了
        long l = unit.convert(cancelTime.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        return l;
    }

    @Override
    public int compareTo(Delayed o) {
        //这里根据取消时间来比较，如果取消时间小的，就会优先被队列提取出来
        return this.getCancelTime().compareTo(((Order) o).getCancelTime());
    }
}
