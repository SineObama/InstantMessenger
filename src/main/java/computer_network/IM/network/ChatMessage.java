package computer_network.IM.network;

public class ChatMessage {
	public String name;
	public String text;

	public ChatMessage(String name, String text) {
		this.name = name;
		this.text = text;
	}

	@Override
	public String toString() {
		return name + "\n" + text;
	}

	public static ChatMessage fromString(String s) {
		int k = s.indexOf('\n');
		if (k < 0)
			return null;
		ChatMessage chatMessage = new ChatMessage(s.substring(0, k), s.substring(k + 1));
		return chatMessage;
	}
}
