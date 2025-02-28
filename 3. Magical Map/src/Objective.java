import java.util.ArrayList;

/***
 * Represents an objective in the mission, defined by its coordinates and options that can be made passable.
 */
public class Objective{
    private int x, y; // the coordinates of the objective
    private ArrayList<Integer> options; // list of options (type of nodes) that the wizard can turn passable

    /***
     * Constructs an Objective with given coordinates and options
     * @param x x coordinate of the objective
     * @param y y coordinate of the objective
     * @param options A list of integers representing node types that can be made passable
     */
    public Objective(int x, int y, ArrayList<Integer> options){
        this.x = x;
        this.y = y;
        this.options = options;
    }

    /***
     * Gets the x coordinate of the objective
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /***
     * Gets the y coordinate of the objective
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /***
     * Gets the list of options representing types of nodes that can be turned to passable
     * @return An ArrayList of integers representing node types
     */
    public ArrayList<Integer> getOptions() {
        return options;
    }
}