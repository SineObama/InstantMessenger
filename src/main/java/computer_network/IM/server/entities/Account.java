package computer_network.IM.server.entities;

/*
 * 保存账户信息
 */
public class Account {
	private static int _count = 0;
	private String _name;

	public Account(String name) {
		_name = name;
		_count++;
	}
	
	public int getCount() {
		return _count;
	}
	
	public String getName() {
		return _name;
	}
}
