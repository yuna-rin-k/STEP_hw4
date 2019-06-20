//未完成!!!!!

import java.io.*;
import java.util.*;

public class CheckSNS_DFS {

	public static void main(String[] args) {

		ArrayList<Account> accounts = readAccounts("nicknames.txt");
		Map<Account, ArrayList<Account>> snsConnection = readSnsConnection("links.txt", accounts.size());

		boolean[] isChecked = new boolean[accounts.size()];
		Arrays.fill(isChecked, false);		
		
		Account startAccount = Account.nameKey_accountVal.get("jacob");
		Account target = Account.nameKey_accountVal.get("billy");

		int shotestStep = findStepDFS(snsConnections, isChecked, startAccount, target);
		System.out.println("shotestSTEP:"+shotestStep);
	}

	static ArrayList<Account> findRoute(Map<Account, ArrayList<Account>> snsConnection, boolean[] isChecked, 
												Account account, Account target, ArrayList<Account> route, ArrayList<Account> bestRoute) {

		if (account == target) {
			Arrays.fill(isChecked, false);
			if (bestRoute.size() > route.size() || bestRoute.size() == 0) bestRoute = route;
			return route;
		}


		return shotestStep;
	}

	static ArrayList<Account> readAccounts(String textName) {

		Scanner accountFile = null;
		ArrayList<Account> accounts = new ArrayList<>();

		try {
			accountFile = new Scanner(new File(textName));
			while (accountFile.hasNext()) {
				int id = accountFile.nextInt();
				String nickName = accountFile.next();
				accounts.add(new Account(nickName, id));
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