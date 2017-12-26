package java_util_concurrent.scheduledexecutorservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Java 定时器ScheduleExecutorService实战
 */
public class ScheduleExecutorServiceTest {

	/**
	 * 获取指定时间对应的毫秒数
	 *
	 * @param time
	 *            "HH:mm:ss"
	 * @return
	 */
	private static long getTimeMillis(String time) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
			Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
			return curDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long
	 * initialDelay, long period, TimeUnit unit); command：执行线程 initialDelay：初始化延时
	 * period：两次开始执行最小间隔时间 unit：计时单位
	 */
	/**
	 * 以固定延迟时间进行执行
	 */
	public static void executeAtFixedRate() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				System.out.println("executeAtFixedRate");
			}
		}, 0, 100, TimeUnit.MILLISECONDS);
	}

	/**
	 * public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long
	 * initialDelay, long delay, TimeUnit unit); command：执行线程 initialDelay：初始化延时
	 * period：前一次执行结束到下一次执行开始的间隔时间（间隔执行延迟时间） unit：计时单位
	 */
	/**
	 * 以固定延迟时间进行执行 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
	 */
	public static void executeWithFixedRate() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				System.out.println("executeWithFixedRate");
			}
		}, 0, 100, TimeUnit.MILLISECONDS);
	}

	/**
	 * scheduleAtFixedRate 每天指定时间 每天定时安排任务进行执行
	 */
	public static void executeAtTimePerDay() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		long oneDay = 24 * 60 * 60 * 1000;
		long initDelay = getTimeMillis("18:27:00") - System.currentTimeMillis();
		initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;

		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				System.out.println("executeEightAtNightPerDay");
			}
		}, initDelay, oneDay, TimeUnit.MILLISECONDS);
	}

	/**
	 * 使用ScheduledExecutorService 延迟定时
	 */
	private static long start;

	public static void executeDelayTime() {

		// 使用工厂方法初始化一个ScheduledThreadPool
		ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(2);

		TimerTask task1 = new TimerTask() {
			@Override
			public void run() {
				try {

					System.out.println("task1 invoked ! " + (System.currentTimeMillis() - start));
					Thread.sleep(3000);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};

		TimerTask task2 = new TimerTask() {
			@Override
			public void run() {
				System.out.println("task2 invoked ! " + (System.currentTimeMillis() - start));
			}
		};
		start = System.currentTimeMillis();
		newScheduledThreadPool.schedule(task1, 1000, TimeUnit.MILLISECONDS);
		newScheduledThreadPool.schedule(task2, 3000, TimeUnit.MILLISECONDS);
	}

	public static void main(String[] args) {
		// executeAtFixedRate();
		// executeWithFixedRate();
		// executeAtTimePerDay();
		executeDelayTime();
	}
}
