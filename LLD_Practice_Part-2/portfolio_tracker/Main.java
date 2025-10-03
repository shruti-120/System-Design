package lld_practice.portfolio_tracker;

import java.util.HashMap;
import java.util.Map;

class User {
    String id, name;
    Portfolio portfolio;
    public User(String id, String name, Portfolio portfolio) {
        this.name = name;
        this.id = id;
        this.portfolio = portfolio;
    }
}
class Portfolio {
    HashMap<String, Holding> stockHoldings = new HashMap<>();
}
class Holding {
    Stock stock;
    int qty;
    Double avgBuyPrice;
    public Holding(Stock stock, int qty, double avgBuyPrice) {
        this.stock = stock;
        this.qty = qty;
        this.avgBuyPrice = avgBuyPrice;
    }
}
class Stock {
    String id, companyName;
    public Stock(String id, String companyName) {
        this.id = id;
        this.companyName = companyName;
    }
}
class Portfolio_Tracker {
    HashMap<String, User> userMap = new HashMap<>();
    HashMap<String, Stock> stockMap = new HashMap<>();

    public void createUser(String id, String name) {
        User user = new User(id, name, new Portfolio());
        userMap.put(id, user);
        System.out.println("User created! - "+name+" "+"id "+ id);
    }

    public void addStock(String id, String companyName) {
        Stock stock = new Stock(id, companyName);
        stockMap.put(id, stock);
        System.out.println("Stock added "+id+" name "+companyName);

    }

    public void buyStock(String userId, String stockId, int qty, double price) {
        Portfolio p = userMap.get(userId).portfolio;
        if(!p.stockHoldings.containsKey(stockId)) {
            Holding h = new Holding(stockMap.get(stockId), qty, price);
            p.stockHoldings.put(stockId, h);
        } else {
            Holding h = p.stockHoldings.get(stockId);
            double totalCost = h.avgBuyPrice * h.qty + price * qty;
            int newQty = h.qty + qty;
            h.avgBuyPrice = totalCost / newQty;
            h.qty = newQty;
        }
        System.out.println("stock bought!"+ stockId +" Quantity "+qty);
    }

    public void sellStock(String userId, String stockId, int qty) {
        Portfolio p = userMap.get(userId).portfolio;
        if(!p.stockHoldings.containsKey(stockId)) {
            System.out.println("User do not have this stock!");
        } else {
            Holding h = p.stockHoldings.get(stockId);
            h.qty = h.qty - qty;
            if (h.qty == 0) {
                p.stockHoldings.remove(stockId); // remove stock entirely if none left
            }
        }
        System.out.println("Stock Sold! "+stockId+" Quantity "+qty);
    }

    public void getCurrentPortfolio(String userId) {
        System.out.println("Portfolio Info: ");

        Portfolio p = userMap.get(userId).portfolio;
        for(Map.Entry<String, Holding> e: p.stockHoldings.entrySet()) {
            Holding h = e.getValue();
            System.out.println(h.stock.companyName+" "+h.qty+" "+h.avgBuyPrice);
        }
    }

    public void getCurrentPrice(String stockId, String userId) {
        Portfolio p = userMap.get(userId).portfolio;
        System.out.println("Current Price: "+stockId+" "+p.stockHoldings.get(stockId).avgBuyPrice);
    }

    public void getInvestmentAmount(String userId) {
        Portfolio p = userMap.get(userId).portfolio;
        Double totalAmount = 0.0;
        for(Map.Entry<String, Holding> e: p.stockHoldings.entrySet()) {
            Holding h = e.getValue();
            totalAmount += h.qty * h.avgBuyPrice;
        }
        System.out.println("Total Investment" + totalAmount);
    }
}
public class Main {
    public static void main(String[] args) {
        Portfolio_Tracker pt= new Portfolio_Tracker();
        pt.createUser("1", "Alex");
        pt.addStock("1", "Amazon");
        pt.addStock("2", "google");
        pt.addStock("3", "walmart");
        pt.buyStock("1", "1", 2, 100);
        pt.buyStock("1", "2", 3, 120);
        pt.getCurrentPrice("2", "1");
        pt.getCurrentPortfolio("1");
        pt.getInvestmentAmount("1");
        pt.sellStock("1", "1", 1);
        pt.getInvestmentAmount("1");
        pt.getCurrentPortfolio("1");
        pt.getCurrentPrice("1", "1");
    }
}
