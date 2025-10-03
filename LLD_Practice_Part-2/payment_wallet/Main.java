package lld_practice.payment_wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class User {
    String id, name;
    Double balance;
    List<Transaction> transactions = new ArrayList<>();
    User(String id, String name, Double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }
}
enum Type {
    WITHDRAW, DEPOSIT, TRANSFER
}
enum Status {
    SUCCESSFUL, FAILED, PARTIALLY_SUCCESSFUL
}
class Transaction {
    static int cnt = 0;
    int id;
    Type type;
    Status status;
    Double amt;
    String targetUserId ="#";
    Transaction(Type type, Status status, Double amt){
        this.id = cnt++;
        this.type = type;
        this.status = status;
        this.amt = amt;
    }
    Transaction(Type type, Status status, Double amt, String targetUserId){
        this.id = cnt++;
        this.type = type;
        this.status = status;
        this.amt = amt;
        this.targetUserId = targetUserId;
    }
}
class WalletSystem {
    HashMap<String, User> userMap = new HashMap<>();
    public void createUser(String id, String name, Double balance) {
        userMap.put(id, new User(id, name, balance));
        System.out.println("User Added! "+name);
    }

    public void deposit(String userId, Double amt) {
        User u = userMap.get(userId);
        u.balance += amt;
        u.transactions.add(new Transaction(Type.DEPOSIT, Status.SUCCESSFUL, amt));
        System.out.println("Amount Deposited!, current Balance: "+u.balance+" name: "+u.name);
    }
    public void withdraw(String userId, Double amt) {
        User u = userMap.get(userId);
        if(u.balance == 0.0) {
            System.out.println("Insufficient funds!,  name: "+u.name);
            u.transactions.add(new Transaction(Type.WITHDRAW, Status.FAILED, amt));
        } else if(u.balance < amt) {
            double bal = u.balance;
            u.balance = 0.0;
            u.transactions.add(new Transaction(Type.WITHDRAW, Status.PARTIALLY_SUCCESSFUL, bal));
            System.out.println("Partial Amount withdrawn!, current balance: "+u.balance+" name: "+u.name);
        } else{
            u.balance -= amt;
            u.transactions.add(new Transaction(Type.WITHDRAW, Status.SUCCESSFUL, amt));
            System.out.println("Amount withdrawn!, current balance: "+u.balance+" name: "+u.name);
        }
    }
    public void transfer(String userId, Double amt, String targetUserId) {
        User u = userMap.get(userId);
        User targetUser = userMap.get(targetUserId);
        if(u.balance < amt){
            Double currBal = u.balance;
            targetUser.balance += currBal;
            u.balance = 0.0;
            u.transactions.add(new Transaction(Type.TRANSFER, Status.PARTIALLY_SUCCESSFUL, amt));
            targetUser.transactions.add(new Transaction(Type.DEPOSIT, Status.PARTIALLY_SUCCESSFUL, currBal, userId));
            System.out.println("Amount: "+currBal+" partially transferred to: "+targetUser.name);
        } else {
            u.balance -= amt;
            targetUser.balance += amt;
            u.transactions.add(new Transaction(Type.TRANSFER, Status.SUCCESSFUL, amt, targetUserId));
            targetUser.transactions.add(new Transaction(Type.DEPOSIT, Status.SUCCESSFUL, amt, userId));
            System.out.println("Amount: "+amt+" transferred to: "+targetUser.name);
        }
    }
    public Double showBalance(String userId) {
        return userMap.get(userId).balance;
    }
    public void showTransactionHistory(String userId) {
        for(Transaction t: userMap.get(userId).transactions){
            System.out.print("T_ID: "+t.id+" TYPE: "+t.type+" STATUS: "+t.status);
            if(!t.targetUserId.equals("#")) System.out.print(" TO: "+t.targetUserId);
            System.out.println();
        }
    }
}
public class Main {
    public static void main(String[] args) {
        WalletSystem w = new WalletSystem();
        w.createUser("1", "Alex", 1000.0);
        w.createUser("2", "bob", 2000.0);
        w.deposit("1", 500.0);
        w.transfer("1", 500.0, "2");
        w.withdraw("1", 500.0);
        Double bal;
        bal = w.showBalance("1");
        System.out.println("Current Balance is: "+bal);
        bal = w.showBalance("2");
        System.out.println("Current Balance is: "+bal);
        w.transfer("1", 700.0, "2");
        w.withdraw("1", 500.0);
        w.transfer("2", 500.0, "1");
        bal = w.showBalance("1");
        System.out.println("Current Balance is: "+bal);
        bal = w.showBalance("2");
        System.out.println("Current Balance is: "+bal);
        w.showTransactionHistory("1");
    }
}

