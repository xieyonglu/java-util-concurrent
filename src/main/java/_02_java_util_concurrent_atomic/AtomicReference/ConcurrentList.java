package _02_java_util_concurrent_atomic.AtomicReference;

import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentList<E> {
	
	/**
	 * 链表结尾元素
	 */
	private AtomicReference<Node<E>> tail = new AtomicReference<>();
	
	public void add(E item) {
		Node<E> newTail = new Node<>(item);
		Node<E> oldTail = null;
		do {
			oldTail = tail.get();
			if(oldTail != null) {
				oldTail.next = newTail;
				newTail.prev = oldTail;
			}
		} while(!tail.compareAndSet(oldTail, newTail));
	}
	
	public E get() {
		Node<E> newTail = null;
		Node<E> oldTail = null;
		
		do {
			oldTail = tail.get();
			if(oldTail == null) {
				continue;
			}
			newTail = oldTail.prev;
			newTail.next = null;
		} while(oldTail != null && !tail.compareAndSet(oldTail, newTail));
		return oldTail.item;
	}
	
	
	private static class Node<E> {
		public E item;
		public Node<E> prev;
		public Node<E> next;
		
		public Node(E item) {
			this.item = item;
		}
	}
	
	public static void main(String[] args) {
		ConcurrentList<String> list = new ConcurrentList<>();
		list.add("A");
		list.add("B");
		list.add("C");
		list.add("D");
		list.add("E");
		
		System.out.println(list.get());
	}
	
}
