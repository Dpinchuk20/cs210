package edu.umb.cs210.p1;

import dsa.WeightedQuickUnionUF;
import stdlib.In;
import stdlib.StdOut;

// Models an N-by-N percolation system.
public class Percolation {
    private int N; // Instance Variable Experiment
    private boolean [][] open; // Instance Variable Experiment
    private int openSites; // Instance Variable Experiment
    private int sink; // Instance Variable Experiment
    // private WeightedQuickUnionUF backwashEliminator;
    private WeightedQuickUnionUF uf;

    // Creates an N-by-N grid, with all sites blocked.
    public Percolation(int N) {
        this.N = N;
        this.openSites = openSites;
        this.uf = new WeightedQuickUnionUF(N * N + 2);
        this.sink = N * N + 1;
        // this.backwashEliminator = new WeightedQuickUnionUF(N + N + 2);
        this.open = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            uf.union(encode(0, i), 0);
            // backwashEliminator.union(encode(0, i), 0);
        }
        for (int j = 0;  j < N; j++) {
            uf.union(encode(N - 1, j), sink);
            // backwashEliminator.union(encode(N - 1 j), sink);
        }
    }
    // Opens site (i, j) if it is not open already.
    public void open(int i, int j) {
        open[i][j] = true;
        openSites++;
        if (i + 1 < N) {
            if (open[i + 1][j]) {
                uf.union(encode(i, j), encode(i + 1, j));
                // backwashEliminator.union(encode(i, j), encode(i + 1, j));
            }
        }
        if (i - 1 > 0) {
            if (open[i - 1][j]) {
                uf.union(encode(i, j), encode(i - 1, j));
                // backwashEliminator.union(encode(i, j), encode(i - 1, j));
            }
        }
        if (j + 1 < N) {
            if (open[i][j + 1]) {
                uf.union(encode(i, j), encode(i, j + 1));
                // backwashEliminator.union(encode(i, j), encode(i, j + 1));
            }
        }
        if (j - 1 > 0) {
            if (open[i][j - 1]) {
                uf.union(encode(i, j), encode(i, j - 1));
                // backwashEliminator.union(encode(i, j), encode(i, j - 1));
            }
        }
    }

    // Checks if site (i, j) is open.
    public boolean isOpen(int i, int j) {
        return open[i][j];
    }

    // Checks if site (i, j) is full.
    public boolean isFull(int i, int j) {
        // return backwashEliminator.connected(0, encode(i,j));
        return uf.connected(encode(i, j), 0);
    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Checks if the system percolates.
    public boolean percolates() {
        return uf.connected(0, sink);
    }

    // Returns an integer ID (1...N) for site (i, j).
    protected int encode(int i, int j) {
        int ID = i * N + j + 1;
        return ID;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        }
        else {
            StdOut.println("does not percolate");
        }

        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}
