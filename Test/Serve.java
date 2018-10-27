package TaxiCall;

public class Serve {
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	public Serve(int x1,int y1,int x2,int y2){
		this.x1=x1;
		this.y1=y1;
		this.x2=y2;
		this.y2=y2;
	}
	public boolean repOK(){
		if(x1>=0&&x1<80&&x2>=0&&x2<80&&y1>=0&&y1<80&&y2>=0&&y2<80){
			return true;
		}
		else return false;
	}
	public int getx1(){
		//EFFECTS:get x1
		return this.x1;
	}
	public int gety1(){
		//EFFECTS:get y1
		return this.x1;
	}
	public int getx2(){
		//EFFECTS:get x2
		return this.x1;
	}
	public int gety2(){
		//EFFECTS:get y2
		return this.x1;
	}
}
