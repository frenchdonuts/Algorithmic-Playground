package io.onedonut.codefights.challenges;

import io.onedonut.Utils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by pamelactan on 2/11/17.
 */
public class SumInPerimeter {
    public void test() {
        int[][] grid = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
        int maxSumInPerimeter = sumInPerimeter(grid, 8);
        Utils.print("" + maxSumInPerimeter);
        assert sumInPerimeter(grid, 8) == 28;
    }

    public int sumInPerimeter(int[][] grid, int p) {
        int res = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Window w = new Window().add(new Coordinate(i, j), grid);
                res = Math.max(res, maxSum(w, p - 4, grid));

            }
        }

        return res;
    }

    private int maxSum(Window s, int p, int[][] grid) {
        if (p == 0 && s.neighbors.isEmpty())
            return s.sum();

        int res = Integer.MIN_VALUE;
        for (Coordinate neighbor : s.neighbors()) {
            Window newWindow = s.add(neighbor, grid);

            int remainingP = p;
            if (!s.isSurrounded(neighbor))
                remainingP = p - 2;

            if (remainingP >= 0)
                res = Math.max(res, maxSum(newWindow, remainingP, grid));
            else
                res = Math.max(res, s.sum());
        }

        return res;
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
}
