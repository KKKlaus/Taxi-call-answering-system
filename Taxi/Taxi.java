import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

public class Taxi implements Runnable {
    private Point currentPoint;
    private Point destination;
    private STATE state;
    private int credit;
    private Passenger passenger;
    private static int no;
    private int id;
    private double time;
    private Edge currentEdge;
    private boolean runningFlag;
    public final static Random random = new Random();

    public boolean repOK() {
	// effects: return whether this representation is right
	if (state != null && time >= 0 && credit >= 0 && currentPoint != null && currentPoint.repOK() && id >= 1 && no >= 0
		&& (passenger == null || passenger.repOK()) && (destination == null || destination.repOK()) && (currentEdge == null || currentEdge.repOK()))
	    return true;
	return false;
    }

    public void setPassenger(Passenger passenger) {
	// requires: passenger is not null
	// modifies: this.passenger
	// effects: set the passenger
	this.passenger = passenger;
    }

    public Taxi(Point p) {
	// requires: point from Map
	// modifies: currentPoint, state, id, no
	// effects: initialization for current point, state,id
	currentPoint = p;
	state = STATE.WAITING;
	currentPoint.addTaxi(this);
	id = ++no;
    }

    public Point getCurrentPoint() {
	// effects: return currentPoint
	return currentPoint;
    }

    @Override
    public void run() {
	try {
	    int waiting = 1;
	    Edge c;
	    while (true) {
		// System.out.println(this);
		switch (state) {
		case STOP:
		    // if (currentEdge != null)
		    // currentEdge.subThroughput();
		    currentEdge = null;
		    if (passenger != null)
			state = STATE.CARRYING;
		    else
			state = STATE.WAITING;
		    Thread.sleep(1000);
		    time += 1;
		    break;
		case WAITING:
		    c = currentEdge;
		    if (!isTurnRight(getWaitingEdge()) && currentPoint.waitLight(c)) {
			// System.out.println("wait for light");
			while (true) {
			    Thread.sleep(50);
			    time += 0.05;
			    if (!currentPoint.waitLight(c))
				break;
			}
		    }
		    currentEdge.addThroughput();
		    Thread.sleep(100);
		    currentPoint.removeTaxi(this);
		    currentPoint = currentEdge.getOther(currentPoint);
		    time += 0.1;
		    currentEdge.subThroughput();
		    currentPoint.addTaxi(this);
		    if (waiting == 200) {
			state = STATE.STOP;
			waiting = 1;
		    } else
			waiting++;
		    break;
		case RUNNING:
		    if (!runningFlag) {
			System.out.println(this + " successfully answered passenger " + passenger);
			runningFlag = true;
		    }
		    if (currentPoint.equals(destination)) {
			System.out.println(this + " has picked up passenger " + passenger);
			destination = passenger.getDestination();
			state = STATE.STOP;
			runningFlag = false;
			break;
		    }
		    c = currentEdge;
		    if (!isTurnRight(findMinEdge()) && currentPoint.waitLight(c)) {
			// System.out.println("wait for light");
			while (true) {
			    Thread.sleep(50);
			    time += 0.05;
			    if (!currentPoint.waitLight(c))
				break;
			}
		    }
		    currentEdge.addThroughput();
		    Thread.sleep(100);
		    currentPoint.removeTaxi(this);
		    currentPoint = currentEdge.getOther(currentPoint);
		    time += 0.1;
		    waiting = 0;
		    currentPoint.addTaxi(this);
		    currentEdge.subThroughput();
		    // System.out.println(this);
		    break;
		case CARRYING:
		    waiting = 0;
		    if (currentPoint.equals(destination)) {
			System.out.println(this + " has arrived passenger " + passenger + "'s destination");
			destination = null;
			passenger = null;
			state = STATE.STOP;
			break;
		    }
		    c = currentEdge;
		    if (!isTurnRight(findMinEdge()) && currentPoint.waitLight(c)) {
			// System.out.println("wait for light");
			while (true) {
			    Thread.sleep(50);
			    time += 0.05;
			    if (!currentPoint.waitLight(c))
				break;
			}
		    }
		    currentEdge.addThroughput();
		    Thread.sleep(100);
		    currentPoint.removeTaxi(this);
		    currentPoint = currentEdge.getOther(currentPoint);
		    time += 0.1;
		    currentPoint.addTaxi(this);
		    currentEdge.subThroughput();
		    // System.out.println(this);
		    break;
		}
	    }
	} catch (Throwable e) {
	    System.out.println("an error occurs");
	    System.exit(0);
	}
    }

