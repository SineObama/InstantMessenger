package computer_network.projects.IM.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IMClientUI {
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("请输入两个参数: <ip> <port>");
			return;
		}
		// 在控制台接受用户指令等
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		IMClient client = new IMClient(args[0], args[1]);
		try {
			outer: while ((userInput = stdIn.readLine()) != null) {
				switch (userInput) {
				case "exit":
					client.exit();
					break outer;
				case "login":
					client.login(stdIn.readLine());
					break;
				case "logout":
					client.logout();
					break;
				case "msg":
					client.send(stdIn.readLine(), stdIn.readLine());
					break;
				default:
					System.out.println("指令错误");
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stdIn.close();
	}
}
