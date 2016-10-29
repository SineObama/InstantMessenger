package computer_network.IM.server;

import computer_network.IM.logging.Logger;
import computer_network.IM.network.Connector;
import computer_network.IM.network.ServerSocket;
import computer_network.IM.network.Socket;

public class IMServer {
	public static void main(String[] args) {
		Logger logger = new Logger("MainThread");
		try { // main丢出异常有意义吗？有就不写这个try了
			if (args.length != 1)
				throw new Exception("请输入端口号作为唯一参数");
			int port = Integer.parseInt(args[0]);
			ServerSocket ss = new ServerSocket(port);
			logger.info("即时聊天服务程序正在监听端口：" + args[0]);
			for (;;) {
				Socket socket = null;
				socket = ss.accept();
				logger.info("主程序收到连接请求：" + socket);
				DispatcherServlet.Dispatch(new Connector(socket));
			}
		} catch (Exception e) {
			logger.unhandledException(e);
		}
	}
}
