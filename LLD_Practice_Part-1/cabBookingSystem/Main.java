package cabBookingSystem;
import java.util.*;

interface Observer {
    void update(String message);
}
interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String message);
}
class User implements Observer{
    String name;
    Location currLocation;
    Ride currRide;
    List<Ride> rideHistory = new ArrayList<>();
    public User(String name, Location currLocation){
        this.name = name;
        this.currLocation = currLocation;
        this.currRide = null;
    }
    public void addRideToHistory(Ride ride){ this.rideHistory.add(ride); }

    public boolean bookRide(Ride ride){
        if(this.currRide != null)return false;
        this.currRide = ride;
        return true;
    }
    public boolean cancelRide(Ride ride){
        if(this.currRide == null)return false;
        this.currRide = null;
        return true;
    }
    public void update(String message) {
        System.out.println("[User " + name + "] Notification: " + message);
    }
}
class Driver extends User{
    Cab cab;
    public Driver(String name, Location location, Cab cab){
        super(name, location);
        this.cab = cab;
    }
    public boolean isAvailable(){
        return (this.currRide == null);
    }
    public boolean acceptRide(Ride ride){
        if(this.currRide != null)return false;
        this.currRide = ride;
        return true;
    }
    public boolean declineRide(Ride ride){
        if(this.currRide == null)return false;
        this.currRide = null;
        return true;
    }
    public Location updateLocation(Driver driver){ return driver.currLocation; }
    public void update(String message) {
        System.out.println("[Driver " + name + "] Notification: " + message);
    }
}
class Cab {
    String cabNumber;
    public Cab(String cabNumber){ this.cabNumber = cabNumber; }
}
class Ride implements Subject {
    static int counter = 1;
    int rideId;
    Driver driver;
    User rider;
    Location pickupLocation, dropLocation;
    double fare;
    String status = "Not started";
    private List<Observer> observers = new ArrayList<>();
    public Ride(Driver driver, User rider, Location pickupLocation, Location dropLocation, double fare){
        this.rideId = counter++;
        this.driver = driver;
        this.rider = rider;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.fare = fare;
    }
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(String message) {
        for (Observer o : observers) o.update(message);
    }
    public void startRide(){ 
        status = "On the Way";
        notifyObservers("Ride " + rideId + " started!");
    }
    public void endRide(){ 
        status = "Ride Ended";
        this.driver.addRideToHistory(this);
        this.rider.addRideToHistory(this);
        this.driver.currRide = null;
        this.rider.currRide = null;
        notifyObservers("Ride " + rideId + " ended!");
    }
    //also need to calculate status of ride based on location
}
class Location {
    int x, y;
    public Location(int x, int y){
        this.x = x;
        this.y = y;
    }
    public double distanceTo(Location loc) {
        return Math.sqrt(Math.pow(this.x - loc.x, 2) + Math.pow(this.y - loc.y, 2));
    }
}
interface SearchStrategy{
    Driver findCab(List<Driver> drivers, Location pickup);
}
class SearchByNearestDriverLocation implements SearchStrategy{
    public Driver findCab(List<Driver> drivers, Location pickup){
        System.out.println("Searching for nearest driver . . . .");
        double min = Integer.MAX_VALUE; Driver nearestDriver = null;
        for(Driver driver: drivers){
            double dist = driver.currLocation.distanceTo(pickup);
            if(dist < min){
                min = dist;
                nearestDriver = driver;
            }
        }
        System.out.println("Driver Found! : "+nearestDriver.name);
        System.out.println(nearestDriver.name +" is "+Math.ceil(min)+" meters away!");
        return nearestDriver;
    }
}
class CabService {
    List<Driver> drivers = new ArrayList<>();
    SearchStrategy strategy = new SearchByNearestDriverLocation();

    public void addDriver(Driver driver){ drivers.add(driver); }

    public Ride requestRide(User user, Location src, Location dest){
        Driver driver = strategy.findCab(drivers, src);
        Ride ride = new Ride(driver, user, src, dest, 100.0);
        ride.registerObserver(user);
        ride.registerObserver(driver);
    
        if (!user.bookRide(ride)) return null;
        driver.acceptRide(ride);
        ride.notifyObservers("Ride " + ride.rideId + " booked successfully!");
        return ride;
    }
    public void endRide(Ride ride) {
        ride.endRide();
    }
}
public class Main {
    public static void main(String[] args){
        CabService cabService = new CabService();
        Driver d1 = new Driver("Bob", new Location(10,15), new Cab("1JS091998"));
        Driver d2 = new Driver("Alex", new Location(2,3), new Cab("1JS091999"));
        Driver d3 = new Driver("Ron", new Location(5,6), new Cab("1JS091997"));
        cabService.addDriver(d1);
        cabService.addDriver(d2);
        cabService.addDriver(d3);

        User u1 = new User("Megan", new Location(1,2));
        Ride myRide = cabService.requestRide(u1, new Location(1, 2), new Location(6, 6));
        
        System.out.println("Ride details : "+myRide);
    }
}
