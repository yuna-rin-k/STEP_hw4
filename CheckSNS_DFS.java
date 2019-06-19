//未完成!!!!!

import java.io.*;
import java.util.*;

public class CheckSNS_DFS {

	public static void main(String[] args) {

		ArrayList<Account> accounts = readAccounts("nicknames.txt");
		int[][] snsConnections = readSnsConnections("test.txt", accounts.size());
		//snsConnections[from][to] = 1　→　fromさんがtoさんをフォローしている
		//snsConnections[from][to] = 0　→　fromさんがtoさんをフォローしていない

		int[][] isChecked = new int[accounts.size()][accounts.size()];
		fillArrayWithInt(isChecked, 0);	//0　→　未チェック , 1　→　チェック済み
		int startAccount = Account.getID("jacob");
		int target = Account.getID("billy");

		//int shotestStep = findStepDFS(snsConnections, isChecked, startAccount, target, 0);
		int shotestStep = findStepDFS(snsConnections, isChecked, 0, 3);
		System.out.println("shotestSTEP:"+shotestStep);
	}

	static int findStepDFS(int[][] connections, int[][] isChecked, int from, int target) {

		if (from == target) {
			return 0;
		}

		int shotestStep = Integer.MAX_VALUE;
		int step = -1;
		for (int i = 0; i < connections.length; i++) {
			if (connections[from][i] == 1 && isChecked[from][i] == 0) {
				isChecked[from][i] = 1;
				step = findStepDFS(connections, isChecked, i, target);
				if (step != -1 && step+1 < shotestStep) shotestStep = step+1;	
			}
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

	static int[][] readSnsConnections(String textName, int numberOfAccounts) {

		int[][] snsConnections = new int[numberOfAccounts][numberOfAccounts];
		fillArrayWithInt(snsConnections, 0);

		try {
			Scanner links = new Scanner(new File(textName));
			while (links.hasNext()) {
				int from = links.nextInt();
				int to = links.nextInt();
				snsConnections[from][to] = 1;			
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return snsConnections;	 
	}

	static void fillArrayWithInt(int[][] array, int x) {

		for (int i = 0 ; i < array[0].length; i++) {
			for (int j = 0; j < array[1].length; j++) {
				array[i][j] = x;
			}
		}
	}

	static class Account {

		static Map<String, Integer> idKey_nameValue = new HashMap<>();
		String nickName;
		int id;

		public Account(String nickName, int id) {

			this.nickName = nickName;
			this.id = id;
			idKey_nameValue.put(nickName, id);
		}

		static int getID(String nickName) {

			if (idKey_nameValue.containsKey(nickName)) return idKey_nameValue.get(nickName);
			return -1;
		}
	}
}