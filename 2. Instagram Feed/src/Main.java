import java.io.*;
import java.util.ArrayList;

/***
 * Main class to simulate Instagram using custom data structures.
 * Handles commands to create users, follow/unfollow users, create posts, like posts, generate feeds, etc.
 * @author Ahmet Erdem Bulut, Student ID:2022400093
 */
public class Main {
    public static void main(String[] args) {
//        long start = System.currentTimeMillis();
        String inputName = args[0];
        String outputName = args[1];

        MyHashMap<String, User> users = new MyHashMap<>(10); // a global hashmap to keep all users in the platform
        MyHashMap<String, Post> posts = new MyHashMap<>(10); // a global hashmap to keep all posts in the platform

        try{
            BufferedReader reader = new BufferedReader(new FileReader(inputName));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputName));
            String line;
            String[] parts;

            // Read each line from the input file and execute commands accordingly
            while ((line = reader.readLine()) != null){
                parts = line.split(" ");
                String command = parts[0];

                switch (command){
                    case "create_user":
                        createUser(users, parts[1], writer);
                        break;
                    case "follow_user":
                        followUser(users, parts[1], parts[2], writer);
                        break;
                    case "unfollow_user":
                        unfollowUser(users, parts[1], parts[2], writer);
                        break;
                    case "create_post":
                        createPost(users, posts, parts[1], parts[2], parts[3], writer);
                        break;
                    case "see_post":
                        seePost(users, posts, parts[1], parts[2], writer);
                        break;
                    case "see_all_posts_from_user":
                        seeAllPostsFromUser(users, parts[1], parts[2], writer);
                        break;
                    case "toggle_like":
                        toggleLike(users, posts, parts[1], parts[2], writer);
                        break;
                    case "generate_feed":
                        generateFeed(users, posts, parts[1], Integer.parseInt(parts[2]), writer);
                        break;
                    case "scroll_through_feed":
                        ArrayList<Integer> likeActions = new ArrayList<>();
                        for (int i = 3; i < parts.length; i++){
                            likeActions.add(Integer.parseInt(parts[i])); // to get which post is liked
                        }
                        scrollThroughFeed(users, posts, parts[1], Integer.parseInt(parts[2]), likeActions, writer);
                        break;
                    case "sort_posts":
                        sortPosts(users, posts, parts[1], writer);
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }

            reader.close();
            writer.close();
        } catch (IOException e){
            System.err.println("Error: " + e.getMessage());
        }

//        long end = System.currentTimeMillis();
//        double duration = (double) (end - start) / 1000;
//        System.out.println(duration + " seconds last");
    }

    /***
     * Creates a new user with the given user ID.
     * @param users The hashmap of all users in the platform
     * @param userID The unique ID for the new user
     * @param writer BufferedWriter to write output messages to the output file.
     * @throws IOException If an I/O error occurs.
     */
    private static void createUser(MyHashMap<String, User> users, String userID, BufferedWriter writer) throws IOException{
        // if the user already exists
        if (users.containsKey(userID)){
            writer.write("Some error occurred in create_user.\n");
        } else {
            users.put(userID, new User(userID));
            writer.write("Created user with Id " + userID + ".\n");
        }
    }

    /***
     * Makes a user follow another user
     * @param users The hashmap of all users in the platform
     * @param userID1 The ID of the user who will follow another user
     * @param userID2 The ID of the user to be followed
     * @param writer BufferedWriter to write output messages to the output file.
     * @throws IOException If an I/O error occurs.
     */
    private static void followUser(MyHashMap<String, User> users, String userID1, String userID2, BufferedWriter writer) throws IOException{
        // if users are not existing or they are the same
        if (!users.containsKey(userID1) || !users.containsKey(userID2) || userID1.equals(userID2)){
            writer.write("Some error occurred in follow_user.\n");
            return;
        }
        User user1 = users.get(userID1);
        User user2 = users.get(userID2);

        if (user1 == null || user2 == null || user1.getUserID().equals(user2.getUserID()) || user1.getFollowing().containsKey(userID2)){
            writer.write("Some error occurred in follow_user.\n");
        } else{
            user1.follow(user2);
            writer.write(userID1 + " followed " + userID2 + ".\n");
        }
    }

    /***
     * Makes a user unfollow another user
     * @param users The hashmap of all users in the platform
     * @param userID1 The ID of the user who will unfollow another user
     * @param userID2 The ID of the user to be unfollowed
     * @param writer BufferedWriter to write output messages to the output file.
     * @throws IOException If an I/O error occurs.
     */
    private static void unfollowUser(MyHashMap<String, User> users, String userID1, String userID2, BufferedWriter writer) throws IOException{
        // if users are not existing or they are the same
        if (!users.containsKey(userID1) || !users.containsKey(userID2) || userID1.equals(userID2)){
            writer.write("Some error occurred in unfollow_user.\n");
            return;
        }
        User user1 = users.get(userID1);
        User user2 = users.get(userID2);

        if (user1 == null || user2 == null || user1.getUserID().equals(user2.getUserID()) || !user1.getFollowing().containsKey(userID2)){
            writer.write("Some error occurred in unfollow_user.\n");
        } else{
            user1.unfollow(user2);
            writer.write(userID1 + " unfollowed " + userID2 + ".\n");
        }
    }

    /***
     * Creates a new post for the user.
     * @param users The hashmap of all users in platform
     * @param posts The hashmap of all posts in platform
     * @param userID The ID of the user creating the post
     * @param postID The ID of the post to be created
     * @param content The content of the post
     * @param writer BufferedWriter to write output messages to the output file.
     * @throws IOException If an I/O error occurs.
     */
    private static void createPost(MyHashMap<String, User> users, MyHashMap<String, Post> posts, String userID,
                                   String postID, String content,BufferedWriter writer) throws IOException{
        if (!users.containsKey(userID)){
            writer.write("Some error occurred in create_post.\n");
            return;
        }
        // if the post already exists
        if (posts.containsKey(postID)){
            writer.write("Some error occurred in create_post.\n");
            return;
        }
        User user = users.get(userID);
        Post post = new Post(postID, content, user);

        user.createPost(postID, content);
        posts.put(postID, post);
        writer.write(userID + " created a post with Id " + postID + ".\n");
    }

    /***
     * Marks a post as seen by a user
     * @param users The hashmap of all users in platform
     * @param posts The hashmap of all posts in platform
     * @param userID The ID of the user who has seen the post
     * @param postID The ID of the post that has been seen
     * @param writer BufferedWriter to write output messages to the output file.
     * @throws IOException If an I/O error occurs.
     */
    private static void seePost(MyHashMap<String, User> users, MyHashMap<String, Post> posts, String userID,
                                String postID, BufferedWriter writer) throws  IOException{
        if (!users.containsKey(userID) || !posts.containsKey(postID)){
            writer.write("Some error occurred in see_post.\n");
            return;
        }
        User user = users.get(userID);
        Post post = posts.get(postID);
        user.addSeen(post);
        writer.write(userID + " saw " + postID + ".\n");
    }

    /***
     * Marks all posts from a specific user as seen by another user.
     * @param users The hashmap of all users in platform
     * @param viewerID The ID of the user viewing the posts
     * @param viewedID The ID of the user whose posts are being viewed
     * @param writer BufferedWriter to write output messages to the output file.
     * @throws IOException If an I/O error occurs.
     */
    private static void seeAllPostsFromUser(MyHashMap<String, User> users, String viewerID, String viewedID, BufferedWriter writer) throws  IOException{
        if (!users.containsKey(viewerID) || !users.containsKey(viewedID)){
            writer.write("Some error occurred in see_all_posts_from_user.\n");
            return;
        }
        User viewer = users.get(viewerID);
        User viewed = users.get(viewedID);
        for (Post post: viewed.getPosts().values()){
            viewer.addSeen(post);
        }
        writer.write(viewerID + " saw all posts of " + viewedID + ".\n");
    }

    /***
     * Toggles the like status of a post for a user
     * @param users The hashmap of all users in platform
     * @param posts The hashmap of all posts in platform
     * @param userID The ID of the user toggling the like
     * @param postID The ID of the post to be liked or unliked
     * @param writer BufferedWriter to write output messages to the output file.
     * @throws IOException If an I/O error occurs.
     */
    private static void toggleLike(MyHashMap<String, User> users, MyHashMap<String, Post> posts, String userID,
                                   String postID, BufferedWriter writer) throws  IOException{
        if (!users.containsKey(userID) || !posts.containsKey(postID)){
            writer.write("Some error occurred in toggle_like.\n");
            return;
        }
        User user = users.get(userID);
        Post post = posts.get(postID);

        // if the user has already liked the post unlike it, otherwise like it
        if (post.isLikedBy(user)){
            post.unlike(user);
            writer.write(userID + " unliked " + postID + ".\n");
        } else {
            post.like(user);
            writer.write(userID + " liked " + postID + ".\n");
        }
        user.addSeen(post); // mark the post as seen after like/unlike

    }

    /***
     * Generates a feed for a user.
     * @param users The hashmap of all users in platform
     * @param posts The hashmap of all posts in platform
     * @param userID The ID of the user whose feed is generated
     * @param num The number of posts to include in the feed
     * @param writer BufferedWriter to write output messages to the output file.
     * @throws IOException If an I/O error occurs.
     */
    private static void generateFeed(MyHashMap<String, User> users, MyHashMap<String, Post> posts, String userID,
                                     int num, BufferedWriter writer) throws IOException{
        if (!users.containsKey(userID)){
            writer.write("Some error occurred in generate_feed.\n");
            return;
        }
        User user = users.get(userID);
        MyPriorityQueue<Post> feedHeap = user.getFeed(posts);
        int count = 0; // keeps the post count in feed

        writer.write("Feed for " + userID + ":\n");
        while (!feedHeap.isEmpty() && count < num){
            Post post = feedHeap.poll(); // gets the most liked post first
            if (!user.getSeenPosts().containsKey(post.getPostID()) && !post.getAuthor().equals(user)){
                writer.write("Post ID: " + post.getPostID() + ", Author: " + post.getAuthor().getUserID() + ", Likes: " + post.getLikeCount() + "\n");
                count++;
            }
        }

        if (count < num){
            writer.write("No more posts available for " + userID + ".\n");
        }

    }

    /***
     * Scrolls through a user's feed, liking posts as specified by the likeActions list.
     * @param users The hashmap of all users in platform
     * @param posts The hashmap of all posts in platform
     * @param userID The ID of the user scrolling through the feed
     * @param num The number of posts the user scrolls through
     * @param likeActions A list of integers indicating whether the user likes each post (1 for like, 0 for no like)
     * @param writer BufferedWriter to write output messages to the output file.
     * @throws IOException If an I/O error occurs.
     */
    private static void scrollThroughFeed(MyHashMap<String, User> users, MyHashMap<String, Post> posts, String userID, int num,
                                          ArrayList<Integer> likeActions, BufferedWriter writer) throws IOException{
        if (!users.containsKey(userID)){
            writer.write("Some error occurred in scroll_through_feed.\n");
            return;
        }
        User user = users.get(userID);
        MyPriorityQueue<Post> feedHeap = user.getFeed(posts);
        int count = 0; // keeps the seen post count in feed

        writer.write(userID + " is scrolling through feed:\n");
        while (!feedHeap.isEmpty() && count < num){
            Post post = feedHeap.poll(); // gets the most liked post first
            if (!post.isSeenBy(user) && !post.getAuthor().equals(user)){
                if (likeActions.get(count) == 1){
                    post.like(user);
                    writer.write(userID + " saw " + post.getPostID() + " while scrolling and clicked the like button.\n");
                } else {
                    writer.write(userID + " saw " + post.getPostID() + " while scrolling.\n");
                }
            }
            user.addSeen(post);
            count++;
        }
        if (count < num){
            writer.write("No more posts in feed.\n");
        }

    }

    /***
     * Sorts all posts made by a user in descending order based on likes.
     * If the like counts are equal, posts are sorted lexicographically by post ID.
     * @param users   The hash map of all users in the platform.
     * @param userID  The ID of the user whose posts are to be sorted.
     * @param writer  BufferedWriter to write output messages to the output file.
     * @throws IOException If an I/O error occurs.
     */
    private static void sortPosts(MyHashMap<String, User> users, MyHashMap<String, Post> posts, String userID, BufferedWriter writer) throws IOException{
        if (!users.containsKey(userID)){
            writer.write("Some error occurred in sort_posts.\n");
            return;
        }
        User user = users.get(userID);
        ArrayList<Post> userPosts = user.getPosts().values();

        if (userPosts.isEmpty()){
            writer.write("No posts from " + userID + ".\n");
            return;
        }
        MyPriorityQueue<Post> sortHeap = new MyPriorityQueue<>(userPosts.size() + 1);
        for (Post post : userPosts){
            post = posts.get(post.getPostID()); // get the post in the global hashmap instead of a copy of it
            sortHeap.add(post);
        }
        writer.write("Sorting " + userID + "'s posts:\n");
        while (!sortHeap.isEmpty()){
            Post post = sortHeap.poll();
            writer.write(post.getPostID() + ", Likes: " + post.getLikeCount() + "\n");
        }
    }
}
