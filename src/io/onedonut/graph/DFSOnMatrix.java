package io.onedonut.graph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pamelactan on 2/3/17.
 */
public class DFSOnMatrix {

    public void test() {
        // Create matrix
        int[][] M = { { 0, 1, 2 }, {3, 4, 5}, {6, 7, 8} };

        assert search(M, Arrays.asList(0, 1, 2, 5, 4, 3, 6, 7, 8));
        assert search(M, Arrays.asList(0, 1, 4, 3, 6, 7, 8, 5));
    }

    public boolean search(int[][] M, List<Integer> sequence) {
        for (int i = 0; i < M.length; i++)
            for (int j = 0; j < M[0].length; j++) {
                if (M[i][j] == sequence.get(0)) {
                    Coordinate startingNode = new Coordinate(i, j);
                    if (dfs(M, sequence, startingNode, 0, new HashSet<>()))
                        return true;
                }
            }

        return false;
    }

    /**
     * Check if this sequence appears in matrix M
     * @param M
     * @param sequence
     * @return
     */
    public boolean dfs(int[][] M, List<Integer> sequence, Coordinate curNode, int pos, Set<Long> exploredNodes) {
        //
        int[][] shift = { {-1, 0}, {0, 1}, {1, 0}, {0, -1} };
        int i = curNode.i;
        int j = curNode.j;

        if (pos == sequence.size() - 1 && sequence.get(pos) == M[i][j])
            return true;

        for (int[] shifter : shift) {
            //
            Coordinate nextNode = new Coordinate(i+shifter[0], j+shifter[1]);
            if (!exploredNodes.contains(hash(nextNode)) && withinBounds(nextNode, M)) {
                exploredNodes.add(hash(nextNode));
                if (sequence.get(pos) == M[i][j] && dfs(M, sequence, nextNode, pos + 1, exploredNodes)) {
                    return true;
                }

                exploredNodes.remove(hash(nextNode));
            }
        }

        return false;
    }

    boolean withinBounds(Coordinate c, int[][] M) {
        int i = c.i;
        int j = c.j;

        return i >= 0 && j >= 0 && i < M.length && j < M[0].length;
    }

    class Coordinate {
        int i;
        int j;

        Coordinate(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    static long hash(Coordinate c) {
        int i = c.i;
        int j = c.j;
        return i >= j ? i * i + i + j : i + j * j;
    }
}
