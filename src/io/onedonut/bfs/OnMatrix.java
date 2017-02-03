package io.onedonut.bfs;

import java.util.*;

/**
 * Created by pamelactan on 2/2/17.
 */
public class OnMatrix {

    public void test() {
        // Create matrix
        int[][] M = { { 0, 1, 2 }, {3, 4, 5}, {6, 7, 8} };

        assert bfs(M, Arrays.asList(0, 1, 2)) : "Could not find 0,1,2";
    }

    boolean bfs(int[][] M, List<Integer> sequence) {
        //
        List<Index> startingIndices = new ArrayList<>();
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[i].length; j++) {
                if (M[i][j] == sequence.get(0))
                    startingIndices.add(new Index(i, j, 0));
            }
        }

        for (Index start : startingIndices) {
            LinkedList<Index> q = new LinkedList<>();
            Set<Index> explored = new HashSet<>();
            int[][] shift = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

            q.add(start);
            explored.add(start);

            start.layer = 0;


            while (!q.isEmpty()) {
                Index curIndex = q.remove();
                int curInt = M[curIndex.i][curIndex.j];

                if (curIndex.layer == sequence.size() - 1 && curInt == sequence.get(curIndex.layer))
                    return true;

                if (curInt == sequence.get(curIndex.layer)) {
                    //
                    for (int[] shifter : shift) {
                        Index newIndex = new Index(curIndex.j + shifter[0], curIndex.i + shifter[1], curIndex.layer + 1);
                        if (!explored.contains(newIndex) && indexIsWithinBounds(newIndex, M)) {
                            q.add(new Index(curIndex.j + shifter[0], curIndex.i + shifter[1], curIndex.layer + 1));
                        }
                    }
                }
            }
        }

        return false;
    }


    boolean indexIsWithinBounds(Index i, int[][] M) {
        //
        return i.i >= 0 && i.i < M.length && i.j >= 0 && i.j < M[0].length;
    }

    class Index {
        public int i;
        public int j;
        public int layer;

        Index(int i, int j, int layer) {
            this.i = i;
            this.j = j;
            this.layer = layer;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
