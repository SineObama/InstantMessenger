package computer_network.projects.IM.server;

import java.io.IOException;
import java.io.StringReader;

import computer_network.projects.IM.network.ChatMessage;
import computer_network.projects.IM.network.Connector;
import computer_network.projects.IM.network.Message;
import computer_network.projects.IM.network.Socket;
import computer_network.projects.IM.network.Type;
import computer_network.projects.IM.server.account.AccountManager;

// 单实例服务。主要的逻辑块
public final class IMService {
	private IMService() {
	}

	public static void serve(Socket socket) {
		String username = "";
		try {
			Connector connector = new Connector(socket);

			// TODO 整个结构感觉不好
			while (true) {
				Message msg = connector.receive();
				if (msg.type.equals(Type.exit))
					break;
				else if (msg.type.equals(Type.login)) {
					if (!username.equals(""))
						connector.send(new Message(Type.error, "您已登陆：" + username));
					else if (msg.text.equals(""))
						connector.send(new Message(Type.error, "用户名不能为空"));
					else if (!AccountManager.add(msg.text))
						connector.send(new Message(Type.error, "用户名已存在"));
					else {
						connector.send(new Message(Type.login, "OK"));
						username = msg.text;
						Register.push(username, connector);
					}
				} else if (username.equals(""))
					connector.send(new Message(Type.error, "请先登陆"));
				else if (msg.type.equals(Type.logout)) {
					Register.remove(username);
					username = "";
					connector.send(new Message(Type.logout, "OK"));
				} else if (msg.type.equals(Type.message)) {
					ChatMessage chatMessage = ChatMessage.fromString(msg.text);
					if (chatMessage == null) {
						connector.send(new Message(Type.error, "消息格式错误"));
						continue;
					}
					Connector connector2 = Register.get(chatMessage.name);
					if (connector2 == null) {
						connector.send(new Message(Type.error, "不存在此用户: " + chatMessage.name));
						continue;
					}
					chatMessage.name = username;
					connector2.send(new Message(Type.message, chatMessage.toString()));
				}
			}

			connector.close();
		} catch (IOException exc) {
			System.out.println(exc);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (!username.equals("")) {
				AccountManager.remove(username);
				Register.remove(username);
			}
		}
	}
}
