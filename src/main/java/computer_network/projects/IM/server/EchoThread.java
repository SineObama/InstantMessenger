package computer_network.projects.IM.server;

import computer_network.projects.IM.logging.Logger;
import computer_network.projects.IM.network.Socket;
import computer_network.projects.IM.server.util.ThreadPool;

// 服务器响应线程。响应结束后挂起。
public class EchoThread extends Thread {
	Socket socket;

	public EchoThread setSocket(Socket s) {
		socket = s;
		return this;
	}

	public void run() {
		Logger logger =new Logger("Thread-" + getId());
		while (true) {
			if (socket == null || socket.isClosed()) {
				logger.error("socket无效，线程终止");
				break;
			}
			logger.info("正在为客户程序提供服务：" + socket);

			// 调用服务。唉怎么好像又把包袱往里丢了
			IMService.serve(socket);

			socket = null;
			try {
				synchronized (this) {
					ThreadPool.push(this);
					logger.info("挂起");
					wait();
					logger.info( "被唤醒");
				}
			} catch (InterruptedException e) {
				logger.unhandledException(e);
				break;
			}
		}
	}
}
