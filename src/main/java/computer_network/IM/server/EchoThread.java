package computer_network.IM.server;

import computer_network.IM.logging.Logger;
import computer_network.IM.network.Connector;
import computer_network.IM.server.services.IMService;
import computer_network.IM.server.util.ThreadPool;

// 服务器响应线程。响应结束后挂起。
public class EchoThread extends Thread {
	Connector connector;

	public EchoThread setConnector(Connector connector) {
		this.connector = connector;
		return this;
	}

	public void run() {
		Logger logger = new Logger("Thread-" + getId());
		while (true) {
			if (connector == null || connector.isClosed()) {
				logger.error(Connector.class.getName() + "无效，线程终止");
				break;
			}
			logger.info("正在为客户程序提供服务：" + connector);

			// 调用服务。唉怎么好像又把包袱往里丢了
			IMService.serve(connector, new Logger("Thread-" + getId()).setPrefix(connector.toString()));

			connector = null;
			try {
				synchronized (this) {
					ThreadPool.push(this);
					logger.info("挂起");
					wait();
					logger.info("被唤醒");
				}
			} catch (InterruptedException e) {
				logger.unhandledException(e);
				break;
			}
		}
	}
}
