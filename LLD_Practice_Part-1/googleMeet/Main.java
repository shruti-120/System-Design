package googleMeet;
import java.util.*;

class User {
    private static int counter = 1;
    private int id; 
    private String name;

    public User(String name) { 
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}

class Meeting {
    private static int counter = 1;
    private int id;
    private User host;
    private List<User> participants = new ArrayList<>();
    private Status status;

    public Meeting(User host) {
        this.id = counter++;
        this.host = host;
    }

    public int getId() { return id; }
    public List<User> getParticipants() { return participants; }
    public User getHost() { return host; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) {this.status = status; }

    public void join(User user) { 
        participants.add(user);        
        System.out.println(user.getName()+" Joined meeting-" + id);
    }
    public void leave(User user) { 
        participants.remove(user);
        System.out.println(user.getName()+" Left meeting"+id); 
    }
    public void end() { this.status = Status.ENDED; }
}

enum Status { SCHEDULED, LIVE, ENDED }

class MediaStream {
    private User user;
    private boolean audioOn = false, vedioOn = false;
    public MediaStream(User user){ this.user = user; }

    public void swtichAudioOn() { audioOn = true; }
    public void swtichAudioOff() { audioOn = false; }
    public void swtichVedioOn() { vedioOn = true; }
    public void swtichVedioOff() { vedioOn = false; }
}

class ChatMessage {
    private User user; 
    private String timeStamp;
    private String body;

    public ChatMessage(User user, String body) { 
        this.user = user;
        this.body = body;
    }
    @Override 
    public String toString() {
        return user.getName()+" - "+body;
    }
}

class GoogleMeet {
    Map<Integer, Meeting> meetings = new HashMap<>();
    Map<Integer, Map<User, MediaStream>> streams = new HashMap<>();
    Map<Integer, List<ChatMessage>> chats = new HashMap<>();

    public Meeting createMeeting(User host) {
        Meeting meeting = new Meeting(host);
        meetings.put(meeting.getId(), meeting);
        streams.put(meeting.getId(), new HashMap<>());
        chats.put(meeting.getId(), new ArrayList<>());
        meeting.setStatus(Status.SCHEDULED);
        return meeting;
    }
    public void joinMeet(int meetId, User participant){
        if(!meetings.containsKey(meetId)){ System.out.println("invalid meetId!");}

        Meeting currMeeting = meetings.get(meetId);

        if(currMeeting.getStatus() == Status.ENDED)System.out.println("Meeting has Ended, can't join");

        currMeeting.join(participant);
        streams.get(meetId).put(participant, new MediaStream(participant));

        if(currMeeting.getParticipants().size() > 0){
            currMeeting.setStatus(Status.LIVE);
        }
    }
    public void leaveMeet(int meetId, User participant){
        if(!meetings.containsKey(meetId)){ System.out.println("invalid meetId!");}

        Meeting currMeeting = meetings.get(meetId);

        if(currMeeting.getStatus() == Status.SCHEDULED)System.out.println("Meeting has not yet Started");

        currMeeting.leave(participant);
        streams.get(meetId).remove(participant);

        if(currMeeting.getParticipants().size() == 0){
            System.out.println("Meeting Ended!");
            currMeeting.setStatus(Status.ENDED);
        }
    }
    public void sendMessage(User user, String message, int meetId){
        ChatMessage msg = new ChatMessage(user, message);
        chats.get(meetId).add(msg);
    }
    public void getChatHistory(int meetId) { 
        for(ChatMessage msg: chats.get(meetId)){
            System.out.println(msg.toString()); 
        }
    } 
    public void getStatus(int meetId ) { System.out.println(meetings.get(meetId).getStatus()+" - meetId : "+meetId);}

}

public class Main {
    public static void main(String[] args) {

        GoogleMeet googleMeet = new GoogleMeet();
        
        User u1 = new User("Alice");
        User u2 = new User("Bob");
        User u3 = new User("John");

        googleMeet.createMeeting(u1);
        googleMeet.getStatus(1);
        googleMeet.joinMeet(1, u1);
        googleMeet.joinMeet(1, u3);
        googleMeet.getStatus(1);
        googleMeet.joinMeet(1, u2);
        googleMeet.sendMessage(u3, "Hi ! i am john", 1);
        googleMeet.sendMessage(u1, "Hi ! i am Alice, nice to meet you all", 1);
        googleMeet.sendMessage(u2, "Hello", 1);
        googleMeet.leaveMeet(1, u3);
        googleMeet.getStatus(1);
        googleMeet.getChatHistory(1);
        googleMeet.leaveMeet(1, u2);
        googleMeet.leaveMeet(1, u1);
        googleMeet.getStatus(1);
    }
}
