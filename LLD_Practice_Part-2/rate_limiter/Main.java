package lld_practice.rate_limiter;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class Request {
    String id, userId;
    Long timestamp;
    Request(String id, String userId, Long timestamp) {
        this.id = id;
        this.userId = userId;
        this.timestamp = timestamp;
    }
}
class RateLimiter {
    int limit = 3;
    Map<String, Deque<Request>> userReq = new HashMap<>(); // userId, dq<user req>

    public void sendRequest(String userId, String reqId) {
        Request r = new Request(reqId, userId, System.currentTimeMillis());
        userReq.putIfAbsent(userId, new LinkedList<>());
        Deque<Request> dq = userReq.get(userId);
        while(!dq.isEmpty() && dq.peekFirst().timestamp < System.currentTimeMillis() - 5000) dq.removeFirst();
        if(dq.size() == limit) {
            System.out.println("Req Blocked!, please try again later!");
            return;
        }
        dq.addLast(r);
        System.out.println("Req allowed, sent to sever!");
    }
}
public class Main {
    public static void main(String[] args) throws InterruptedException {
        RateLimiter r = new RateLimiter();
        r.sendRequest("1", "1");
        r.sendRequest("2", "2");
        r.sendRequest("1", "3");
        r.sendRequest("1", "4");
//        Thread.sleep(5000);
        r.sendRequest("1", "5");
        r.sendRequest("2", "6");
        r.sendRequest("2", "7");
        r.sendRequest("2", "8");
    }
}
