
public class LightThread extends Thread {
    @Override
    public void run() {
	while (true) {
	    Point.changeLight();
	    try {
		Thread.sleep(300);
	    } catch (Throwable e) {
		System.out.println("sleep failed");
		System.exit(0);
	    }
	}
    }
}

enum LIGHT {
    RED, GREEN
}
