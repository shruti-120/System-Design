package lld_practice.watchlist_system;

import java.util.*;

class User {
    String id, name;
    Watchlist watchlist;
    User(String id, String name, Watchlist watchlist) {
        this.id = id;
        this.name = name;
        this.watchlist = watchlist;
    }
}
class Watchlist {
    HashMap<String, Stock> stockMap = new HashMap<>();
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
class Watchlist_System {
    HashMap<String, User> userMap = new HashMap<>();
    HashMap<String, Stock> stocks = new HashMap<>();

    public void createUser(String id, String name) {
        User user = new User(id, name, new Watchlist());
        userMap.put(id, user);
        System.out.println("User Added !");

    }
    public void addStockToSystem(String id, String name, Double price){
        Stock stock = new Stock(id, name, price);
        stocks.put(id, stock);
        System.out.println("New Stock Added!");
    }

    public void addStock(String userId, String stockId) {
        User u = userMap.get(userId);
        Stock s = stocks.get(stockId);
        u.watchlist.stockMap.put(stockId, s);
        System.out.println("Stock Added to user watchlist!");

    }
    public void removeStock(String userId, String stockId) {
        User u = userMap.get(userId);
        Stock s = stocks.get(stockId);
        u.watchlist.stockMap.remove(stockId);
        System.out.println("Stock Removed from user watchlist!");
    }
    List<Stock> viewWatchlist(String userId){
        System.out.println("User's WatchList --> ");

        List<Stock> watchlist = new ArrayList<>();
        Watchlist w = userMap.get(userId).watchlist;
        for(Map.Entry<String, Stock> e : w.stockMap.entrySet()) {
            watchlist.add(e.getValue());
        }
        return watchlist;
    }
    List<Stock> viewWatchlist(String userId, int topN) {
        System.out.println("Top "+ topN+" Stocks!");

        List<Stock> watchlist = new ArrayList<>();
        PriorityQueue<Stock> pq = new PriorityQueue<>((a, b) -> Double.compare(a.price, b.price));
        Watchlist w = userMap.get(userId).watchlist;
        for(Map.Entry<String, Stock> e : w.stockMap.entrySet()) {
            pq.add(e.getValue());
            if(pq.size() > topN) pq.remove();
        }
        while(!pq.isEmpty()) watchlist.add(pq.remove());
        watchlist.sort((a, b) -> Double.compare(b.price, a.price));
        return watchlist;
    }
}
public class Main {
    public static void main(String[] args) {
        Watchlist_System ws = new Watchlist_System();
        ws.createUser("1", "Alex");
        ws.addStockToSystem("1", "TCS", 75.0);
        ws.addStockToSystem("2", "wipro", 100.0);
        ws.addStockToSystem("3", "Amazon", 130.0);
        ws.addStockToSystem("4", "Walmart", 140.50);
        ws.addStockToSystem("5", "Google", 200.0);
        ws.addStock("1", "1");
        ws.addStock("1", "2");
        ws.addStock("1", "3");
        ws.addStock("1", "4");
        ws.addStock("1", "5");
        List<Stock> stocks = ws.viewWatchlist("1");
        for(Stock s: stocks) {
            System.out.println("Name: "+s.name+" Price: "+s.price);
        }
        stocks.clear();
        List<Stock> topN = ws.viewWatchlist("1", 3);
        for(Stock s: topN) {
            System.out.println("Name: "+s.name+" Price: "+s.price);
        }
        topN.clear();
        ws.removeStock("1", "3");
        stocks = ws.viewWatchlist("1");
        for(Stock s: stocks) {
            System.out.println("Name: "+s.name+" Price: "+s.price);
        }
        topN = ws.viewWatchlist("1", 3);
        for(Stock s: topN) {
            System.out.println("Name: "+s.name+" Price: "+s.price);
        }
        topN.clear();
    }
}
