package computer_network.projects.IM.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Connector {
	private Socket socket;
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

	public Message receive() throws ClassNotFoundException, IOException {
		Object obj;
		synchronized (i) {
			obj = new ObjectInputStream(i).readObject();
		}
		if (obj instanceof Message)
			return (Message) obj;
		else
			return null;// TODO expected exception
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
