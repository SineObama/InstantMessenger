package computer_network.IM.server.util;

import java.util.ArrayList;
import java.util.List;

// 单例线程池，保存了闲置线程。
public final class ThreadPool {
	private ThreadPool() {
	}

	private static List<Thread> list = new ArrayList<Thread>();

	// 压入空闲线程
	public static synchronized void push(Thread t) {
		list.add(t);
	}

	// 取出线程
	public static synchronized Thread pop() {
		if (list.isEmpty())
			return null;
		return list.remove(0);
	}
}
