package computer_network.IM.server.services;

import java.io.IOException;
import java.net.SocketException;

import computer_network.IM.logging.Logger;
import computer_network.IM.network.ChatMessage;
import computer_network.IM.network.Connector;
import computer_network.IM.network.Message;
import computer_network.IM.network.Type;
import computer_network.IM.server.entities.repositories.AccountRepository;
import computer_network.IM.server.util.Register;

// 核心逻辑块
public final class IMService {
	private IMService() {
	}

	public static void serve(Connector connector, Logger logger) {
		String username = "";
		try {
			// FIXME 整个结构怎么改进呢
			while (true) {
				Message msg = null;
				msg = connector.receive();
				if (msg.type.equals(Type.login)) {
					if (!username.equals(""))
						connector.send(new Message(Type.error, "您已登录：" + username));
					else if (!msg.text.matches("^[a-zA-Z]++$"))
						connector.send(new Message(Type.error, "用户名非法，必须是一个以上的英文字母"));
					else if (!AccountRepository.add(msg.text))
						connector.send(new Message(Type.error, "用户名已被使用"));
					else {
						username = msg.text;
						connector.send(new Message(Type.login, username));
						Register.push(username, connector);
						logger.info("用户 " + username + " 登录");
					}
				} else if (username.equals("")) {
					connector.send(new Message(Type.error, "请先登录"));
				} else if (msg.type.equals(Type.logout)) {
					break;
				} else if (msg.type.equals(Type.send)) {
					ChatMessage chatMessage = ChatMessage.fromString(msg.text);
					logger.info("收到来自 " + username + " 的消息");
					if (chatMessage == null) {
						connector.send(new Message(Type.error, "消息格式非法，可能是程序内部错误"));
						continue;
					}
					Connector connector2 = Register.get(chatMessage.name);
					if (connector2 == null) {
						connector.send(new Message(Type.error, "不存在此用户: " + chatMessage.name));
						continue;
					}
					chatMessage.name = username;
					connector2.send(new Message(Type.receive, chatMessage.toString()));
					connector.send(new Message(Type.send, "服务器已收到消息"));
				}
			}
		} catch (SocketException e) {
			logger.error(e.toString());
		} catch (Exception e) {
			logger.unhandledException(e);
		} finally {
			if (!username.equals("")) {
				AccountRepository.remove(username);
				Register.remove(username);
			}
			// FIXME 结构很奇怪？
			if (!connector.isClosed())
				try {
					connector.close();
				} catch (IOException e) {
					logger.unhandledException(e);
				}
		}
	}
}
