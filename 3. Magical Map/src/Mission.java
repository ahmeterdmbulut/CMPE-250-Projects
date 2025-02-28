import java.util.ArrayList;

/***
 * Represents a mission with a list of objectives to complete.
 * It includes a starting point, a radius of visibility, and a list of objectives
 */
public class Mission {
    private int radius; // radius of the line of sight
    private int startX; // x coordinate of the starting node
    private int startY; // y coordinate of the starting node
    private ArrayList<Objective> objectives; // a list of objectives to reach during the travel

    /***
     * Constructs a Mission with a given radius, starting coordinates and an empty list of objectives
     * @param radius The radius of the line of sight for revealing nodes
     * @param startX x coordinate of the starting node
     * @param startY y coordinate of the starting node
     */
    public Mission(int radius, int startX, int startY){
        this.radius = radius;
        this.startX = startX;
        this.startY = startY;
        this.objectives = new ArrayList<>();
    }

    /***
     * Adds an objective to the mission
     * @param x x coordinate of the objective
     * @param y y coordinate of the objective
     * @param options A list of integers representing node types that can be made passable by wizard
     */
    public void addObjective(int x, int y, ArrayList<Integer> options){
        objectives.add(new Objective(x, y, options));
    }

    /***
     * Gets the radius of the line of sight
     * @return the radius of the line of sight
     */
    public int getRadius() {
        return radius;
    }

    /***
     * Gets the x coordinate of the starting node
     * @return x coordinate of the starting node
     */
    public int getStartX() {
        return startX;
    }

    /***
     * Gets th y coordinate of the starting node
     * @return y coordinate of the starting node
     */
    public int getStartY() {
        return startY;
    }

    /***
     * Gets the list of objectives in the mission
     * @return An ArrayList of objectives
     */
    public ArrayList<Objective> getObjectives() {
        return objectives;
    }
}
