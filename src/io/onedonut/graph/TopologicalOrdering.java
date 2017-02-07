package io.onedonut.graph;

import java.util.*;

import static io.onedonut.Utils.print;
import static io.onedonut.Utils.printArray;
import static io.onedonut.Utils.printIterable;

/**
 * Created by pamelactan on 2/6/17.
 */
public class TopologicalOrdering {
    Node n7 = new Node(7, Collections.EMPTY_LIST);
    Node n6 = new Node(6, Arrays.asList(n7));
    Node n5 = new Node(5, Arrays.asList(n6, n7));
    Node n4 = new Node(4, Arrays.asList(n5));
    Node n3 = new Node(3, Arrays.asList(n4, n5));
    Node n2 = new Node(2, Arrays.asList(n3, n5, n6));
    Node n1 = new Node(1, Arrays.asList(n4, n5, n7));

    Node[] dag = new Node[] { n1, n2, n3, n4, n5, n6, n7 };
    Node[] ordering1 = dag;
    Node[] ordering2 = new Node[] { n2, n1, n3, n4, n5, n6, n7 };

    public void test() {
        //
        Node[] computedOrdering = topologicalSortVariant2(dag);

        print("computedOrdering: ");
        System.out.println(printArray(computedOrdering));

        assert Arrays.equals(topologicalSortVariant2(dag), ordering1) || Arrays.equals(topologicalSortVariant2(dag), ordering2);
    }

    /**
     * This variant of topological sort is a modification of depth-first search.
     * It is both efficient and slick. Seen in Tim's Coursera course.
     * @param dag
     */
    void topologicalSortVariant0(Node[] dag) {
        //
    }

    /**
     * This variant of topological sort constructs the ordering "backwards".
     * That is, we start with a sink of the DAG, label it, delete it, and recurse.
     * A sink is a node with no outgoing edges.
     * Seen in Tim's Coursera course.
     * @param dag
     */
    void topologicalSortVariant1(Node[] dag) {
        //
    }

    /**
     * This variant of topological sort constructs the ordering "forwards".
     * That is, we start with a source of the DAG, label it, delete it, and recurse.
     * A source is a node with no incoming edges.
     * Seen in Kleinberg and Tardos' Algorithms Design textbook.
     * @param dag
     */
    Node[] topologicalSortVariant2(Node[] dag) {
        //
        Node[] ordering = new Node[dag.length];
        int currentIndex = 0;
        Map<Node, Integer> nodeIncomingEdgesMap = mapNodesToIncomingEdges(dag);

        LinkedList<Node> activeSources = findSources(nodeIncomingEdgesMap);

        while (!activeSources.isEmpty()) {
            Node activeSource = activeSources.remove();
            LinkedList<Node> activeSourcesNeighbors = new LinkedList<>(activeSource.neighbors);

            ordering[currentIndex] = activeSource;
            currentIndex++;

            for (Node n : activeSourcesNeighbors) {
                //
                int incomingEdges = nodeIncomingEdgesMap.get(n);
                if (incomingEdges == 1) {
                    activeSources.add(n);
                }
                nodeIncomingEdgesMap.put(n, incomingEdges - 1);
            }
        }

        return ordering;
    }

    Map<Node, Integer> mapNodesToIncomingEdges(Node[] dag) {
        //
        Map<Node, Integer> res = new HashMap<>();

        for (Node n : dag) {
            // This makes sure we get all the sources
            if (!res.containsKey(n))
                res.put(n, 0);

            for (Node neighbor : n.neighbors) {
                // This makes sure we get all the sinks
                if (!res.containsKey(neighbor)) {
                    res.put(neighbor, 1);

                } else {
                    int curCount = res.get(neighbor);
                    res.put(neighbor, curCount + 1);
                }
            }

        }

        System.out.println("mapNodesToIncomingEdges: ");
        System.out.println(printIterable(res.entrySet()));
        return res;
    }

    LinkedList<Node> findSources(Map<Node, Integer> nodeToIncomingEdgesMap) {
        LinkedList<Node> res = new LinkedList<>();

        for (Node n : nodeToIncomingEdgesMap.keySet()) {
            if (nodeToIncomingEdgesMap.get(n) == 0) {
                res.add(n);
            }
        }

        print("findSources: ");
        print(printIterable(res));
        return res;
    }

    class Node {
        int data = -1;
        List<Node> neighbors;

        Node(int data, List<Node> neighbors) {
            this.data = data;
            this.neighbors = neighbors;
        }

        @Override
        public String toString() {
            return data + "";
        }
    }
}
