import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Path extends LinkedList<Point> {
    private TWIterator iterator;
    private Passenger passenger;

    public Path(Passenger passenger) {
	//requires: p!=null
	//modifies:this
	//effects:initialization for this
	super();
	this.passenger = passenger;
    }

    public boolean repOK() {
   	// effects: return whether this representation is right
   	if (passenger!=null)
   	    return true;
   	return false;
       }
    public Passenger getPassenger() {
	//effects:return passenger
	return passenger;
    }

    private class TWIterator implements TwoWayIterator<Point> {
	private int n;
	private int p = size() - 1;

	@Override
	public boolean hasPrevious() {
	    //effects:return whether it has previous elements
	    return p >= 0;
	}

	@Override
	public boolean hasNext() {
	  //effects:return whether it has next elements
	    return n < size();
	}

	@Override
	public Point next() {
	    //requires: hasNext is true
	    //effects:return next element if hasNext is true;otherwise throw NoSuchElementException
	    if (hasNext()) {
		n++;
		return get(n - 1);
	    } else
		throw new NoSuchElementException();
	}

	@Override
	public Point previous() {
	    //requires: hasPrevious is true
	    //effects:return previous element if hasPrevious is true;otherwise throw NoSuchElementException
	    if (hasPrevious()) {
		p--;
		return get(p + 1);
	    } else
		throw new NoSuchElementException();
	}

    }

    public TwoWayIterator<Point> getTwoWayIterator() {
	//return a two way iterator
	iterator = new TWIterator();
	return iterator;
    }
}

interface TwoWayIterator<E> extends Iterator<E> {
    public boolean hasPrevious();

    public E previous();

}