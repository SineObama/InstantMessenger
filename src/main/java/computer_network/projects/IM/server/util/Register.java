package computer_network.projects.IM.server.util;

import java.util.Map;
import java.util.TreeMap;

import computer_network.projects.IM.network.Connector;

public class Register {
	private static Map<String, Connector> map = new TreeMap<String, Connector>();

	public static synchronized void push(String s, Connector c) {
		map.put(s, c);
	}

	public static synchronized void remove(String s) {
		map.remove(s);
	}

	public static synchronized Connector get(String s) {
		return map.get(s);
	}
}
