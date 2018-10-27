import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;


public class TestSystem {
    public static void main(String[] args) {
	try {
	    Map.initial();
	    LinkedBlockingQueue<Passenger> passengerQueue = new LinkedBlockingQueue<>(305);
	    //comments starts here
	    int taxiSize = 100;
	    Random r = new Random();
	    Taxi[] taxiList = new Taxi[taxiSize];
	    Thread[] threads = new Thread[taxiSize];
	    for (int i = 0; i < 70; i++) {
		taxiList[i] = new Taxi(Map.getPoint(r.nextInt(80) + 1, r.nextInt(80) + 1));
	    }
	    for (int i = 70; i < taxiSize; i++) {
		taxiList[i] = new TraceableTaxi(Map.getPoint(r.nextInt(80) + 1, r.nextInt(80) + 1));
	    }
	    for (int i = 0; i < taxiSize; i++) {
		threads[i] = new Thread(taxiList[i]);
		threads[i].start();
	    }
	    Scheduler scheduler = new Scheduler(passengerQueue);
	    Thread s = new Thread(scheduler);
	    Thread.sleep(3000);
	    s.start();
	    LightThread lightThread = new LightThread();
	    lightThread.start();
	    System.out.println("initialization over");
	    TestThread test = new TestThread(passengerQueue, taxiList);
	    Thread t = new Thread(test);
	    t.start();
	  //comments ends here
	} catch (Throwable e) {
	    System.out.println("an error occurs");
	    System.exit(0);
	}
    }
}

enum STATE {
    STOP, RUNNING, WAITING, CARRYING
}
