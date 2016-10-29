package computer_network.IM.server.entities.repositories;

import java.util.Set;
import java.util.TreeSet;

import computer_network.IM.server.entities.Account;
import computer_network.IM.server.entities.AccountComparator;

public final class AccountRepository {
	private AccountRepository() {
	}

	private static Set<Account> accounts = new TreeSet<Account>(new AccountComparator());

	public static synchronized boolean add(String name) {
		return accounts.add(new Account(name));
	}
 
	public static synchronized boolean remove(String name) {
		return accounts.remove(new Account(name));
	}
}
