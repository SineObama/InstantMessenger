package computer_network.projects.IM.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import computer_network.projects.IM.network.ChatMessage;
import computer_network.projects.IM.network.Connector;
import computer_network.projects.IM.network.Message;
import computer_network.projects.IM.network.Socket;
import computer_network.projects.IM.network.Type;

public class IMClient {

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("127.0.0.1", Integer.parseInt(args[0]));
		System.out.println("当前socket信息：" + socket);
		Connector connector = new Connector(socket);

		new Receiver(connector).start();

		// 在控制台接受用户指令等
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		try {
			outer: while ((userInput = stdIn.readLine()) != null) {
				switch (userInput) {
				case "exit":
					connector.send(new Message(Type.exit, "OK"));
					break outer;
				case "login":
					userInput = stdIn.readLine();
					connector.send(new Message(Type.login, userInput));
					break;
				case "logout":
					connector.send(new Message(Type.logout, "OK"));
					break;
				case "msg":
					ChatMessage chatMessage = new ChatMessage();
					chatMessage.name = stdIn.readLine();
					chatMessage.text = stdIn.readLine();
					connector.send(new Message(Type.message, chatMessage.toString()));
					break;
				default:
					System.out.println("指令错误");
					break;
				}
				// if (userInput.equals("exit"))
				// break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		stdIn.close();
		connector.close();
	}
}

class Receiver extends Thread {
	private Connector c;

	public Receiver(Connector _c) {
		c = _c;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Message message=c.receive();
				switch (message.type) {
				case error:
					System.out.println("服务器返回错误信息：" + message.text);
					break;
				case login:
					System.out.println("登录成功");
					break;
				case logout:
					System.out.println("登出成功");
					break;
				case message:
					ChatMessage chatMessage = ChatMessage.fromString(message.text);
					System.out.println(chatMessage.name + " 光明正大地对你说: " + chatMessage.text);
					break;
				default:
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
