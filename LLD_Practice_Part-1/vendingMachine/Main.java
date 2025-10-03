package vendingMachine;
import java.util.*;

class Item {
    static int counter = 1;
    private int id, price;
    private String name;
    public Item (String name, int price) {
        this.name = name; 
        this.price = price;
    }
    public String getName() { return name; }
    public int getPrice() { return price; }
}
enum Coin {
    Rs_1,
    Rs_2,
    Rs_5,
    Rs_10,
    Rs_20,
}
class Inventory {
    Map<Item, Integer> stock = new HashMap<>();

    public boolean isAvailable(Item item) { return stock.containsKey(item) && stock.get(item) > 0; }

    public void addItem(Item item, int quantity) { 
        if(stock.containsKey(item))stock.put(item, stock.get(item) + quantity);
        else stock.put(item, quantity);
    }
    public void update(Item item, int quantity) { 
        stock.put(item, stock.get(item) - quantity);
    }
    public void displayAvailableItems() {
        for(Map.Entry<Item, Integer> entry : stock.entrySet()){
            Item item = entry.getKey();
            int quantity = entry.getValue();
            String name = item.getName();
            int price = item.getPrice();
            if(quantity > 0)System.out.println("Item Name: "+name+", price: Rs "+price+", Quantity: "+quantity);
        }
    }
}
class VendingMachine {
    Inventory inventory = new Inventory();
    int totalCoins = 0, totalPrice = 0;
    List<Coin> coins = new ArrayList<>();
    
    public void addItem(Item item, int quantity) { inventory.addItem(item, quantity);}

    public void displayAvailableItems() {
        inventory.displayAvailableItems();
    }
    public void insertCoins(Coin coin){
        coins.add(coin);
        switch (coin) {
            case Rs_1: totalCoins += 1; break;
            case Rs_2: totalCoins += 2; break;
            case Rs_5: totalCoins += 5; break;
            case Rs_10: totalCoins += 10; break;
            case Rs_20: totalCoins += 20; break;
        }  
    }
    public void selectItem(Item item, int quantity) {
        if (!inventory.isAvailable(item) || inventory.stock.get(item) < quantity) {
            System.out.println("Item not available in requested quantity.");
            return;
        }
        totalPrice += item.getPrice()*quantity;
        if (totalCoins < totalPrice) {
            System.out.println("Insufficient balance. Please insert more coins.");
            return;
        }
        System.out.println("Purchased " + quantity + " x " + item.getName() + " for Rs " + item.getPrice() * quantity);

        inventory.update(item, quantity);
    }
    public List<Coin> returnChange() {
        List<Coin> returnCoins = new ArrayList<>();
        if(totalCoins == totalPrice)System.out.println("No change remaining");
        else if(totalCoins > totalPrice){
            int remaining = totalCoins - totalPrice;
            while (remaining >= 20) { remaining -= 20; returnCoins.add(Coin.Rs_20); }
            while (remaining >= 10) { remaining -= 10; returnCoins.add(Coin.Rs_10); }
            while (remaining >= 5)  { remaining -= 5;  returnCoins.add(Coin.Rs_5); }
            while (remaining >= 2)  { remaining -= 2;  returnCoins.add(Coin.Rs_2); }
            while (remaining >= 1)  { remaining -= 1;  returnCoins.add(Coin.Rs_1); }
        }
        return returnCoins;
    }
    public List<Coin> cancelTransaction() { 
        List<Coin> returned = new ArrayList<>(coins);
        coins.clear();
        totalCoins = 0;
        totalPrice = 0;
        return returned; 
    }
}
public class Main {
    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine();
        Item i1 = new Item("coca cola", 40);
        Item i2 = new Item("Thumps Up", 35);
        Item i3 = new Item("Green Lays", 10);
        Item i4 = new Item("Blue Lays", 20);
        Item i5 = new Item("5 Star", 5);
        Item i6 = new Item("Milky Bar", 10);

        vendingMachine.addItem(i1, 10);vendingMachine.addItem(i2, 10);vendingMachine.addItem(i3, 8);vendingMachine.addItem(i4, 2);vendingMachine.addItem(i5, 1);vendingMachine.addItem(i6, 5);
        vendingMachine.displayAvailableItems();

        vendingMachine.insertCoins(Coin.Rs_20);
        vendingMachine.insertCoins(Coin.Rs_20);
        vendingMachine.insertCoins(Coin.Rs_20);
        vendingMachine.insertCoins(Coin.Rs_10);
        vendingMachine.insertCoins(Coin.Rs_10);
        vendingMachine.insertCoins(Coin.Rs_5);
        vendingMachine.insertCoins(Coin.Rs_2);

        vendingMachine.selectItem(i1, 1);
        vendingMachine.selectItem(i3, 2);
        vendingMachine.selectItem(i5, 1);



        List<Coin> coins = vendingMachine.returnChange();
        for(Coin coin : coins)System.out.println(coin);

    }
}
