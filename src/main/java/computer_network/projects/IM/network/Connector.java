package computer_network.projects.IM.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import computer_network.projects.IM.exception.IMIOException;

public class Connector {
	private Socket socket;

	// 不知道这整个的用法对不对
	private OutputStream o;
	private InputStream i;

	public Connector(Socket socket) throws IOException {
		this.socket = socket;
		i = socket.getInputStream();
		o = socket.getOutputStream();
	}

	public void send(Message m) throws IOException {
		synchronized (o) {
			new ObjectOutputStream(o).writeObject(m);
		}
	}

	public Message receive() throws ClassNotFoundException, IOException, IMIOException {
		Object obj;
		synchronized (i) {
			obj = new ObjectInputStream(i).readObject();
		}
		if (obj instanceof Message)
			return (Message) obj;
		throw new IMIOException("输入流中的对象不能转换为" + Message.class.getName());
	}

	public synchronized boolean isClosed() {
		return socket.isClosed();
	}

	public synchronized void close() throws IOException {
		i.close();
		o.close();
		socket.close();
	}
}
