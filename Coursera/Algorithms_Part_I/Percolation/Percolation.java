import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private enum Site { BLOCKED, OPEN }

    private final Site[] _sites;
    private final boolean[] _sitesConnectedToBottom;
    private final int _topVirtualSiteIndex = 0;
    private final int _gridLineLength;
    private final WeightedQuickUnionUF _unionFind;

    // create n-by-n grid, with all _sites blocked
    public Percolation(int n) {

        if (n <= 0) throw new IllegalArgumentException("n can't be less then 0 or equal");

        _gridLineLength = n;
        int gridSize = n * n + 1; // + 1 for virtual top side

        _sites = new Site[gridSize];
        _sitesConnectedToBottom = new boolean[gridSize];
        _unionFind = new WeightedQuickUnionUF(gridSize);

        _sites[_topVirtualSiteIndex] = Site.OPEN;
        for (int i = 1; i < gridSize; i++) {
            _sites[i] = Site.BLOCKED;
        }

        // mark all sites in the last row as "connected to bottom"
        int lastRowIndex = gridSize - _gridLineLength;
        for (int i = lastRowIndex; i < gridSize; i++) {
            _sitesConnectedToBottom[i] = true;
        }
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        int row = i;
        int column = j;
        isValidRange(row, column);

        int siteIndex = getSiteIndex(row, column);
        if (_sites[siteIndex] == Site.BLOCKED) {

            _sites[siteIndex] = Site.OPEN;

            if (row == 1) { // 1 == top row, we should connect to virtual site
                union(siteIndex, _topVirtualSiteIndex);
            }

            if (row > 1) { // connect to top site
                tryToConnect(row - 1, column, siteIndex);
            }

            if (row < _gridLineLength) { // connect to down site
                tryToConnect(row + 1, column, siteIndex);
            }

            if (column > 1) { // connect to left site
                tryToConnect(row, column - 1, siteIndex);
            }

            if (column < _gridLineLength) { // connect to right site
                tryToConnect(row, column + 1, siteIndex);
            }
        }
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        int row = i;
        int column = j;
        isValidRange(row, column);

        int siteIndex = getSiteIndex(row, column);
        return _sites[siteIndex] == Site.OPEN;
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        int row = i;
        int column = j;
        isValidRange(row, column);

        int siteIndex = getSiteIndex(row, column);
        return _unionFind.connected(siteIndex, _topVirtualSiteIndex);
    }

    // does the system percolate?
    public boolean percolates() {
        int rootOfVirtualTop = _unionFind.find(_topVirtualSiteIndex);
        return _sitesConnectedToBottom[rootOfVirtualTop];
    }

    private void isValidRange(int row, int column) {
        if (row < 1 || column < 1 || row > _gridLineLength || column > _gridLineLength)
            throw new IndexOutOfBoundsException("row or column has violated range");
    }

    private int getSiteIndex(int row, int column) {
        return (row - 1) * _gridLineLength + column;
    }

    private void tryToConnect(int row, int column, int siteIndex) {
        int targetSiteIndex = getSiteIndex(row, column);
        if (_sites[targetSiteIndex] == Site.OPEN) {
            union(siteIndex, targetSiteIndex);
        }
    }

    private void union(int p, int q) {
        connectRootsToBottom(p, q);
        _unionFind.union(p, q);
    }

    private void connectRootsToBottom(int p, int q) {
        int pRoot = _unionFind.find(p);
        int qRoot = _unionFind.find(q);

        boolean anyOfRootsConnectedToBottom = _sitesConnectedToBottom[pRoot]
                || _sitesConnectedToBottom[qRoot];

        _sitesConnectedToBottom[pRoot] = anyOfRootsConnectedToBottom;
        _sitesConnectedToBottom[qRoot] = anyOfRootsConnectedToBottom;
    }

    public static void main(String[] args) {

        Percolation p = new Percolation(3);
        p.open(1, 1);
        p.open(2, 1);
        p.open(3, 1);
        StdOut.println(p.isFull(3, 1));
        StdOut.println(p.percolates());

        p.open(2, 3);
        p.open(3, 3);
        StdOut.println(p.isFull(2, 3));
    }
}
