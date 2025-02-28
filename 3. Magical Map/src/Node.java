/***
 * Represents a node in the land with coordinates, type, visibility, and passability conditions.
 */
public class Node {
    private int x, y; // coordinates
    private int type; // type of node (0: passable, 1: impassable, >=2 seems passable until becomes in the line of sight)
    private boolean known; // indicates if the type of the node is known
    private boolean passable; // indicates if the node is passable
    private boolean visited; // indicates if the node is visited

    /***
     * Constructs a node with given coordinates and type.
     * Initializes the known and passable status according to type
     * @param x x coordinate of the node
     * @param y y coordinate of the node
     * @param type The type of the node (0: passable, 1: impassable, >=2 seems passable until becomes in the line of sight)
     */
    public Node(int x, int y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
        this.known = (type == 0 || type == 1); // nodes with type 0 and 1 are initially known
        this.passable = (type == 0 || type >= 2); // type >= 2 initially seems passable but when it is in the radius it will change
        this.visited = false; // initially unvisited
    }

    /***
     * Checks if the node has been visited
     * @return True if visited, otherwise false
     */
    public boolean isVisited() {
        return visited;
    }

    /***
     * Sets the visited status of the node
     * @param visited True to mark the node as visited, false otherwise
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /***
     * Gets the x coordinate of the node
     * @return x coordinate of the node
     */
    public int getX() {
        return x;
    }

    /***
     * Gets the y coordinate of the node
     * @return y coordinate of the node
     */
    public int getY() {
        return y;
    }

    /***
     * Checks if the node is passable
     * @return True if passable, otherwise false
     */
    public boolean isPassable() {
        return passable;
    }

    /***
     * Checks if the node's type is known
     * @return True if known, otherwise false
     */
    public boolean isKnown() {
        return known;
    }

    /***
     * Gets the type of the node
     * @return The node type
     */
    public int getType() {
        return type;
    }

    /***
     * Reveals the node's type, marking it as known and updating its passable status
     */
    public void reveal(){
        this.known = true;
        this.passable = (type == 0); // only nodes with type 0 are passable
    }

    /***
     * Sets the passable status of the node
     * @param passable Tru to make the node passable, false otherwise
     */
    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    /***
     * Updates the type of the node
     * @param type The new type to set for the node
     */
    public void setType(int type) {
        this.type = type;
    }

}
