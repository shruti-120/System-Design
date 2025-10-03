package carPooling;
import java.util.*;

class User {
    private static int counter = 1;
    private int id;
    private String name;
    private Location location;
    
    public User(String name, Location location) {
        this.id = counter++;
        this.name = name;
        this.location = location;
    }
    public String getName() { return name; }
    public Location getLocation() { return location; }
    public void updateLocation(Location newLocation) { this.location = newLocation; }
}
class Driver extends User {
    private Cab cab;
    private boolean isAvailable = true;

    public Driver(String name, Location location, Cab cab) {
        super(name, location);
        this.cab = cab;
    }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailableStatus(boolean value) { this.isAvailable = value; }
    public Cab getCab() { return cab; }
}
class Cab {
    private String cabNumber; 
    private int availableSeats;
    
    public Cab(String cabNumber, int availableSeats) {
        this.cabNumber = cabNumber;
        this.availableSeats = availableSeats;
    }
    public int getAvailableSeats() { return this.availableSeats; }
    public void updateAvailableSeats(int value) { this.availableSeats = value; }
}
class Location {
    private String name;

    public Location(String name) { this.name = name; }

    public String getName() { return name; }
}
class Route {
    private Driver driver;
    private List<Location> locations;

    public Route(Driver driver) {
        this.driver = driver;
        this.locations = new ArrayList<>();
    }

    public void addLocationToRoute(Location location) { locations.add(location); }
    public List<Location> getLocations() { return locations; }
    public Driver getDriver() { return driver; }
}

interface RideMatchingStrategy {
    Driver search(List<Route> routes, Location src, int requiredSeats);
}
class SearchByRouteAndSeatAvailability implements RideMatchingStrategy {
    public Driver search(List<Route> routes, Location src, int requiredSeats){
        for(Route route : routes){
            if(route.getLocations().contains(src)){
                Driver driver = route.getDriver();
                Cab cab = driver.getCab();
                if (driver.isAvailable() && cab.getAvailableSeats() >= requiredSeats) {
                    return driver;
                }
            }
        }
        return null;
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

class CarPoolingService {
    List<User> users = new ArrayList<>();
    List<Driver> drivers = new ArrayList<>();
    Map<User, Ride> rides = new HashMap<>(); 
    List<Route> routes = new ArrayList<>();
    private RideMatchingStrategy matchingStrategy = new SearchByRouteAndSeatAvailability();

    public void addUser(User user) { users.add(user); }
    public void addDriver(Driver driver) { drivers.add(driver); }
    public void addRoute(Route route) { routes.add(route); }

    public void bookRide(User user, Location source, Location destination, int seatsRequired) {
        if (rides.containsKey(user)) {
            System.out.println("User already has an active ride.");
            return;
        }
        Driver matchedDriver = matchingStrategy.search(routes, source, seatsRequired);
        if (matchedDriver == null) {
            System.out.println("No driver found for source: " + source.getName());
            return;
        }
        Cab cab = matchedDriver.getCab();
        cab.updateAvailableSeats(cab.getAvailableSeats() - seatsRequired);

        Ride ride = new Ride(user, matchedDriver, source, destination, 100.0); // Fare can be dynamic
        rides.put(user, ride);
        System.out.println("Ride booked for user: " + user.getName() + " with driver: " + matchedDriver.getName());
    }
    public void completeRide(User user) {
        Ride ride = rides.get(user);
        if (ride == null) {
            System.out.println("No active ride found.");
            return;
        }
        ride.completeRide();
        Cab cab = ride.getDriver().getCab();
        cab.updateAvailableSeats(cab.getAvailableSeats() + 1); // assume 1 seat for now
        rides.remove(user);
        System.out.println("Ride completed for user: " + user.getName());
    }

}

public class Main {
    public static void main(String[] args) {
        CarPoolingService service = new CarPoolingService();

        Location l1 = new Location("A");
        Location l2 = new Location("B");
        Location l3 = new Location("C");

        User u1 = new User("Alice", l1);
        service.addUser(u1);

        Cab cab1 = new Cab("CAB123", 3);
        Driver d1 = new Driver("Bob", l1, cab1);
        service.addDriver(d1);

        Route r1 = new Route(d1);
        r1.addLocationToRoute(l1);
        r1.addLocationToRoute(l2);
        r1.addLocationToRoute(l3);
        service.addRoute(r1);

        service.bookRide(u1, l1, l2, 1);
        service.completeRide(u1);
    }
}
