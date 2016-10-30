package computer_network.IM.server;

import computer_network.IM.logging.Logger;
import computer_network.IM.network.Connector;
import computer_network.IM.network.IMConstant;
import computer_network.IM.network.ServerSocket;
import computer_network.IM.network.Socket;

public class IMServer {
	public static void main(String[] args) {
		Logger logger = new Logger("MainThread");
		try { // main丢出异常有意义吗？有就不写这个try了
			// 解析参数
			int port;
			if (args.length == 0)
				port = IMConstant.defaultPort;
			else if (args.length == 1)
				port = Integer.parseInt(args[0]);
			else {
				System.out.println("请输入一个参数：<port>");
				return;
			}
			
			// 开始监听
			ServerSocket ss = new ServerSocket(port);
			logger.info("即时聊天服务程序正在监听端口：" + port);
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
