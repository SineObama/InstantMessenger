package computer_network.projects.IM.server;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Logger;

import computer_network.projects.IM.network.ChatMessage;
import computer_network.projects.IM.network.Connector;
import computer_network.projects.IM.network.Message;
import computer_network.projects.IM.network.Socket;
import computer_network.projects.IM.network.Type;
import computer_network.projects.IM.server.account.AccountManager;

// 核心逻辑块
public final class IMService {
	private IMService() {
	}

	public static void serve(Socket socket) {
		Logger logger = Logger.getLogger("EchoThread-" + Thread.currentThread().getId());
		String username = "";
		try {
			Connector connector = new Connector(socket);
			// TODO 整个结构不好，太大？
			while (true) {
				Message msg = connector.receive();
				if (msg.type.equals(Type.login)) {
					if (!username.equals(""))
						connector.send(new Message(Type.error, "您已登陆：" + username));
					else if (!msg.text.matches("^[a-zA-Z]++$"))
						connector.send(new Message(Type.error, "用户名非法，必须是一个以上的英文字母"));
					else if (!AccountManager.add(msg.text))
						connector.send(new Message(Type.error, "用户名已被使用"));
					else {
						connector.send(new Message(Type.login, "登陆成功"));
						username = msg.text;
						Register.push(username, connector);
						logger.info(logger.getName() + "\t用户 " + username + " 登陆");
					}
				} else if (username.equals(""))
					connector.send(new Message(Type.error, "请先登陆"));
				else if (msg.type.equals(Type.logout)) {
					break;
				} else if (msg.type.equals(Type.send)) {
					ChatMessage chatMessage = ChatMessage.fromString(msg.text);
					if (chatMessage == null) {
						connector.send(new Message(Type.error, "消息格式非法，可能是程序内部错误"));
						continue;
					}
					Connector connector2 = Register.get(chatMessage.name);
					if (connector2 == null) {
						connector.send(new Message(Type.error, "不存在此用户: " + chatMessage.name));
						continue;
					}
					logger.info(logger.getName() + "\t收到来自 " + username + " 的消息");
					chatMessage.name = username;
					connector2.send(new Message(Type.receive, chatMessage.toString()));
					connector.send(new Message(Type.send, "服务器收到消息，已发送给对方"));
				}
			}
		} catch (SocketException e) {
			logger.warning(logger.getName() + "\t" + e + " 可能是连接意外断开：" + socket);
		} catch (Exception e) {
			logger.severe(logger.getName() + "\t未被预测的异常：\n" + e);
			e.printStackTrace();
		} finally {
			if (!username.equals("")) {
				AccountManager.remove(username);
				Register.remove(username);
			}
		}
	}
}
