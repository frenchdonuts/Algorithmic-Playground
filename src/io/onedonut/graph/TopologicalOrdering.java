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
    Node[] ordering3 = new Node[] { n2, n3, n1, n4, n5, n6, n7 };

    public void test() {
        //
        Node[] computedOrdering = topologicalSort(dag);

        print("computedOrdering: ");
        System.out.println(printArray(computedOrdering));

        assert Arrays.equals(computedOrdering, ordering1)
                || Arrays.equals(computedOrdering, ordering2)
                || Arrays.equals(computedOrdering, ordering3);
    }

    /**
     * This variant of topological sort is a modification of iterative depth-first search.
     * It is both efficient and slick. Seen in Tim's Coursera course.
     * @param dag
     * @return
     */
    Node[] topologicalSort(Node[] dag) {
        Node[] ordering = new Node[dag.length];
        int label = dag.length - 1;
        Set<Node> explored = new HashSet<>(dag.length);
        LinkedList<Node> stack = new LinkedList<>();

        for (Node s : dag) {
            if (!explored.contains(s)) {
                stack.push(s);
                while(!stack.isEmpty()) {
                    Node curNode = stack.peek();
                    if (!explored.contains(curNode)) {
                        for (Node n : curNode.neighbors) {
                            if (!explored.contains(n))
                                stack.push(n);
                        }
                        explored.add(curNode);
                    } else
                        ordering[label--] = stack.pop();
                }
            }
        }

        return ordering;
    }

    /**
     * This variant of topological sort is a modification of depth-first search.
     * It is both efficient and slick. Seen in Tim's Coursera course.
     * @param dag
     */
    Node[] topologicalSortVariant0(Node[] dag) {
        Node[] ordering = new Node[dag.length];
        Set<Node> explored = new HashSet<>();
        int index = dag.length - 1;

        for(Node n : dag) {
            if (!explored.contains(n))
                index = dfs(n, explored, index, ordering);
        }
        return ordering;
    }

    int dfs(Node s, Set<Node> explored, int index, Node[] ordering) {
        explored.add(s);

        for (Node n : s.neighbors) {
            if (!explored.contains(n)) {
                index = dfs(n, explored, index, ordering);
            }
        }
        ordering[index] = s;

        index--;
        return index;
    }

    /**
     * This variant of topological sort constructs the ordering "backwards".
     * That is, we start with a sink of the DAG, label it, delete it, and recurse.
     * A sink is a node with no outgoing edges.
     * Seen in Tim's Coursera course.
     * @param dag
     */
    Node[] topologicalSortVariant1(Node[] dag) {
        //
        Node[] ordering = new Node[dag.length];
        int currentIndex = dag.length - 1;
        int[] nodeToOutgoingEdgesMap = mapNodesToOutgoingEdges(dag);

        LinkedList<Integer> activeSinks = findSinks(nodeToOutgoingEdgesMap);
        while (!activeSinks.isEmpty()) {
            Node sink = dag[activeSinks.remove()];
            // Add sink to ordering
            ordering[currentIndex] = sink;
            currentIndex--;
            // Remove sink from graph by subtracting the number of outgoing edges
            // from all nodes that point to this sink
            // TODO: How would I efficiently find all the nodes that point to this sink?
            for (int i = 0; i < dag.length; i++) {
                if (dag[i].neighbors.contains(sink)) {
                    int edges = nodeToOutgoingEdgesMap[i];
                    nodeToOutgoingEdgesMap[i] = edges - 1;
                    if (edges == 1) {
                        activeSinks.add(i);
                    }
                }
            }
        }

        return ordering;
    }

    int[] mapNodesToOutgoingEdges(Node[] dag) {
        int[] res = new int[dag.length];

        for (int i = 0; i < dag.length; i++) {
            res[i] = dag[i].neighbors.size();
        }

        return res;
    }

    LinkedList<Integer> findSinks(int[] nodesToOutgoingEdgesMap) {
        LinkedList<Integer> res = new LinkedList<>();

        for (int i = 0; i < nodesToOutgoingEdgesMap.length; i++) {
            if (nodesToOutgoingEdgesMap[i] == 0)
                res.add(i);
        }

        return res;
    }

    /**
     * This variant of topological sort constructs the ordering "forwards".
     * That is, we start with a source of the DAG, label it, delete it, and recurse.
     * A source is a node with no incoming edges.
     * Seen in Kleinberg and Tardos' Algorithms Design textbook (p. 101-104).
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
