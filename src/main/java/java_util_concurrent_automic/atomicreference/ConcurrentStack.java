package java_util_concurrent_automic.atomicreference;

import java.util.concurrent.atomic.AtomicReference;

/**
 * <h1>无锁同步栈实现</h1>
 * @param <E>
 */
public class ConcurrentStack<E> {
	
	/**
	 * 栈顶元素
	 */
	private AtomicReference<Node<E>> top = new AtomicReference<>();
	
	public void push(E item) {
		Node<E> newHead = new Node<>(item);
		Node<E> oldHead = null;
		do {
			oldHead = top.get();
			newHead.next = oldHead;
		} while(!top.compareAndSet(oldHead, newHead));
	}
	
	public E pop() {
		Node<E> oldHead = null;
		Node<E> newHead = null;
		do {
			oldHead = top.get();
			if(oldHead == null) {
				continue;
			}
			newHead = oldHead.next;
		} while(oldHead == null || !top.compareAndSet(oldHead, newHead));
		
		return oldHead.item;
	}
	
	private static class Node<E> {
		private E item;
		private Node<E> next;
		
		public Node(E item) {
			this.item = item;
		}
	}
	
}
