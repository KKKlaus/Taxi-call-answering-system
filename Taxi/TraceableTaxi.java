import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class TraceableTaxi extends Taxi {
    private LinkedList<Path> pathList;
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
    private boolean carryingFlag;

    @Override
    public boolean repOK() {
	// effects: return whether this representation is right
	if (super.repOK()&&state != null && time >= 0 && credit >= 0 && currentPoint != null && currentPoint.repOK() && id >= 1 && no >= 0
		&& (passenger == null || passenger.repOK()) && (destination == null || destination.repOK()) && (currentEdge == null || currentEdge.repOK())){
	    for(Path path : pathList)
		if(!path.repOK())
		    return false;
	    return true;
	}
	    
	return false;
    }

    @Override
    public void setPassenger(Passenger passenger) {
	// requires: passenger is not null
	// modifies: this.passenger
	// effects: set the passenger
	this.passenger = passenger;
    }

    @Override
    public Point getCurrentPoint() {
	// effects: return currentPoint
	return currentPoint;
    }

    public TraceableTaxi(Point p) {
	super(p);
	currentPoint = p;
	state = STATE.WAITING;
	currentPoint.addTaxi(this);
	id = ++no;
	pathList = new LinkedList<>();
    }

    @Override
    public void run() {
	try {
	    int waiting = 1;
	    Edge c;
	    Path path = null;
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
		    if (!carryingFlag) {
			path = new Path(passenger);
			pathList.add(path);
			carryingFlag = true;
		    }
		    path.add(currentPoint);
		    waiting = 0;
		    if (currentPoint.equals(destination)) {
			System.out.println(this + " has arrived passenger " + passenger + "'s destination");
			destination = null;
			passenger = null;
			state = STATE.STOP;
			carryingFlag = false;
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
		    //System.out.println(this);
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
	boolean turn = (l != null && u != null && u.equals(currentEdge) && l.equals(next))
		|| (r != null && d != null && d.equals(currentEdge) && r.equals(next)) || (d != null && l != null && l.equals(currentEdge) && d.equals(next))
		|| (u != null && r != null && r.equals(currentEdge) && u.equals(next));
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
	if (currentPoint.getUpEdge() != null)
	    i[0] = Map.getPathLength(Map.getUpPoint(currentPoint), destination, 0);
	if (currentPoint.getDownEdge() != null)
	    i[1] = Map.getPathLength(Map.getDownPoint(currentPoint), destination, 0);
	if (currentPoint.getLeftEdge() != null)
	    i[2] = Map.getPathLength(Map.getLeftPoint(currentPoint), destination, 0);
	if (currentPoint.getRightEdge() != null)
	    i[3] = Map.getPathLength(Map.getRightPoint(currentPoint), destination, 0);
	min = i[0];
	e = currentPoint.getUpEdge();
	if (e==null||i[1] < min || (min == i[1] && e.getThroughput() > currentPoint.getDownEdge().getThroughput())) {
	    min = i[1];
	    e = currentPoint.getDownEdge();
	}
	if (e==null||i[2] < min || (min == i[2] && e.getThroughput() > currentPoint.getLeftEdge().getThroughput())) {
	    min = i[2];
	    e = currentPoint.getLeftEdge();
	}

	if (e==null||i[3] < min || (min == i[3] && e.getThroughput() > currentPoint.getRightEdge().getThroughput())) {
	    min = i[3];
	    e = currentPoint.getRightEdge();
	}
	return e;
    }

    private Edge getWaitingEdge() {
	// effects: find the minimum traffic flow
	LinkedList<Edge> edges = new LinkedList<>();
	if (currentPoint.getUpEdge() != null)
	    edges.add(currentPoint.getUpEdge());
	if (currentPoint.getDownEdge() != null)
	    edges.add(currentPoint.getDownEdge());
	if (currentPoint.getLeftEdge() != null)
	    edges.add(currentPoint.getLeftEdge());
	if (currentPoint.getRightEdge() != null)
	    edges.add(currentPoint.getRightEdge());
	Collections.sort(edges);
	for (int i = 1; i < edges.size(); i++)
	    if (edges.get(i).compareTo(edges.get(0)) < 0) {
		edges.remove(i);
		i--;
	    }
	return edges.get(random.nextInt(edges.size()));
    }
    
    public int getTotalServingTimes(){
	//effects:return total serving times
	return pathList.size();
    }

    public Path getPathByPassenger(Passenger p) {
	// requires: p exists in path in path list
	// effects: search for path by passenger
	Iterator<Path> iterator = pathList.iterator();
	Path i = null;
	while (iterator.hasNext()) {
	    i = iterator.next();
	    if (i.getPassenger().equals(p))
		return i;
	}
	System.out.println("no such passenger");
	System.exit(0);
	return null;
    }

    @Override
    public int getCredit() {
	// effects: return credit
	return credit;
    }

    @Override
    public STATE getState() {
	// effects: return state
	return state;
    }

    @Override
    public void addCredit(int i) {
	// requires: i=1 or i=3 and credit<int.maxvalue
	// modifies: credit
	// effects: add credit
	credit += i;
    }

    @Override
    public void setDestination(Point destination) {
	// requires: destination from Map
	// modifies: this.destination
	// effects: set the destination
	this.destination = destination;
    }

    @Override
    public void setState(STATE state) {
	// requires: state is not null
	// modifies: this.state
	// effects: set the state
	this.state = state;
    }

    @Override
    public String toString() {
	// effects: return taxi info in string
	return "Traceable Taxi No." + id + " current location: " + currentPoint + " current time: " + Math.round(time * 10) / 10.0 + " credit: " + credit;
    }

}
