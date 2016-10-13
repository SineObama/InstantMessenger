package computer_network.projects.IM.server;

import java.io.IOException;

import computer_network.projects.IM.network.ServerSocket;
import computer_network.projects.IM.network.Socket;

public class IMServer {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		if (args.length != 1) {
			System.out.println("请输入端口号作为唯一参数");
			return;
		}
		ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));
		System.out.println("即时聊天服务程序正在监听端口：" + args[0]);

		for (;;) {
			System.out.println("主程序正在等待连接");
			Socket socket = ss.accept();
			System.out.println("主程序收到连接请求：" + socket);
			Thread t = ThreadPool.pop();
			if (t == null)
				new EchoThread().setSocket(socket).start();
			else if (t instanceof EchoThread) {
				EchoThread et = (EchoThread) t;
				synchronized (et) {
					et.setSocket(socket).notify();
				}
			} else {
				// impossible exception
			}
			System.out.println("主程序已启动线程");
		}
	}
}
