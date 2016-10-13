package computer_network.projects.IM.server;

import computer_network.projects.IM.network.Socket;

// 服务器响应线程。响应结束后挂起。
public class EchoThread extends Thread {
	Socket socket;

	public EchoThread setSocket(Socket s) {
		socket = s;
		return this;
	}

	public void run() {
		while (true) {
			if (socket == null || socket.isClosed()) {
				System.out.println("线程" + getId() + "\tsocket无效，线程终止");
				break;
			}
			System.out.println("线程" + getId() + "\t正在为客户程序提供服务：" + socket);
			
			// 调用服务
			IMService.serve(socket);
			
			socket = null;
			try {
				synchronized (this) {
					ThreadPool.push(this);
					System.out.println("线程" + getId() + "\t挂起");
					wait();
					System.out.println("线程" + getId() + "\t被唤醒");
				}
			} catch (InterruptedException e) {
			}
		}
	}
}
