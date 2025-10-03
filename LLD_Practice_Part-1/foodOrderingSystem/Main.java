package foodOrderingSystem;
import java.util.*;

class User {
    private String name;
    private static int counter = 1;
    private int id;
    public User(String name){
        this.id = counter++;
        this.name = name;
    }
}
class Restaurant {
    private int id;
    private static int counter = 1;
    private String name;
    private Set<MenuItem> menu = new HashSet<>();
    public Restaurant(String name){
        this.id = counter++;
        this.name = name;
    }
    public Set<MenuItem> getMenu() { return menu; }
    public void addMenuItem(MenuItem item) { this.menu.add(item); }
}
class MenuItem {
    private String name;
    private double price;
    public MenuItem(String name, double price){
        this.name = name; 
        this.price = price;
    }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
class Order {
    private User customer;
    private Restaurant restaurant;
    private double totalPrice = 0.0;
    private Map<MenuItem, Integer> itemsOrdered = new HashMap<>();
    public Order(User customer, Restaurant restaurant){
        this.customer = customer;
        this.restaurant = restaurant;
    }
    public Map<MenuItem, Integer> getItemsOrdered() { return itemsOrdered; }
    public void addItem(MenuItem item, int quantity) { 
        if(!restaurant.getMenu().contains(item)){
            System.out.println("Not in menu order something else!");return;
        }
        itemsOrdered.put(item, itemsOrdered.getOrDefault(item, 0)+quantity);
    }
    public void removeItem(MenuItem item, int quantity) { 
        if(!itemsOrdered.containsKey(item)){
            System.out.println("order does not contain this item");
        }
        else if(itemsOrdered.get(item) < quantity)itemsOrdered.remove(item);
        else {
            itemsOrdered.put(item, itemsOrdered.getOrDefault(item, 0)-quantity);
            if(itemsOrdered.get(item) == 0)itemsOrdered.remove(item);
        }
    }
    public double getOrderTotal() {
        for(Map.Entry<MenuItem, Integer> entry : itemsOrdered.entrySet()){
            MenuItem item = entry.getKey(); int quantity = entry.getValue();
            System.out.println(item.getName()+" - "+quantity);
            totalPrice += item.getPrice() * quantity;
        }
        return totalPrice;
    }
}
class Zomato {
    Order order;

    public void showMenu(Restaurant restaurant) { 
        for(MenuItem item: restaurant.getMenu()){
            System.out.println(item.getName()+" - "+item.getPrice());
        }
    }
    public void takeOrder(User user, Restaurant restaurant){
        order = new Order(user, restaurant);
    }
    public void addItemToOrder(MenuItem item, int quantity){
        order.addItem(item, quantity);
    }
    public void removeItemToOrder(MenuItem item, int quantity){
        order.removeItem(item, quantity);
    }
    public void showOrderTotal() { System.out.println(order.getOrderTotal()); }
    
}

public class Main {
    public static void main(String[] args) {
        Zomato zomato = new Zomato();
        User u1 = new User("Shruti");
        Restaurant r = new Restaurant("Pizza Bakery");
        MenuItem m1 = new MenuItem("Margarita", 480);
        MenuItem m2 = new MenuItem("diavolia", 510);
        r.addMenuItem(m1);
        r.addMenuItem(m2);
        zomato.showMenu(r);
        zomato.takeOrder(u1, r);
        zomato.addItemToOrder(m1, 1);
        zomato.addItemToOrder(m2, 2);
        zomato.showOrderTotal();
    }
}
