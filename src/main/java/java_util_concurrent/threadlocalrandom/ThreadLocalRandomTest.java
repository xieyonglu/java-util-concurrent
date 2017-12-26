package java_util_concurrent.threadlocalrandom;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadLocalRandomTest {
	public static void main(String[] args) throws Exception {
		Random ran = new Random();
		System.out.println(ran.nextInt());// 生成一个int类型取值范围的随机数
		System.out.println(ran.nextInt(100));// 生成一个0-100的int类型的随机数
		System.out.println(ran.nextFloat());// 生成一个Float类型的随机数
		System.out.println(ran.nextDouble());// 生成一个Double类型的随机数
		/*
		 * 结果1： -405998184 81 0.7216265 0.8820657901017879
		 */
		Random r2 = new Random(50);// 种子为50的对象
		Random r3 = new Random(50);// 种子为50的对象
		System.out.println("r2.nextInt():" + r2.nextInt() + "-------r3.nextInt():" + r3.nextInt());// 如果两个Random对象种子数相同，那么他们生成的结果将是一样。可以使用当前时间最为种子：System.currentTimeMillis()
		System.out.println("---------------多线程环境下使用ThreadLocalRandom类，用法跟Random类基本类似---------------------");
		ThreadLocalRandom tlr = ThreadLocalRandom.current();
		System.out.println(tlr.nextInt(10, 50));// 生成一个10~50之间的随机数
	}
}
