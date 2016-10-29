package computer_network.IM.server.entities;

import java.util.Comparator;

public class AccountComparator implements Comparator<Account> {
	public int compare(Account o1, Account o2) {
		return o1.getName().compareTo(o2.getName());
	}
}
