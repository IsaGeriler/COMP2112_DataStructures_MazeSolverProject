package Project.mazesolverproject;

public class MazeSolverProject {
    private static final int DEAD_END = -1;
    private static final int FREE_SPOT = 0;
    private static final int ROBOT = 2;
    private static final int VISITED = 3;

    public static void main(String[] args) throws InterruptedException {
        int grid = 5;
        int[][] maze = getMaze(grid);

        int startRow = 1;
        int startCol = 1;

        int endRow = ((2 * grid) - 1);
        int endCol = ((2 * grid) - 1);
        
        Stack<String> path = new Stack<>();

        maze[startRow][startCol] = ROBOT;
        MazeUtility.plotMaze(maze);

        // To do: starting from the coordinates [1,1], use the path stack to navigate in the maze and
        // find a way to [2*grid-1, 2*grid-1] coordinates
        // use the following code to print the maze at each step
        // MazeUtility.plotMaze(maze);
        // DO NOT change any of the given code

        solveMaze(maze, startRow, startCol, endRow, endCol, path);
    }
    
    public static int[][] getMaze(int grid) {
        MazeGenerator maze = new MazeGenerator(grid);
        String str = maze.toString();

        int[][] maze2D = MazeUtility.Convert2D(str);
        return maze2D;
    }

    // -- Logic behind the MazeSolverProject --
    // 1. First, start looking at the neighbours of the starting cell (1, 1).
    // 2. If the right neighbour is free, move the robot to that designated cell. Else, then look at the other
    //    neighbours (left, up, down) and move the robot accordingly if one of the mentioned neighbours are free.
    // 3. Else, then robot will retreat to the previous spot, and make that spot unreachable/dead-end.
    // 4. Once the robot advances to the next cell, mark the previous one as visited, so that the robot
    //    won't go back and forth mindlessly.
    // 5. Repeat the steps above until the robot reaches the south-east spot of the maze,
    //    which is (2 * grid - 1, 2 * grid - 1).
    // Note - The directions will be stored in a stack for printing, and for marking unreachable spots etc.
    // Note - Free Spot -> 0, Wall -> 1, Robot's current spot -> 2,
    //        Don't traverse this spot again -> -1, Mark this spot as traversed -> 3
    // Note - Row Offsets: ROW_OFFSET_DOWN (Robot goes down) = 1, ROW_OFFSET_UP (Robot goes up) = -1
    //        Column Offsets: COL_OFFSET_RIGHT (Robot goes right) = 1, COL_OFFSET_LEFT (Robot goes left) = 1
    // Note - Robot is also facing east direction (right), so by compass logic, that direction will be it's north.
    //        Hence, North - Right || South - Left, East - Down, West - Up

    private static final int ROW_OFFSET_DOWN = 1;
    private static final int ROW_OFFSET_UP = -1;
    private static final int COL_OFFSET_RIGHT = 1;
    private static final int COL_OFFSET_LEFT = -1;

    public static void solveMaze(int[][] maze, int startRow, int startCol, int endRow, int endCol, Stack<String> path)
            throws InterruptedException{
        Stack<String> coordinates = new Stack<>();
        boolean isSolving = true;

        while (isSolving) {
            System.out.println("Robot's current coordinates: [" + startRow + "," + startCol + "]\n");
            if (isCellFree(maze, startRow, 0, startCol, COL_OFFSET_RIGHT)) {
                move(maze, startRow, 0, startCol, COL_OFFSET_RIGHT, path, coordinates,"Right");
                startCol++;
            } else if (isCellFree(maze, startRow, ROW_OFFSET_DOWN, startCol, 0)) {
                move(maze, startRow, ROW_OFFSET_DOWN, startCol, 0, path, coordinates,"Down");
                startRow++;
            } else if (isCellFree(maze, startRow, 0, startCol, COL_OFFSET_LEFT)) {
                move(maze, startRow, 0, startCol, COL_OFFSET_LEFT, path, coordinates,"Left");
                startCol--;
            } else if (isCellFree(maze, startRow, ROW_OFFSET_UP, startCol, 0)) {
                move(maze, startRow, ROW_OFFSET_UP, startCol, 0, path, coordinates,"Up");
                startRow--;
            } else {
                coordinates.pop();
                markDeadEnd(maze, startRow, startCol);
                String direction = path.pop();

                switch (direction) {
                    // Enhanced switch case, applies for Java 17 but not Java 8
                    case "Right" -> maze[startRow][--startCol] = ROBOT;
                    case "Left" -> maze[startRow][++startCol] = ROBOT;
                    case "Down" -> maze[--startRow][startCol] = ROBOT;
                    case "Up" -> maze[++startRow][startCol] = ROBOT;
                }
            }
            // Thread usage has been used, which is covered in Operating Systems course, to make the program
            // sleep for two second before each iteration inside the while loop (solving)
            Thread.sleep(2000);
            MazeUtility.plotMaze(maze);
            if ((startRow == endRow && startCol == endCol)) {
                isSolving = false;
                coordinates.push("[" + startRow + "," + startCol + "]");
                System.out.println("Robot's current coordinates: [" + startRow + "," + startCol + "]");
                System.out.println("The robot has reached the south-east coordinate!\n");
            }
        }
        System.out.println(path);
        System.out.println(coordinates);
    }

    private static boolean isCellFree(int[][] maze, int row, int rowOffset, int col, int colOffset) {
        return  (row > 0 && row < maze.length) &&
                (col > 0 && col < maze[0].length) &&
                (maze[row + rowOffset][col + colOffset] == FREE_SPOT);
    }

    private static void move(int[][] maze, int row, int rowOffset, int col, int colOffset,
                             Stack<String> path, Stack<String> coordinates, String direction) {
        markVisited(maze, row, col);
        path.push(direction);
        coordinates.push("[" + row + "," + col + "]");
        maze[row + rowOffset][col + colOffset] = ROBOT;
    }

    private static void markVisited(int[][] maze, int row, int col) { maze[row][col] = VISITED; }
    private static void markDeadEnd(int[][] maze, int row, int col) { maze[row][col] = DEAD_END; }
}