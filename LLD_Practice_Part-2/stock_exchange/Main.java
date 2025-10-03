package lld_practice.stock_exchange;

import java.util.*;

class User {
    String id, name;
    User(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
class Order {
    String id, time, stock, type;
    double price;
    int qty;
    Order(String id, String time, String stock, String type, double price, int qty) {
        this.id = id;
        this.time = time;
        this.stock = stock;
        this.type = type;
        this.price = price;
        this.qty = qty;
    }
}
class StockExchange {
    HashMap<String, User> userMap = new HashMap<>();
    PriorityQueue<Order> buyQueue = new PriorityQueue<>(
            (a, b) -> a.price == b.price ? a.time.compareTo(b.time) : Double.compare(b.price, a.price));
    PriorityQueue<Order> sellQueue = new PriorityQueue<>(
            (a, b) -> a.price == b.price ? a.time.compareTo(b.time) : Double.compare(a.price, b.price));

    public void createUser(String id, String name) {
        User user = new User(id, name);
        userMap.put(id, user);
        System.out.println("User Added! : "+name);
    }
    public void placeOrder(String orderId, String time, String stock, String type, double price, int qty) {
        Order order = new Order(orderId, time, stock, type, price, qty);
        if(type.equalsIgnoreCase("BUY")){
            buyStock(order);
        } else if(type.equalsIgnoreCase("SELL")){
            sellStock(order);
        }
    }
    public void buyStock(Order order) {
        buyQueue.add(order);
        List<Order> temp = new ArrayList<>();
        while(!sellQueue.isEmpty()) {
            Order sellOrder = sellQueue.peek();
            if(!order.stock.equalsIgnoreCase(sellOrder.stock)) {
                temp.add(sellQueue.remove()); continue;
            }
            if(order.price >= sellOrder.price) {
                if(order.qty > sellOrder.qty){
                    createReceipt(order.id, sellOrder.price, sellOrder.qty, sellOrder.id);
                    order.qty -= sellOrder.qty;
                    sellQueue.remove();
                } else if(order.qty == sellOrder.qty) {
                    createReceipt(order.id, sellOrder.price, sellOrder.qty, sellOrder.id);
                    sellQueue.remove();
                    buyQueue.remove(order);
                    break;
                } else {
                    createReceipt(order.id, sellOrder.price, order.qty, sellOrder.id);
                    sellOrder.qty -= order.qty;
                    buyQueue.remove();
                    break;
                }
            } else {
                temp.add(sellQueue.remove());
            }
        }
        sellQueue.addAll(temp);
    }
    public void sellStock(Order sellOrder) {
        sellQueue.add(sellOrder);
        List<Order> temp = new ArrayList<>();
        while(!buyQueue.isEmpty()) {
            Order buyOrder = buyQueue.peek();
            if(!sellOrder.stock.equalsIgnoreCase(buyOrder.stock)) {
                temp.add(buyQueue.remove()); continue;
            }
            if(sellOrder.price <= buyOrder.price) {
                if(sellOrder.qty < buyOrder.qty){
                    createReceipt(buyOrder.id, sellOrder.price, sellOrder.qty, sellOrder.id);
                    buyOrder.qty -= sellOrder.qty;
                    sellQueue.remove();
                    break;
                } else if(sellOrder.qty == buyOrder.qty) {
                    createReceipt(buyOrder.id, sellOrder.price, buyOrder.qty, sellOrder.id);
                    sellQueue.remove();
                    buyQueue.remove(buyOrder);
                    break;
                } else {
                    createReceipt(buyOrder.id, sellOrder.price, buyOrder.qty, sellOrder.id);
                    sellOrder.qty -= buyOrder.qty;
                    buyQueue.remove();
                }
            } else {
                temp.add(buyQueue.remove());
            }
        }
        buyQueue.addAll(temp);
    }
    public void createReceipt(String buyOrderId, double price, int qty, String sellOrderId) {
        System.out.println(buyOrderId+" "+price+" "+qty+" "+sellOrderId);
    }
}
public class Main {
    public static void main(String[] args) {
        StockExchange se = new StockExchange();
        se.createUser("1", "Alex");
        se.placeOrder("#1", "09:45", "BAC", "SELL", 240.12, 100);
        se.placeOrder("#2", "09:46", "BAC", "SELL", 237.45, 90);
        se.placeOrder("#3", "09:47", "BAC", "BUY", 238.10, 110);
        se.placeOrder("#4", "09:48", "BAC", "BUY", 237.80, 10);
        se.placeOrder("#5", "09:49", "BAC", "BUY", 237.80, 40);
        se.placeOrder("#6", "09:50", "BAC", "SELL", 236.00, 50);
        se.placeOrder("#1", "09:30", "AAPL", "BUY", 175.00, 100);
        se.placeOrder("#2", "09:31", "AAPL", "SELL", 176.00, 50);
        se.placeOrder("#3", "09:32", "GOOG", "BUY", 2850.00, 10);
        se.placeOrder("#4", "09:33", "GOOG", "SELL", 2845.00, 5);
        se.placeOrder("#5", "09:34", "MSFT", "BUY", 310.00, 200);
        se.placeOrder("#6", "09:35", "MSFT", "SELL", 309.50, 150);
        se.placeOrder("#7", "09:36", "BAC", "BUY", 235.50, 100);
        se.placeOrder("#8", "09:37", "BAC", "SELL", 236.00, 50);
        se.placeOrder("#9", "09:38", "AAPL", "SELL", 174.50, 70);   // matches with BUY at 175.00
        se.placeOrder("#10", "09:39", "GOOG", "BUY", 2855.00, 8);   // matches with SELL at 2845.00
        se.placeOrder("#11", "09:40", "BAC", "BUY", 236.00, 60);    // matches with SELL at 236.00
        se.placeOrder("#12", "09:41", "MSFT", "SELL", 309.00, 100); // matches with BUY at 310.00

    }
}
