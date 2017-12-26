package java_util_concurrent.phaser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * 三、API简述
 * 1、Phaser()：构造函数，创建一个Phaser；默认parties个数为0。此后我们可以通过register()、bulkRegister()方法来注册新的parties。每个Phaser实例内部，都持有几个状态数据：termination状态、已经注册的parties个数（registeredParties）、当前phase下已到达的parties个数（arrivedParties）、当前phase周期数，还有2个同步阻塞队列Queue。Queue中保存了所有的waiter，即因为advance而等待的线程信息；这两个Queue分别为evenQ和oddQ，这两个Queue在实现上没有任何区别，Queue的元素为QNode，每个QNode保存一个waiter的信息，比如Thread引用、阻塞的phase、超时的deadline、是否支持interrupted响应等。两个Queue，其中一个保存当前phase中正在使用的waiter，另一个备用，当phase为奇数时使用evenQ、oddQ备用，偶数时相反，即两个Queue轮换使用。当advance事件触发期间，新register的parties将会被放在备用的Queue中，advance只需要响应另一个Queue中的waiters即可，避免出现混乱。
 * 2、Phaser(int parties)：构造函数，初始一定数量的parties；相当于直接regsiter此数量的parties。
 * 3、arrive()：到达，阻塞，等到当前phase下其他parties到达。如果没有register（即已register数量为0），调用此方法将会抛出异常，此方法返回当前phase周期数，如果Phaser已经终止，则返回负数。
 * 4、arriveAndDeregister()：到达，并注销一个parties数量，非阻塞方法。注销，将会导致Phaser内部的parties个数减一（只影响当前phase），即下一个phase需要等待arrive的parties数量将减一。异常机制和返回值，与arrive方法一致。
 * 5、arriveAndAwaitAdvance()：到达，且阻塞直到其他parties都到达，且advance。此方法等同于awaitAdvance(arrive())。如果你希望阻塞机制支持timeout、interrupted响应，可以使用类似的其他方法（参见下文）。如果你希望到达后且注销，而且阻塞等到当前phase下其他的parties到达，可以使用awaitAdvance(arriveAndDeregister())方法组合。此方法的异常机制和返回值同arrive()。
 * 6、awaitAdvance(int phase)：阻塞方法，等待phase周期数下其他所有的parties都到达。如果指定的phase与Phaser当前的phase不一致，则立即返回。
 * 7、awaitAdvanceInterruptibly(int phase)：阻塞方法，同awaitAdvance，只是支持interrupted响应，即waiter线程如果被外部中断，则此方法立即返回，并抛出InterrutedException。
 * 8、awaitAdvanceInterruptibly(int phase,long timeout,TimeUnit unit)：阻塞方法，同awaitAdvance，支持timeout类型的interrupted响应，即当前线程阻塞等待约定的时长，超时后以TimeoutException异常方式返回。
 * 9、forceTermination()：强制终止，此后Phaser对象将不可用，即register等将不再有效。此方法将会导致Queue中所有的waiter线程被唤醒。
 * 10、register()：新注册一个party，导致Phaser内部registerPaties数量加1；如果此时onAdvance方法正在执行，此方法将会等待它执行完毕后才会返回。此方法返回当前的phase周期数，如果Phaser已经中断，将会返回负数。
 * 11、bulkRegister(int parties)：批量注册多个parties数组，规则同10、。
 * 12、getArrivedParties()：获取已经到达的parties个数。
 * 13、getPhase()：获取当前phase周期数。如果Phaser已经中断，则返回负值。
 * 14、getRegisteredParties()：获取已经注册的parties个数。
 * 15、getUnarrivedParties()：获取尚未到达的parties个数。
 * 16、onAdvance(int phase,int registeredParties)：这个方法比较特殊，表示当进入下一个phase时可以进行的事件处理，如果返回true表示此Phaser应该终止（此后将会把Phaser的状态为termination，即isTermination()将返回true。），否则可以继续进行。phase参数表示当前周期数，registeredParties表示当前已经注册的parties个数。
 * 默认实现为：return registeredParties == 0；在很多情况下，开发者可以通过重写此方法，来实现自定义的advance时间处理机制。
 * 
 * @author yonglu.xie
 *
 */
public class PhaserTest {

	public static void main(String[] args) {
		// 创建时，就需要指定参与的parties个数
		int parties = 12;
		// 可以在创建时不指定parties
		// 而是在运行时，随时注册和注销新的parties
		Phaser phaser = new Phaser();
		// 主线程先注册一个
		// 对应下文中，主线程可以等待所有的parties到达后再解除阻塞（类似与CountDownLatch）
		phaser.register();
		ExecutorService executor = Executors.newFixedThreadPool(parties);
		for (int i = 0; i < parties; i++) {
			phaser.register();// 每创建一个task，我们就注册一个party
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						int i = 0;
						while (i < 3 && !phaser.isTerminated()) {
							System.out.println("Generation:" + phaser.getPhase());
							Thread.sleep(3000);
							// 等待同一周期内，其他Task到达
							// 然后进入新的周期，并继续同步进行
							phaser.arriveAndAwaitAdvance();
							i++; // 我们假定，运行三个周期即可
						}
					} catch (Exception e) {

					} finally {
						phaser.arriveAndDeregister();
					}
				}
			});
		}
		// 主线程到达，且注销自己
		// 此后线程池中的线程即可开始按照周期，同步执行。
		phaser.arriveAndDeregister();
	}

}
