package computer_network.projects.IM.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;

import computer_network.projects.IM.network.ChatMessage;
import computer_network.projects.IM.network.Connector;
import computer_network.projects.IM.network.Message;
import computer_network.projects.IM.network.Socket;
import computer_network.projects.IM.network.Type;

public class IMClient {
	private Connector connector = null;
	private Receiver receiver = null;
	private String ip;
	private int port;

	public IMClient(String ip, String port) {
		this.ip = ip;
		this.port = Integer.parseInt(port);
	}

	public void login(String username) throws IOException {
		if (connector == null) {
			connector = new Connector(new Socket(ip, port));
			receiver = new Receiver(connector);
			receiver.start();
		}
		connector.send(new Message(Type.login, username));
	}

	public void send(String name, String msg) throws IOException {
		if (connector != null)
			connector.send(new Message(Type.send, new ChatMessage(name, msg).toString()));
	}

	public void logout() throws IOException {
		if (connector == null)
			return;
		connector.send(new Message(Type.logout, "OK"));
		connector.close();
		connector = null;
	}

	public void exit() throws IOException {
		logout();
	}
}

class Receiver extends Thread {
	public static final PrintStream printStream = System.out;

	private Connector connector;
	private boolean done = false;

	public Receiver(Connector c) {
		this.connector = c;
	}

	public void exit() {
		done = true;
	}

	@Override
	public void run() {
		try {
			while (!done) {
				Message message = connector.receive();
				switch (message.type) {
				case error:
					printStream.println("服务器返回错误信息 \"" + message.text + "\"");
					break;
				case login:
				case send:
					printStream.println(message.text);
					break;
				case receive:
					ChatMessage chatMessage = ChatMessage.fromString(message.text);
					printStream.println("[" + chatMessage.name + "]对你说: " + chatMessage.text);
					break;
				default:
					break;
				}
			}
		} catch (SocketException e) {
			// TODO 此处只当做主动断开连接，退出线程
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
