package computer_network.IM.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import computer_network.IM.network.IMConstant;

// 在命令行接受用户指令
public class IMClientUI {
	public static void main(String[] args) throws IOException {
		// 解析参数
		String ip;
		int port;
		if (args.length == 0) {
			ip = "127.0.0.1";
			port = IMConstant.defaultPort;
		} else if (args.length == 1) {
			ip = args[0];
			port = IMConstant.defaultPort;
		} else if (args.length == 2) {
			ip = args[0];
			port = Integer.parseInt(args[1]);
		} else {
			System.out.println("请输入两个参数: <ip> <port>");
			return;
		}

		// 从命令行读取指令并执行
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		IMClient imClient = new IMClient(ip, port);
		while ((userInput = stdIn.readLine()) != null) {
			if (userInput.equals("exit")) {
				imClient.exit();
				break;
			} else if (userInput.equals("help")) {
				System.out.print("登陆：login <用户名>\n" + "登出：logout\n" + "发送消息：send <用户名> <发送内容>\n" + "退出：exit\n");
			} else if (userInput.equals("logout")) {
				imClient.logout();
			} else if (userInput.startsWith("login ")) {
				imClient.login(userInput.substring(6));
			} else {
				String[] strings = userInput.split(" ");
				if (userInput.startsWith("send ") && strings.length > 2)
					imClient.send(strings[1], userInput.substring(6 + strings[1].length()));
				else
					System.out.println("指令错误");
			}
		}
		stdIn.close();
	}
}
