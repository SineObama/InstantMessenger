package computer_network.projects.IM.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.SocketException;

import computer_network.projects.IM.logging.Logger;
import computer_network.projects.IM.network.ChatMessage;
import computer_network.projects.IM.network.Connector;
import computer_network.projects.IM.network.Message;
import computer_network.projects.IM.network.Socket;
import computer_network.projects.IM.network.Type;

public class IMClient {
	private static final PrintStream printStream = System.out;
	private Connector connector = null;
	private Receiver receiver = null;
	private String ip;
	private int port;
	private Logger logger = new Logger("Client");

	public IMClient(String ip, String port) {
		this.ip = ip;
		this.port = Integer.parseInt(port);
	}

	public void login(String username) {
		try {
			if (connector == null || connector.isClosed()) {
				printStream.println("正在与服务器创建连接。。。");
				connector = new Connector(new Socket(ip, port));
				receiver = new Receiver(connector, this);
				receiver.start();
			}
			printStream.println("正在发送登录请求。。。");
			connector.send(new Message(Type.login, username));
		} catch (ConnectException e) {
			printStream.println("登录出错，请检查网络。");
		} catch (SocketException e) {
			receiver = null;
			connector = null;
			printStream.println("连接出错，已断开。");
		} catch (IOException e) {
			logger.unhandledException(e);
		}
	}

	public void send(String name, String msg) {
		if (connector == null) {
			printStream.println("未登陆");
			return;
		}
		try {
			connector.send(new Message(Type.send, new ChatMessage(name, msg).toString()));
		} catch (SocketException e) {
			receiver = null;
			connector = null;
			printStream.println("发送消息出错。请重新登录");
		} catch (IOException e) {
			logger.unhandledException(e);
		}
	}

	public void logout() {
		if (connector == null) {
			printStream.println("未登陆");
			return;
		}
		try {
			connector.send(new Message(Type.logout, "OK"));
			connector.close();
			printStream.println("登出成功");
		} catch (SocketException e) {
			printStream.println("登出出错，连接已断开。");
		} catch (IOException e) {
			logger.unhandledException(e);
		} finally {
			connector = null;
		}
	}

	public void exit() {
		logout();
	}
	
	public void  reset() {
		connector = null;
		receiver = null;
	}
}

class Receiver extends Thread {
	private static final PrintStream printStream = System.out;

	private Connector connector;
	private IMClient client;
	private boolean done = false;

	public Receiver(Connector c, IMClient client) {
		this.connector = c;
		this.client=client;
	}

	public void exit() {
		done = true;
	}

	@Override
	public void run() {
		Logger logger = new Logger("Client");
		try {
			while (!done) {
				Message message = connector.receive();
				switch (message.type) {
				case error:
					printStream.println("错误： \"" + message.text + "\"");
					break;
				case login:
					printStream.println("登录成功");
					break;
				case send:
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
			logger.fine(e.toString());
			client.reset();
			printStream.println("连接已断开");
		} catch (Exception e) {
			logger.unhandledException(e);
		}
	}
}
