package bookMyShow;
import java.sql.Time;
import java.util.*;

class Movie {
    private String name, genre;
    private int duration;
    public Movie(String name, String genre, int duration){
        this.name =  name;
        this.genre = genre;
        this.duration = duration;
    }
    public String getName() { return name; }
    public String getGenre() { return genre; }
    public int getDuration() { return duration; }
}
class Show {
    private String time;
    private Movie movie;
    private int seats;
    private HashSet<String> availableSeats;
    public Show(String time, Movie movie, int seats, int availableSeats){
        this.time = time;
        this.movie = movie;
        this.seats = seats;
        this.availableSeats = new HashSet<>();
        for (int i = 1; i <= seats; i++) {
            this.availableSeats.add("S" + i); // e.g., S1, S2...
        }
    }
    public String getTime() { return time; }
    public Movie getMovie() { return movie; }
    public int getTotalSeats() { return seats; }
    public HashSet<String> getAvailableSeats() { return availableSeats; }
    public void addToAvailableSeats(String seatNumber) { this.availableSeats.add(seatNumber);}
    public void removeFromAvailableSeats(String seatNumber) { this.availableSeats.remove(seatNumber);}

    public boolean book(Show show, User user, HashSet<String> seats){
        for(String seatNumber: seats){
            if (!availableSeats.contains(seatNumber)) return false;
            removeFromAvailableSeats(seatNumber);
        }
        for (String seatNumber : seats) {
            Ticket ticket = new Ticket(seatNumber, this, user);
            user.addTickets(ticket);
        }
        return true;
    }
    public boolean cancel(Show show, User user, HashSet<String> seats){
        for(String seatNumber: seats){
            addToAvailableSeats(seatNumber);
        }
        return true;
    }
}
class Theatre {
    private final String name;
    private final City city;
    private final List<Show> shows;
    public Theatre(String name, City city) { 
        this.name = name; 
        this.city = city;
        this.shows = new ArrayList<>();
    }
    public String getName(){ return name; }
    public City getCity(){ return city; }
    public List<Show> getShowsList(){ return shows; }
    public void addShow(Show show){ this.shows.add(show); } 
}
class City {
    private final String name;
    private List<Theatre> theatres;
    public City(String name){
        this.name = name;
        this.theatres = new ArrayList<>();
    }
    public String getName() { return name; }
    public List<Theatre> getTheatresList() { return theatres; }
    public void addTheatre(Theatre theatre) { this.theatres.add(theatre); }
}
class User {
    private final String name;
    private List<Ticket> tickets;
    public User(String name){
        this.name = name;
        this.tickets = new ArrayList<>();
    }
    public String getName() { return name; }
    public List<Ticket> getTicketsList() { return tickets; }
    public void addTickets(Ticket ticket) { this.tickets.add(ticket); }
}
class Ticket {
    private static int counter = 1;
    private int ticketNumber;
    private String seatNumber;
    private Show show;
    private User user;
    public Ticket(String seatNumber, Show show, User user){
        this.ticketNumber = counter++;
        this.seatNumber = seatNumber;
        this.show = show;
        this.user = user;
    } 
    public int getTicketNumber() { return ticketNumber; }
    public String getSeatNumber() { return seatNumber; }
    public Show getShow() { return show; }
    public User getUser() { return user; }
}
interface SearchStrategy {
    List<Show> search(Bookings bookings, String keyword);
}
class SearchByMovieName implements SearchStrategy {
    @Override
    public List<Show> search(Bookings bookings, String movieName) {
        List<Show> result = new ArrayList<>();
        for (City city : bookings.getCities()) {
            for (Theatre theatre : city.getTheatresList()) {
                for (Show show : theatre.getShowsList()) {
                    if (show.getMovie().getName().equalsIgnoreCase(movieName)) {
                        result.add(show);
                    }
                }
            }
        }
        return result;
    }
}

class SearchByGenre implements SearchStrategy {
    @Override
    public List<Show> search(Bookings bookings, String genre) {
        List<Show> result = new ArrayList<>();
        for (City city : bookings.getCities()) {
            for (Theatre theatre : city.getTheatresList()) {
                for (Show show : theatre.getShowsList()) {
                    if (show.getMovie().getGenre().equalsIgnoreCase(genre)) {
                        result.add(show);
                    }
                }
            }
        }
        return result;
    }
}
class SearchContext {
    private SearchStrategy strategy;

    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Show> executeSearch(Bookings bookings, String keyword) {
        if (strategy == null) throw new IllegalStateException("Strategy not set");
        return strategy.search(bookings, keyword);
    }
}

class Bookings {
    private List<City> cities;

    public Bookings() {
        this.cities = new ArrayList<>();
    }

    public void addCity(City city) {
        this.cities.add(city);
    }

    public List<City> getCities() {
        return cities;
    }

    // Optionally, you could provide some helper methods
    public List<Theatre> getAllTheatres() {
        List<Theatre> allTheatres = new ArrayList<>();
        for (City city : cities) {
            allTheatres.addAll(city.getTheatresList());
        }
        return allTheatres;
    }

    public List<Show> getAllShows() {
        List<Show> allShows = new ArrayList<>();
        for (Theatre theatre : getAllTheatres()) {
            allShows.addAll(theatre.getShowsList());
        }
        return allShows;
    }
}


public class Main {
    public static void main(String[] args) {
        // 1. Create system-wide booking container
        Bookings bookings = new Bookings();

        // 2. Set up city, theatre, movie, show
        City city = new City("Bangalore");
        Movie movie = new Movie("Dune", "Sci-Fi", 180);
        Show show = new Show("18:30", movie, 100, 100);
        Theatre theatre = new Theatre("PVR Orion", city);
        theatre.addShow(show);
        city.addTheatre(theatre);
        bookings.addCity(city);

        // 3. Search by movie name
        SearchContext searchContext = new SearchContext();
        searchContext.setStrategy(new SearchByMovieName());
        List<Show> matchedShows = searchContext.executeSearch(bookings, "Dune");
        System.out.println("Found shows: " + matchedShows.size());

        if (matchedShows.isEmpty()) {
            System.out.println("No shows found for the movie.");
            return;
        }

        // 4. Book seats
        User user = new User("Alice");
        Show selectedShow = matchedShows.get(0);

        HashSet<String> seatsToBook = new HashSet<>(Arrays.asList("S1", "S2", "S3"));
        boolean bookingStatus = selectedShow.book(selectedShow, user, seatsToBook);

        if (bookingStatus) {
            System.out.println("Booking successful!");
        } else {
            System.out.println("Booking failed!");
        }

        // 5. Print tickets
        System.out.println("\nTickets for " + user.getName() + ":");
        for (Ticket ticket : user.getTicketsList()) {
            System.out.println("Ticket#: " + ticket.getTicketNumber() +
                               ", Seat: " + ticket.getSeatNumber() +
                               ", Movie: " + ticket.getShow().getMovie().getName() +
                               ", Time: " + ticket.getShow().getTime() +
                               ", Theatre: " + theatre.getName() +
                               ", City: " + city.getName());
        }
    }
}


