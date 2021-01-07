/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {

    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles.clone();
    }

    // string representation of this board
    public String toString() {
        String result = String.valueOf(this.dimension()) + "\n";
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                result += " " + String.valueOf(this.tiles[i][j]);
            }
            result += "\n";
        }
        return result;
    }

    // board dimension n
    public int dimension() {
        return this.tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int size = this.dimension();
        int[][] goalTiles = buildGoalTiles(size);
        int result = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int goalValue = goalTiles[i][j];
                if (goalValue == 0) continue;

                if (goalValue != this.tiles[i][j]) {
                    result++;
                }
            }
        }
        return result;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int size = this.dimension();
        int result = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int val = this.tiles[i][j];
                if (val == 0) continue;

                int goalX = (val - 1) / size;
                int goalY = val - goalX * size - 1;
                int distX = Math.abs(goalX - i);
                int distY = Math.abs(goalY - j);
                result += distX + distY;
            }
        }
        return result;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int[][] goalTiles = buildGoalTiles(this.dimension());
        return Arrays.equals(this.tiles, goalTiles);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (!y.getClass().equals(Board.class)) { return false; }
        if (((Board) y).dimension() != this.dimension()) { return false; }
        return Arrays.equals(this.tiles, ((Board) y).tiles);
    }

    // // all neighboring boards
    // public Iterable<Board> neighbors() {}
    //
    // // a board that is obtained by exchanging any pair of tiles
    // public Board twin() {}

    // unit testing (not graded)
    public static void main(String[] args) {
        // check construction and 'toString'
        StdOut.println(" --- toString() ---");
        int[][] tiles = { {0, 1, 2}, {3, 4, 5}, {6, 7, 8} };
        Board board = new Board(tiles);
        StdOut.print(board.toString());
        // check comparison
        StdOut.println(" --- isGoal() ---");
        StdOut.println(board.isGoal());
        // check distances
        StdOut.println(" --- distances ---");
        int [][] distTiles = { {8, 1, 3}, {4, 0, 2}, {7, 6, 5} };
        Board distBoard = new Board(distTiles);
        StdOut.println("hamming = " + distBoard.hamming());
        StdOut.println("manhattan = " + distBoard.manhattan());
    }

    // Private helpers

    private static int[][] buildGoalTiles(int size) {
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = i * size + (j + 1);
            }
        }
        result[size - 1][size - 1] = 0;
        return result;
    }
}
