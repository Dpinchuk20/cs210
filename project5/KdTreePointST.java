package edu.umb.cs210.p5;

import dsa.LinkedQueue;
import dsa.MaxPQ;
import dsa.Point2D;
import dsa.RectHV;
import stdlib.StdIn;
import stdlib.StdOut;

public class KdTreePointST<Value> implements PointST<Value> {
    private Node root; // the root of the KD tree
    private int N; // number of nodes in the KD tree

    // 2d-tree (generalization of a BST in 2d) representation.
    private class Node {
        private Point2D p;   // the point
        private Value val;   // the symbol table maps the point to this value
        private RectHV rect; // the axis-aligned rectangle corresponding to
        // this node
        private Node lb;     // the left/bottom subtree
        private Node rt;     // the right/top subtree

        // Construct a node given the point, the associated value, and the
        // axis-aligned rectangle corresponding to the node.
        Node(Point2D p, Value val, RectHV rect) {
            this.p = p;
            this.val = val;
            this.rect = rect;
        }
    }

    // Construct an empty symbol table of points.
    public KdTreePointST() {
        root = null;
        N = 0;
    }

    // Is the symbol table empty?
    public boolean isEmpty() {
        return this.N == 0;
    }

    // Number of points in the symbol table.
    public int size() {
        return this.N;
    }

    // Associate the value val with point p.
    public void put(Point2D p, Value val) {
        if (p == (null) || val == null)
            throw new NullPointerException("null argument");
        double pInf = Double.POSITIVE_INFINITY;
        double nInf = Double.NEGATIVE_INFINITY;
        if (this.isEmpty()) {
            root = new Node(p, val, new RectHV(nInf, nInf, pInf, pInf));
            N++;
            return;
        }
        put(root, p, val, new RectHV(nInf, nInf, pInf, pInf), true);
    }

    // Helper for put(Point2D p, Value val).
    private Node put(Node x, Point2D p, Value val, RectHV rect, boolean lr) {
        if (x == null) {
            N++;
            return new Node(p, val, rect);
        }
        else if (x.p.equals(p)) {
            x.val = val;
        }
        else {
            if (lr) {
                double cmp = p.x() - x.p.x();
                if (cmp < 0.0) {
                    x.lb = put(x.lb, p, val,
                               new RectHV(rect.xmin(), rect.ymin(),
                            x.p.x(), rect.ymax()), false);
                }
                else {
                    x.rt = put(x.rt, p, val,
                               new RectHV(x.p.x(), rect.ymin(),
                            rect.xmax(), rect.ymax()), false);
                }
            }
            else {
                double cmp = p.y() - x.p.y();
                if (cmp < 0.0) {
                    x.lb = put(x.lb, p, val, new RectHV(
                            rect.xmin(), rect.ymin(),
                            rect.xmax(), x.p.y()), true);
                }
                else {
                    x.rt = put(x.rt, p, val, new RectHV(rect.xmin(), x.p.y(),
                            rect.xmax(), rect.ymax()), true);
                }
            }
        }
        return x;
    }
    // Value associated with point p.
    public Value get(Point2D p) {
        if (p == (null))  {
            throw new NullPointerException("null argument");
        }
        return get(root, p, true);
    }

    // Helper for get(Point2D p).
    private Value get(Node x, Point2D p, boolean lr) {
        if (x == null) { return null; }
        else if (x.p.equals(p)) { return x.val; }
        else {
            if (lr) {
                double cmp = p.x()-x.p.x();
                if (cmp < 0.0) { return get(x.lb, p, false); }
                else { return get(x.rt, p, false); }
            }
            else {
                double cmp = p.y() - x.p.y();
                if (cmp < 0.0) { return get(x.lb, p, true); }
                else { return get(x.rt, p, true); }
            }
        }
    }

    // Does the symbol table contain the point p?
    public boolean contains(Point2D p) {
        if (p == null) { throw new NullPointerException("null argument"); }
        return get(p) != null;
    }

