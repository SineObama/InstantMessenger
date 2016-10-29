package computer_network.IM.server;

import computer_network.IM.network.Connector;
import computer_network.IM.server.util.ThreadPool;

public class DispatcherServlet {
	private DispatcherServlet() {
	}

	public static void Dispatch(Connector connector) throws Exception {
		Thread t = ThreadPool.pop();
		if (t == null)
			new EchoThread().setConnector(connector).start();
		else if (t instanceof EchoThread) {
			EchoThread et = (EchoThread) t;
			synchronized (et) {
				et.setConnector(connector).notify();
			}
		} else {
			throw new Exception("线程池中对象无法转换");
		}
	}
}
