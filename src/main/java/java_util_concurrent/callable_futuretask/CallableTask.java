package java_util_concurrent.callable_futuretask;

import java.util.concurrent.Callable;

public class CallableTask implements Callable<Integer> {

	@Override
	public Integer call() throws Exception {
		return 120;
	}

}
