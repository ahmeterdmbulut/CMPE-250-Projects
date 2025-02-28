import java.io.*;
import java.util.*;

/***
 * Main class to execute the mission. Reads input files, processes objectives, and writes the outputs.
 */
public class Main {
    /***
     * The main method to handle operations.
     * @param args Command-line arguments: node file, edge file, objective file, and output file
     * @throws IOException if any I/O errors occur
     */
    public static void main(String[] args) throws IOException {
//        long start = System.currentTimeMillis();

        // Input file paths
        String nodeFile = args[0];
        String edgeFile = args[1];
        String objectiveFile = args[2];
        String outputFile = args[3];

        // Parse input files and initialize land and mission
        Land land = parseNodeFile(nodeFile);
        parseEdgeFile(edgeFile, land);
        Mission mission = parseObjectiveFile(objectiveFile);

        // Writer to handle output
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        executeMission(land, mission, writer); // perform operations

        writer.close();
//        long end = System.currentTimeMillis();
//        double duration = (double) (end - start) / 1000;
//        System.out.println(duration + " seconds last");
    }

    /***
     * Parses the node file to create the land
     * @param nodeFile Path to the node input file
     * @return A land object initialized with nodes
     * @throws IOException if any I/O errors occur
     */
    private static Land parseNodeFile(String nodeFile) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(nodeFile));
        String[] dimensions = reader.readLine().split(" ");
        int width = Integer.parseInt(dimensions[0]);
        int height = Integer.parseInt(dimensions[1]);

        Land land = new Land(width, height);

        String line;
        // add nodes to the land
        while ((line = reader.readLine()) != null){
            String[] parts = line.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int type = Integer.parseInt(parts[2]);
            land.addNode(x, y, type);
        }
        reader.close();
        return land;
    }

    /***
     * Parses the edge file to load travel time between nodes
     * @param edgeFile Path to the edge input file
     * @param land The land object to which travel times will be added
     * @throws IOException if any I/O errors occur
     */
    private static void parseEdgeFile(String edgeFile, Land land) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(edgeFile));
        String line;
        // load travel times between nodes
        while((line = reader.readLine()) != null){
            String[] parts = line.split(" ");
            String[] coordinates = parts[0].split(",");
            String[] firstNode = coordinates[0].split("-");
            String[] secondNode = coordinates[1].split("-");
            int x1 = Integer.parseInt(firstNode[0]);
            int y1 = Integer.parseInt(firstNode[1]);
            int x2 = Integer.parseInt(secondNode[0]);
            int y2 = Integer.parseInt(secondNode[1]);
            double time = Double.parseDouble(parts[1]);
            land.addTravelTime(x1, y1, x2, y2, time);
        }
        reader.close();
    }

    /***
     * Parses the objective file to extract objectives and starting node
     * @param objectiveFile Path to the objective input file
     * @return A mission object initialized with objectives and starting node
     * @throws IOException if any I/O errors occur
     */
    private static Mission parseObjectiveFile(String objectiveFile) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(objectiveFile));
        int radius = Integer.parseInt(reader.readLine());
        String[] startingCoordinates = reader.readLine().split(" ");
        int startX = Integer.parseInt(startingCoordinates[0]);
        int startY = Integer.parseInt(startingCoordinates[1]);

        Mission mission = new Mission(radius, startX, startY); // Initialize mission object

        String line;
        // add objectives to the mission object
        while ((line = reader.readLine()) != null){
            String[] parts = line.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            // check if the wizard offers any help or not
            if (parts.length == 2){
                mission.addObjective(x, y, null);
            } else {
                ArrayList<Integer> options = new ArrayList<>();
                for (int i = 2; i < parts.length; i++){
                    options.add(Integer.parseInt(parts[i]));
                }
                mission.addObjective(x, y, options);
            }
        }
        reader.close();
        return mission;
    }

    /***
     * Implements Dijkstra's shortest path algorithm to calculate the optimal path from start to end
     * @param land The Land object
     * @param start Starting node
     * @param end Target node
     * @return A list of nodes representing the shortest path from start to end
     */
    public static ArrayList<Node> dijkstra(Land land, Node start, Node end){
        // Priority queue to prioritize the nodes with the shortest distance
        MyPriorityQueue<PathNode> pq = new MyPriorityQueue<>(land.getWidth() * land.getHeight());
        // Maps to store distances from start node and previous nodes for path reconstruction
        MyHashMap<String, Double> distances = new MyHashMap<>(land.getWidth() * land.getHeight());
        MyHashMap<String, Node> previous = new MyHashMap<>(land.getWidth() * land.getHeight());

        // Possible movement directions
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        String startKey = start.getX() + "-" + start.getY();
        distances.put(startKey, 0.0);

        pq.add(new PathNode(start, 0));
        while (!pq.isEmpty()){
            PathNode current = pq.poll();
            Node currentNode = current.node;
            String currentKey = currentNode.getX() + "-" + currentNode.getY();


            // Skip already visited nodes
            if (currentNode.isVisited()){
                continue;
            }
            currentNode.setVisited(true);

            // If the target node is reached, break the loop
            if (currentNode == end){
                break;
            }

            // Explore neighbor nodes
            for (int[] dir : directions){
                int nx = currentNode.getX() + dir[0];
                int ny = currentNode.getY() + dir[1];

                // Check if the new coordinates are out of bounds or not
                if (nx < 0 || ny < 0 || nx >= land.getWidth() || ny >= land.getHeight()){
                    continue;
                }

                Node neighbor = land.getNode(nx, ny);

                // Skip nodes that are already visited or impassable
                if (!neighbor.isPassable() || neighbor.isVisited()){
                    continue;
                }

                String neighborKey = nx + "-" + ny;

                // Calculate the new distance to the neighbor node
                double newDistance = distances.getOrDefault(currentKey, Double.MAX_VALUE) +
                        land.getTravelTime(currentNode.getX(), currentNode.getY(), neighbor.getX(), neighbor.getY());

                // Update distance and previous node if a shorter path is found
                if (newDistance < distances.getOrDefault(neighborKey, Double.MAX_VALUE)){
                    distances.put(neighborKey, newDistance);
                    previous.put(neighborKey, currentNode);
                    pq.add(new PathNode(neighbor, newDistance));

                }
            }
        }

        // Reconstruct the path by backtracking from end node
        ArrayList<Node> path = new ArrayList<>();
        Node step = end;
        while (step != null){
            path.add(0, step);
            step = previous.get(step.getX()+ "-" + step.getY());
        }
        return path;
    }

    /***
     * Resets the visited status for all nodes in the land
     * @param land The Land object
     */
    public static void resetVisited(Land land){
        for (int x = 0; x < land.getWidth(); x++){
            for (int y = 0; y < land.getHeight(); y++){
                Node node = land.getNode(x, y);
                if (node != null){
                    node.setVisited(false);
                }
            }
        }
    }

    /***
     * Executes the mission by navigating through objectives
     * @param land The land
     * @param mission The mission containing objectives and starting point
     * @param writer BufferedWriter to write the output logs
     * @throws IOException if any I/O errors occur
     */
    public static void executeMission(Land land, Mission mission, BufferedWriter writer) throws IOException{
        Node current = land.getNode(mission.getStartX(), mission.getStartY());
        int radius = mission.getRadius(); // radius of line of sight
        int objectiveIndex = 1;

        land.revealNodes(current, radius); // reveal nodes within the starting radius

        ArrayList<Objective> objectives = mission.getObjectives(); // a list to keep track of objectives
        for (int i = 0; i < objectives.size(); i++){
            Objective currentObjective = objectives.get(i);
            Node target = land.getNode(currentObjective.getX(), currentObjective.getY());
            int chosenOption = -1;

            resetVisited(land);
            ArrayList<Node> path = dijkstra(land, current, target);

            int pathIndex = 1;
            // Move through the path and write outputs
            while (pathIndex < path.size()){
                Node next = path.get(pathIndex);

                writer.write("Moving to " + next.getX() + "-" + next.getY() + "\n");
                land.revealNodes(next, radius);

                // Check if the path has any impassable node
                boolean pathValid = true;
                for (int j = pathIndex; j < path.size(); j++){
                    if (!path.get(j).isPassable()){
                        pathValid = false;
                        writer.write("Path is impassable!\n");
                        break;
                    }
                }

                // If path is not valid then create a new path from that point
                if (!pathValid){
                    resetVisited(land);
                    path = dijkstra(land, next, target);
                    pathIndex = 1;
                    continue;
                }
                current = next;
                pathIndex++;


            }
            if (i + 1 < objectives.size()){
                Objective nextObjective = objectives.get(i + 1);
                if (currentObjective.getOptions() != null){
                    chosenOption = wizardHelp(land, current, land.getNode(nextObjective.getX(), nextObjective.getY()),
                            currentObjective.getOptions());
                }
            }
            writer.write("Objective " + objectiveIndex + " reached!\n");
            if (chosenOption != -1){
                writer.write("Number " + chosenOption + " is chosen!\n");
            }
            objectiveIndex++;
            current = target;
        }
    }

    /***
     * Determines which option is the best among wizard's offers for reaching the next objective
     * @param land The land
     * @param start Starting node
     * @param end Target node
     * @param options A list of node types that can be made passable
     * @return The option that results in the fastest path
     */
    public static int wizardHelp(Land land, Node start, Node end, ArrayList<Integer> options){
        double minTime = Double.MAX_VALUE;
        int bestOption = -1;

        for (int option : options){
            //Temporarily make all nodes with the type of current option passable
            ArrayList<Node> changedNodes = new ArrayList<>();
            for (int x = 0; x < land.getWidth(); x++){
                for (int y = 0; y < land.getHeight(); y++){
                    Node node = land.getNode(x, y);
                    if (node.getType() == option){
                        if (!node.isPassable()){
                            node.setPassable(true);
                            changedNodes.add(node);
                        }
                    }

                }
            }
            resetVisited(land); // reset visited nodes for next dijkstra call
            ArrayList<Node> path = dijkstra(land, start, end);
            double pathTime = calculatePathTime(path, land);

            // Change back the modified nodes' passable status
            for (int i = 0; i < changedNodes.size(); i++){
                Node node = changedNodes.get(i);
                if (node.isPassable()){
                    node.setPassable(false);
                }
            }

            if (pathTime < minTime){
                minTime = pathTime;
                bestOption = option;
            }
        }
        //Permanently make the best option passable
        for (int x = 0; x < land.getWidth(); x++){
            for (int y = 0; y < land.getHeight(); y++){
                Node node = land.getNode(x, y);
                if (node.getType() == bestOption){
                    node.setPassable(true);
                    node.setType(0);
                }
            }
        }
        return bestOption;
    }

    /***
     * Calculates the total time of the path
     * @param path The list of nodes representing the path
     * @param land The Land object
     * @return The total travel time
     */
    public static double calculatePathTime(ArrayList<Node> path, Land land){
        // if there is no path return infinity
        if (path == null || path.isEmpty()){
            return Double.MAX_VALUE;
        }
        double totalTime = 0;
        for (int i = 0; i < path.size() - 1; i++){
            totalTime += land.getTravelTime(path.get(i).getX(), path.get(i).getY(), path.get(i + 1).getX(), path.get(i + 1).getY());
        }
        return totalTime;
    }

    /***
     * A helper class representing a node in the priority queue with its associated cost
     * This is used for Dijkstra's algorithm
     */
    private static class PathNode implements Comparable<PathNode>{
        Node node;
        double cost; // The cost (time in this case) to reach this node

        /***
         * Constructs a PathNode object
         * @param node The node being represented
         * @param cost The time to reach this node
         */
        public PathNode(Node node, double cost){
            this.node = node;
            this.cost = cost;
        }

        /***
         * Compares two PathNode objects based on their costs (times)
         * Used for prioritizing nodes in the priority queue
         * @param other the object to be compared.
         * @return A negative integer, zero, or a positive integer as this cost is less than, equal to, or greater than
         * the specified cost
         */
        @Override
        public int compareTo(PathNode other) {
            return Double.compare(this.cost, other.cost);
        }
    }
}
