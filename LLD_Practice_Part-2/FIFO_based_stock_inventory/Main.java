package lld_practice.FIFO_based_stock_inventory;

import java.util.LinkedList;
import java.util.Queue;

class Transaction {
    String id;
    int qty;
    Double pricePerUnit;
    Transaction(String id, int qty, Double pricePerUnit) {
        this.id = id;
        this.qty = qty;
        this.pricePerUnit = pricePerUnit;
    }
}
class StockInventory {
    Queue<Transaction> inventory = new LinkedList<>();
    Double totalValue = 0.0;
    public void buy(String id, int qty, Double price) {
        Transaction t = new Transaction(id, qty, price);
        inventory.add(t);
    }
    public void sell(int qty, Double sellPricePerUnit) {
        int remaining = qty;
        while(remaining > 0 && !inventory.isEmpty()) {
            Transaction t = inventory.peek();
            int sellQty = Math.min(remaining, t.qty);

            totalValue += sellQty * (sellPricePerUnit - t.pricePerUnit);

            t.qty -= sellQty;
            remaining -= sellQty;

            if(t.qty == 0) inventory.remove();
        }
        if(remaining > 0) System.out.println("Insuficient stock to sell" + qty + "stocks !");
    }
    public Double getTotalValue() {
        System.out.println("Your profit/loss is: "+totalValue);
        return totalValue;
    }
}
public class Main {
    public static void main(String[] args) {
        StockInventory si = new StockInventory();
        si.buy("1", 5, 100.0);
        si.buy("2", 10, 75.0);
        si.getTotalValue();
        si.sell(15, 120.0);
        si.sell(20, 110.0);
        si.getTotalValue();
        si.buy("5", 25, 120.0);
        si.sell( 10, 90.0);
        Double value = si.getTotalValue();
    }
}
