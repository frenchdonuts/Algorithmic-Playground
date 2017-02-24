package io.onedonut.codefights.challenges;

import io.onedonut.Utils;

import java.util.*;

/**
 * Created by pamelactan on 2/11/17.
 */
public class SumInPerimeter {
    public void test() {
        int[][] grid = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

        assert sumInPerimeter(grid, 8) == 28;
        assert sumInPerimeter(grid, 6) == 17;
    }

    public int sumInPerimeter(int[][] grid, int p) {
        int res = Integer.MIN_VALUE;
        Set<Coordinate> explored = new HashSet<>();
        Integer[][][][][] cache = new Integer[grid.length][grid[0].length][p + 1][grid.length][grid[0].length];

        //private int maxSum(Coordinate startingCoordinate, int p, Window s, int[][] grid, Integer[][][][][] cache) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Coordinate startingCoordinate = new Coordinate(i, j);
                res = Math.max(res, maxSumAgain(startingCoordinate, p-4, grid, explored, new HashSet<>()));
                explored.add(startingCoordinate);
/*

                Window w = new Window().add(startingCoordinate, explored, grid);

                //res = Math.max(res, maxSum(startingCoordinate, p - 4, w, grid, cache));
                res = Math.max(res, maxSum(startingCoordinate, p - 4, w, grid, explored));
                explored.add(startingCoordinate);
*/

            }
        }

        return res;
    }

    private int maxSumAgain(Coordinate curCoordinate, int p, int[][] grid, Set<Coordinate> explored, Set<Coordinate> window) {
        //
        Set<Coordinate> neighbors = neighbors(curCoordinate, grid, explored);
        if (neighbors.isEmpty() || p <= 0)
            return 0;

        int max = Integer.MIN_VALUE;
        for (Coordinate neighbor : neighbors) {
            Set<Coordinate> explored_ = new HashSet<>(explored);
            explored_.add(curCoordinate);
            Set<Coordinate> window_ = new HashSet<>(window);
            window_.add(curCoordinate);

            int remainingP = p;
            if (window_.size() == 1)
                remainingP = p;
            else if (!isSurrounded(neighbor, window_))
                remainingP = p - 2;


            max = Math.max(max, maxSumAgain(neighbor, remainingP, grid, explored_, window_));
        }

        return max + grid[curCoordinate.i][curCoordinate.j];
    }

    private int maxSum(Coordinate s, int p, Set<Coordinate> window, Set<Coordinate> neighbors, Set<Coordinate> explored, int[][] grid) {
        //
        if (neighbors.isEmpty() || p <= 0) {
            System.out.println("Window: " + window);
            return grid[s.i][s.j];
        }

        int res = Integer.MIN_VALUE;
        for (Coordinate neighbor : neighbors) {
            int remainingP = p;
            if (!isSurrounded(neighbor, window))
                remainingP = p - 2;

            Set<Coordinate> newWindow = new HashSet<>(window);
            newWindow.add(neighbor);

            Set<Coordinate> newNeighbors = new HashSet<>(neighbors);
            newNeighbors.addAll(neighbors(neighbor, grid, newWindow, explored));

            res = Math.max(res, maxSum(neighbor, remainingP, newWindow, newNeighbors, explored, grid));
        }

        return res + grid[s.i][s.j];
    }

    Set<Coordinate> neighbors(Coordinate c, int[][] grid, Set<Coordinate> w, Set<Coordinate> explored) {
        Set<Coordinate> neighbors = new HashSet<>();
        for (int[] s : shift) {
            Coordinate newCoordinate = new Coordinate(c.i + s[0], c.j + s[1]);
            if (isValid(newCoordinate, grid) && !w.contains(newCoordinate) && !explored.contains(newCoordinate)) {
                neighbors.add(newCoordinate);
            }
        }

        return neighbors;
    }

    private int maxSum(Coordinate startingCoordinate, int p, Window s, int[][] grid, Set<Coordinate> explored) {
        if (s.neighbors().isEmpty())
            return s.sum();

        int res = Integer.MIN_VALUE;

        for (Coordinate neighbor : s.neighbors()) {
            int remainingP = p;
            if (!s.isSurrounded(neighbor))
                remainingP = p - 2;

            if (remainingP >= 0) {
                Window newWindow = s.add(neighbor, explored, grid);
                res = Math.max(res, maxSum(neighbor, remainingP, newWindow, grid, explored));
            } else
                res = Math.max(res, s.sum());
        }

        return res;
    }

    private int maxSum(Coordinate startingCoordinate, int p, Window s, int[][] grid, Integer[][][][][] cache) {
        if (s.neighbors().isEmpty())
            return s.sum();

        int res = Integer.MIN_VALUE;

        for (Coordinate neighbor : s.neighbors()) {
            int remainingP = p;
            if (!s.isSurrounded(neighbor))
                remainingP = p - 2;

            if (remainingP >= 0) {
                Integer cachedAnswer = cache[startingCoordinate.i][startingCoordinate.j][remainingP][neighbor.i][neighbor.j];
                if (cachedAnswer != null) {
                    res = cachedAnswer;
                } else {
                    Window newWindow = s.add(neighbor, grid);
                    res = Math.max(res, maxSum(neighbor, remainingP, newWindow, grid, cache));
                    cache[startingCoordinate.i][startingCoordinate.j][remainingP][neighbor.i][neighbor.j] = res;
                }
            } else {
                res = Math.max(res, s.sum());
            }
        }

        return res;
    }

    private int maxSum(int i, int j, Set<Coordinate> window, Set<Coordinate> cutCoordinates, int p, int[][] grid) {
        // In both cases we add the current Coordinate to our set of explored Coordinates b/c:
        //   In case 1, we know its not in the maximum sum, so we don't need to explore it again.
        //   In case 2, we're adding the value of the current Coordinate, so we don't want to add it to our max sum again
        // In both cases, we are reducing the problem by 1(this coordinate).
        // However, in case 2, the net size seems to blow up instead of shrinking...

        // Case 1: grid[i][j] IS NOT in the max sum
        //   So maxSum(grid) == maxSum(grid - {grid[i][j]})

        //int case1 = maxSum(moveForward(i, j), explored, p, grid);

        // Case 2: grid[i][j] IS in the max sum
        //  So maxSum(grid) == maxSum(grid - {grid[i][j]}) + grid[i][j]
        // If it IS included, and p>0, then AT LEAST ONE of its neighbors(that isn't already in our sum) also MUST be included
        // So...try all combinations? The max between: 1 of its neighbors, 2 of its neighbors, 3 of its neighbors, etc.
        // Up until p<=0.

        //int case2 = grid[i][j] + maxSum(moveForward(i, j), explored, p-2, grid);

        return 0;
    }

    /**
     * Either a cell is in the maximum sum, or it isn't - a recursive implementation of brute force
     *
     * @param n
     * @param window
     * @param explored
     * @param p
     * @param grid
     * @return
     */
    private int maxSum_(Coordinate n, Set<Coordinate> window, Set<Coordinate> explored, int p, int[][] grid) {
        //System.out.println("Current coordinate: " + n);
        if (explored.contains(n)) {
            //System.out.println("Final window: " + window);
            return Integer.MIN_VALUE;
        }

        // Case 1: n is NOT part of the optimal solution aka. we don't add it to our window
        Set<Coordinate> window1 = new HashSet<>(window);
        Set<Coordinate> explored1 = new HashSet<>(explored);
        explored1.add(n);
        Coordinate pred = pred(n, grid);
        System.out.println("Coordinate: " + n + ", pred: " + pred);
        int case1 = maxSum_(pred, window1, explored1, p, grid);

/*
        // Case 2: n is part of the optimal solution aka. we add it to our window
        int case2 = Integer.MIN_VALUE;
        Set<Coordinate> neighbors = neighbors(n, grid, explored);
        for (Coordinate neighbor : neighbors) {
            Set<Coordinate> window2 = new HashSet<>(window);
            window2.add(n);
            Set<Coordinate> explored2 = new HashSet<>(explored);
            explored.add(n);

            int remainingP = p;
            if (explored.isEmpty())
                remainingP = remainingP - 4;
            else if (!isSurrounded(neighbor, window))
                remainingP = p - 2;

            case2 = Math.max(case2, maxSum_(neighbor, window2, explored2, remainingP, grid));
        }
*/
        int case2 = isPartOfMaxSum(n, window, explored, p, grid);

        return Math.max(case1, case2 + grid[n.i][n.j]);
    }

    private int isPartOfMaxSum(Coordinate n, Set<Coordinate> window, Set<Coordinate> explored, int p, int[][] grid) {
        if (explored.contains(n) || p < 2) {
            //System.out.println("Final window: " + window);
            return 0;
        }

        // Case 2: n is part of the optimal solution aka. we add it to our window
        int case2 = Integer.MIN_VALUE;
        Set<Coordinate> neighbors = neighbors(n, grid, explored);
        for (Coordinate neighbor : neighbors) {
            Set<Coordinate> window2 = new HashSet<>(window);
            window2.add(n);
            Set<Coordinate> explored2 = new HashSet<>(explored);
            explored.add(n);

            int remainingP = p;
            if (explored.isEmpty())
                remainingP = remainingP - 4;
            else if (!isSurrounded(neighbor, window))
                remainingP = p - 2;

            case2 = Math.max(case2, isPartOfMaxSum(neighbor, window2, explored2, remainingP, grid));
        }

        return case2 + grid[n.i][n.j];
    }

    private Coordinate pred(Coordinate c, int[][] grid) {
        int i = c.i;
        int j = c.j;

        if (j == 0 && i == 0) {
            i = 0;
            j = 0;
        } else if (j == 0) {
            i = i - 1;
            j = grid[i].length - 1;
        } else {
            j = j - 1;
        }

        return new Coordinate(i, j);
    }

    Set<Coordinate> neighbors(Coordinate c, int[][] grid, Set<Coordinate> w) {
        Set<Coordinate> neighbors = new HashSet<>();
        for (int[] s : shift) {
            Coordinate newCoordinate = new Coordinate(c.i + s[0], c.j + s[1]);
            if (isValid(newCoordinate, grid) && !w.contains(newCoordinate)) {
                neighbors.add(newCoordinate);
            }
        }

        return neighbors;
    }

    boolean isSurrounded(Coordinate next, Set<Coordinate> w) {
        int i = next.i;
        int j = next.j;

        Coordinate above = new Coordinate(i - 1, j);
        Coordinate right = new Coordinate(i, j + 1);
        Coordinate below = new Coordinate(i + 1, j);
        Coordinate left = new Coordinate(i, j - 1);

        return (w.contains(above) && w.contains(right))
                || (w.contains(right) && w.contains(below))
                || (w.contains(below) && w.contains(left))
                || (w.contains(left) && w.contains(above));
    }

    // A greedy approach: DOESN'T WORK (Implemented correctly, it would compute 9+8+7=24 for the test grid)
    // If this works, use a max-priority queue instead of a Set for neighbors
    private int maxSum__(int p, int sum, Set<Coordinate> explored, Set<Coordinate> neighbors, int[][] grid) {
        while (!neighbors.isEmpty()) {
            System.out.println("Current sum: " + sum);
            Coordinate maxC = max(neighbors, grid);
            System.out.println("Current element: " + grid[maxC.i][maxC.j]);
            neighbors.remove(maxC);
            explored.add(maxC);

            if (!isSurrounded(maxC, explored))
                p = p - 2;

            if (p > 0) {
                sum = sum + grid[maxC.i][maxC.j];
                neighbors.addAll(neighbors(maxC, grid, explored));
            }
        }

        return sum;
    }

    private Coordinate max(Set<Coordinate> cs, int[][] grid) {
        Coordinate max = null;
        for (Coordinate c : cs) {
            if (max == null)
                max = c;
            else {
                if (grid[c.i][c.j] > grid[max.i][max.j])
                    max = c;
            }
        }

        return max;
    }

    static final int[][] shift = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    boolean isValid(Coordinate next, int[][] grid) {
        int i = next.i;
        int j = next.j;

        return i >= 0
                && j >= 0
                && i < grid.length
                && j < grid[i].length;
    }

    class Window {

        private Set<Coordinate> cs;
        private Set<Coordinate> neighbors;
        private int sum;

        Window() {
            cs = new HashSet<>();
            neighbors = new HashSet<>();
        }

        private Window(Set<Coordinate> cs, Set<Coordinate> neighbors, int sum) {
            this.cs = cs;
            this.neighbors = neighbors;
            this.sum = sum;
        }

        public Window add(Coordinate c, Set<Coordinate> explored, int[][] grid) {
            Set<Coordinate> cs = new HashSet<>(this.cs);
            cs.add(c);

            Set<Coordinate> neighbors = new HashSet<>(this.neighbors);
            neighbors.remove(c);
            for (int[] s : shift) {
                Coordinate newCoordinate = new Coordinate(c.i + s[0], c.j + s[1]);
                if (isValid(newCoordinate, grid) && !cs.contains(newCoordinate) && !explored.contains(newCoordinate)) {
                    neighbors.add(newCoordinate);
                }
            }

            int sum = this.sum + grid[c.i][c.j];

            return new Window(cs, neighbors, sum);
        }

        public Window add(Coordinate c, int[][] grid) {
            Set<Coordinate> cs = new HashSet<>(this.cs);
            cs.add(c);

            Set<Coordinate> neighbors = new HashSet<>(this.neighbors);
            neighbors.remove(c);
            for (int[] s : shift) {
                Coordinate newCoordinate = new Coordinate(c.i + s[0], c.j + s[1]);
                if (isValid(newCoordinate, grid) && !cs.contains(newCoordinate)) {
                    neighbors.add(newCoordinate);
                }
            }

            int sum = this.sum + grid[c.i][c.j];

            return new Window(cs, neighbors, sum);
        }

        boolean isValid(Coordinate next, int[][] grid) {
            int i = next.i;
            int j = next.j;

            return i >= 0
                    && j >= 0
                    && i < grid.length
                    && j < grid[i].length;
        }

        public int sum() {
            return sum;
        }

        public Set<Coordinate> neighbors() {
            return neighbors;
        }

        public boolean isSurrounded(Coordinate next) {
            int i = next.i;
            int j = next.j;

            Coordinate above = new Coordinate(i - 1, j);
            Coordinate right = new Coordinate(i, j + 1);
            Coordinate below = new Coordinate(i + 1, j);
            Coordinate left = new Coordinate(i, j - 1);

            return (cs.contains(above) && cs.contains(right))
                    || (cs.contains(right) && cs.contains(below))
                    || (cs.contains(below) && cs.contains(left))
                    || (cs.contains(left) && cs.contains(above));
        }

        @Override
        public String toString() {
            return Utils.printIterable(cs);
        }
    }

    class Coordinate {
        private int i;
        private int j;

        Coordinate(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Coordinate)) {
                return false;
            }

            Coordinate c = (Coordinate) o;
            return c.i == i
                    && c.j == j;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }

        @Override
        public String toString() {
            return "(" + i + ", " + j + ")";
        }
    }

    long hash(Coordinate c, int p) {
        int a = c.hashCode();
        int b = p;

        return a >= b ? a * a + a + b : a + b * b;
    }

    class AnswerKey {
        Coordinate curCoordinate;
        Set<Coordinate> neighbors;
        int p;

        AnswerKey(Coordinate curCoordinate, Set<Coordinate> neighbors, int p) {
            this.curCoordinate = curCoordinate;
            this.neighbors = neighbors;
            this.p = p;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof AnswerKey))
                return false;

            AnswerKey aKey = (AnswerKey) o;
            return Objects.equals(curCoordinate, aKey.curCoordinate)
                    && Objects.equals(neighbors, aKey.neighbors)
                    && p == aKey.p;
        }

        @Override
        public int hashCode() {
            return Objects.hash(curCoordinate, neighbors, p);
        }
    }
}
