package onlinePollingSystem;
import java.util.*;

class User {
    private static int counter = 1; 
    private final int id;
    private String name;
    public User(String name){
        this.id = counter++;
        this.name = name;
    }
    public String getName() { return this.name; }
}
class Poll {
    private static int counter = 1;
    private final int id;
    private String question;
    private User creator;
    private boolean isClosedFlag = false;
    private Set<Option> options= new HashSet<>();
    private Set<User> voters = new HashSet<>();
    public Poll(String question, User creator) {
        this.id = counter++;
        this.question = question;
        this.creator = creator;
    } 
    public int getId() { return this.id; }
    public Set<Option> getOptions() { return options; }
    public boolean isPollClosed() { return isClosedFlag; }
    public void closePoll() { this.isClosedFlag = true; }
    public void addOption(Option option) { this.options.add(option); }
    public boolean hasUserVoted(User user) { return voters.contains(user); }
    public void markUserVoted(User user) { voters.add(user); }
}
class Option {
    private static int counter = 1;
    private final int id;
    private String text;
    List<Vote> votes = new ArrayList<>();
    public Option(String text){
        this.id = counter++;
        this.text = text;
    }
    public String getOptionText() { return this.text; }
    public int getNumberOfVotes() { return this.votes.size(); }
    public void addVote(Vote vote) { this.votes.add(vote); }
}
class Vote {
    private User user;
    private Poll poll;
    private Option selectedOption;
    public Vote(User user, Poll poll, Option selectedOption){
        this.user = user;
        this.poll = poll;
        this.selectedOption = selectedOption;
    }
}
class PollingService {
    public void vote(Poll poll, Option selectedOption, User user){
        if(poll.isPollClosed())System.out.println("poll is closed ! , can't vote.");
        else if (!poll.getOptions().contains(selectedOption)) {
            System.out.println("Invalid option for this poll.");
            return;
        }
        else if (poll.hasUserVoted(user)) {
            System.out.println(user.getName() + " has already voted.");
        } else {
            Vote vote = new Vote(user, poll, selectedOption);
            selectedOption.addVote(vote);
            poll.markUserVoted(user);
        }
    }
    public void getResults(Poll poll){
        for(Option option: poll.getOptions()){
            System.out.println(option.getOptionText()+" : "+option.getNumberOfVotes());
        }
    }
    public void closePoll(Poll poll){
        poll.closePoll();
        System.out.println("poll "+poll.getId()+" closed!");
    }
}

public class Main {
    public static void main(String[] args) {
        PollingService pollingService = new PollingService();
        Poll poll = new Poll("which is your preferred Language", new User("john"));
        Option option1 = new Option("c++");
        Option option2 = new Option("JAVA");
        Option option3 = new Option("Pyhton");
        Option option4 = new Option("JavaScript");
        
        poll.addOption(option1);
        poll.addOption(option2);
        poll.addOption(option3);
        poll.addOption(option4);

        pollingService.vote(poll,  option1, new User("Alice"));
        pollingService.vote(poll,  option1, new User("Bob"));
        pollingService.vote(poll,  option2, new User("Max"));
        pollingService.vote(poll,  option3, new User("Alex"));
        pollingService.vote(poll,  option2, new User("meghan"));
        pollingService.vote(poll,  option4, new User("vanilla"));
        pollingService.vote(poll,  option2, new User("choco"));

        pollingService.vote(poll,  new Option("Java8"), new User("coco"));
        User u1 = new User("Aman");
        pollingService.vote(poll,  option2, u1);
        pollingService.vote(poll,  option3, u1);
        pollingService.closePoll(poll);
        pollingService.getResults(poll);

    }
}
