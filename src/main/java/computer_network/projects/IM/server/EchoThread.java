package computer_network.projects.IM.server;

import java.util.logging.Logger;

import computer_network.projects.IM.network.Socket;

// 服务器响应线程。响应结束后挂起。
public class EchoThread extends Thread {
	Socket socket;

	public EchoThread setSocket(Socket s) {
		socket = s;
		return this;
	}

	public void run() {
		Logger logger = Logger.getLogger("EchoThread-" + getId());
		while (true) {
			if (socket == null || socket.isClosed()) {
				logger.severe(logger.getName() + "\tsocket无效，线程终止");
				break;
			}
			logger.info(logger.getName() + "\t正在为客户程序提供服务：" + socket);

			// 调用服务。唉怎么好像又把包袱往里丢了
			IMService.serve(socket);

			socket = null;
			try {
				synchronized (this) {
					ThreadPool.push(this);
					logger.info(logger.getName() + "\t挂起");
					wait();
					logger.info(logger.getName() + "\t被唤醒");
				}
			} catch (InterruptedException e) {
				logger.severe("未定义的中断(InterruptedException)。线程将终止");
				break;
			}
		}
	}
}
