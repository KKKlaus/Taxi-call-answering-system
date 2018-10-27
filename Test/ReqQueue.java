package TaxiCall;

import java.util.concurrent.BlockingQueue;

public class ReqQueue implements Runnable{
    Map map;
	Taxis taxis;
	private BlockingQueue<Request> rq;
	
	ReqQueue(Map map,Taxis taxis,BlockingQueue<Request> rq){
		this.map=map;
		this.taxis=taxis;
		this.rq=rq;
	}
	public void addreq(int x1,int y1,int x2,int y2){
		//REQURES:input right condition
		//MODIFIES:rq
		//EFFECTS:add a new request to the request queue
		if(x1>=0&&x1<80&&y1>=0&&y1<80&&x2>=0&&x2<80&&y2>=0&&y2<80){
			Request req=new Request(x1,y1,x2,y2);
			rq.add(req);
		}
		else{
			System.out.println("Wrong Request input!");
		}
	}
	public void run(){
		while(true){
			//System.out.println("test 5");
			try{
				while(!rq.isEmpty()){
					Request reqtemp=rq.remove();//reqtemp保存每次请求队列的首位
					int x1=reqtemp.nowx;
					int y1=reqtemp.nowy;
					for(int i=0;i<100;i++){
						if(taxis.taxis[i].x>=x1-2 && taxis.taxis[i].x<=x1+2 && taxis.taxis[i].y>=y1-2 && taxis.taxis[i].y<=y1+2){
							if(taxis.taxis[i].condition=="wait"){
								taxis.taxis[i].reputation++;//每次抢单信誉度加一
								reqtemp.tid.add(i);//枪弹的车辆保存在reqtemp的tip，即出租车标号队列中
							}
						}
					}
					if(reqtemp.nowtime+3000<System.currentTimeMillis()){
						int flag=-1;
						while(reqtemp.tid.isEmpty()!=false){
							int tidtemp=reqtemp.tid.remove();
							if(flag==-1&&taxis.taxis[tidtemp].condition=="wait"&&taxis.taxis[tidtemp].ax==-1){
								flag=tidtemp;
							}
							else if(flag>-1&&taxis.taxis[tidtemp].reputation>taxis.taxis[flag].reputation&&taxis.taxis[tidtemp].condition=="wait"&&taxis.taxis[tidtemp].ax==-1){
								flag=tidtemp;
							}
							else if(flag>-1&&taxis.taxis[tidtemp].reputation==taxis.taxis[flag].reputation&&taxis.taxis[tidtemp].condition=="wait"&&taxis.taxis[tidtemp].ax==-1){
								if(map.bfs(x1, y1, taxis.taxis[tidtemp].x, taxis.taxis[tidtemp].y)<map.bfs(x1, y1, taxis.taxis[flag].x, taxis.taxis[flag].y)){
									flag=tidtemp;;
								}
							}
						
						}
						if(flag==-1){
							System.out.println("The passagers in("+x1+","+y1+") can't call for any taxis!");
						}
						else{
							taxis.taxis[flag].px=x1;
							taxis.taxis[flag].py=y1;
							taxis.taxis[flag].reputation+=3;
							System.out.println("The taxi "+flag+" accept the call in ("+x1+","+y1+").");
						}
					}
					else{
						rq.add(reqtemp);
					}
									
				
				}
			}catch(Exception e){
				System.out.println("Wrong require!");
				System.exit(0);
			}
		}
	}
	public boolean repOK(){
		if(map!=null||taxis!=null||rq!=null) return true;
		else return false;
	}
}
