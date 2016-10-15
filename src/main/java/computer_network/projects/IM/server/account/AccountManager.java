package computer_network.projects.IM.server.account;

import java.util.Set;
import java.util.TreeSet;

public final class AccountManager {
	private AccountManager() {
	}

	private static Set<Account> accounts = new TreeSet<Account>(new AccountComparator());

	public static synchronized boolean add(String name) {
		return accounts.add(new Account(name));
	}
 
	public static synchronized boolean remove(String name) {
		return accounts.remove(new Account(name));
	}
}
