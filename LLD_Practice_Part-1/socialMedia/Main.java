package socialMedia;
import java.util.*;
class User {
    private String name, email;
    private ArrayList<Post> posts = new ArrayList<>();
    private HashSet<User> following = new HashSet<>();
    public User(String name, String email){
        this.name = name;
        this.email = email;
        this.following.add(this);
    }
    public ArrayList<Post> getPosts() { return posts; }
    public void addPost(Post post) { this.posts.add(post); }
    public void removePost(Post post) { this.posts.remove(post); }
    public HashSet<User> getFollowing() { return following; }
    public void addFollowing(User user) { this.following.add(user); }
    public void removeFollowing(User user) { this.following.remove(user); }
}
class Post {
    static int counter = 1, time = 0;
    private final int id;
    int timeStamp;
    private String title, body;
    private final User author;
    public Post(String title, String body, User author) {
        this.id = counter++;
        this.timeStamp = time++;
        this.title = title;
        this.body = body;
        this.author = author;
    }
    public String getPost() {
        return "[" + id + "] " + title + ": " + body;
    }
}
interface IPostService {
    void create(Post post, User user);
    void delete(Post post, User user);
    List<Post> fetchUserPosts(User user);
}
interface IFollowService {
    void follow(User followee, User follower);
    void unfollow(User followee, User follower);
}
class FollowService implements IFollowService {
    public void follow(User followee, User follower) {
        follower.addFollowing(followee);
    }
    public void unfollow(User followee, User follower) {
        follower.removeFollowing(followee);
    }
}
class PostService implements IPostService {
    public void create(Post post, User user){
        user.addPost(post);
    }
    public void delete(Post post, User user){
        user.removePost(post);
    }
    public List<Post> fetchUserPosts(User user){
        return user.getPosts();
    }
}
class Facebook {
    ArrayList<Post> allPosts = new ArrayList<>();
    ArrayList<User> allUsers = new ArrayList<>();
    PostService postService = new PostService();
    FollowService followService = new FollowService();

    public void addUser(User user) { allUsers.add(user); }
    public void createPost(Post newPost, User user){
        postService.create(newPost, user);
        allPosts.add(newPost);
    }
    public void deletePost(Post newPost, User user){
        postService.delete(newPost, user);
        allPosts.remove(newPost);
    }
    public void fetchFeed(User user, int N){
        PriorityQueue<Post> pq = new PriorityQueue<>((a, b) -> b.timeStamp - a.timeStamp);
        for (User followee : user.getFollowing()) {
            for (Post post : followee.getPosts()) {
                pq.offer(post);
            }
        }
        ArrayList<Post> topPosts = new ArrayList<>();
        for (int i = 0; i < N && !pq.isEmpty(); i++) {
            topPosts.add(pq.poll());
        }
        for(Post post: topPosts)System.out.println(post.getPost());
    }
    public void follow(User followee, User follower){
        followService.follow(followee, follower);
    }
    public void unfollow(User followee, User follower){
        followService.unfollow(followee, follower);
    }
}
public class Main {
    public static void main(String[] args){
        Facebook facebook = new Facebook();
        User u1 = new User("john", "john@gmail.com");
        User u2 = new User("alice", "alice@gmail.com");
        User u3 = new User("bob", "bob@gmail.com");
        User u4 = new User("max", "max@gmail.com");

        facebook.addUser(u1);facebook.addUser(u2);facebook.addUser(u3);facebook.addUser(u4);

        facebook.follow(u3, u4);facebook.follow(u2, u4);facebook.follow(u1, u2);facebook.follow(u1, u3);

        Post p1 = new Post("today's news", "ceasefire from both countries", u4);
        Post p2 = new Post("new travel destinattion", "travelling to Bali", u2);
        Post p3 = new Post("hi everyone", "i am new to facebook", u3);

        facebook.createPost(p1, u4);
        facebook.createPost(p2, u2);
        facebook.createPost(p3, u3);

        facebook.fetchFeed(u4, 10);
    }
}
