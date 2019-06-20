import java.io.*;
import java.util.*;

public class CheckSNS {

	public static void main(String[] args) {

		ArrayList<Account> accounts = readAccounts("nicknames.txt");
		SNS sns = new SNS(accounts);
		Map<Account, ArrayList<Account>> snsConnection = readSnsConnection(sns, "links.txt", accounts.size());

		Account startAccount = sns.nicknameKey_accountVal.get("jacob");
		Account target = sns.nicknameKey_accountVal.get("billy");

		ArrayList<Account> route = findRoute(sns, startAccount, target);

		showRoute(route);

	}

	public static ArrayList<Account> findRoute(SNS sns, Account startAccount, Account target) {

		ArrayList<Account> accounts = sns.getAccountList();
		Map<Account, ArrayList<Account>> snsConnection = sns.getSNSConnection();

		int numOfAccount = accounts.size();
		int[] preID = new int[numOfAccount];
		Arrays.fill(preID, -1);

		boolean[] isChecked = new boolean[numOfAccount];
		Arrays.fill(isChecked, false);
		isChecked[startAccount.id] = true;

		return findRouteBFS(sns, startAccount, target, snsConnection, isChecked, preID);
	}

	public static ArrayList<Account> findRouteBFS(SNS sns, Account startAccount, Account target, 
							Map<Account, ArrayList<Account>> snsConnection, boolean[] isChecked, int[] preID) {

		Queue<Account> q = new ArrayDeque<>();
		q.add(startAccount);

		while (q.size() > 0) {

			Account account = q.poll();

			if (account.equals(target))
				break;

			if (!snsConnection.containsKey(account)) 
				break;

			ArrayList<Account> follows = snsConnection.get(account);
			for (int i = 0; i < follows.size(); i++) {
				Account follow = follows.get(i);
				if (isChecked[follow.id] == false) {
					isChecked[follow.id] = true;
					q.add(follow);
					preID[follow.id] = account.id;
				}	
			} 
		}

		if (preID[target.id] != -1) {

			ArrayList<Account> route = new ArrayList<>();
				Account preAccount;
				int preAccountID = target.id;
				route.add(target);
				Account currentAccount = target;

				while (true) {
					preAccountID = preID[currentAccount.id];
					preAccount = sns.getAccountWithID(preAccountID);
					if (preAccount.equals(startAccount)){
						route.add(preAccount);
						break;
					}
					route.add(preAccount);
					currentAccount = preAccount;
				}
				return route;
		}		
		return null;
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


	static Map<Account, ArrayList<Account>> readSnsConnection(SNS sns, String textName, int numberOfAccounts) {

		Map<Account, ArrayList<Account>> snsConnection = new HashMap<>();
		
		try {
			Scanner links = new Scanner(new File(textName));
			while (links.hasNext()) {

				int from = links.nextInt();
				int to = links.nextInt();

				Account fromAccount = sns.getAccountWithID(from);
				Account toAccount = sns.getAccountWithID(to);
				ArrayList<Account> toArray;
				if (snsConnection.containsKey(fromAccount)) toArray = snsConnection.get(fromAccount);
				else toArray = new ArrayList<>();

				toArray.add(toAccount);
				snsConnection.put(fromAccount, toArray);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		sns.setSNSConnection(snsConnection);

		return snsConnection;	 
	}

	static void showRoute(ArrayList<Account> route) {

		System.out.println("step:" + (route.size()-1));

		for (int i = route.size()-1; i > 0; i--) {
			Account account = route.get(i);
			System.out.print(account.nickname + "(" + account.id + ") → ");
		}
		System.out.println(route.get(0).nickname + "("+route.get(0).id+")");
	}

	static class Account {

		int id;
		String nickname;

		public Account(int id, String nickname) {

			this.id = id;
			this.nickname = nickname;
		}
	}

	static class SNS {

		private ArrayList<Account> accountList = new ArrayList<>();
		private Map<Account, ArrayList<Account>> snsConnection;
		private Map<Integer, Account> idKey_accountVal = new HashMap<>();
		private Map<String, Account> nicknameKey_accountVal = new HashMap<>();

		public SNS(ArrayList<Account> accountList) {
			this.accountList = accountList;
			creatIDkeyMap(accountList);
			creatNicknamekeyMap(accountList);
		}

		private void creatIDkeyMap(ArrayList<Account> accounts) {

			this.idKey_accountVal = new HashMap<>();
			for (int i = 0; i < accountList.size(); i++) {
				Account account = accounts.get(i);
				int id = account.id;
				idKey_accountVal.put(id, account);
			}
		}

		private void creatNicknamekeyMap(ArrayList<Account> accounts) {

			this.nicknameKey_accountVal = new HashMap<>();
			for (int i = 0; i < accountList.size(); i++) {
				Account account = accounts.get(i);
				String nickname = account.nickname;
				nicknameKey_accountVal.put(nickname, account);
			}
		}

		public ArrayList<Account> getAccountList() {
			return this.accountList;
		}

		public Map<Account, ArrayList<Account>> getSNSConnection() {
			return this.snsConnection;
		}

		public Account getAccountWithID(int id) {
			return this.idKey_accountVal.get(id);
		}

		public Account getAccountWithNickname(String nickName) {
			return this.nicknameKey_accountVal.get(nickName);
		}

		public void setSNSConnection(Map<Account, ArrayList<Account>> snsConnection) {
			this.snsConnection = snsConnection;
		}
	}
}