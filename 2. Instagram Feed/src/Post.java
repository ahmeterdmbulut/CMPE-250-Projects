/***
 * A class representing an Instagram post.
 * Each post has an ID, content, an author, and keeps track of likes and views.
 */
public class Post implements Comparable<Post> {
    private String postID;
    private String content;
    private User author;
    private int likeCount;
    private MyHashMap<String, User> likedBy; // hashmap for keeping track of users who liked the post
    private MyHashMap<String, User> seenBy; // hashmap for keeping track of users who saw the post

    /***
     * Constructor to initialize a post with an ID, content, and author.
     * @param postID The unique ID of the post.
     * @param content The content of the post.
     * @param author The author of the post.
     */
    public Post(String postID, String content, User author){
        this.postID = postID;
        this.content = content;
        this.author = author;
        this.likeCount = 0; // initially, the post has no like
        this.likedBy = new MyHashMap<>(10);
        this.seenBy = new MyHashMap<>(10);
    }

    /***
     * Gets the ID of the post.
     * @return The pot ID.
     */
    public String getPostID() {
        return postID;
    }

    /***
     * Gets the author of the post.
     * @return The author of the post.
     */
    public User getAuthor() {
        return author;
    }

    /***
     * Gets the number of likes the post has received.
     * @return The like count of the post.
     */
    public int getLikeCount() {
        return likeCount;
    }

    /***
     * Adds a like to the post from the specified user.
     * @param user The user who liked the post.
     */
    public void like(User user){
        if (!likedBy.containsKey(user.getUserID())){
            likedBy.put(user.getUserID(), user);
            likeCount++;
        }
    }

    /***
     * Removes a like from the post by the specified user.
     * @param user The user who unliked the post.
     */
    public void unlike(User user){
        if (likedBy.containsKey(user.getUserID())){
            likedBy.remove(user.getUserID());
            likeCount--;
        }
    }

    /***
     * Checks if the post is liked by the specified user.
     * @param user The user to check
     * @return True if the user has liked the post, false otherwise.
     */
    public boolean isLikedBy(User user){
        return likedBy.containsKey(user.getUserID());
    }

    /***
     * Marks the post as seen by the specified user.
     * @param user The user who has seen the post.
     */
    public void markSeen(User user){
        if (!seenBy.containsKey(user.getUserID())){
            seenBy.put(user.getUserID(), user);
        }
    }

    /***
     * Checks if the post has been seen by the specified user.
     * @param user The user to check.
     * @return True if the user has seen the post, false otherwise.
     */
    public boolean isSeenBy(User user){
        return seenBy.containsKey(user.getUserID());
    }

    /***
     * Compares this post to another post based on their like counts.
     * If the like counts are equal, compares lexicographically by post ID.
     * @param other the other post to be compared.
     * @return A negative integer, zero, or a positive integer as this post is less than,
     * equal to, or greater than the specified post.
     */
    @Override
    public int compareTo(Post other){
        if (this.likeCount == other.likeCount){
            return this.postID.compareTo(other.postID); // compares lexicographically when the like counts are equal
        }
        return Integer.compare(this.likeCount, other.likeCount);
    }
}
