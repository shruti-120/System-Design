package machine_coding;

import java.util.HashMap;
import java.util.Map;

enum TradeType { BUY, SELL }

class Holding {
    Stock stock;
    int qty;
    Double avgBuyPrice = 0.0;
    Holding(Stock stock, int qty) {
        this.stock = stock;
        this.qty = qty;
    }
}
class Stock {
    String ticker;
    Double price;
    Stock(String ticker, Double price) {
        this.ticker = ticker;
        this.price = price;
    }
}
class PortfolioTracker {
    Map<String, Stock> stockMap = new HashMap<>();
    Map<String, Holding> stockHoldings = new HashMap<>();
    //Add Trade
    public void addTrade(String ticker, Double price, int sharesQty, TradeType type) {
        if(sharesQty <= 0) {
            System.out.println("Invalid shares quantity input!");
            return;
        }
        Stock s = new Stock(ticker, price);
        stockMap.put(ticker,  s);
        if(type == TradeType.BUY) {
            buyStock(s, sharesQty, price);

        } else if (type == TradeType.SELL) {
            sellStock(s, sharesQty, price);
        }
    }
    public void buyStock(Stock s, int sharesQty, Double price) {
        if(stockHoldings.containsKey(s.ticker)) {
            Holding h = stockHoldings.get(s.ticker);
            int oldQty = h.qty;
            h.qty = h.qty + sharesQty;
            h.avgBuyPrice = updateAvgPrice(h.avgBuyPrice, oldQty, sharesQty, price);
            stockHoldings.put(s.ticker, h);
        }
        else {
            Holding h = new Holding(s, sharesQty);
            h.avgBuyPrice = price;
            stockHoldings.put(s.ticker, h);
        }
    }
    public void sellStock(Stock s, int sharesQty, Double price) {
        if (stockHoldings.containsKey(s.ticker)) {
            Holding h = stockHoldings.get(s.ticker);
            if(h.qty < sharesQty) {
                System.out.println("You do not have enough stocks!");
                return;
            }
            h.qty = h.qty - sharesQty;
            stockHoldings.put(s.ticker, h);
        } else {
            System.out.println("You do have this stock!");
        }
    }
    //helper
    public Double updateAvgPrice(Double oldPrice, int oldQty, int addedQty, Double price){
        int newQty = oldQty + addedQty;
        Double newAvgBuyPrice = ((oldQty * oldPrice) + (addedQty * price)) / (newQty);
        return  newAvgBuyPrice;
    }
    //fetch holdings
    public void fetchHoldings() {
        if(stockHoldings.isEmpty()) {
            System.out.println("You do not have any stocks currently !");
            return;
        }
        for (Map.Entry<String, Holding> e: stockHoldings.entrySet()) {
            Holding h = e.getValue();
            String hName = e.getKey();
            System.out.println(hName+": "+h.qty+"@ Rs."+h.avgBuyPrice);
        }
    }
    //fetch returns
    public Double fetchReturns() {
        Double sum = 0.0;
        if(stockHoldings.isEmpty()) {
            System.out.println("You do not have any stocks currently !");
            return sum;
        }
        for (Map.Entry<String, Holding> e: stockHoldings.entrySet()) {
            Holding h = e.getValue();
            sum += (h.avgBuyPrice + 100 - h.avgBuyPrice) * h.qty;
        }
        return sum;
    }
}
public class Main {
    public static void main(String[] args) {
        PortfolioTracker p = new PortfolioTracker();
        p.addTrade("TCS", 1833.45, 5, TradeType.BUY);
        p.addTrade("Wipro", 319.25 , 10, TradeType.BUY);
        p.addTrade("Godrej", 535.00 , 2, TradeType.BUY);
        p.addTrade("Godrej", 400.00 , 5, TradeType.BUY);
        p.addTrade("TCS", 1100.0, 100, TradeType.SELL);
        p.addTrade("Walmart", 1100.0, 100, TradeType.BUY);
        p.fetchHoldings();
        Double val = p.fetchReturns();
        System.out.println("Total Returns: "+val);
//        System.out.println();
//        p.addTrade("TCS", 1200.0, 8, TradeType.BUY);
//        p.fetchHoldings();
    }
}
