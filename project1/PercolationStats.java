package edu.umb.cs210.p1;

import stdlib.StdOut;
import stdlib.StdRandom;
import stdlib.StdStats;

// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {
    int T; //  the number of independent experiments
    double[] p; // percolation threshold for the T experiments
    private Percolation perc; //  The perc object for N x N percolation system

    // Performs T independent experiments (Monte Carlo simulations) on an
    // N-by-N grid.
    public PercolationStats(int N, int T) {
        p = new double[T];
        this.T = T;
        int site = 0;
        double size = N * N;
        for (int t = 0; t < T; t++) {
            site = 0;
            perc = new Percolation(N);
            int row = 0;
            int column = 0;
            if (N <= 0 || T <= 0) {
                throw new IllegalArgumentException();
            }
            while (!perc.percolates()) {
                row = StdRandom.uniform(0, N);
                column = StdRandom.uniform(0, N);
                if (!perc.isOpen(row, column)) {
                    perc.open(row, column);
                    site++;
                }
            }
            p[t] = site / size;
        }
    }

    // Returns sample mean of percolation threshold.
    public double mean() { return StdStats.mean(p); }

    // Returns sample standard deviation of percolation threshold.
    public double stddev() { return StdStats.stddev(p); }

    // Returns low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    // Returns high endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}

