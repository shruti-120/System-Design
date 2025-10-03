package lld_practice.notification_system;

import java.text.SimpleDateFormat;
import java.util.*;

// ------------------ ENTITIES ------------------

class User {
    String id, name, email;
    Long ph;
    List<NotifChannelType> preference = new ArrayList<>();
    Map<String, String> receivedEmails = new HashMap<>();
    Map<Long, String> receivedSMS = new HashMap<>();

    User(String id, String name, String email, Long ph, List<NotifChannelType> preference) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.ph = ph;
        this.preference = preference;
    }
}

class Notification {
    String id, message;
    List<String> recipientIds;
    Long timestamp;
    List<NotifChannelType> channel;
    Long timer = 0L; // when to deliver

    Notification(String id, String message, List<String> recipientIds, List<NotifChannelType> channel) {
        this.id = id;
        this.message = message;
        this.recipientIds = recipientIds;
        this.timestamp = System.currentTimeMillis();
        this.channel = channel;
    }

    Notification(String id, String message, List<String> recipientIds,
                 List<NotifChannelType> channel, Long timer) {
        this(id, message, recipientIds, channel);
        this.timer = timer;
    }
}

enum NotifChannelType {
    EMAIL, SMS
}

enum NotificationType {
    INSTANT, SCHEDULED
}

// ------------------ CHANNEL STRATEGY ------------------

interface NotificationChannel {
    void send(User sender, User recipient, Notification n);
}

class EmailChannel implements NotificationChannel {
    @Override
    public void send(User sender, User recipient, Notification n) {
        recipient.receivedEmails.put(n.id, n.message);
        System.out.println("Email sent to " + recipient.id + " (" + recipient.email + "): " + n.message);
    }
}

class SMSChannel implements NotificationChannel {
    @Override
    public void send(User sender, User recipient, Notification n) {
        recipient.receivedSMS.put(sender.ph, n.message);
        System.out.println("SMS sent to " + recipient.id + " (" + recipient.ph + "): " + n.message);
    }
}

// ------------------ NOTIFICATION SYSTEM ------------------

class NotificationSystem {
    Map<String, Notification> notificationMap = new HashMap<>();
    Map<String, User> userMap = new HashMap<>();
    Queue<Notification> messageQueue = new LinkedList<>();

    Map<NotifChannelType, NotificationChannel> channelMap = new HashMap<>();

    public NotificationSystem() {
        channelMap.put(NotifChannelType.EMAIL, new EmailChannel());
        channelMap.put(NotifChannelType.SMS, new SMSChannel());
    }

    public void addUser(String id, String name, String email, Long ph, List<NotifChannelType> preference) {
        userMap.put(id, new User(id, name, email, ph, preference));
        System.out.println("User Added!: " + name);
    }

    // ------------------ INSTANT SEND ------------------

    public void sendNotification(Notification n, String senderId, NotificationType type) {
        User sender = userMap.get(senderId);

        for (String rid : n.recipientIds) {
            User recipient = userMap.get(rid);

            // Respect recipient preferences
            for (NotifChannelType ch : n.channel) {
                if (recipient.preference.contains(ch)) {
                    channelMap.get(ch).send(sender, recipient, n);
                }
            }
        }
    }

    public void sendNotification(String id, String message, List<String> recipientIds,
                                 List<NotifChannelType> channel, String senderId, NotificationType type) {
        Notification n = new Notification(id, message, recipientIds, channel);
        sendNotification(n, senderId, type);
    }

    // ------------------ SCHEDULE ------------------

    public void scheduleNotification(String id, String message, List<String> recipientIds,
                                     List<NotifChannelType> channel, String senderId, Long scheduledTime) {
        Notification n = new Notification(id, message, recipientIds, channel, scheduledTime);
        messageQueue.add(n);
        System.out.println("Notification scheduled at " + new Date(scheduledTime));
    }

    // Method to check and process due notifications
    public void processQueue(String senderId) {
        long now = System.currentTimeMillis();
        while (!messageQueue.isEmpty() && messageQueue.peek().timer <= now) {
            Notification n = messageQueue.poll();
            sendNotification(n, senderId, NotificationType.SCHEDULED);
        }
    }
}

// ------------------ MAIN ------------------

public class Main {
    public static void main(String[] args) throws Exception {
        NotificationSystem ns = new NotificationSystem();

        ns.addUser("1", "Alex", "alex@gmail.com", 9193784438L,
                Arrays.asList(NotifChannelType.EMAIL, NotifChannelType.SMS));
        ns.addUser("2", "Sam", "sam@gmail.com", 9195784438L,
                Arrays.asList(NotifChannelType.EMAIL));
        ns.addUser("3", "Bob", "bob@gmail.com", 9199384438L,
                Arrays.asList(NotifChannelType.SMS));

        // INSTANT
        ns.sendNotification("N1", "Hello Everyone",
                Arrays.asList("2", "3"),
                Arrays.asList(NotifChannelType.EMAIL, NotifChannelType.SMS),
                "1", NotificationType.INSTANT);

        // SCHEDULED
        String inputTime = "04-09-2025 22:54";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Long time = sdf.parse(inputTime).getTime();

        ns.scheduleNotification("N2", "Scheduled Msg for Alex",
                Arrays.asList("1"),
                Arrays.asList(NotifChannelType.EMAIL, NotifChannelType.SMS),
                "2", time);

        // Simulate processing queue (like a cron job would do)
        Thread.sleep(4000);
        ns.processQueue("2"); // will only send if current time >= scheduled time
    }
}
