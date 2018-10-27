package TaxiCall;

import java.util.Random;

import TaxiCall.Light.lightcolor;

public class Taxis implements Runnable{
	private Map map;
	private Light light;
	private int[][] flow=new int[80][80];
	Taxi []taxis=new Taxi[100];
	public  void reset(){
		for(int i=0;i<70;i++){
			int x=new Random().nextInt(80);
			int y=new Random().nextInt(80);
			taxis[i]=new Taxi(i,x,y,x,y,-1,-1,-1,-1,"wait",0,0,0,System.currentTimeMillis());
		}
		for(int i=0;i<30;i++){
			int x=new Random().nextInt(80);
			int y=new Random().nextInt(80);
			taxis[i]=new VIPtaxis(i,x,y,x,y,-1,-1,-1,-1,"wait",0,0,0,System.currentTimeMillis());
		}
		
	}
	//MODIFIES:this
	//EFFECTS:为每个计程车的初始地址赋值,前70辆为普通出租车，后30辆为特殊出租车
	public Taxis(Map map,Light light){
		this.map=map;
		this.light=light;
	}
	public void WatchTaxi(int i){
		//REQUIRES:input a right id of the car that you want to get information from
		//EFFECTS:show the taxi information
		if(taxis[i].id>=0&&taxis[i].id<=69){
			System.out.println("Id:"+taxis[i].id+"\nposition:("+taxis[i].x+","+taxis[i].y+")\n"+"condition:"+taxis[i].condition+"\nreputation:"+taxis[i].reputation);
		}
	}
	public void run(){
		while(true){
			try{
				for(int i=0;i<80;i++){
					for(int j=0;j<80;j++){
						flow[i][j]=0;
					}
				}
				update();
			}catch(Exception e){
				System.out.println("illegal require!");
			}
		}
	}
	public void update(){
		//EFFECTS:基本的刷新时间间隔是100毫秒，如果存在汽车停止状况则延长到1000ms，每次刷新时更新每辆汽车的状况;更新每条边上流量数据
		//MODIFIES:this
		for(int i=0;i<100;i++){
			flow[taxis[i].x][taxis[i].y]++;
		}
		for(int i=0;i<100;i++){
			if(taxis[i].condition=="wait" && System.currentTimeMillis()>taxis[i].flashtime){
				taxis[i].x=taxis[i].nx;
				taxis[i].y=taxis[i].ny;
				taxis[i].flashtime=System.currentTimeMillis()+100;
				if(map.map[taxis[i].x-1][taxis[i].y]==2||map.map[taxis[i].x-1][taxis[i].y]==3){
					taxis[i].nx=taxis[i].x-1;
					taxis[i].ny=taxis[i].y;
				}
				else if(map.map[taxis[i].x][taxis[i].y-1]==1||map.map[taxis[i].x-1][taxis[i].y]==3){
					taxis[i].ny=taxis[i].y-1;
					taxis[i].nx=taxis[i].x;
				}
				else if(map.map[taxis[i].x][taxis[i].y]==2||map.map[taxis[i].x][taxis[i].y]==3){
					taxis[i].nx=taxis[i].x+1;
					taxis[i].ny=taxis[i].y;
				}
				else if(map.map[taxis[i].x][taxis[i].y]==1||map.map[taxis[i].x][taxis[i].y]==3){
					taxis[i].ny=taxis[i].y+1;
					taxis[i].nx=taxis[i].x;
				}
				else{
					taxis[i].nx=taxis[i].x;
					taxis[i].ny=taxis[i].y;
				}
				taxis[i].runtime +=100;
				if(taxis[i].px>=0){
					taxis[i].condition="ready";
					taxis[i].runtime=0;
				}
				if(taxis[i].runtime>=20000){
					taxis[i].condition="stop";
					taxis[i].runtime=0;
					taxis[i].flashtime +=1000;
				}
			}
			else if(taxis[i].condition=="ready" && System.currentTimeMillis()>taxis[i].flashtime){
				taxis[i].x=taxis[i].nx;
				taxis[i].y=taxis[i].ny;
				taxis[i].flashtime=System.currentTimeMillis()+100;
				int a1 = 9999,a2=9999,a3=9999,a4=9999;
				if(map.map[taxis[i].x-1][taxis[i].y]==2||map.map[taxis[i].x-1][taxis[i].y]==3){
					a1=map.bfs(taxis[i].x-1, taxis[i].y, taxis[i].px, taxis[i].py);
				}
				if(map.map[taxis[i].x][taxis[i].y-1]==1||map.map[taxis[i].x-1][taxis[i].y]==3){
					a2=map.bfs(taxis[i].x, taxis[i].y-1, taxis[i].px, taxis[i].py);
				}
				if(map.map[taxis[i].x][taxis[i].y]==2||map.map[taxis[i].x][taxis[i].y]==3){
					a3=map.bfs(taxis[i].x+1, taxis[i].y, taxis[i].px, taxis[i].py);
				}
				if(map.map[taxis[i].x][taxis[i].y]==1||map.map[taxis[i].x][taxis[i].y]==3){
					a4=map.bfs(taxis[i].x, taxis[i].y+1, taxis[i].px, taxis[i].py);
				}
				if(a1<a2&&a1<a3&&a1<a4){
					taxis[i].nx=taxis[i].x-1;
					taxis[i].ny=taxis[i].y;
				}
				else if(a2<a1&&a2<a3&&a2<a4){
					taxis[i].ny=taxis[i].y-1;
					taxis[i].nx=taxis[i].x;
				}
				else if(a3<a1&&a3<a2&&a3<a4){
					taxis[i].nx=taxis[i].x+1;
					taxis[i].ny=taxis[i].y;
				}
				else{
					taxis[i].ny=taxis[i].y+1;
					taxis[i].nx=taxis[i].x;
				}
				if((taxis[i].x)==(taxis[i].px)&&(taxis[i].y)==(taxis[i].py)){
					System.out.println("taxi"+i+"arrive at ("+taxis[i].x+","+taxis[i].y+")\n");
					taxis[i].condition ="stop";
					taxis[i].flashtime +=1000;
					taxis[i].px=-1;
					taxis[i].py=-1;
				}
			}
			else if(taxis[i].condition =="working" && System.currentTimeMillis()>taxis[i].flashtime){
				if(i>=70){
					Serve tmp=new Serve(taxis[i].px,taxis[i].py,taxis[i].ax,taxis[i].ay);
					 taxis[i].path.addpath(tmp);
				}
				taxis[i].x=taxis[i].nx;
				taxis[i].y=taxis[i].ny;
				taxis[i].flashtime=System.currentTimeMillis()+100;
				int a1=9999,a2=9999,a3=9999,a4=9999;
				if(map.map[taxis[i].x-1][taxis[i].y]==2||map.map[taxis[i].x-1][taxis[i].y]==3){
					a1=map.bfs(taxis[i].x-1, taxis[i].y, taxis[i].px, taxis[i].py);
				}
				if(map.map[taxis[i].x][taxis[i].y-1]==1||map.map[taxis[i].x-1][taxis[i].y]==3){
					a2=map.bfs(taxis[i].x, taxis[i].y-1, taxis[i].px, taxis[i].py);
				}
				if(map.map[taxis[i].x][taxis[i].y]==2||map.map[taxis[i].x][taxis[i].y]==3){
					a3=map.bfs(taxis[i].x+1, taxis[i].y, taxis[i].px, taxis[i].py);
				}
				if(map.map[taxis[i].x][taxis[i].y]==1||map.map[taxis[i].x][taxis[i].y]==3){
					a4=map.bfs(taxis[i].x, taxis[i].y+1, taxis[i].px, taxis[i].py);
				}
				if(a1<a2&&a1<a3&&a1<a4){
					taxis[i].nx=taxis[i].x-1;
					taxis[i].ny=taxis[i].y;
				}
				else if(a2<a1&&a2<a3&&a2<a4){
					taxis[i].ny=taxis[i].y-1;
					taxis[i].nx=taxis[i].x;
				}
				else if(a3<a1&&a3<a2&&a3<a4){
					taxis[i].nx=taxis[i].x+1;
					taxis[i].ny=taxis[i].y;
				}
				else{
					taxis[i].ny=taxis[i].y+1;
					taxis[i].nx=taxis[i].x;
				}
				if((taxis[i].x)==(taxis[i].ax)&&(taxis[i].y)==(taxis[i].ay)){
					System.out.println("taxi"+i+"arrive at ("+taxis[i].x+","+taxis[i].y+")\n");
					taxis[i].condition ="stop";
					taxis[i].flashtime +=1000;
					taxis[i].ax=-1;
					taxis[i].ax=-1;
				}
			}
			else if(taxis[i].condition =="stop" && System.currentTimeMillis()>taxis[i].flashtime){
				if(taxis[i].ax==-1&&taxis[i].px==-1){
					taxis[i].condition="wait";
				}
				else if(taxis[i].ax!=-1&&taxis[i].px==-1){
					taxis[i].condition="working";
				}
			}
			if(light.light[taxis[i].x][taxis[i].y]==lightcolor.RED&&(taxis[i].nx==taxis[i].x+1||taxis[i].nx==taxis[i].x-1)){
				taxis[i].nx=taxis[i].x;
				taxis[i].ny=taxis[i].y;
			}
			else if(light.light[taxis[i].x][taxis[i].y]==lightcolor.GREEN&&(taxis[i].ny==taxis[i].y+1||taxis[i].ny==taxis[i].y-1)){
				taxis[i].nx=taxis[i].x;
				taxis[i].ny=taxis[i].y;
			}//当路灯为红时出租车将停下等待，但运行状态不变
		}
		
	}
	public boolean repOK(){
		if(map!=null) return true;
		else return false;
	}

}
