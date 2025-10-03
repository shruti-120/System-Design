package lld_practice.portfoio_management_system;

import javax.sound.sampled.Port;
import java.util.*;

class User {
    String id, name;
    Portfolio portfolio;
    User(String id, String name, Portfolio portfolio) {
        this.id = id;
        this.name = name;
        this.portfolio = portfolio;
    }
}
class Stock {
    String id, name;
    Double price;
    Stock(String id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
class Holding {
    int qty;
    Stock stock;
    Holding(int qty, Stock stock) {
        this.qty = qty;
        this.stock = stock;
    }
    Double avgPrice;
}
class Portfolio {
    HashMap<String, Holding> stockHoldings = new HashMap<>();
    Double value;
    Portfolio(Double value) {
        this.value = value;
    }
}
class Portfolio_System {
    HashMap<String, User> userMap = new HashMap<>();
    HashMap<String, Stock> stockMap = new HashMap<>();

    public void createUser(String id, String name) {
        User user = new User(id, name, new Portfolio(null));
        userMap.put(id, user);
        System.out.println("User Added! - "+name);

    }
    public void addStockToSystem(String id, String name, Double price) {
        Stock s = new Stock(id, name, price);
        stockMap.put(id, s);
        System.out.println("Stock Added! - "+name);

    }
    public void buyStock(String userId, String stockId, int qty) {
        Portfolio p = userMap.get(userId).portfolio;
        Stock s = stockMap.get(stockId);
        if(p.stockHoldings.containsKey(stockId)) {
            Holding h = p.stockHoldings.get(stockId);
            updateAvgPrice(h, h.qty, qty, s.price);
            h.qty = h.qty + qty;
            p.stockHoldings.put(stockId, h);
        } else {
            p.stockHoldings.put(stockId, new Holding(qty, stockMap.get(stockId)));
            Holding h = p.stockHoldings.get(stockId);
            h.avgPrice = s.price;
        }
        System.out.println("Stock bought! "+stockId+" qty - "+qty);
        double value = 0;
        for(Holding h : p.stockHoldings.values()) {
            value += h.qty * h.stock.price; // market value
        }
        p.value = value;
    }
    public void updateAvgPrice(Holding h, int oldQty, int newQty, Double price) {
        h.avgPrice = ((h.avgPrice * oldQty) + (price * newQty)) / (oldQty + newQty);
    }
    public void sellStock(String userId, String stockId, int qty) {
        Portfolio p = userMap.get(userId).portfolio;
        Stock s = stockMap.get(stockId);
        if(p.stockHoldings.containsKey(stockId)) {
            Holding h = p.stockHoldings.get(stockId);
            if(h.qty - qty >= 0) h.qty = h.qty - qty;
            else {
                System.out.println("User do not have sufficient stocks!");
                return;
            }
            if (h.qty == 0) {
                p.stockHoldings.remove(stockId);
            }
            p.stockHoldings.put(stockId, h);
            double value = 0;
            for(Holding holding : p.stockHoldings.values()) {
                value += holding.qty * holding.stock.price; // market value
            }
            p.value = value;
            System.out.println("Stock sold!" + stockId);
        } else {
            System.out.println("User do not have any of this stock");
        }
    }
    public List<Portfolio> fetchTopNPortfolio(int n) {
        PriorityQueue<Portfolio> pq = new PriorityQueue<>((a, b) -> Double.compare(a.value, b.value));
        for(Map.Entry<String, User> e: userMap.entrySet()) {
            User u = e.getValue();
            pq.add(u.portfolio);
            if(pq.size() > n) pq.remove();
        }
        List<Portfolio> portfolios = new ArrayList<>();
        while(!pq.isEmpty()) portfolios.add(pq.remove());
        portfolios.sort((a, b) -> Double.compare(b.value, a.value));
        return portfolios;
    }
    public Portfolio fetchPortfolio(String userId) {
        Portfolio p = userMap.get(userId).portfolio;
        return p;
    }
}
public class Main {
    public static void main(String[] args) {
        Portfolio_System ps = new Portfolio_System();
        ps.createUser("1", "alex");
        ps.createUser("2", "Max");
        ps.addStockToSystem("1", "TCS", 100.0);
        ps.addStockToSystem("2", "Meesho", 120.00);
        ps.addStockToSystem("3", "flipkart", 230.00);
        ps.addStockToSystem("4", "Amazon", 420.00);
        ps.buyStock("1", "1", 5);
        ps.buyStock("1", "2", 10);
        ps.buyStock("1", "3", 20);
        ps.buyStock("1", "4", 5);
        ps.buyStock("2", "1", 10);
        ps.buyStock("2", "4", 15);
        ps.buyStock("2", "3", 25);
        Portfolio p1 = ps.fetchPortfolio("1");
        System.out.println("Portfolio Value: "+p1.value);
        for(Map.Entry<String, Holding> e: p1.stockHoldings.entrySet()) {
            Holding h = e.getValue();
            System.out.println("Stock Id: "+h.stock.id+" Stock Name: "+h.stock.name+" Qty: "+h.qty);
        }
        Portfolio p2 = ps.fetchPortfolio("2");
        System.out.println("Portfolio Value: "+p2.value);
        for(Map.Entry<String, Holding> e: p2.stockHoldings.entrySet()) {
            Holding h = e.getValue();
            System.out.println("Stock Id: "+h.stock.id+" Stock Name: "+h.stock.name+" Qty: "+h.qty);
        }
        List<Portfolio> list = ps.fetchTopNPortfolio(2);
        System.out.println("Top Portfolios: ");

        for(Portfolio p: list) {
            System.out.println(p.value);
        }
        ps.sellStock("1", "2", 5);
        Portfolio p3 = ps.fetchPortfolio("1");
        System.out.println("Portfolio Value: "+p3.value);
        for(Map.Entry<String, Holding> e: p3.stockHoldings.entrySet()) {
            Holding h = e.getValue();
            System.out.println("Stock Id: "+h.stock.id+" Stock Name: "+h.stock.name+" Qty: "+h.qty);
        }
    }
}
