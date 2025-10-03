package rideBookingSystem;
import java.util.*;

class User {
    private String name;
    private static int counter = 1;
    private int id;
    Location location;
    public User(String name, Location location) { 
        this.name = name;
        this.id = counter++;
        this.location = location;
    }
    public String getName() { return name; }
    public Location getLocation() { return location; }
    public void updateLocation(Location newLocation) { this.location = newLocation; }
}
class Driver extends User {
    private boolean isAvailable = true;
    private Cab cab;
    public Driver(String name, Location location, Cab cab){
        super(name, location);
        this.cab = cab;
    }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailableStatus(boolean value) { this.isAvailable = value; }
}
class Cab {
    private String cabNumber;
    public Cab(String cabNumber){ this.cabNumber = cabNumber; }
}
class Location {
    private int x, y;
    public Location(int x, int y){
        this.x = x;
        this.y = y;
    }
    public double calculateDistance(Location location){
        return Math.sqrt(Math.pow(this.x - location.x, 2)+Math.pow(this.y - location.y, 2));
    }
}
class Ride {
    private User rider;
    private Driver driver;
    Location source, destination;
    private static int counter = 1;
    private int id;
    private double fare;
    private boolean isCompleted = false;
    public Ride(User rider, Driver driver, Location source, Location destination, double fare){
        this.rider = rider; 
        this.driver = driver;
        this.source = source;
        this.destination = destination;
        this.fare = fare;
    }
    public void completeRide() { this.isCompleted = true; }
    public Driver getDriver() { return driver; }
    public double getFare() { return fare; }
}
interface SearchDriverStrategy {
    Driver search(List<Driver> drivers, Location src);
}
class SearchDriverByNearestDist implements SearchDriverStrategy {
    public Driver search(List<Driver> drivers, Location src){
        Driver nearestDriver = null;
        double nearestDist = Double.MAX_VALUE;
        for(Driver driver : drivers){
            double dist = (driver.getLocation()).calculateDistance(src);
            if(driver.isAvailable() && dist < nearestDist){
                nearestDist = dist;
                nearestDriver = driver;
            }
        }
        return nearestDriver;
    }
}
class RideBookingService {
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Driver> drivers = new ArrayList<>();
    double ratePerKm = 10.0;
    SearchDriverStrategy searchStrategy = new SearchDriverByNearestDist();
    private final Map<User, Ride> activeRides = new HashMap<>(); 

    public void addUser(User user){ users.add(user); }

    public void addDriver(Driver driver){ drivers.add(driver); }

    public double calculateFare(Location src, Location dest){
        double totalFare = src.calculateDistance(dest) * ratePerKm;
        System.out.println("Your total fare is : Rs "+Math.ceil(totalFare));
        return totalFare;
    }
    public void bookRide(User rider, Location src, Location dest){
        if (activeRides.containsKey(rider)) {
            System.out.println("User already has an active ride.");
            return;
        }
        Driver driver = searchStrategy.search(drivers, src);
        if (driver == null) {
            System.out.println("No available drivers nearby.");
            return;
        }
        Ride newRide = new Ride(rider, driver, src, dest, calculateFare(src, dest));
        driver.setAvailableStatus(false); 
        activeRides.put(rider, newRide); 
        System.out.println("Ride for "+rider.getName()+" booked!");
    }
    public void completeRide(User rider) {
        Ride ride = activeRides.get(rider);
        if (ride == null) {
            System.out.println("No active ride found for this user.");
            return;
        }
        ride.completeRide();
        ride.getDriver().setAvailableStatus(true);
        ride.getDriver().updateLocation(ride.destination);
        System.out.println("Ride completed!, please pay : Rs "+Math.ceil(ride.getFare()));
    }
}

public class Main {
    public static void main(String[] args) {
        RideBookingService rideBookingService = new RideBookingService();

        User u1 = new User("Shruti", new Location(0, 0));
        rideBookingService.addUser(u1);
        User u2 = new User("Apurva", new Location(1, 5));
        rideBookingService.addUser(u2);
        Driver d1 = new Driver("Ron", new Location(1, 0), new Cab("1JS190956"));
        rideBookingService.addDriver(d1);
        Driver d2 = new Driver("Max", new Location(1, 1), new Cab("1JS190957"));
        rideBookingService.addDriver(d2);
        Driver d3 = new Driver("Bob", new Location(1, 2), new Cab("1JS190958"));
        rideBookingService.addDriver(d3);

        rideBookingService.bookRide(u1, new Location(0,0), new Location(2, 3));
        rideBookingService.bookRide(u2, new Location(1,5), new Location(2, 3));
        rideBookingService.completeRide(u1);
        rideBookingService.completeRide(u2);
    }
}
