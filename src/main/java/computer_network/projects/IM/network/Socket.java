package computer_network.projects.IM.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;

// 未真正实现的底层
public class Socket {
	private java.net.Socket socket;

	public Socket(String addr, int port) throws UnknownHostException, IOException {
		socket = new java.net.Socket(addr, port);
	}

	// 用于接受ServerSocket
	public Socket(java.net.Socket s) {
		socket = s;
	}

	public boolean isClosed() {
		return socket.isClosed();
	}

	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}

	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}

	public void close() throws IOException {
		socket.close();
	}

	public String toString() {
		return socket.toString();
	}
}
