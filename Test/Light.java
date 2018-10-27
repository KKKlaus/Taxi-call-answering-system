package TaxiCall;

import java.util.Random;
import static java.lang.Thread.sleep;

public class Light implements Runnable{
	private Map map;
	lightcolor[][] light=new lightcolor[80][80];
	public Light(Map map){
		this.map=map;
	}
	public enum lightcolor{
		RED,GREEN,NO
	}
	public void lightreset(){
		for(int i=0;i<80;i++){
			for(int j=0;j<80;j++){
				if(map.cross[i][j]==0){
					this.light[i][j]=lightcolor.NO;
				}
				else if(map.cross[i][j]==1){
					int num=0;
					if(map.map[i][j]==1||map.map[i][j]==2){
						num++;
					}
					else if(map.map[i][j]==3){
						num+=2;	
					}
					if(i>0&&(map.map[i-1][j]==2||map.map[i-1][j]==3)){
						num++;
					}
					if(j>0&&(map.map[i][j-1]==2||map.map[i][j-1]==2)){
						num++;
					}
					if(num>=3){
						int x=new Random().nextInt(1);
						if(x==0){
							this.light[i][j]=lightcolor.RED;
						}
						else{
							this.light[i][j]=lightcolor.GREEN;
						}
					}
					else{
						this.light[i][j]=lightcolor.NO;
					}
				}
			}
		}
	}
	//MODIFIES:this
	//EFFECTS:reset all the red green signals
	@Override
	public void run() {
		while(true){
			try{
				sleep(3000);
				for(int i=0;i<80;i++){
					for(int j=0;j<80;j++){
						if(this.light[i][j]==lightcolor.RED){
							this.light[i][j]=lightcolor.GREEN;
						}
						else if(this.light[i][j]==lightcolor.GREEN){
							this.light[i][j]=lightcolor.RED;
						}
					}
				}
			}catch(Exception e){
				System.exit(0);
			}
		}
	}
	public boolean repOK() {
		if(map==null) return false;
		else return true;
    }
}
	

