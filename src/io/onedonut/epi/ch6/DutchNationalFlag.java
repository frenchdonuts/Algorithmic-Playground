// Copyright (c) 2015 Elements of Programming Interviews. All rights reserved.
package io.onedonut.epi.ch6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DutchNationalFlag {
    // My soln:

    public static void myDutchFlagPartition(int pivotIndex, List<Integer> A) {
        int pivot = A.get(pivotIndex);

        /**
         * My invariant is different in that it places the unexplored
         * region last. Aka relative to EPI's soln, I swapped the top
         * group and unclassified group.
         *
         * Keep the following invariants during partitioning:
         * bottom(less than) group: A.subList(L, smaller).
         * middle(equal to) group: A.subList(smaller, larger).
         * top(greater than) group: A.subList(larger, i).
         * unclassified group: A.subList(i, U).
         */
        int L = 0, U = A.size(), smaller = L, larger = L, i = L;
        // Keep iterating as long as there is an unclassified element.
        while (i < U) {
            // A.get(i) is the incoming unclassified element.
            if (A.get(i) < pivot) {
                Collections.swap(A, i, smaller);

                // If the middle group was non-empty(meaning we have already
                // encountered elements equal to the pivot), we have swapped
                // an element in the middle group to the unclassified group.
                // We must correct this to maintain our invariant.
                if (A.get(i) == pivot)
                    Collections.swap(A, i, larger);


                // Expand the size of the bottom group by 1.
                smaller++;

                // Why must we increment larger? Well, we have technically
                // shrunken the middle group's partition by 1 when we
                // increment smaller. To correct for this transgression,
                // we must increment larger.
                // Will this accidently insert a top-group element into the
                // middle group?
                // No, because:
                //   Either larger is now pointing to an element equal to p (middle group is non-empty)
                //      Incrementing larger here would
                //   Or larger is now pointing at an element less than p (middle group is empty)
                // What if have we accidentally inserted a top group element into the middle group you say?
                // This is impossible because:
                //   Either larger was pointing at an element equal to p (middle group is non-empty),
                //   Or larger was pointing at an element less than p (middle group is empty).
                // In both cases, incrementing larger would correctly maintain our invariant - larger will now
                // point at either an element greater than p (top group is non-empty) or p will be pointing at
                // an element in the unclassified region (top group is empty aka larger == i).
                larger++;
            } else if (A.get(i) == pivot) {
                Collections.swap(A, i, larger);

                // Expand the size of the middle group by 1.
                larger++;
            }

            // Every iteration, we shrink the unexplored region by 1.
            i++;
        }
    }

    /**
     * This is the same thing as the above method, except I tried to avoid
     * repeating the swap code in the A.get(i) < pivot branch.
     * @param pivotIndex
     * @param A
     */
    public static void myDutchFlagPartition_(int pivotIndex, List<Integer> A) {
        int pivot = A.get(pivotIndex);

        int L = 0, U = A.size(), smaller = L, larger = L, i = L;
        boolean correcting = false;
        // Keep iterating as long as there is an unclassified element.
        while (i < U) {
            // A.get(i) is the incoming unclassified element.
            if (A.get(i) < pivot) {
                Collections.swap(A, i, smaller);

                if (A.get(i) == pivot) {
                    correcting = true;
                } else {
                    larger++;
                }

                // Expand the size of the bottom group by 1.
                smaller++;
            } else if (A.get(i) == pivot) {
                Collections.swap(A, i, larger);

                // Expand the size of the middle group by 1.
                larger++;

                correcting = false;
            }

            // If we've swapped something into the unclassified region
            // that we were not supposed to swap, we need to correct this
            // in the next iteration.
            if (!correcting)
                i++;
        }
    }

    // EPI soln:
    // @include
    public static void dutchFlagPartition(int pivotIndex, List<Integer> A) {
        int pivot = A.get(pivotIndex);

        /**
         * Keep the following invariants during partitioning:
         * bottom group: A.subList(0, smaller).
         * middle group: A.subList(smaller, equal).
         * unclassified group: A.subList(equal, larger).
         * top group: A.subList(larger, A.size()).
         */
        int smaller = 0, equal = 0, larger = A.size();
        // Keep iterating as long as there is an unclassified element.
        while (equal < larger) {
            // A.get(equal) is the incoming unclassified element.
            if (A.get(equal) < pivot) {
                Collections.swap(A, smaller++, equal++);
            } else if (A.get(equal) == pivot) {
                ++equal;
            } else { // A.get(equal) > pivot.
                Collections.swap(A, equal, --larger);
            }
        }
    }

    // @exclude

    private static List<Integer> randArray(int len) {
        Random r = new Random();
        List<Integer> ret = new ArrayList<>(len);
        for (int i = 0; i < len; ++i) {
            ret.add(r.nextInt(10));
        }
        return ret;
    }

    public static void main(String[] args) {

        Random gen = new Random();

        for (int times = 0; times < 10; ++times) {
            int n;

            if (args.length == 1) {
                n = Integer.parseInt(args[0]);
            } else {
                n = gen.nextInt(100) + 1;
            }

            List<Integer> A = randArray(n);
            List<Integer> Adup = new ArrayList<>(A);

            int pivotIndex = gen.nextInt(n);
            int pivot = A.get(pivotIndex);

            new DutchNationalFlag().myDutchFlagPartition(pivotIndex, A);

            if (!check(pivot, A, Adup)) {
                System.exit(-1);
            }
        }
        System.out.println("All tests passed!");
    }

    public static boolean check(int pivot, List<Integer> A, List<Integer> Adup) {
        int n = A.size();
        int i = 0;
        while (i < n && A.get(i) < pivot) {
            // System.out.print(A.get(i) + " ");
            ++i;
        }
        while (i < n && A.get(i) == pivot) {
            // System.out.print(A.get(i) + " ");
            ++i;
        }
        while (i < n && A.get(i) > pivot) {
            // System.out.print(A.get(i) + " ");
            ++i;
        }

        if (i != n) {
            System.err.println("Failing test: pivot = " + pivot + " array = " + Adup
                    + "."
                    + "Your code updated to " + A);
            System.exit(-1);
        }
        return true;
    }
}