    private boolean isTurnRight(Edge next) {
	// requires: next is not null
	// modifies: current edge
	// effects: return whether the taxi is turning right and change current edge
	Edge d, u, l, r;
	d = currentPoint.getDownEdge();
	u = currentPoint.getUpEdge();
	l = currentPoint.getLeftEdge();
	r = currentPoint.getRightEdge();
	boolean turn = (currentPoint.turnLeft() && u != null && u.equals(currentEdge) && l.equals(next))
		|| (currentPoint.turnRight() && d != null && d.equals(currentEdge) && r.equals(next))
		|| (currentPoint.turnDown() && l != null && l.equals(currentEdge) && d.equals(next))
		|| (currentPoint.turnUp() && r != null && r.equals(currentEdge) && u.equals(next));
	currentEdge = next;
	return turn;
    }

    private Edge findMinEdge() {
	// requires: destination is not null
	// effects: find the minimum destination and throughput edge
	int[] i = new int[4];
	int min;
	Edge e;
	i[0] = i[1] = i[2] = i[3] = Integer.MAX_VALUE;
	if (currentPoint.turnUp())
	    i[0] = Map.getPathLength(Map.getUpPoint(currentPoint), destination, 1);
	if (currentPoint.turnDown())
	    i[1] = Map.getPathLength(Map.getDownPoint(currentPoint), destination, 1);
	if (currentPoint.turnLeft())
	    i[2] = Map.getPathLength(Map.getLeftPoint(currentPoint), destination, 1);
	if (currentPoint.turnRight())
	    i[3] = Map.getPathLength(Map.getRightPoint(currentPoint), destination, 1);
	min = i[0];
	e = currentPoint.getUpEdge();
	if (e == null || i[1] < min || (min == i[1] && e.getThroughput() > currentPoint.getDownEdge().getThroughput())) {
	    min = i[1];
	    e = currentPoint.getDownEdge();
	}
	if (e == null || i[2] < min || (min == i[2] && e.getThroughput() > currentPoint.getLeftEdge().getThroughput())) {
	    min = i[2];
	    e = currentPoint.getLeftEdge();
	}

	if (e == null || i[3] < min || (min == i[3] && e.getThroughput() > currentPoint.getRightEdge().getThroughput())) {
	    min = i[3];
	    e = currentPoint.getRightEdge();
	}
	return e;
    }

    private Edge getWaitingEdge() {
	// effects: find the minimum traffic flow
	LinkedList<Edge> edges = new LinkedList<>();
	if (currentPoint.turnUp())
	    edges.add(currentPoint.getUpEdge());
	if (currentPoint.turnDown())
	    edges.add(currentPoint.getDownEdge());
	if (currentPoint.turnLeft())
	    edges.add(currentPoint.getLeftEdge());
	if (currentPoint.turnRight())
	    edges.add(currentPoint.getRightEdge());
	Collections.sort(edges);
	for (int i = 1; i < edges.size(); i++)
	    if (edges.get(i).compareTo(edges.get(0)) < 0) {
		edges.remove(i);
		i--;
	    }
	return edges.get(random.nextInt(edges.size()));
    }

    public int getCredit() {
	// effects: return credit
	return credit;
    }

    public STATE getState() {
	// effects: return state
	return state;
    }

    public void addCredit(int i) {
	// requires: i=1 or i=3 and credit<int.maxvalue
	// modifies: credit
	// effects: add credit
	credit += i;
    }

    public void setDestination(Point destination) {
	// requires: destination from Map
	// modifies: this.destination
	// effects: set the destination
	this.destination = destination;
    }

    public void setState(STATE state) {
	// requires: state is not null
	// modifies: this.state
	// effects: set the state
	this.state = state;
    }

    @Override
    public String toString() {
	// effects: return taxi info in string
	return "Taxi No." + id + " current location: " + currentPoint + " current time: " + Math.round(time * 10) / 10.0 + " credit: " + credit;
    }

}

class TaxiComparator implements Comparator<Taxi> {
    private Passenger p;

    public TaxiComparator(Passenger p) {
	// requires: p is not null
	// modifies: this.p
	// effects: set the passenger
	this.p = p;
    }

    public boolean repOK() {
	// effects: return whether this representation is right
	if (p != null && p.repOK())
	    return true;
	return false;
    }

    @Override
    public int compare(Taxi o1, Taxi o2) {
	// requires: o1 and o2 are not null
	// effects: compare 2 taxis
	if (o1.getCredit() > o2.getCredit())
	    return -1;
	else if (o1.getCredit() < o2.getCredit())
	    return 1;
	else {
	    int i = o1 instanceof TraceableTaxi ? Map.getPathLength(o1.getCurrentPoint(), p.getCurrent(), 0)
		    : Map.getPathLength(o1.getCurrentPoint(), p.getCurrent(), 1);
	    int j = o2 instanceof TraceableTaxi ? Map.getPathLength(o2.getCurrentPoint(), p.getCurrent(), 0)
		    : Map.getPathLength(o2.getCurrentPoint(), p.getCurrent(), 1);
	    if (i > j)
		return 1;
	    else if (i < j)
		return -1;
	    else
		return 0;
	}
    }
}