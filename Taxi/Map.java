import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public final class Map {
    public static final int size = 80;
    private static Point[][] points = new Point[size + 1][size + 1];
    private static Edge[][] edges = new Edge[size * 2][size + 1];

    private Map() {
	// effects: prevent creating instance
    }

    public static void initial() {
	// Requires:file meets the requirement
	// modify: points, edges
	// effects: construct point array and edge array using map.txt
	// if map.txt does not exist or wrong, throws IOException
	try {
	    char[] line = new char[size];
	    FileReader fileReader = new FileReader("d:\\map.txt");
	    for (short i = 1; i <= Map.size; i++) {
		fileReader.read(line);
		for (short j = 1; j <= Map.size; j++) {
		    points[i][j] = new Point(i, j, line[j - 1] - '0');
		}
		fileReader.read(line, 0, 2);
	    }
	    fileReader.close();
	    fileReader = new FileReader("d:\\info1.txt");
	    for (int i = 1; i <= size; i++) {
		fileReader.read(line);
		for (int j = 1; j <= size; j++) {
		    if (line[j - 1] == '0')
			points[i][j].setOverpass(true);
		}
		fileReader.read(line, 0, 2);
	    }
	    fileReader.close();
	    for (int i = 1; i <= size; i++)
		for (int j = 1; j <= size; j++) {
		    if (j < size && points[i][j].turnRight())
			edges[2 * i - 1][j] = new Edge(points[i][j], points[i][j + 1], points[i][j].turnRight());
		    if (i < size && points[i][j].turnDown())
			edges[2 * i][j] = new Edge(points[i][j], points[i + 1][j], points[i][j].turnDown());
		}
	    for (int i = 1; i <= size; i++)
		for (int j = 1; j <= size; j++) {
		    if (i > 1)
			points[i][j].setUpEdge(edges[2 * (i - 1)][j]);
		    if (i < size)
			points[i][j].setDownEdge(edges[2 * i][j]);
		    if (j > 1)
			points[i][j].setLeftEdge(edges[2 * i - 1][j - 1]);
		    if (j < size)
			points[i][j].setRightEdge(edges[2 * i - 1][j]);
		}
	} catch (IOException e) {
	    System.out.println("file wrong!");
	    System.exit(0);
	}
    }

    public static Point getLeftPoint(Point p) {
	// require: point from Map.getPoint
	// effects: return the left point of point p, if does not exist, return null
	short x = p.getX();
	short y = p.getY();
	if (y > 1) {
	    return points[x][y - 1];
	} else
	    return null;
    }

    public static Point getUpPoint(Point p) {
	// require: point from Map.getPoint
	// effects: return the up point of point p, if does not exist, return null
	short x = p.getX();
	short y = p.getY();
	if (x > 1) {
	    return points[x - 1][y];
	} else
	    return null;
    }

    public static Point getDownPoint(Point p) {
	// require: point from Map.getPoint
	// effects: return the down point of point p, if does not exist, return null
	short x = p.getX();
	short y = p.getY();
	if (x != size)
	    return points[x + 1][y];
	else
	    return null;
    }

    public static Point getRightPoint(Point p) {
	// require: point from Map.getPoint
	// effects: return the right point of point p, if does not exist, return null
	short x = p.getX();
	short y = p.getY();
	if (y != size)
	    return points[x][y + 1];
	else
	    return null;
    }

    public static Point[] getPoints(int x1, int x2, int y1, int y2) {
	// require: x2>=x1, y2>=y1
	// effects: return points from x1 to x2 and y1 to y2
	if (x1 < 1)
	    x1 = 1;
	if (x2 > size)
	    x2 = size;
	if (y1 < 1)
	    y1 = 1;
	if (y2 > size)
	    y2 = size;
	Point[] tmp = new Point[(x2 - x1 + 1) * (y2 - y1 + 1)];
	int k = 0;
	for (int i = x1; i <= x2; i++)
	    for (int j = y1; j <= y2; j++)
		tmp[k++] = points[i][j];
	return tmp;
    }

    public static Point getPoint(int x, int y) {
	// require: 1<=x<=80 1<=y<=80
	// effects: return point[x][y]
	if (x > size || y > size || x < 1 || y < 1) {
	    System.out.println("wrong point");
	    System.exit(0);
	}
	return points[x][y];
    }

    public static int getPathLength(Point current, Point destination, int mode) {// mode =1 normal mode=0 traceable
	// requires: points from Map mode =0 or mode =1
	// effects: calculate shortest path distance
	if (current.equals(destination))
	    return 0;
	byte[] shortest = new byte[size * size + 1];
	Queue<Point> queue = new LinkedList<>();
	boolean[][] marked = new boolean[size + 1][size + 1];
	marked[current.getX()][current.getY()] = true;
	queue.add(current);
	Point v;
	Point tmp = null;
	short x, y;
	while (!queue.isEmpty()) {
	    v = queue.poll();
	    x = v.getX();
	    y = v.getY();
	    if (((mode == 1 && v.turnUp()) || (mode == 0 && v.getUpEdge() != null)) && !marked[x - 1][y]) {
		marked[x - 1][y] = true;
		tmp = points[x - 1][y];
		shortest[tmp.getIndex()] = 1;
		if (tmp.equals(destination))
		    break;
		queue.add(tmp);
	    }
	    if (((mode == 1 && v.turnDown()) || (mode == 0 && v.getDownEdge() != null)) && !marked[x + 1][y]) {
		marked[x + 1][y] = true;
		tmp = points[x + 1][y];
		shortest[tmp.getIndex()] = 2;
		if (tmp.equals(destination))
		    break;
		queue.add(tmp);
	    }
	    if (((mode == 1 && v.turnLeft()) || (mode == 0 && v.getLeftEdge() != null))&& !marked[x][y - 1]) {
		marked[x][y - 1] = true;
		tmp = points[x][y - 1];
		shortest[tmp.getIndex()] = 3;
		if (tmp.equals(destination))
		    break;
		queue.add(tmp);
	    }
	    if (((mode == 1 && v.turnRight()) || (mode == 0 && v.getRightEdge() != null)) && !marked[x][y + 1]) {
		marked[x][y + 1] = true;
		tmp = points[x][y + 1];
		shortest[tmp.getIndex()] = 4;
		if (tmp.equals(destination))
		    break;
		queue.add(tmp);
	    }
	}
	int length = 0;
	while (true) {
	    switch (shortest[tmp.getIndex()]) {
	    case 1:
		tmp = Map.getDownPoint(tmp);
		break;
	    case 2:
		tmp = Map.getUpPoint(tmp);
		break;
	    case 3:
		tmp = Map.getRightPoint(tmp);
		break;
	    case 4:
		tmp = Map.getLeftPoint(tmp);
		break;
	    }
	    length++;
	    if (tmp.equals(current))
		break;
	}
	return length;
    }
}
