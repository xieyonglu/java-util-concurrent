package java_util_concurrent_automic.atomicreference;

public class AutomicReferenceTest {

	public static void main(String[] args) {
		ConcurrentStack<String> stack = new ConcurrentStack<>();
		
		stack.push("a");
		stack.push("b");
		stack.push("c");
		
		System.out.println(stack.pop());
	}
	
}
