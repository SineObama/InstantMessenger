package computer_network.projects.IM.network;

public class ChatMessage {
	public String name;
	public String text;

	@Override
	public String toString() {
		return name + "\n" + text;
	}

	public static ChatMessage fromString(String s) {
		int k = s.indexOf('\n');
		if (k < 0)  // TODO
			return null;
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.name = s.substring(0, k);
		chatMessage.text = s.substring(k + 1);
		return chatMessage;
	}
}
