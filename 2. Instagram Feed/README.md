# Instagram Feed Manager

This project is a simulation of an Instagram Feed Manager, developed as part of the CmpE 250 Data Structures and Algorithms course. The application is designed to handle user creation, post management, user interactions, and dynamic feed generation, mimicking the core functionalities of Instagram's early feed management system.

## Features

- **User Management:** Create users, follow/unfollow users, and manage user connections.
- **Post Management:** Create posts with unique IDs and content, view posts, and like/unlike posts.
- **Feed Generation:** Generate personalized feeds for users based on likes and unseen posts.
- **Sorting Mechanism:** Sort posts by likes and lexicographical order when likes are the same.
- **Data Structures:** Utilizes custom implementations of HashMap and Priority Queue for optimized performance.

## How to Run

1. **Compilation:**
```bash
javac *.java
```

2. **Execution:**
```bash
java Main <input_file> <output_file>
```
- `<input_file>`: File containing input commands.
- `<output_file>`: File where the log of operations will be saved.

## Example Usage

Example input file:
```text
create_user user1
create_post user1 post1 Hello World
follow_user user1 user2
see_post user1 post1
```

Expected output in log file:
```text
Created user with Id user1.
user1 created a post with Id post1.
user1 followed user2.
user1 saw post1.
```
## Code Structure

- **Main.java:** Handles input parsing, command execution, and output logging.
- **MyHashMap.java:** Custom HashMap implementation for fast key-value storage and retrieval.
- **MyPriorityQueue.java:** Custom Priority Queue implemented using a max-heap for sorting posts.
- **Post.java:** Represents a post with ID, content, author, likes, and views.
- **User.java:** Represents a user with ID, followers, following, posts, and seen posts.
