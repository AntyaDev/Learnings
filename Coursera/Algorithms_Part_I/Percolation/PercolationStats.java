import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    private final int _gridTilesCount;
    private final int _gridLength;
    private final double[] _thresholds;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {

        if (N <= 0 || T <= 0) throw new IllegalArgumentException("N & T must be greater than 0.");

        _gridLength = N;
        _gridTilesCount = N * N;
        _thresholds = new double[T];

        for (int i = 0; i < T; i++) {
            Percolation grid = new Percolation(N);

            int openedTiles = 0;
            while (!grid.percolates()) {
                openRandomTile(grid);
                openedTiles++;
            }

            _thresholds[i] = calcThreshold(openedTiles, _gridTilesCount);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(_thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(_thresholds);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(_thresholds.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(_thresholds.length);
    }

    private void openRandomTile(Percolation grid) {
        boolean isOpen = true;
        int randomRow = 0;
        int randomCol = 0;

        while (isOpen) {
            randomRow = StdRandom.uniform(1, _gridLength + 1);
            randomCol = StdRandom.uniform(1, _gridLength + 1);

            isOpen = grid.isOpen(randomRow, randomCol);
        }
        grid.open(randomRow, randomCol);
    }

    private double calcThreshold(int openedTiles, int tilesCount) {
        return ((double) openedTiles / (double) tilesCount);
    }

    // test client (described below)
    public static void main(String[] args) {

        Stopwatch timer = new Stopwatch();

        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(N, T);

        StdOut.println("mean\t\t\t\t\t= " + ps.mean());
        StdOut.println("stddev\t\t\t\t\t= " + ps.stddev());
        StdOut.println("95% confidence interval\t= " + ps.confidenceLo() + ", " + ps.confidenceHi());
        StdOut.println("Time elapsed\t\t\t= " + timer.elapsedTime());
    }
}
