package java_util_concurrent.copyonwritearrayset;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * CopyOnWriteArrayList/CopyOnWriteArraySet : “写入并复制”
 * 注意：添加操作多时，效率低，因为每次添加时都会进行复制，开销非常的大。并发迭代操作多时可以选择。
 */
public class CopyOnWriteArraySetTest {

	public static void main(String[] args) {
		HelloThread ht = new HelloThread();

		for (int i = 0; i < 10; i++) {
			new Thread(ht).start();
		}
	}

}

class HelloThread implements Runnable {

	private static Set<String> list = Collections.synchronizedSet(new HashSet<String>());

//	private static CopyOnWriteArraySet<String> list = new CopyOnWriteArraySet<>();

	static {
		list.add("AA");
		list.add("BB");
		list.add("CC");
	}

	@Override
	public void run() {
		Iterator<String> it = list.iterator();

		while (it.hasNext()) {
			System.out.println(it.next());

			list.add("AA");
			// 普通的list会发生ConcurrentModificationException异常
		}

	}

}
