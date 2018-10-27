import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Scheduler implements Runnable {
    private LinkedBlockingQueue<Passenger> passengerQueue;

    public Scheduler(LinkedBlockingQueue<Passenger> passengerQueue) {
	// requires: passengerQueue is not null
	// modifies: this.passengerQuueue
	// effects: initialization for passengerQueue
	this.passengerQueue = passengerQueue;
    }

    public boolean repOK() {
	// effects: return whether this representation is right
	if (passengerQueue != null)
	    return true;
	return false;
    }

    @Override
    public void run() {
	try {
	    while (true) {
		Passenger passenger = passengerQueue.take();
		Point current = passenger.getCurrent();
		new FindThread(passenger, current).start();
	    }
	} catch (Throwable e) {
	    System.out.println("an error occurs");
	    System.exit(0);
	}
    }

    private class FindThread extends Thread {
	private Point p;
	private LinkedList<Taxi> taxiQueue;
	private Passenger passenger;

	public FindThread(Passenger passenger, Point p) {
	    // requires: none of the parameters are null and point from Map
	    // modifies: this.passenger = passenger;this.p = p;this.taxiQueue = new LinkedList<>();
	    // effects: initialization for passenger, p, taxiQueue
	    this.passenger = passenger;
	    this.p = p;
	    this.taxiQueue = new LinkedList<>();
	}

	@Override
	public void run() {
	    try {
		LinkedList<Taxi> tmp = null;
		taxiQueue.clear();
		short x = p.getX();
		short y = p.getY();
		Point[] points = Map.getPoints(x - 2, x + 2, y - 2, y + 2);
		int i = 1;
		while (i <= 60) {
		    for (Point p : points) {
			synchronized (p) {
			    if ((tmp = p.getTaxiList()) != null) {
				for (Taxi taxi : tmp)
				    if (taxi != null && taxi.getState() == STATE.WAITING && !taxiQueue.contains(taxi)) {
					taxiQueue.add(taxi);
					taxi.addCredit(1);
					System.out.println(taxi + " is answering to passenger " + passenger);
				    }
			    }
			}
		    }
		    Thread.sleep(50);
		    i++;
		}
		Collections.sort(taxiQueue, new TaxiComparator(passenger));
		Taxi j;
		// System.out.println(taxiQueue);
		while (!taxiQueue.isEmpty()) {
		    j = taxiQueue.poll();
		    synchronized (j) {
			if (j.getState() == STATE.WAITING) {
			    j.setDestination(p);
			    j.setState(STATE.RUNNING);
			    j.setPassenger(passenger);
			    j.addCredit(3);
			    return;
			}
		    }
		}
		System.out.println("no car available for passenger " + passenger);
	    } catch (Throwable e) {
		System.out.println("an error occurs");
		System.exit(0);
	    }
	}
    }
}
