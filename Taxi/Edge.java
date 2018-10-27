
public class Edge implements Comparable<Edge> {
    private final Point p1;
    private final Point p2;
    private boolean isOpen;
    private int throughput;
    
    public Edge(Point p1, Point p2, boolean isOpen) {
	// requires: points from Map
	// modifies: p1 p2 this.isOpen
	// effects: initialization for points and state
	this.p1 = p1;
	this.p2 = p2;
	this.isOpen = isOpen;
    }

    public boolean repOK() {
	// effects: return whether this representation is right
	if (p1 != null && p2 != null && p1.repOK() && p2.repOK()&&throughput >=0)
	    return true;
	return false;
    }

    public void setOpen(boolean isOpen) {
	// modifies: this.isOpen
	// effects: set boolean isOpen
	this.isOpen = isOpen;
    }

    public synchronized int getThroughput() {
	// effects: return the throughput
	return throughput;
    }

    public synchronized void addThroughput() {
	// modifies: throughput
	// effects: add 1 to throughput
	throughput++;
    }

    public synchronized void subThroughput() {
	//requires: throughput >=1
	// modifies: throughput
	// effects: subtract 1 to throughput
	throughput--;
    }

    public Point getOther(Point p) {
	// requires: p equals to p1 or p2
	// effects: return another point
	if (p.equals(p1))
	    return p2;
	else
	    return p1;
    }

    @Override
    public int compareTo(Edge o) {
	// requires: o is not null
	// effects: compare 2 edges
	if (!o.isOpen && isOpen)
	    return -1;
	else if (o.isOpen && !isOpen)
	    return 1;
	else {
	    if (o.getThroughput() > throughput)
		return -1;
	    else if (o.getThroughput() < throughput)
		return 1;
	    else
		return 0;
	}
    }

    @Override
    public String toString() {
	// effects: return edge info in string
	return p1 + "-" + p2;
    }
}
