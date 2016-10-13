package computer_network.projects.IM.network;

import java.io.Serializable;

// 直接用于客户端和服务器的信息传递
@SuppressWarnings("serial")
public class Message implements Serializable {
	public Type type;
	public String text = "";

	public Message(Type t, String s) {
		type = t;
		text = s;
	}

	@Override
	public String toString() {
		return "type: " + type.toString() + "|text: " + text;
	}
}
