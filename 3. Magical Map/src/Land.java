/***
 * Represents the land with nodes, travel times, and node visibility functionalities
 */
public class Land {
    private int width; // width of the land
    private int height; // height of the land
    private Node[][] grid; // 2D grid representing nodes in land
    private MyHashMap<String, Double> travelTimes; // A map storing travel times between connected nodes

    /***
     * Constructs the Land with specified dimensions.
     * @param width width of the land
     * @param height height of the land
     */
    public Land(int width, int height){
        this.width = width;
        this.height = height;
        this.grid = new Node[width][height];
        this.travelTimes = new MyHashMap<>(100);
    }

    /***
     * Adds a node to the land
     * @param x x coordinate of the node
     * @param y y coordinate of the node
     * @param type type of the node
     */
    public void addNode(int x, int y, int type){
        grid[x][y] = new Node(x, y, type);
    }

    /***
     * Retrieves a node at the specified coordinates
     * @param x x coordinate of the node
     * @param y y coordinate of the node
     * @return The node at (x, y), or null if coordinates are out of bounds
     */
    public Node getNode(int x, int y){
        // boundary check
        if (x < 0 || x >= width || y < 0 || y >= height){
            return null;
        }
        return grid[x][y];
    }

    /***
     * Adds travel time between two connected nodes
     * @param x1 x coordinate of the first node
     * @param y1 y coordinate of the first node
     * @param x2 x coordinate of the second node
     * @param y2 y coordinate of the second node
     * @param time The travel time between the nodes
     */
    public void addTravelTime(int x1, int y1, int x2, int y2, double time){
        String key = x1 + "-" + y1 + "," + x2 + "-" + y2;
        travelTimes.put(key, time);
        key = x2 + "-" + y2 + "," + x1 + "-" + y1; // add reverse travel time since the graph is undirected
        travelTimes.put(key, time);
    }

    /***
     * Gets the travel time between two connected nodes
     * @param x1 x coordinate of the first node
     * @param y1 y coordinate of the first node
     * @param x2 x coordinate of the second node
     * @param y2 y coordinate of the second node
     * @return The travel time between two nodes or Double.MAX_VALUE if not directly connected
     */
    public double getTravelTime(int x1, int y1, int x2, int y2){
        String key = x1 + "-" + y1 + "," + x2 + "-" + y2;
        if (travelTimes.get(key) != null){
            return travelTimes.get(key);
        }
        return Double.MAX_VALUE;
    }

    /***
     * Reveals nodes within a certain radius around the current node
     * @param current The current node whose surroundings are to be revealed
     * @param radius The radius of line of sight
     */
    public void revealNodes(Node current, int radius){
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++){
                int nx = current.getX() + i;
                int ny = current.getY() + j;
                if (nx >= 0 && ny >= 0 && nx < width && ny < height){
                    Node neighbor = getNode(nx, ny);
                    double distance = Math.sqrt(i * i + j * j);
                    if (distance <= radius && !neighbor.isKnown()){
                        neighbor.reveal();
                    }
                }
            }
        }
    }

    /***
     * Gets the width of the land
     * @return The width
     */
    public int getWidth() {
        return width;
    }

    /***
     * Gets the height of the land
     * @return The height
     */
    public int getHeight() {
        return height;
    }
}
