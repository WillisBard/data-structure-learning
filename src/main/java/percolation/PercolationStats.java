package percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int trialsCount;

    private static final int MIN = 1;

    private static final double CONST = 1.96;

    private double[] openSiteCounts;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        if (trials <= 0) throw new IllegalArgumentException("trials must be positive");
        trialsCount = trials;
        openSiteCounts = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(MIN, n + MIN);
                int col = StdRandom.uniform(MIN, n + MIN);
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                }
            }
            openSiteCounts[i] = (double) percolation.numberOfOpenSites() / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(openSiteCounts);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (trialsCount == MIN) return Double.NaN;
        return StdStats.stddev(openSiteCounts);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONST * stddev()) / Math.sqrt(trialsCount));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONST * stddev()) / Math.sqrt(trialsCount));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.printf("mean = %f", percolationStats.mean());
        System.out.println();
        System.out.printf("stddev = %f", percolationStats.stddev());
        System.out.println();
        System.out.print("95% confidence interval = ");
        System.out.printf("[%f, %f]", percolationStats.confidenceLo(), percolationStats.confidenceHi());
    }

}