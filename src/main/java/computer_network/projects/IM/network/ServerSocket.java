package computer_network.projects.IM.network;

import java.io.IOException;

//未真正实现的底层
public class ServerSocket {
	private java.net.ServerSocket serverSocket;

	public ServerSocket(int port) throws IOException {
		serverSocket = new java.net.ServerSocket(port);
	}

	public Socket accept() throws IOException {
		return new Socket(serverSocket.accept());
	}
}
