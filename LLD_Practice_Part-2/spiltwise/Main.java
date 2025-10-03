package lld_practice.spiltwise;

import java.util.*;

class User {
    String id;
    String name;
    User(String id, String name) {
        this.id = id;
        this.name = name;
    }
}

class Splitwise {
    Map<String, User> users = new HashMap<>();
    Map<String, Map<String, Integer>> balances = new HashMap<>();

    void createUser(String id, String name) {
        users.put(id, new User(id, name));
        balances.put(id, new HashMap<>());
    }

    void addExpense(String paidBy, int amount, String type, List<String> participants, List<Integer> values) {
        int n = participants.size();
        List<Integer> shares = new ArrayList<>();

        if (type.equals("EQUAL")) {
            int share = amount / n;
            for (int i = 0; i < n; i++) shares.add(share);
        } else if (type.equals("EXACT")) {
            shares = values;
        } else if (type.equals("PERCENT")) {
            for (int val : values) shares.add((amount * val) / 100);
        }

        for (int i = 0; i < n; i++) {
            String user = participants.get(i);
            int share = shares.get(i);
            if (!user.equals(paidBy)) {
                balances.get(user).put(paidBy, balances.get(user).getOrDefault(paidBy, 0) + share);
                balances.get(paidBy).put(user, balances.get(paidBy).getOrDefault(user, 0) - share);
            }
        }
    }

    void show() {
        boolean empty = true;
        for (String u : balances.keySet()) {
            for (Map.Entry<String, Integer> e : balances.get(u).entrySet()) {
                if (e.getValue() > 0) {
                    empty = false;
                    System.out.println(users.get(u).name + " owes " + users.get(e.getKey()).name + ": " + e.getValue());
                }
            }
        }
        if (empty) System.out.println("No balances");
    }

    void show(String userId) {
        boolean empty = true;
        for (Map.Entry<String, Integer> e : balances.get(userId).entrySet()) {
            if (e.getValue() > 0) {
                empty = false;
                System.out.println(users.get(userId).name + " owes " + users.get(e.getKey()).name + ": " + e.getValue());
            }
        }
        if (empty) System.out.println("No balances");
    }
}

public class Main {
    public static void main(String[] args) {
        Splitwise s = new Splitwise();
        s.createUser("u1", "Alice");
        s.createUser("u2", "Bob");
        s.createUser("u3", "Charlie");
        s.createUser("u4", "David");
        s.addExpense("u1", 1000, "EQUAL", Arrays.asList("u1", "u2", "u3", "u4"), new ArrayList<>());
        s.show();
        s.addExpense("u2", 1250, "EXACT", Arrays.asList("u1", "u2"), Arrays.asList(370, 880));
        s.show();
        s.addExpense("u3", 1200, "PERCENT", Arrays.asList("u1", "u2", "u3", "u4"), Arrays.asList(40, 20, 20, 20));
        s.show();
    }
}
