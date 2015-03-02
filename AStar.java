import java.util.ArrayList;
import java.util.List;


public class AStar
{
	private List<Integer> closedSet; //set of nodes already evaluated
	private List<Integer> openSet; //set of nodes to be evaluated
	private int parent[];
	private double gScore[];
	private double fScore[];
	
	//Returns the node value with the lowest F score
	private int findLowestF()
	{
		if ((openSet==null) || (openSet.size()<1))	return -1;
		double cost;
		int bestindex,index;
		bestindex=0;
		cost=fScore[openSet.get(bestindex)];
		for(int i=0;i<openSet.size();i++)
		{
			index=openSet.get(i);
			if (fScore[index]<cost)
			{
				cost=fScore[index];
				bestindex=index;
			}
		}
		return bestindex;
	}
	
	//Computes a number corresponding to the given x,y position
	private int computePos(int x, int y, int xlen)
	{
		return x+y*xlen;
	}
	
	//Generate the neighbors of the given position
	private ArrayList<Integer> generateNeighbors(int board[][],int pos)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		int ylen=board.length;
		int xlen=board[0].length;
		int x = pos%xlen;
		int y = pos/xlen;
		if ((x-1>=0) && (y-1>=0) && (board[x-1][y-1]==1))	list.add(computePos(x-1,y-1,xlen));
		else if ((x>=0) && (y-1>=0) && (board[x][y-1]==1))	list.add(computePos(x,y-1,xlen));
		else if ((x+1<xlen) && (y-1>=0) && (board[x+1][y-1]==1))	list.add(computePos(x+1,y-1,xlen));
		else if ((x-1>=0) && (y>=0) && (board[x-1][y]==1))	list.add(computePos(x-1,y,xlen));
		else if ((x+1<xlen) && (y>=0) && (board[x+1][y]==1))	list.add(computePos(x+1,y,xlen));
		else if ((x-1>=0) && (y+1<ylen) && (board[x-1][y+1]==1))	list.add(computePos(x-1,y+1,xlen));
		else if ((x>=0) && (y+1<ylen) && (board[x][y+1]==1))	list.add(computePos(x,y+1,xlen));
		else if ((x+1<xlen) && (y+1<ylen) && (board[x+1][y+1]==1))	list.add(computePos(x+1,y+1,xlen));
		return list;
	}
	
	//Initialize int array
	private void initi(int[] a, int len, int val)
	{
		a=new int[len];
		for(int i=0;i<len;i++)	a[i]=val;
	}
	
	//Initialize double array
	private void init(double[] a, int len, int val)
	{
		a=new double[len];
		for(int i=0;i<len;i++)	a[i]=val;
	}
	
	//Method prints the shortest path found
	private void printShortestPath(int pos)
	{
		System.out.print("Shortest path is: ");
		StringBuilder sb = new StringBuilder();
		while(parent[pos]!=-1)
		{
			sb.append(Integer.toString(pos)+" "+" << ");
			pos=parent[pos];
		}
		sb.append(Integer.toString(pos));
		sb.reverse();
		System.out.println(sb.toString());
	}
	
	//Computes the squared euclidean distance between two points on a grid
	private double distance(int pos1, int pos2, int xlen)
	{
		int pos1x=pos1%xlen;
		int pos1y=pos1/xlen;
		int pos2x=pos2%xlen;
		int pos2y=pos2/xlen;
		double res = Math.abs((pos1x-pos2x))*Math.abs((pos1x-pos2x))+Math.abs((pos1y-pos2y))*Math.abs((pos1y-pos2y));
		return res;
	}
	
	//Iterates over the given list to find the value pos
	private boolean scanSet(int pos, List<Integer> list)
	{
		int val;
		for(int i=0;i<list.size();i++)
		{
			val=list.get(i);
			if (pos==val) return true;
		}
		return false;
	}
	
	//The main method which computes the shortes path using the A* algorithm
	public int[] astar(int startNode, int goalNode, int nNodes, int board[][])
	{
		//Initialization
		closedSet = new ArrayList<Integer>();
		openSet = new ArrayList<Integer>();
		initi(parent,nNodes,-1);
		init(gScore,nNodes,0);
		init(fScore,nNodes,0);
		openSet.add(startNode);
		int current;
		double gTemp;
		List<Integer> neighbors;
		while(openSet.size()>0)
		{
			current = findLowestF(); //find the node with lowest F score
			openSet.remove(Integer.valueOf(current)); //remove this node from the open set
			if (current==goalNode)
			{
				printShortestPath(current);
				return parent;
			}
			closedSet.add(current); //add the current node to the closed set
			neighbors = generateNeighbors(board,current);
			for(int node: neighbors)
			{
				if (scanSet(node,closedSet)) continue;
				gTemp = gScore[current]+distance(current,node,board[0].length);
				if ((scanSet(node,openSet)) && (gTemp>gScore[node])) continue;
				parent[node]=current;
				gScore[node]=gTemp;
				fScore[node]=gTemp+distance(node,goalNode,board[0].length);
				if (!scanSet(node,openSet))	openSet.add(node);
			}
			
		}
		return new int[0]; //failure: did not find a path
	}
}
