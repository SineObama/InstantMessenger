package computer_network.projects.IM.server;

import java.security.InvalidParameterException;
import java.util.logging.Logger;

import computer_network.projects.IM.network.ServerSocket;
import computer_network.projects.IM.network.Socket;

public class IMServer {
	public static void main(String[] args) {
		try { // main丢出异常有意义吗？有就不写这个try了
			if (args.length != 1)
				throw new InvalidParameterException("请输入端口号作为唯一参数");
			int port = Integer.parseInt(args[0]);
			Logger logger = Logger.getGlobal();
			ServerSocket ss = new ServerSocket(port);
			logger.info("即时聊天服务程序正在监听端口：" + args[0]);
			for (;;) {
				logger.fine("主程序正在等待连接");
				Socket socket = null;
				socket = ss.accept();
				logger.info("主程序收到连接请求：" + socket);
				Thread t = ThreadPool.pop();
				if (t == null)
					new EchoThread().setSocket(socket).start();
				else if (t instanceof EchoThread) {
					EchoThread et = (EchoThread) t;
					synchronized (et) {
						et.setSocket(socket).notify();
					}
				} else {
					throw new Exception("线程池中对象无法转换");
				}
				logger.fine("主程序已启动线程");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
