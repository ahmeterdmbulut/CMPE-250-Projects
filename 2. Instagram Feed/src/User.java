/***
 * Represents a user in Instagram.
 * Each user has an ID, followers, followings, posts and a history of seen posts.
 */
public class User {
    private String userID;
    private MyHashMap<String, User> followers;
    private MyHashMap<String, User> following;
    private MyHashMap<String, Post> posts;
    private MyHashMap<String, Post> seenPosts; // hashmap for keeping track of posts user saw

    /***
     * Constructor to initialize a user with given ID.
     * Initializes empty maps for followers, followings, posts and seen posts
     * @param userID The unique ID of the user
     */
    public User(String userID){
        this.userID = userID;
        this.followers = new MyHashMap<>(10);
        this.following = new MyHashMap<>(10);
        this.posts = new MyHashMap<>(10);
        this.seenPosts = new MyHashMap<>(10);
    }

    /***
     * Makes the current user follow another user
     * @param user The user to follow
     */
    public void follow(User user){
        if (!following.containsKey(user.getUserID())){
            following.put(user.getUserID(), user);
            user.addFollower(this); // when current user follows someone, he/she becomes a follower of the other user
        }
    }

    /***
     * Makes the current user unfollow another user
     * @param user The user to unfollow
     */
    public void unfollow(User user){
        if (following.containsKey(user.getUserID())){
            following.remove(user.getUserID());
            user.removeFollower(this);
        }
    }

    /***
     * Adds a follower to the current user
     * @param user The user who started following this user
     */
    private void addFollower(User user) {
        if (!followers.containsKey(user.getUserID())){
            followers.put(user.getUserID(), user);
        }
    }

    /***
     * Removes a follower from the current user
     * @param user The user who stopped following this user
     */
    private void removeFollower(User user){
        if (following.containsKey(user.getUserID())){
            followers.remove(user.getUserID());
        }
    }

    /***
     * Creates a new post for the user.
     * @param postID The unique ID of the post.
     * @param content The content of the post.
     */
    public void createPost(String postID, String content){
        if (!posts.containsKey(postID)){
            Post newPost = new Post(postID, content, this);
            posts.put(postID, newPost);
        }
    }

    /***
     * Gets all posts made by the user
     * @return A map containing all posts made by the user.
     */
    public MyHashMap<String, Post> getPosts() {
        return posts;
    }

    /***
     * Generates a feed for the user containing posts from users they follow.
     * Only posts that haven't been seen by the user are included in user's feed.
     * @param allPosts A map of all posts in the platform.
     * @return A priority queue containing the posts in the user's feed.
     */
    public MyPriorityQueue<Post> getFeed(MyHashMap<String, Post> allPosts) {
        MyPriorityQueue<Post> userFeed = new MyPriorityQueue<>(10);
        for (User followedUser : following.values()){
            for (Post post : followedUser.getPosts().values()) {
                post = allPosts.get(post.getPostID()); // in the case of posts with same IDs refer to different post instances,
                                                       // make the post instance the same with the post in global posts hashmap
                if (!seenPosts.containsKey(post.getPostID())){
                    userFeed.add(post);
                }
            }
        }
        return userFeed;
    }

    /***
     * Gets the user ID.
     * @return The user ID.
     */
    public String getUserID() {
        return userID;
    }

    /***
     * Gets the map of users that the current user is following.
     * @return A map containing all users that the current user is following.
     */
    public MyHashMap<String, User> getFollowing(){
        return following;
    }

    /***
     * Adds a post to the user's seen posts.
     * @param post The post to be marked as seen.
     */
    public void addSeen(Post post){
        if (!seenPosts.containsKey(post.getPostID())){
            seenPosts.put(post.getPostID(), post);
            post.markSeen(this);
        }
    }

    /***
     * Gets all posts that the user has seen.
     * @return A map containing all seen posts.
     */
    public MyHashMap<String, Post> getSeenPosts() {
        return seenPosts;
    }
}
