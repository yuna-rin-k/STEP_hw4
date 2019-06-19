import java.io.*;
import java.util.*;

public class CheckSNS {

	public static void main(String[] args) {

		ArrayList<Account> accounts = readAccounts("nicknames.txt");
		Map<Account, ArrayList<Account>> snsConnection = readSnsConnection("links.txt", accounts.size());	

		Account startAccount = Account.nameKey_accountVal.get("jacob");
		Account target = Account.nameKey_accountVal.get("billy");

		boolean[] isChecked = new boolean[accounts.size()];
		Arrays.fill(isChecked, false);

		ArrayList<Account> route = new ArrayList<>();
		route.add(startAccount);

		route = findRoute(startAccount, target, accounts.size(), snsConnection, isChecked, route);	
		showRoute(route);

	}

	public static ArrayList<Account> findRoute(Account startAccount, Account target, int numberOfAccounts, 
							Map<Account, ArrayList<Account>> snsConnection, boolean[] isChecked, ArrayList<Account> route) {

		Queue<Account> q = new ArrayDeque<>();
		q.add(startAccount);

		while (q.size() > 0) {

			Account account = q.poll();
			if (account.equals(target)) return route;

			if (!snsConnection.containsKey(account)) return null;
			ArrayList<Account> follows = snsConnection.get(account);

			for (int i = 0; i < follows.size(); i++) {
				Account follow = follows.get(i);
				if (isChecked[follow.id] == false) {
					isChecked[follow.id] = true;
					route.add(follow);
					route = findRoute(follow, target, numberOfAccounts, snsConnection, isChecked, route);
					if (route != null) break;
				}	
			} 
		}
		return route;
	}
	static ArrayList<Account> readAccounts(String textName) {

		Scanner accountFile = null;
		ArrayList<Account> accounts = new ArrayList<>();

		try {
			accountFile = new Scanner(new File(textName));
			while (accountFile.hasNext()) {
				int id = accountFile.nextInt();
				String nickname = accountFile.next();
				accounts.add(new Account(id, nickname));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		return accounts;		 
	}


	static Map<Account, ArrayList<Account>> readSnsConnection(String textName, int numberOfAccounts) {

		Map<Account, ArrayList<Account>> snsConnection = new HashMap<>();
		
		try {
			Scanner links = new Scanner(new File(textName));
			while (links.hasNext()) {

				int from = links.nextInt();
				int to = links.nextInt();

				Account fromAccount = Account.idKey_accountVal.get(from);
				Account toAccount = Account.idKey_accountVal.get(to);
				ArrayList<Account> toArray;
				if (snsConnection.containsKey(fromAccount)) toArray = snsConnection.get(fromAccount);
				else toArray = new ArrayList<>();

				toArray.add(toAccount);
				snsConnection.put(fromAccount, toArray);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return snsConnection;	 
	}

	static void showRoute(ArrayList<Account> route) {

		System.out.println("step:" + (route.size()-1));
		for (int i = 0; i < route.size()-1; i++) {
			String name = route.get(i).nickname;
			System.out.print(name + " → ");
		}
		System.out.println(route.get(route.size()-1).nickname);
	}

	static class Account {

		static Map<Integer, Account> idKey_accountVal = new HashMap<>();
		static Map<String, Account> nameKey_accountVal = new HashMap<>();
		int id;
		String nickname;

		public Account(int id, String nickname) {

			this.id = id;
			this.nickname = nickname;
			idKey_accountVal.put(id, this);
			nameKey_accountVal.put(nickname, this);
		}
	}
}