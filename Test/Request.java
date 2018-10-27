package TaxiCall;

import java.util.concurrent.*;

public class Request {
	public int nowx;
	public int nowy;
	public int aimx;
	public int aimy;
	public long nowtime;
	BlockingQueue<Integer> tid = new ArrayBlockingQueue<Integer>(100);
	
	Request(int x1,int y1,int x2,int y2){
		if(x1>0 && x1<81 && x2>0 && x2<81 && y1>0 && y1<81 && y2>0 && y2<81 ){
			this.nowx=x1; 
			this.nowy=y1;
			this.aimx=x2;
			this.aimy=y2;
			this.nowtime=System.currentTimeMillis();
		}
	}
	public boolean repOK(){
		return true;
	}
}
