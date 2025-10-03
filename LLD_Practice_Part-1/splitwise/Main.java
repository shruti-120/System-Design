package splitwise;
import java.util.*;

class User {
    private static int counter = 1;
    private int id;
    private String name;
    public User(String name){
        this.id = counter++;
        this.name = name;
    }
    public String getName() { return name; }
}
class Expense {
    private double amount;
    private User paidBy;
    private List<User> splitBetween = new ArrayList<>();
    public Expense(double amount, User paidBy){
        this.amount = amount;
        this.paidBy = paidBy;
    }
    public double getAmount() { return amount; }
    public User getPaidByUser() { return paidBy; }
    public List<User> getParticipants() { return splitBetween; }
    public void addParticipants(User payee) { splitBetween.add(payee); }
}

interface SpiltStrategy {
    void split(Expense expense, Map<User, Map<User, Double>> balances);
}

class SplitByEqual implements SpiltStrategy {
    public void split(Expense expense, Map<User, Map<User, Double>> balanceSheet) {
        double share = expense.getAmount() / expense.getParticipants().size();
        User paidBy = expense.getPaidByUser();

        for(User participant : expense.getParticipants()){
            if(participant.equals(paidBy))continue;
            balanceSheet.get(participant).put(paidBy,
                balanceSheet.get(participant).getOrDefault(paidBy, 0.0) + share);
        }
    }
}
class Splitwise {
    List<User> users = new ArrayList<>();
    private Map<User, Map<User, Double>> balanceSheet = new HashMap<>();
    SpiltStrategy spiltStrategy = new SplitByEqual();

    public void adduser(User user) { 
        users.add(user);
        balanceSheet.put(user, new HashMap<>());
     }

    public void showBalance() { 
        for (User user : users) {
            for (Map.Entry<User, Double> entry : balanceSheet.get(user).entrySet()) {
                if (entry.getValue() > 0) {
                    System.out.println(user.getName() + " owes " + entry.getKey().getName() + ": Rs " + entry.getValue());
                }
            }
        }
    }

    public void addExpense(double amount, User paidBy, List<User> participants){
        Expense expense = new Expense(amount, paidBy);
        for(User participant : participants)expense.addParticipants(participant);

        spiltStrategy.split(expense, balanceSheet);
    }

}

public class Main {
    public static void main(String[] args) {
        Splitwise splitwise = new Splitwise();
        User u1 = new User("Alice");
        User u2 = new User("bob");
        User u3 = new User("john");
        User u4 = new User("maxy");
        
        splitwise.adduser(u1); splitwise.adduser(u2);
        splitwise.adduser(u3); splitwise.adduser(u4);

        ArrayList<User> splitBetween = new ArrayList<>();
        splitBetween.add(u2); splitBetween.add(u3); splitBetween.add(u4);splitBetween.add(u1);
        splitwise.addExpense(120, u1, splitBetween);
        splitwise.addExpense(500, u2, splitBetween);
        splitwise.showBalance();
    }
}
