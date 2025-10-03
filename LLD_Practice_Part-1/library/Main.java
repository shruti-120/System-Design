package library;
import java.util.*;
interface SearchStrategy {
    void search(String searchString, Set<Book> books);    
}
class SearchByAuthor implements SearchStrategy{
    public void search(String author, Set<Book> books){
        System.out.println("Searching book by author "+author+". . . .");
        boolean found = false;
        for(Book book: books){
            if((book.getAuthor()).equalsIgnoreCase(author)){
                found = true;
                System.out.println(book.getName());
            }
        }
        if(!found)System.out.println("Books by "+author+" not present!");
    }
}
class SearchByTitle implements SearchStrategy{
    public void search(String title, Set<Book> books){
        System.out.println("Searching book by title: " + title+". . . .");
        boolean found = false;
        for (Book book : books) {
            if (book.getName().equalsIgnoreCase(title)) {
                found = true;
                System.out.println("Found: " + book.getName());
            }
        }
        if (!found) System.out.println("No book found with title " + title);
    }
}
class Book {
    private final String name, author;
    private boolean isBorrowed;
    private final int ID;
    Book(String name, String author, Boolean isBorrowed, int ID){
        this.name = name;
        this.author = author;
        this.isBorrowed = isBorrowed;
        this.ID = ID;
    }
    public String getName(){ return name; }
    public String getAuthor() { return author; }
    public boolean getIsBorrowed() { return isBorrowed; }
    public void setIsBorrowed(boolean value) {
        this.isBorrowed = value;
    }
    public int getID() { return ID; }
}
class Library{
    private HashSet<Book> library = new HashSet<>();

    public void addBook(Book book){
        library.add(book);
    }
    public void removeBook(Book book){
        library.remove(book);
    }
    public void borrowBook(Book book){
        if (!book.getIsBorrowed()) {
            book.setIsBorrowed(true);
            System.out.println("Borrowed: " + book.getName());
        } else {
            System.out.println("Already borrowed: " + book.getName());
        }
    }
    public void searchBook(String searchString, SearchStrategy strategy) {
        strategy.search(searchString, library);
    }
}

public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        Book b1 = new Book("death", "maine", false, 1);
        Book b2 = new Book("faith in you", "maine", false, 2);
        Book b3 = new Book("sweat proof", "lowe", false, 3);
        Book b4 = new Book("money buys happiness", "flyod", true, 4);

        library.addBook(b1);
        library.addBook(b2);
        library.addBook(b3);
        library.addBook(b4);

        // Search
        library.searchBook("death", new SearchByTitle());
        library.searchBook("maine", new SearchByAuthor());
        library.searchBook("yoi", new SearchByAuthor());
        library.searchBook("love", new SearchByTitle());

        // Borrow a book
        library.borrowBook(b1);  // Borrow "death"
        library.borrowBook(b4);  // Try to borrow already borrowed book

    }
}
