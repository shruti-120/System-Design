package shoppingCart;
import java.util.*;
class Item {
    String name; 
    double price;
    int quantity;
    public Item(String name, double price, int quantity){
        this.name = name; 
        this.price = price;
        this.quantity = quantity;
    }
}
class Cart {
    HashSet<Item> cartItems = new HashSet<>();
    public void addItems(Item item){
        cartItems.add(item);
    }
    public void removeItems(Item item){
        cartItems.remove(item);
    }
    public double viewTotalPrice(){
        double total = 0.0;
        for(Item item: cartItems){
            total += item.price * item.quantity;
        }
        return total;
    }
}
public class Main {
    public static void main(String[] args){
        Item notebook = new Item("notebook", 30.0, 2);
        Item stapler = new Item("stapler", 60.0, 1);
        Item pen = new Item("pen", 10.2, 1);
        Cart cart = new Cart();
        cart.addItems(notebook);
        cart.addItems(stapler);
        cart.addItems(pen);
        double totalPrice = cart.viewTotalPrice();
        System.out.println("Cart Total : "+totalPrice);
    }
    
}
