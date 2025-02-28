# Magical Map

This project is a simulation of the "Magical Map" game, developed as part of the CmpE 250 Data Structures and Algorithms course in Fall 2025. The application simulates navigating through a magical land using an enchanted map, solving objectives while encountering hidden surprises and challenges designed by the wizard Oz.

## Features

- **Grid-Based Navigation:** Navigate through a rectangular grid of nodes with varying types and passability.
- **Enchanted Map Logic:** Handles hidden and revealed nodes based on proximity, implementing a line of sight.
- **Dijkstra's Algorithm:** Utilized for shortest path calculation through the land.
- **Wizard's Help:** The wizard offers strategic options at specific objectives to make certain nodes passable.
- **Dynamic Route Recalculation:** Recalculates paths if new obstacles are discovered while traversing.
- **Custom Data Structures:** Includes custom implementations of HashMap (`MyHashMap.java`) and Priority Queue (`MyPriorityQueue.java`).

## How to Run

1. **Compilation:**
```bash
javac *.java
```

2. **Execution:**
```bash
java Main <input_file0> <input_file1> <input_file2> <output_file>
```
- `<input_file0>`: The land file containing grid information.
- `<input_file1>`: The file with travel times between nodes.
- `<input_file2>`: The mission file outlining objectives and wizard interactions.
- `<output_file>`: The output file where the results of the simulation are logged.

## Example Usage

Example input for the mission file:
```text
3
0 0
1 1
2 2 3 4 5
```

Expected output in the log file:
```text
Moving to 0-1
Moving to 1-1
Objective 1 reached!
Number 3 is chosen!
Moving to 2-3
Objective 2 reached!
```

## Code Structure

- **Main.java:** Executes the mission, handles input parsing, and manages outputs.
- **Land.java:** Represents the grid-based land with nodes and travel times.
- **Mission.java:** Manages objectives, starting points, and wizard interactions.
- **Node.java:** Represents each grid cell with properties like type, visibility, and passability.
- **Objective.java:** Defines mission objectives and available options when the wizard offers help.
- **MyHashMap.java:** Custom implementation of a HashMap for efficient data management.
- **MyPriorityQueue.java:** Custom priority queue using a min-heap for shortest path calculations.
