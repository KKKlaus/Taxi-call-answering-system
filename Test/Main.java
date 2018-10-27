package TaxiCall;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
	public static void main(String args){
		try{
			Map map =new Map();
			map.readmap();
			Light light=new Light(map);
			light.lightreset();
			Taxis taxis =new Taxis(map,light);
			taxis.reset();
			BlockingQueue<Request> rq = new ArrayBlockingQueue<Request>(300);
			ReqQueue reqqueue= new ReqQueue(map,taxis,rq);
			new Thread(taxis).start();
			new Thread(light).start();
			new Thread(reqqueue).start();
					
		}catch(Exception e){
			System.exit(0);
		}
	}
}
