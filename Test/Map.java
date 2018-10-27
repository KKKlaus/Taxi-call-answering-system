package TaxiCall;

import java.io.*;
import java.util.*;

enum Direction{
	U,D,L,R,S;
}
public class Map {
	public int [][]map=new int[80][80];
	public int [][]cross=new int[80][80];
	
	public void readmap(){//如果读取文件有效则载入地图
		ArrayList<String> maptemp=new ArrayList();
		File file= new File("map.txt");
		if(file.exists()==false){
			System.out.println("Can't load map!Running end.");
			System.exit(0);
		}
		try{
			BufferedReader tmp= new BufferedReader(new FileReader(file));
			String strtemp=null;
			while((strtemp=tmp.readLine())!=null){
				maptemp.add(strtemp);
			}
			tmp.close();
		}catch (Exception e){
			System.out.println("Fail to load map!");
		}
		if(maptemp.size()<80){
			System.out.println("The map is illegal!");
			System.exit(0);
		}
		for(int i=0;i<80;i++){
			char[] chartemp=maptemp.get(i).toCharArray();
			if(chartemp.length<80){
				System.out.println("The map is illegal!");
				System.exit(0);
			}
			for(int j=0;j<80;j++){
				this.map[i][j]=chartemp[j]-'0';
			}
		}
		System.out.println("The map load successful");
	}
	//REQUIRES:读取map.txt文件正确
	//MODIFIES:this*
	//EFFECTS:读取地图，如果满足80*80则读取成功并保存，否则读取失败
	public  void crossget() {
		ArrayList<String> crosstemp=new ArrayList();
		File file= new File("cross.txt");
		if(file.exists()==false){
			System.out.println("Can't load map!Running end.");
			System.exit(0);
		}
		try{
			BufferedReader tmp= new BufferedReader(new FileReader(file));
			String strtemp=null;
			while((strtemp=tmp.readLine())!=null){
				crosstemp.add(strtemp);
			}
			tmp.close();
		}catch (Exception e){
			System.out.println("Fail to load map!");
		}
		if(crosstemp.size()<80){
			System.out.println("The map is illegal!");
			System.exit(0);
		}
		for(int i=0;i<80;i++){
			char[] chartemp=crosstemp.get(i).toCharArray();
			if(chartemp.length<80){
				System.out.println("The map is illegal!");
				System.exit(0);
			}
			for(int j=0;j<80;j++){
				cross[i][j]=chartemp[j]-'0';
			
			}
		}
	}
	//REQUIRES:读取cross.txt文件正确
	//MODIFIES:this*
	//EFFECTS:读取道路交叉路口信息，如果满足80*80则读取成功并保存，否则读取失败
	public  String findNeighbor(int i, int j)// 如果输入合法地图点坐标，则可以在地图上查找相邻点
	{
		//Requires: input two int as rowNo and colNo of an coordinate 
		//Modifies: none
		//Effects: return a String whose length is 4 that indicates the neighbor of direction UP LEFT DOWN　RIGHT
		//			such as "0110". 1: (i,j) has a neighbor; 0:(i,j) does not have a neighbor in that direction    
		String ret="";
//		System.out.println("findNeighbor in");
		synchronized(map){
			if((i-1)>=0 && (map[i-1][j]==2||map[i-1][j]==3)){//上
				ret = ret+"1";
			}
			else{
				ret = ret+"0";
			}
			if((j-1)>=0 && (map[i][j-1]==1||map[i][j-1]==3)){//左
				ret = ret+"1";
			}
			else{
				ret = ret+"0";
			}
			if(map[i][j]==0){
				ret = ret+"00";
			}
			else if(map[i][j]==1){//右
				ret = ret+"01";
			}
			else if(map[i][j]==2){//下
				ret = ret+"10";
			}
			else if(map[i][j]==3){//右 下
				ret = ret+"11";
			}
		}
//		System.out.println("findNeighbor over");
//		if(ret.equals("0000")){
//			throw new MyException(ExceptionType.MapUnConnected);
//		}
		return ret;
	}
	public  void openroad(int x1,int y1,int x2,int y2,String req){
		try{
		if(req=="open"){
			if(x1==x2){
				if(y1+1==y2){
					if(map[x1][y1]==0){
						map[x1][y1]=1;
					}
					else if(map[x1][y1]==2){
						map[x1][y1]=3;
					}
					else{
						System.out.println("meaningless request!");
					}
				}
				else if(y1-1==y2){
					if(map[x2][y2]==0){
						map[x2][y2]=1;
					}
					else if(map[x2][y2]==2){
						map[x2][y2]=3;
					}
					else{
						System.out.println("meaningless request!");
					}
				}
				else{
					System.out.println("Wrong request!");
				}
			}
			else if(y1==y2){
				if(x1+1==x2){
					if(map[x1][y1]==0){
						map[x1][x2]=2;
					}
					else if(map[x1][x2]==1){
						map[x1][x2]=3;
					}
					else{
						System.out.println("meaningless request!");
					}
				}
				else if(x1-1==x2){
					if(map[x2][y2]==0){
						map[x2][y2]=2;
					}
					else if(map[x2][y2]==1){
						map[x2][y2]=3;
					}
					else{
						System.out.println("meaningless request!");
					}
				}
				else{
					System.out.println("Wrong request!");
				}
			}
			else{
				System.out.println("Wrong request!");
			}
		}
		else if(req=="close"){
			if(x1==x2){
				if(y1+1==y2){
					if(map[x1][y1]==1){
						map[x1][y1]=0;
					}
					else if(map[x1][y1]==3){
						map[x1][y1]=2;
					}
					else{
						System.out.println("meaningless request!");
					}
				}
				else if(y1-1==y2){
					if(map[x2][y2]==1){
						map[x2][y2]=0;
					}
					else if(map[x2][y2]==3){
						map[x2][y2]=2;
					}
					else{
						System.out.println("meaningless request!");
					}
				}
				else{
					System.out.println("Wrong request!");
				}
			}
			else if(y1==y2){
				if(x1+1==x2){
					if(map[x1][y1]==2){
						map[x1][x2]=0;
					}
					else if(map[x1][x2]==3){
						map[x1][x2]=1;
					}
					else{
						System.out.println("meaningless request!");
					}
				}
				else if(x1-1==x2){
					if(map[x2][y2]==2){
						map[x2][y2]=0;
					}
					else if(map[x2][y2]==3){
						map[x2][y2]=1;
					}
					else{
						System.out.println("meaningless request!");
					}
				}
				else{
					System.out.println("Wrong request!");
				}
			}
			else{
				System.out.println("Wrong request!");
			}
		}
		else{
			System.out.println("Illegal input!");
		}
		}catch(Exception e){
			System.out.println("Wrong input");
		}
	}
	//REQUIRES:input right request to open or close the road
	//MODIFIES:map[][]
	//EFFECTS:change the condition of the roads order to the request
	public int bfs(int row1,int col1,int row2,int col2)//如果正确输入两个的横纵坐标，则可以找到他们最短路径长度
	{
		//Requires:4 int;(row1,col1):startCoordinate;(row2,col2):endCoordinate 
		//Modifies:none
		//Effects:return the shortest path from (row1,col1) to (row2,col2)
		if(row1==row2 && col1==col2)
			return 0;
		int pathLen=0;
		int mark=0;
		int[][] symbol = new int[80][80];
		for(int m=0;m<80;m++){
			for(int n=0;n<80;n++){
				symbol[m][n] = 0;
			}
		}
		String flag;
		LinkedList<Integer> q1 = new LinkedList<Integer>();
		LinkedList<Integer> q2 = new LinkedList<Integer>();
		int i=1;
		int startRow,startCol;
//		System.out.println("p1"+row1+" "+col1+"p2"+row2+" "+col2);
		q1.add(row1);q1.add(col1);
		symbol[row1][col1] = 1;
		while(!(q1.isEmpty() && q2.isEmpty())){
//			System.out.println("bfs loop"+q1.size()+" "+q2.size());
			if(i==1){
				while(!q1.isEmpty()){
					startRow = q1.remove();startCol = q1.remove();
//					System.out.println("startRow:"+startRow+" "+"startCol:"+startCol);
					flag = findNeighbor(startRow, startCol);
					if(flag.charAt(0)=='1' && symbol[startRow-1][startCol]==0){//UP
						if((startRow-1)==row2 && startCol==col2){
							mark = 1;
							break;
						}
						q2.add(startRow-1);q2.add(startCol);
						symbol[startRow-1][startCol]=1;
					}
					if(flag.charAt(1)=='1' && symbol[startRow][startCol-1]==0){//LEFT
						if(startRow==row2 && (startCol-1)==col2){
							mark = 1;
							break;
						}
						q2.add(startRow);q2.add(startCol-1);
						symbol[startRow][startCol-1]=1;
					}
					if(flag.charAt(2)=='1' && symbol[startRow+1][startCol]==0){//DOWN
						if((startRow+1==row2) && startCol==col2){
							mark = 1;
							break;
						}
						q2.add(startRow+1);q2.add(startCol);
						symbol[startRow+1][startCol]=1;
					}
					if(flag.charAt(3)=='1' && symbol[startRow][startCol+1]==0){//RIGHT
						if(startRow==row2 && (startCol+1)==col2){
							mark = 1;
							break;
						}
						q2.add(startRow);q2.add(startCol+1);
						symbol[startRow][startCol]=1;
					}
				}
			}
			else{
				while(!q2.isEmpty()){
					startRow = q2.remove();startCol = q2.remove();
//					System.out.println("startRow:"+startRow+" "+"startCol:"+startCol);
//					System.out.println("findNeighbor start");
					flag = findNeighbor(startRow, startCol);
//					System.out.println("findNeighbor over");
					if(flag.charAt(0)=='1' && symbol[startRow-1][startCol]==0){//UP
						if((startRow-1)==row2 && startCol==col2){
							mark = 1;
							break;
						}
						q1.add(startRow-1);q1.add(startCol);
						symbol[startRow-1][startCol]=1;
					}
					if(flag.charAt(1)=='1' && symbol[startRow][startCol-1]==0){//LEFT
						if(startRow==row2 && (startCol-1)==col2){
							mark = 1;
							break;
						}
						q1.add(startRow);q1.add(startCol-1);
						symbol[startRow][startCol-1]=1;
					}
					if(flag.charAt(2)=='1' && symbol[startRow+1][startCol]==0){//DOWN
						if((startRow+1==row2) && startCol==col2){
							mark = 1;
							break;
						}
						q1.add(startRow+1);q1.add(startCol);
						symbol[startRow+1][startCol]=1;
					}
					if(flag.charAt(3)=='1' && symbol[startRow][startCol+1]==0){//RIGHT
						if(startRow==row2 && (startCol+1)==col2){
							mark = 1;
							break;
						}
						q1.add(startRow);q1.add(startCol+1);
						symbol[startRow][startCol+1]=1;
					}
				}
			}
			pathLen++;
			if(mark==1){
//				System.out.println("bfs over");
				return pathLen;
			}
				
			i = (i+1)%2;
		}
		return -1;
	}
	public boolean repOK(){
		if(map!=null||cross!=null) return true;
		else return false;
	}

	
}
