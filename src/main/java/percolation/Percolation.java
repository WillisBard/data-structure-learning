package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final int MIN = 1;

    private final WeightedQuickUnionUF weightedQuickUnionUF;

    private final boolean[] grid;

    private final int virtualTop;

    private final int virtualBottom;

    private final int max;

    private int count;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        count = 0;
        max = n;
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        grid = new boolean[n * n + 2];
        virtualTop = 0;
        virtualBottom = n * n + 1;
        for (int i = MIN; i <= n * n; i++) {
            grid[i] = false;
            if (i <= n) {
                weightedQuickUnionUF.union(i, virtualTop);
            }
            if (i > (n * n) - n) {
                weightedQuickUnionUF.union(i, virtualBottom);
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || row > max) throw new IndexOutOfBoundsException("row index " + row + " out of bounds");
        if (col <= 0 || col > max) throw new IndexOutOfBoundsException("col index " + col + " out of bounds");
        int current = getIndex(max, row, col);
        grid[current] = true;
        count++;
        if (row != MIN) {
            int prevRow = row - 1;
            if (isOpen(prevRow, col)) {
                int top = getIndex(max, prevRow, col);
                weightedQuickUnionUF.union(top, current);
            }
        }
        if (row != max) {
            int nextRow = row + 1;
            if (isOpen(nextRow, col)) {
                int bottom = getIndex(max, nextRow, col);
                weightedQuickUnionUF.union(bottom, current);
            }
        }
        if (col != MIN) {
            int prevCol = col - 1;
            if (isOpen(row, prevCol)) {
                int left = getIndex(max, row, prevCol);
                weightedQuickUnionUF.union(left, current);
            }
        }
        if (col != max) {
            int nextCol = col + 1;
            if (isOpen(row, nextCol)) {
                int right = getIndex(max, row, nextCol);
                weightedQuickUnionUF.union(right, current);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return grid[getIndex(max, row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return weightedQuickUnionUF.connected(getIndex(max, row, col), virtualTop) && isOpen(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return weightedQuickUnionUF.connected(virtualTop, virtualBottom);
    }

    // test client (optional)
    public static void main(String[] args) {
    }

    private static int getIndex(int size, int row, int col) {
        return (row * size) - (size - col);
    }
}
