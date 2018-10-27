package TaxiCall;

public class Taxi{
	public int id;
	public int x;//��ǰλ��
	public int y;
	public int nx;//��һ������λ��
	public int ny;
	public int px;//Ŀ��˿͵�ַ
	public int py;
	public int ax;//�˿�Ŀ�ĵ�
	public int ay;
	public String condition;
	public int reputation;
	public long runtime;
	public int movement;
	public long flashtime;
	public Path path=new Path();
	
	Taxi(int id,int x, int y,int nx,int ny,int px,int py,int ax,int ay,String condition,int reputation,long runtime,int movement,long flashtime){
		this.id=id;
		this.x=x;
		this.y=y;
		this.nx=nx;
		this.ny=ny;
		this.px=px;
		this.py=py;
		this.ax=ax;
		this.ay=ay;
		this.condition=condition;
		this.reputation=reputation;
		this.runtime=runtime;
		this.movement=movement;	
		this.flashtime=flashtime;
	}
	//REQUIRES:input right taxis information
	//MODIFIES:this
	//EFFECTS:set a new class taxi and give it information
	public boolean repOK(){
		return true;
	}
}