    // All points in the symbol table, in level order.
    public Iterable<Point2D> points() {
        LinkedQueue<Point2D> keys = new LinkedQueue<Point2D>();
        LinkedQueue<Node> queue = new LinkedQueue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x.p);
            if (x.lb != null) { queue.enqueue(x.lb); }
            if (x.rt != null) { queue.enqueue(x.rt); }
        }
        return keys;
    }

    // All points in the symbol table that are inside the rectangle rect.
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == (null)) {
            throw new NullPointerException("null argument");
        }
        LinkedQueue<Point2D> queue = new LinkedQueue<Point2D>();
        range(root, rect, queue);
        return queue;
    }

    // Helper for public range(RectHV rect).
    private void range(Node x, RectHV rect, LinkedQueue<Point2D> q) {
        if (x == null || !x.rect.intersects(rect)) {
            return;
        }
        else if (rect.contains(x.p)) {
            q.enqueue(x.p);
        }
        range(x.lb, rect, q);
        range(x.rt, rect, q);

    }

    // A nearest neighbor to point p; null if the symbol table is empty.
    public Point2D nearest(Point2D p) {
        if (p == (null)) throw new NullPointerException("null argument");
        return nearest(root, p, null, Double.POSITIVE_INFINITY, true);
    }

    // Helper for public nearest(Point2D p).
    private Point2D nearest(Node x, Point2D p, Point2D nearest,
                            double nearestDistance, boolean lr) {
        if (x == null) return nearest;
        if (nearestDistance < x.rect.distanceSquaredTo(p)) return nearest;
        if (!p.equals(x.p)
                && nearestDistance > x.p.distanceSquaredTo(p)) {
            nearestDistance = x.p.distanceSquaredTo(p);
            nearest = x.p;
        }
        if (lr) {
            if (p.x() > x.p.x()) {
                nearest = nearest(x.rt, p, nearest, nearestDistance, false);
                nearestDistance = nearest.distanceSquaredTo(p);
                nearest = nearest(x.lb, p, nearest, nearestDistance, false);
            } else {
                nearest = nearest(x.lb, p, nearest, nearestDistance, false);
                nearestDistance = nearest.distanceSquaredTo(p);
                nearest = nearest(x.rt, p, nearest, nearestDistance, false);
            }
        }
        else {
            if (p.y() > x.p.y()) {
                nearest = nearest(x.rt, p, nearest, nearestDistance, true);
                nearestDistance = nearest.distanceSquaredTo(p);
                nearest = nearest(x.lb, p, nearest, nearestDistance, true);
            }
            else {
                nearest = nearest(x.rt, p, nearest, nearestDistance, true);
                nearestDistance = nearest.distanceSquaredTo(p);
                nearest = nearest(x.lb, p, nearest, nearestDistance, true);
            }
        }
        return nearest;
    }

    // k points that are closest to point p.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        if (p == (null)) {
            throw new NullPointerException("null argument");
        }
        MaxPQ<Point2D> maxPQ = new MaxPQ<Point2D>(p.distanceToOrder());
        nearest(root, p, k, maxPQ, true);
        return maxPQ;
    }

    // Helper for public nearest(Point2D p, int k).
    private void nearest(Node x, Point2D p, int k, MaxPQ<Point2D> pq,
                         boolean lr) {
        if (x == null) {
            return;
        }
        if (pq.size() == k
                && pq.max().distanceSquaredTo(p) < x.rect.distanceSquaredTo(p))
        { return; }
        if (!p.equals(x.p)) { pq.insert(x.p); }
        if (pq.size() > k) { pq.delMax(); }

        if (lr) {
            if (p.x() > x.p.x()) {
                nearest(x.rt, p, k, pq, false);
                nearest(x.lb, p, k, pq, false);
            }
            else {
                nearest(x.lb, p, k, pq, false);
                nearest(x.rt, p, k, pq, false);
            }
        }
        else {
            if (p.y() > x.p.y()) {
                nearest(x.rt, p, k, pq, true);
                nearest(x.lb, p, k, pq, true);
            }
            else {
                nearest(x.lb, p, k, pq, true);
                nearest(x.rt, p, k, pq, true);
            }
        }

    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        KdTreePointST<Integer> st = new KdTreePointST<Integer>();
        double qx = Double.parseDouble(args[0]);
        double qy = Double.parseDouble(args[1]);
        double rx1 = Double.parseDouble(args[2]);
        double rx2 = Double.parseDouble(args[3]);
        double ry1 = Double.parseDouble(args[4]);
        double ry2 = Double.parseDouble(args[5]);
        int k = Integer.parseInt(args[6]);
        Point2D query = new Point2D(qx, qy);
        RectHV rect = new RectHV(rx1, ry1, rx2, ry2);
        int i = 0;
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            st.put(p, i++);
        }
        StdOut.println("st.empty()? " + st.isEmpty());
        StdOut.println("st.size() = " + st.size());
        StdOut.println("First " + k + " values:");
        i = 0;
        for (Point2D p : st.points()) {
            StdOut.println("  " + st.get(p));
            if (i++ == k) {
                break;
            }
        }
        StdOut.println("st.contains(" + query + ")? " + st.contains(query));
        StdOut.println("st.range(" + rect + "):");
        for (Point2D p : st.range(rect)) {
            StdOut.println("  " + p);
        }
        StdOut.println("st.nearest(" + query + ") = " + st.nearest(query));
        StdOut.println("st.nearest(" + query + ", " + k + "):");
        for (Point2D p : st.nearest(query, k)) {
            StdOut.println("  " + p);
        }
    }
}