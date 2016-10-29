package computer_network.IM.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// 在控制台接受用户指令
public class IMClientUI {
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("请输入两个参数: <ip> <port>");
			return;
		}
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		IMClient imClient = new IMClient(args[0], args[1]);
		while ((userInput = stdIn.readLine()) != null) {
			if (userInput.equals("exit")) {
				imClient.exit();
				break;
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
