package TaxiCall;


public class VIPtaxis extends Taxi{
	VIPtaxis(int id, int x, int y, int nx, int ny, int px, int py, int ax, int ay, String condition, int reputation,
			long runtime, int movement, long flashtime) {
		super(id, x, y, nx, ny, px, py, ax, ay, condition, reputation, runtime, movement, flashtime);		
	}
	private Path path=new Path();
	public  void addpath(Serve serve){
		//REQUIRES:input a right serve
		//MODIFIES:path
		//EFFECTS:add a new serve to the serve queue
		path.path.add(serve);
	}
}
