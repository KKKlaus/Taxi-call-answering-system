package TaxiCall;

import java.util.ArrayList;
import java.util.Iterator;

public class Path implements Iterator{
	private int iter;
	private int n;

	ArrayList<Serve>path=new ArrayList();
	public Path(){
		this.n=path.size();
	}
	@Override
	public boolean hasNext(){
		//EFFECTS:return iter>=0
		if(iter<n) return true;
		else return false;
	}
	public  void addpath(Serve serve){
		//REQUIRES:input a right serve
		//MODIFIES:path
		//EFFECTS:add a new serve to the serve queue
		path.add(serve);
	}
	//EFFECTS:return iter>=0
	@Override
	public Object next()
	{
		if (iter < n)
		{
			Serve temp = new Serve(path.get(iter).getx1(), path.get(iter).gety1(),path.get(iter).getx2(),path.get(iter).gety2());
			iter++;
			return temp;
		}
		else
			return null;
	}
	public boolean repOK(){
		if(iter<n&&path!=null){
			return true;
		}
		else{
			return false;
		}
	}
	
	
}
