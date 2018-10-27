public class Passenger {
    private final Point current;
    private final Point destination;
    private final String name;

    public Passenger(String name, Point current, Point destination) {
	// requires: none of the parameter is null and points from Map
	// modifies: this.name = name;this.current = current;this.destination = destination
	// effects: initialization for current ,destination and name
	this.name = name;
	this.current = current;
	this.destination = destination;
    }

    public Point getCurrent() {
	// effects: return current point
	return current;
    }

    public Point getDestination() {
	// effects: return destination point
	return destination;
    }

    public boolean repOK() {
	// effects: return whether this representation is right
	if (current != null && destination != null && current.repOK() && destination.repOK() && name != null)
	    return true;
	return false;
    }

    @Override
    public String toString() {
	// effects: return passenger info in string
	return name;
    }
}
