import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//Prim's minimum spanning tree algorithm
public class PrimMST
{
	private static Map<Integer,List<Integer>> graph;
	private static List<Integer> treenodes;
	private static List<Edge> mstEdges;
	private static Map<Edge,Integer> edges;
	
	public static class Edge
	{
		public int id1;
		public int id2;
		public int weight;
		
		public Edge(int id1,int id2)
		{
			if (id1<=id2)
			{
				this.id1 = id1;
				this.id2 = id2;
			}
			else
			{
				this.id1 = id2;
				this.id2 = id1;
			}
		}
		
		@Override
		 public boolean equals(Object o)
		 {
			if(o == null) return false;
			if (!(o instanceof Edge)) return false;
			Edge e = (Edge)o;
			if ((this.id1==e.id1) && (this.id2==e.id2)) return true;
			else return false;
		 }
		
		@Override
		public int hashCode()
		{
			return id1*id2;
		}
	}
	
	public static void insertEdge(int node1, int node2, int weight)
	{
		//Insert into graph
		List<Integer> list = graph.get(node1);
		if (list==null) list = new ArrayList<Integer>();
		list.add(node2);
		graph.put(node1, list);
		list = graph.get(node2);
		if (list==null) list = new ArrayList<Integer>();
		list.add(node1);
		graph.put(node2, list);
		//Insert into edge hash table
		Edge e = new Edge(node1, node2);
		edges.put(e, weight);
	}
	
	public static boolean isInList(List<Integer> list, int x)
	{
		for(Integer i: list) if (i.intValue()==x) return true;
		return false;
	}
	
	public static long prim(int nnodes)
	{
		long mstCost = 0;
		//Choose random vertex
		Random r = new Random();
		int iv = r.nextInt(nnodes)+1;
		treenodes.add(iv);
		int xedges=1,min,weight,nextv=-1;
		Edge minEdge,nextEdge;
		List<Integer> l;
		while(xedges<nnodes)
		{
			min=Integer.MAX_VALUE;
			minEdge=null;
			nextv=-1;
			for(Integer u: treenodes)
			{
				l = graph.get(u);
				for(Integer v: l)
				{
					if (!isInList(treenodes,v))
					{
						nextEdge = new Edge(u,v);
						weight = edges.get(nextEdge);
						if (weight<min)
						{
							min = weight;
							nextv = v;
							minEdge = nextEdge;
						}
					}
				}
			}
			mstCost += min;
			treenodes.add(nextv);
			mstEdges.add(minEdge);
			xedges++;
		}
		return mstCost;
	}
	
	public static void main(String args[])
	{
		BufferedReader br = null;
		try
		{
			FileReader fr = new FileReader("C:\\Users\\Dany\\Downloads\\edges.txt");
			br = new BufferedReader(fr);
			String line;
			String strparts[];
			int linecounter=0;
			line = br.readLine();
			int nnodes,nedges,node1,node2,cost;
			strparts = line.split("\\s+");
			if (strparts.length!=2)
			{
				System.out.println("First line needs to contain two space-separated integers for the number of nodes and edges respectively!");
				System.exit(0);
			}
			nnodes = Integer.parseInt(strparts[0]);
			nedges = Integer.parseInt(strparts[1]);
			graph = new HashMap<Integer,List<Integer>>();
			treenodes = new ArrayList<Integer>();
			edges = new HashMap<Edge,Integer>();
			mstEdges = new ArrayList<Edge>();
			line = br.readLine();
			while((line!=null) && (linecounter<nedges))
			{
				strparts = line.split("\\s+");
				if (strparts.length!=3)
				{
					System.out.println("All but the first line need to contain three space-separated integers for the ids of the edge's first node and the second node and the corresponding weight!");
					System.exit(0);
				}
				node1 = Integer.parseInt(strparts[0]);
				node2 = Integer.parseInt(strparts[1]);
				cost = Integer.parseInt(strparts[2]);
				insertEdge(node1,node2,cost);
				linecounter++;
				line = br.readLine();
			}
			System.out.println("Minimum spanning tree cost = "+prim(nnodes));
		}
		catch(IOException e)
		{
			System.out.println("Error reading file!");
		}
		catch(NumberFormatException e)
		{
			System.out.println("Invalid number in input file!");
		}
		finally
		{
			if (br!=null)
			try
			{
				br.close();
			}
			catch (IOException e)
			{
				System.out.println("An error occurred while closing the BufferedReader!");
			}
		}
	}
}
