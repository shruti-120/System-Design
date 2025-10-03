package lld_practice.Reddit_nested_comments;

import java.util.*;

import java.util.*;

class User {
    String userId;
    String username;
    String email;

    public User(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}

class Comment {
    String commentId;
    String userId;
    String postId;
    String parentId; // null if top-level
    String content;
    Date timestamp;
    List<Comment> replies;

    public Comment(String commentId, String userId, String postId, String parentId, String content) {
        this.commentId = commentId;
        this.userId = userId;
        this.postId = postId;
        this.parentId = parentId;
        this.content = content;
        this.timestamp = new Date();
        this.replies = new ArrayList<>();
    }

    public void addReply(Comment reply) {
        replies.add(reply);
    }
}

class Post {
    String postId;
    String userId;
    String title;
    String content;
    Date timestamp;
    List<Comment> comments;

    public Post(String postId, String userId, String title, String content) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.timestamp = new Date();
        this.comments = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}

class Reddit {
    Map<String, Post> posts;
    Map<String, Comment> comments;

    public Reddit() {
        posts = new HashMap<>();
        comments = new HashMap<>();
    }

    // Create a post
    public Post createPost(String userId, String title, String content) {
        String postId = UUID.randomUUID().toString();
        Post post = new Post(postId, userId, title, content);
        posts.put(postId, post);
        return post;
    }

    // Add comment or reply
    public Comment addComment(String userId, String postId, String parentId, String content) {
        if (!posts.containsKey(postId)) {
            throw new RuntimeException("Post not found");
        }
        String commentId = UUID.randomUUID().toString();
        Comment comment = new Comment(commentId, userId, postId, parentId, content);
        comments.put(commentId, comment);

        if (parentId == null) {
            posts.get(postId).addComment(comment);
        } else {
            Comment parentComment = comments.get(parentId);
            if (parentComment == null) throw new RuntimeException("Parent comment not found");
            parentComment.addReply(comment);
        }
        return comment;
    }

    // Print post with nested comments
    public void printPost(String postId) {
        if (!posts.containsKey(postId)) {
            System.out.println("Post not found");
            return;
        }
        Post post = posts.get(postId);
        System.out.println("Post: " + post.title + " by " + post.userId);
        System.out.println(post.content);
        for (Comment c : post.comments) {
            printComment(c, 1);
        }
    }

    private void printComment(Comment comment, int level) {
        System.out.println("  ".repeat(level) + "- " + comment.content + " by " + comment.userId+ "time: "+ comment.timestamp);
        for (Comment reply : comment.replies) {
            printComment(reply, level + 1);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Reddit reddit = new Reddit();
        User user1 = new User("u1", "Alice", "alice@email.com");
        User user2 = new User("u2", "Bob", "bob@email.com");

        // Create a post
        Post post = reddit.createPost(user1.userId, "My First Post", "Hello Reddit!");

        // Add comments
        Comment c1 = reddit.addComment(user2.userId, post.postId, null, "Nice post!");
        Comment c2 = reddit.addComment(user1.userId, post.postId, c1.commentId, "Thanks!");
        Comment c3 = reddit.addComment(user2.userId, post.postId, c2.commentId, "You're welcome!");

        // Print post with nested comments
        reddit.printPost(post.postId);
    }
}


