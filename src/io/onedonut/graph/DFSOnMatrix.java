package io.onedonut.graph;

import java.util.*;

/**
 * Created by pamelactan on 2/3/17.
 */
public class DFSOnMatrix {

    public void test() {
        // Create matrix
        int[][] M = { { 0, 1, 2 }, {3, 4, 5}, {6, 7, 8} };

        assert search(M, Arrays.asList(0, 1, 2, 5, 4, 3, 6, 7, 8));
        assert search(M, Arrays.asList(0, 1, 4, 3, 6, 7, 8, 5));

        assert searchIter(M, Arrays.asList(0, 1, 2, 5, 4, 3, 6, 7, 8));
        assert searchIter(M, Arrays.asList(0, 1, 4, 3, 6, 7, 8, 5));
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

    public List<Boolean> containsSequences(int[][] M, List<List<Integer>> sequences) {
        List<Boolean> acc = new ArrayList<>(sequences.size());
        Map<Integer, List<Coordinate>> cachedSearches = new HashMap<>();

        for (List<Integer> sequence : sequences) {
            acc.add(containsSequencesHelper(M, sequence, cachedSearches));
        }

        return acc;
    }

    public boolean containsSequencesHelper(int[][] M, List<Integer> sequence, Map<Integer, List<Coordinate>> cachedSearches) {
        Coordinate furthestSearch = new Coordinate(0, 0);
        if (cachedSearches.containsKey(sequence.get(0))) {
            for (Coordinate c : cachedSearches.get(sequence.get(0))) {
                if (c.i == M.length)
                    return false;

                furthestSearch = c;
                if (dfsIter(M, sequence, c))
                    return true;
            }
        }

        for (int i = furthestSearch.i; i < M.length; i++)
            for (int j = furthestSearch.j; j < M[0].length; j++) {
                if (M[i][j] == sequence.get(0)) {
                    Coordinate startingNode = new Coordinate(i, j);
                    if (cachedSearches.containsKey(sequence.get(0))) {
                        List<Coordinate> listOfCoordinates = new ArrayList<>();
                        listOfCoordinates.add(startingNode);
                        cachedSearches.put(sequence.get(0), listOfCoordinates);
                    } else
                        cachedSearches.get(sequence.get(0)).add(startingNode);

                    if (dfsIter(M, sequence, startingNode)) {
                        return true;
                    }
                }
            }

        if (cachedSearches.containsKey(sequence.get(0))) {
            cachedSearches.get(sequence.get(0)).add(new Coordinate(M.length, M[0].length));
        } else {
            List<Coordinate> coordinates = new ArrayList<>();
            coordinates.add(new Coordinate(M.length, M[0].length));
            cachedSearches.put(sequence.get(0), coordinates);
        }

        return false;
    }

    public boolean searchIter(int[][] M, List<Integer> sequence) {
        for (int i = 0; i < M.length; i++)
            for (int j = 0; j < M[0].length; j++) {
                if (M[i][j] == sequence.get(0)) {
                    Coordinate startingNode = new Coordinate(i, j);
                    if (dfsIter(M, sequence, startingNode))
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

    public boolean dfsIter(int[][] M, List<Integer> sequence, Coordinate startNode) {
        //
        LinkedList<Coordinate> stack = new LinkedList<>();
        Set<Long> exploredNodes = new HashSet<>();
        int[][] shift = { {-1, 0}, {0, 1}, {1, 0}, {0, -1} };

        startNode.pos = 0;
        stack.push(startNode);

        while(!stack.isEmpty()) {
            //
            Coordinate curNode = stack.pop();
            int i = curNode.i;
            int j = curNode.j;

            if (M[i][j] == sequence.get(curNode.pos)) {
                // We've matched the final integer in the sequence
                if (curNode.pos == sequence.size() - 1)
                    return true;

                // Keep exploring the neighbors
                for (int[] s : shift) {
                    Coordinate nextNode = new Coordinate(i + s[0], j + s[1]);
                    nextNode.pos = curNode.pos + 1;

                    if (withinBounds(nextNode, M) && !exploredNodes.contains(hash(nextNode))) {
                        stack.push(nextNode);
                        exploredNodes.add(hash(curNode));
                    }

                }
            } else {
                exploredNodes.remove(hash(curNode));
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
        int pos;

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
