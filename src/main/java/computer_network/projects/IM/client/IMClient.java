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

	private static Connector connector = null;
	private static boolean connected = false;

	private static String ip = "127.0.0.1";
	private static int port = 8000;

	private static Logger logger = new Logger("Client");

	public static void setServer(String _ip, String _port) {
		ip = _ip;
		port = Integer.parseInt(_port);
	}

	public static void login(String username) {
		try {
			if (!connected) {
				connected = true;
				printStream.println("正在与服务器创建连接。。。");
				connector = new Connector(new Socket(ip, port));
				new Receiver().start();
			}
			printStream.println("正在发送登录请求。。。");
			connector.send(new Message(Type.login, username));
		} catch (ConnectException e) {
			printStream.println("登录出错，请检查网络。");
		} catch (SocketException e) {
			connected = false;
			printStream.println("连接出错，已断开。");
		} catch (IOException e) {
			logger.unhandledException(e);
		}
	}

	public static void send(String name, String msg) {
		// TODO 不能处理睡眠重启后的情况，似乎出现阻塞
		if (!connected) {
			printStream.println("未登陆");
			return;
		}
		try {
			connector.send(new Message(Type.send, new ChatMessage(name, msg).toString()));
		} catch (SocketException e) {
			connected = false;
			printStream.println("发送消息出错。请重新登录");
		} catch (IOException e) {
			logger.unhandledException(e);
		}
	}

	public static void logout() {
		if (!connected) {
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
			connected = false;
		}
	}

	public static void exit() {
		logout();
	}

	static class Receiver extends Thread {

		@Override
		public void run() {
			Logger logger = new Logger("Client");
			try {
				while (true) {
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
				// FIXME “接收”线程退出。直接修改了连接状态
				connected = false;
				printStream.println("连接已断开");
			} catch (Exception e) {
				logger.unhandledException(e);
			}
		}

	}

}
