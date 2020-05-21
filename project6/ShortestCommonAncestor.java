package edu.umb.cs210.p6;

import dsa.DiGraph;
import dsa.LinkedQueue;
import dsa.SeparateChainingHashST;
import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

// An immutable data type for computing shortest common ancestors.
public class ShortestCommonAncestor {
    private DiGraph G; // rooted DAG

    // Construct a ShortestCommonAncestor object given a rooted DAG.
    public ShortestCommonAncestor(DiGraph G) {
        if (G == null)
            throw new NullPointerException("G is null");
        if (G.V() == 0)
            throw new IllegalArgumentException();
        this.G = G;
    }

    // Length of the shortest ancestral path between v and w.
    public int length(int v, int w) {
        if (v < 0 || v >= G.V())
            throw new IndexOutOfBoundsException("v is out of bounds");
        if (w < 0 || w >= G.V())
            throw new IndexOutOfBoundsException("w is out of bounds");
        int ancestor = ancestor(v, w);
        return distFrom(v).get(ancestor) + distFrom(w).get(ancestor);
    }

    // Shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V())
            throw new IndexOutOfBoundsException("v is out of bounds");
        if (w < 0 || w >= G.V())
            throw new IndexOutOfBoundsException("w is out of bounds");
        SeparateChainingHashST<Integer, Integer> pathv = distFrom(v);
        SeparateChainingHashST<Integer, Integer> pathw = distFrom(w);
        int ancestor = -1;
        double min = Double.POSITIVE_INFINITY;
        for (int i: pathv.keys())
        {
            if (pathw.contains(i))
            {
                int dis = pathv.get(i) + pathw.get(i);
                if (min > dis)
                {
                    min = dis;
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

    // Length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null)
            throw new NullPointerException("A is null");
        if (B == null)
            throw new NullPointerException("B is null");
        if (!A.iterator().hasNext())
            throw new IllegalArgumentException("A is empty");
        if (!B.iterator().hasNext())
            throw new IllegalArgumentException("B is empty");
        return length(triad(A, B)[1], triad(A, B)[2]);
    }

    // A shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null)
            throw new NullPointerException("A is null");
        if (B == null)
            throw new NullPointerException("B is null");
        if (!A.iterator().hasNext())
            throw new IllegalArgumentException("A is empty");
        if (!B.iterator().hasNext())
            throw new IllegalArgumentException("B is empty");
        return triad(A, B)[0];
    }

    // Helper: Return a map of vertices reachable from v and their
    // respective shortest distances from v.
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        SeparateChainingHashST<Integer, Integer> st =
                new SeparateChainingHashST<Integer, Integer>();
        LinkedQueue<Integer> lq = new LinkedQueue<Integer>();
        st.put(v, 0);
        lq.enqueue(v);
        while (!lq.isEmpty())
        {
            int q = lq.dequeue();
            for (int i :G.adj(q))
            {
                if (!st.contains(i))
                {
                    st.put(i, st.get(q) + 1);
                    lq.enqueue(i);
                }
            }
        }
        return st;
    }

    // Helper: Return an array consisting of a shortest common ancestor a
    // of vertex subsets A and B, and vertex v from A and vertex w from B
    // such that the path v-a-w is the shortest ancestral path of A and B.
    private int[] triad(Iterable<Integer> A, Iterable<Integer> B) {
        double minLen = Double.POSITIVE_INFINITY;
        int ancestor = 0, v = 0, w = 0;
        for (int a: A) {
            for (int b: B) {
                int len = length(a, b);
                if (len < minLen) {
                    minLen = len;
                    ancestor = ancestor(a, b);
                    v = a;
                    w = b;
                }
            }
        }
        return new int[]{ancestor, v, w};
    }

    // helper method that produces a defensive copy of G
    private DiGraph defensiveCopy(DiGraph g) {
        if (G == null) {
            throw new NullPointerException("null argument");
        }
        DiGraph copy = new DiGraph(g.V());
        for (int from = 0; from < g.V(); from++) {
            for (int to : g.adj(from)) {
                copy.addEdge(from, to);
            }
        }
        return copy;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        DiGraph G = new DiGraph(in);
        in.close();
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}