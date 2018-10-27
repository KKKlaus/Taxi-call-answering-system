import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class TestThread implements Runnable {
    private LinkedBlockingQueue<Passenger> passengerQueue;
    private Taxi[] taxiList;

    public TestThread(LinkedBlockingQueue<Passenger> passengerQueue, Taxi[] taxiList) {
	// requires: passengerQueue and taxiList are not null
	// modifies: this.passengerQueue this.taxiList
	// effects: initialization for passengerQueue and taxiList
	this.passengerQueue = passengerQueue;
	this.taxiList = taxiList;
    }


    @Override
    public void run() {

	try {
	    /* example: Passenger passenger = new Passenger("1", Map.getPoint(12, 24),Map.getPoint(20, 30));
		passengerQueue.put(passenger);
		getInfo(71);
	    TwoWayIterator<Point> iterator=getPathInfoByPassenger(i, passenger);
	    iterator.next();
	     */
	
	} catch (Throwable e) {
	    System.out.println("failed");
	}

    }

    public int getServingTimes(int i) {
	// requires: taxi i is a traceable taxi and 70<=i<=100
	// effects: return the total serving times of traceable taxi i
	if (i < 70 || i > 100) {
	    System.out.println("wrong number!");
	    System.exit(0);
	    return 0;
	} else {
	    int size = 0;
	    try {
		size = ((TraceableTaxi) taxiList[i]).getTotalServingTimes();
	    } catch (ClassCastException e) {
		System.out.println("this is not a traceable taxi");
		System.exit(0);
	    }
	    return size;
	}
    }

    public TwoWayIterator<Point> getPathInfoByPassenger(int i, Passenger p) {
	// requires: p!=null and taxi i is a traceable taxi and taxi i served p before and 70<=i<=100
	// effects: return a two way iterator of the path when serving passenger p
	if (i < 70 || i > 100 || p == null) {
	    System.out.println("wrong!");
	    System.exit(0);
	    return null;
	} else {
	    Path path = null;
	    try {
		path = ((TraceableTaxi) taxiList[i]).getPathByPassenger(p);
	    } catch (ClassCastException e) {
		System.out.println("this is not a traceable taxi");
		System.exit(0);
	    }
	    return path.getTwoWayIterator();
	}
    }

    public void getInfo(int i) {
	// requires: 1<=i<=100
	// effects: print the required taxi info; if i is invalid ,print wrong number
	if (i < 1 || i > 100) {
	    System.out.println("wrong number!");
	    System.exit(0);
	} else
	    System.out.println(taxiList[i - 1]);
    }

    public boolean repOK() {
	// effects: return whether this representation is right
	if (passengerQueue != null && taxiList != null && taxiList.length == 100)
	    return true;
	return false;
    }

    public void changeEdge(Point p1, Point p2, boolean isOpen) {
	// requires: p1 p2 belongs to Map and there is an edge between them
	// modifies: p1 p2 and edge between them
	// effects: change the connection state of the edge between p1 and p2
	if (p1.getDownEdge() != null && p1.getDownEdge().getOther(p1).equals(p2)) {
	    p1.getDownEdge().setOpen(isOpen);
	    p1.setDown(isOpen);
	    p2.setUp(isOpen);
	} else if (p1.getUpEdge() != null && p1.getUpEdge().getOther(p1).equals(p2)) {
	    p1.getUpEdge().setOpen(isOpen);
	    p1.setUp(isOpen);
	    p2.setDown(isOpen);
	} else if (p1.getLeftEdge() != null && p1.getLeftEdge().getOther(p1).equals(p2)) {
	    p1.getLeftEdge().setOpen(isOpen);
	    p1.setLeft(isOpen);
	    p2.setRight(isOpen);
	} else if (p1.getRightEdge() != null && p1.getRightEdge().getOther(p1).equals(p2)) {
	    p1.getRightEdge().setOpen(isOpen);
	    p1.setRight(isOpen);
	    p2.setLeft(isOpen);
	} else {
	    System.out.println("wrong edge");
	    System.exit(0);
	}
	// System.out.println("changed edge");
    }

}
