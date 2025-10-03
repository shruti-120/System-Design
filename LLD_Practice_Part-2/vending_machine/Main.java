package lld_practice.vending_machine;

import java.util.HashMap;

class Item {
    static int cnt = 0;
    int id;
    String name;
    Double price;
    public Item(String name, Double price) {
        this.id = cnt++;
        this.name = name;
        this.price = price;
    }
}
class Inventory {
    HashMap<Integer, Item> itemMap = new HashMap<>();
    HashMap<Integer, Integer> stocks = new HashMap<>();

    public void addItem(int itemId, Item item, int qty){
        itemMap.put(itemId, item);
        stocks.put(itemId, stocks.getOrDefault(itemId, 0)+ qty);
    }
    public void updateStocks(int itemId) {
        stocks.put(itemId, stocks.get(itemId) - 1);
    }
    public boolean isAvailable(int itemId) {
        return stocks.getOrDefault(itemId, 0) > 0;
    }
}
enum Coin {
    ONE(1), TWO(2), FIVE(5), TEN(10), TWENTY(20);
    final int value;
    Coin(int value) { this.value = value; }
}
class VendingMachine {
    Inventory inventory;
    public VendingMachine(Inventory inventory) {
        this.inventory = inventory;
    }
    int balance = 0;
    double selectedItemsPrice = 0.0;
    public void insertCoin(Coin coin) {
        balance += coin.value;
    }
    public void selectItem(int itemId) {
        if(!inventory.isAvailable(itemId)) {
            System.out.println("Item not available" + itemId);
            return;
        }
        Item item = inventory.itemMap.get(itemId);
        if (balance < item.price) {
            System.out.println("Insufficient balance. Please insert more coins.");
            return;
        }

        inventory.updateStocks(itemId);
        balance -= item.price;
        System.out.println("Dispensed: " + item.name);
    }
    public int getChange() {
        int change = balance;
        balance = 0;
        return change;
    }
}
public class Main {
    public static void main(String[] args) {
        Inventory inv = new Inventory();
        inv.addItem(1, new Item("Coke", 15.0), 5);
        inv.addItem(2, new Item("Pepsi", 12.0), 3);
        VendingMachine vm = new VendingMachine(inv);
        vm.insertCoin(Coin.TEN);
        vm.insertCoin(Coin.TEN);
        vm.selectItem(1); // Coke
        System.out.println("Change returned: " + vm.getChange());
    }
}
