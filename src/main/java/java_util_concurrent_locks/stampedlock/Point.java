package java_util_concurrent_locks.stampedlock;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock是java8在java.util.concurrent.locks新增的一个API。
 * ReentrantReadWriteLock 在沒有任何读写锁时，才可以取得写入锁，这可用于实现了悲观读取。然而，如果读取很多，写入很少的情况下，使用 ReentrantReadWriteLock 可能会使写入线程遭遇饥饿问题，也就是写入线程无法竞争到锁定而一直处于等待状态。
 * 
 * StampedLock有三种模式的锁，用于控制读取/写入访问。StampedLock的状态由版本和模式组成。锁获取操作返回一个用于展示和访问锁状态的票据（stamp）变量，它用相应的锁状态表示并控制访问，数字0表示没有写锁被授权访问。在读锁上分为悲观锁和乐观锁。
 * 锁释放以及其他相关方法需要使用邮编（stamps）变量作为参数，如果他们和当前锁状态不符则失败，这三种模式为：
 * • 写入：方法writeLock可能为了获取独占访问而阻塞当前线程，返回一个stamp变量，能够在unlockWrite方法中使用从而释放锁。也提供了tryWriteLock。当锁被写模式所占有，没有读或者乐观的读操作能够成功。
 * • 读取：方法readLock可能为了获取非独占访问而阻塞当前线程，返回一个stamp变量，能够在unlockRead方法中用于释放锁。也提供了tryReadLock。
 * • 乐观读取：方法tryOptimisticRead返回一个非0邮编变量，仅在当前锁没有以写入模式被持有。如果在获得stamp变量之后没有被写模式持有，方法validate将返回true。这种模式可以被看做一种弱版本的读锁，可以被一个写入者在任何时间打断。
 *   乐观读取模式仅用于短时间读取操作时经常能够降低竞争和提高吞吐量。
 *   
 * @author yonglu.xie
 *
 */
public class Point {
	// 一个点的x，y坐标
	private double x, y;
	/**
	 * Stamped类似一个时间戳的作用，每次写的时候对其+1来改变被操作对象的Stamped值
	 * 这样其它线程读的时候发现目标对象的Stamped改变，则执行重读
	 */
	private final StampedLock stampedLock = new StampedLock();

	// an exclusively locked method
	void move(double deltaX, double deltaY) {
		/**
		 * stampedLock调用writeLock和unlockWrite时候都会导致stampedLock的stamp值的变化，即每次+1，直到加到最大值，然后从0重新开始
		 */
		long stamp = stampedLock.writeLock(); // 写锁
		try {
			x += deltaX;
			y += deltaY;
		} finally {
			stampedLock.unlockWrite(stamp);// 释放写锁
		}
	}

	double distanceFromOrigin() { // A read-only method
		/**
		 * tryOptimisticRead是一个乐观的读，使用这种锁的读不阻塞写 每次读的时候得到一个当前的stamp值（类似时间戳的作用）
		 */
		long stamp = stampedLock.tryOptimisticRead();

		// 这里就是读操作，读取x和y，因为读取x时，y可能被写了新的值，所以下面需要判断
		double currentX = x, currentY = y;

		/**
		 * 如果读取的时候发生了写，则stampedLock的stamp属性值会变化，此时需要重读，
		 * 再重读的时候需要加读锁（并且重读时使用的应当是悲观的读锁，即阻塞写的读锁）
		 * 当然重读的时候还可以使用tryOptimisticRead，此时需要结合循环了，即类似CAS方式 读锁又重新返回一个stampe值
		 */
		if (!stampedLock.validate(stamp)) {
			stamp = stampedLock.readLock(); // 读锁
			try {
				currentX = x;
				currentY = y;
			} finally {
				stampedLock.unlockRead(stamp);// 释放读锁
			}
		}
		// 读锁验证成功后才执行计算，即读的时候没有发生写
		return Math.sqrt(currentX * currentX + currentY * currentY);
	}
}
