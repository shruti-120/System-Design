package lld_practice.stock_brokerage_system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class User {
    String name, id;
    double balance;
    List<Transaction> transactionHistory = new ArrayList<>();
    HashMap<String, Holding> stockHoldings = new HashMap<>();

    User(String id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public void showTransactionHistory() {
        System.out.println("Transaction History of User : ");

        for(Transaction t: transactionHistory) {
            System.out.println("Stock ID: "+t.stockId+" Type: "+t.type+" Quantity "+t.qty);
        }
    }
}
class Transaction {
    static int cnt = 0;
    int id;
    String stockId, type;
    int qty;
    Transaction(String stockId, String type, int qty) {
        this.id = cnt++;
        this.stockId = stockId;
        this.type = type;
        this.qty = qty;
    }
}
class Stock {
    String id, name;
    double price;
    Stock(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
class Holding {
    Stock stock;
    int qty;
    Holding(Stock stock, int qty) {
        this.stock = stock;
        this.qty = qty;
    }
}
class StockBrokerageSystem {
    HashMap<String, User> userMap = new HashMap<>();
    HashMap<String, Stock> stockMap = new HashMap<>();

    public void createUser(String userId, String name, double balance) {
        User u = new User(userId, name, balance);
        if(userMap.containsKey(userId)){
            System.out.println("User already present!"); return;
        }
        userMap.put(userId, u);
        System.out.println("User Added! Name: "+name);
    }
    public void addStock(String stockId, String name, double price) {
        Stock s = new Stock(stockId, name, price);
        if(stockMap.containsKey(stockId)){
            System.out.println("Stock already present!"); return;
        }
        stockMap.put(stockId, s);
        System.out.println("Stock Added! Name - "+name);
    }
    public void buyStock(String userId, String stockId, int qty) {
        if(!userMap.containsKey(userId)) {
            System.out.println("Invalid userId");
            return;
        }
        if(!stockMap.containsKey(stockId)){
            System.out.println("Invalid stockId"); return;
        }
        User u = userMap.get(userId);
        if(qty * stockMap.get(stockId).price > u.balance) {
            System.out.println("Insufficient balance");
            return;
        }
        if(u.stockHoldings.containsKey(stockId)) {
            Holding h = u.stockHoldings.get(stockId);
            h.qty += qty;
            u.balance -= qty * stockMap.get(stockId).price;
        } else {
            Holding h = new Holding(stockMap.get(stockId), qty);
            u.stockHoldings.put(stockId, h);
            u.balance -= qty * stockMap.get(stockId).price;
        }
        System.out.println("User "+userId+" bought "+qty+" of "+stockMap.get(stockId).name+" stocks, Remaining balance "+u.balance);
        u.transactionHistory.add(new Transaction(stockId, "BUY", qty));
    }
    public void sellStock(String userId, String stockId, int qty) {
        if(!userMap.containsKey(userId)) {
            System.out.println("Invalid userId");
            return;
        }
        User u = userMap.get(userId);
        if(u.stockHoldings.containsKey(stockId)) {
            Holding h = u.stockHoldings.get(stockId);
            if(h.qty > qty) h.qty -= qty;
            else if(h.qty == qty)u.stockHoldings.remove(stockId);
            else {
                System.out.println("Insufficient stock to sell " + qty + " stocks!");
                return;
            }
            u.balance += qty * stockMap.get(stockId).price;
        } else {
            System.out.println("User does not have this stock!");
            return;
        }
        System.out.println("User "+userId+" sold "+qty+" of "+stockMap.get(stockId).name+" stocks, New Balance: "+u.balance);

        u.transactionHistory.add(new Transaction(stockId, "SELL", qty));
    }
    public void showTransactionHistory(String userId) {
        if(!userMap.containsKey(userId)) {
            System.out.println("Invalid userId");
            return;
        }
        User u = userMap.get(userId);
        u.showTransactionHistory();
    }
}
public class Main {
    public static void main(String[] args) {
        StockBrokerageSystem sb = new StockBrokerageSystem();
        sb.createUser("1", "Alex", 10000);
        sb.addStock("1", "TCS", 100.0);
        sb.addStock("2", "Amazon", 120.0);
        sb.addStock("3", "walmart", 110.0);
        sb.addStock("4", "myntra", 90.0);
        sb.buyStock("1", "1", 5);
        sb.buyStock("1","2", 25);
        sb.buyStock("1","4", 15);
        sb.sellStock("1","1", 5);
        sb.sellStock("1","2", 5);
        sb.sellStock("1","3", 50);
        sb.showTransactionHistory("1");
    }
}
