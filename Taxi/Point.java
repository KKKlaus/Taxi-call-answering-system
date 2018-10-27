import java.util.LinkedList;

public class Point {
    private final short x;
    private final short y;
    private boolean down, right, left, up;
    private LinkedList<Taxi> taxiList;
    private int index;
    private Edge downEdge, rightEdge, leftEdge, upEdge;
    private static LIGHT HTrafficLight = LIGHT.RED;
    private static LIGHT VTrafficLight = LIGHT.GREEN;
    private boolean isOverpass;

    public static void changeLight() {
	// modifies: HTrafficLight VTrafficLight
	// effects: change the traffic lights
	if (HTrafficLight == LIGHT.RED)
	    HTrafficLight = LIGHT.GREEN;
	else
	    HTrafficLight = LIGHT.RED;
	if (VTrafficLight == LIGHT.RED)
	    VTrafficLight = LIGHT.GREEN;
	else
	    VTrafficLight = LIGHT.RED;
	//System.out.println("hlight: " + HTrafficLight + " vlight: " + VTrafficLight);
    }

    public boolean repOK() {
	// effects: return whether this representation is right
	if (x >= 1 && x <= 80 && y >= 1 && y <= 80)
	    return true;
	return false;
    }

    public boolean waitLight(Edge e) {
	// requires: e equals to downEdge or rightEdge or leftEdge or upEdge
	// effects: return whether a taxi should wait for the traffic light
	if (e == null || isOverpass)
	    return false;
	else if ((!down && right && left && up) || (down && !right && left && up) || (down && right && !left && up) || (down && right && left && !up)
		|| (down && right && left && up)) {
	    if ((down && e.equals(downEdge)) || (up && e.equals(upEdge))) {
		if (VTrafficLight == LIGHT.RED)
		    return true;
		else
		    return false;
	    } else {
		if (HTrafficLight == LIGHT.RED)
		    return true;
		else
		    return false;
	    }
	} else
	    return false;
    }

    public void setOverpass(boolean isOverpass) {
	// modifies: this.isOverpass
	// effects: set this.isOverpass
	this.isOverpass = isOverpass;
    }

    public Edge getDownEdge() {
	// effects: return down edge
	return downEdge;
    }

    public Edge getRightEdge() {
	// effects: return right edge
	return rightEdge;
    }

    public Edge getLeftEdge() {
	// effects: return left edge
	return leftEdge;
    }

    public Edge getUpEdge() {
	// effects: return up edge
	return upEdge;
    }

    public void setDownEdge(Edge downEdge) {
	// modifies: downEdge
	// effects: set down edge
	this.downEdge = downEdge;
    }

    public void setRightEdge(Edge rightEdge) {
	// modifies: rightEdge
	// effects: set right edge
	this.rightEdge = rightEdge;
    }

    public void setLeftEdge(Edge leftEdge) {
	// modifies: leftEdge
	// effects: set left edge
	this.leftEdge = leftEdge;
    }

    public void setUpEdge(Edge upEdge) {
	// modifies: upEdge
	// effects: set up edge
	this.upEdge = upEdge;
    }

    public Point(short x, short y, int state) {
	// requires: 1<=x<=80 1<=y<=80 0<=state<=3
	// modifies: x y down right left up index
	// effects: initialization for x, y index and connection state
	if (x > Map.size || y > Map.size || x < 1 || y < 1) {
	    System.out.println("wrong point");
	    System.exit(0);
	}
	this.x = x;
	this.y = y;
	index = (x - 1) * Map.size + y;
	left = checkLeft();
	up = checkUp();
	taxiList = new LinkedList<>();
	switch (state) {
	case 0:
	    down = right = false;
	    break;
	case 1:
	    right = true;
	    down = false;
	    break;
	case 2:
	    right = false;
	    down = true;
	    break;
	case 3:
	    right = down = true;
	    break;
	default:
	    System.out.println("wrong number!");
	    System.exit(0);
	    break;
	}
    }

    public synchronized void addTaxi(Taxi taxi) {
	// modifies: taxi list
	// effects: add taxi to taxi list
	taxiList.add(taxi);
    }

    public synchronized void removeTaxi(Taxi taxi) {
	// modifies: taxi list
	// effects: remove taxi from taxi list
	taxiList.remove(taxi);
    }

    public synchronized boolean turnRight() {
	// effects: return boolean right
	return right;
    }

    public synchronized boolean turnDown() {
	// effects: return boolean down
	return down;
    }

    public short getX() {
	// effects: return x
	return x;
    }

    public short getY() {
	// effects: return y
	return y;
    }

    public synchronized LinkedList<Taxi> getTaxiList() {
	// effects: return taxi list
	return taxiList;
    }

    public synchronized boolean turnLeft() {
	// effects: return boolean left
	return left;
    }

    public synchronized boolean turnUp() {
	// effects: return boolean up
	return up;
    }

    public synchronized void setDown(boolean down) {
	// modifies: this.down
	// effects: set boolean down
	this.down = down;
    }

    public synchronized void setLeft(boolean left) {
	// modifies: this.left
	// effects: set boolean left
	this.left = left;
    }

    public synchronized void setRight(boolean right) {
	// modifies: this.right
	// effects: set boolean right
	this.right = right;
    }

    public synchronized void setUp(boolean up) {
	// modifies: this.up
	// effects: set boolean up
	this.up = up;
    }

    private boolean checkLeft() {
	// effects: check whether left is open
	Point point = Map.getLeftPoint(this);
	if (point == null || !point.turnRight())
	    return false;
	return true;
    }

    private boolean checkUp() {
	// effects: check whether up is open
	Point point = Map.getUpPoint(this);
	if (point == null || !point.turnDown())
	    return false;
	return true;
    }

    @Override
    public String toString() {
	// effects:return point info in string
	return "(" + x + " , " + y + ")";
    }

    public int getIndex() {
	// effects: return index
	return index;
    }

    @Override
    public boolean equals(Object obj) {
	// effects: check whether 2 points are equal
	if (obj instanceof Point) {
	    Point point = (Point) obj;
	    if (point.getX() == x && point.getY() == y)
		return true;
	}
	return false;
    }
}
