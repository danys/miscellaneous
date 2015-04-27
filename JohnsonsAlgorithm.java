import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Main
{
	
	public static class Node
	{
		public int id;
		public int weight;
		
		public Node(int id, int weight)
		{
			this.id = id;
			this.weight = weight;
		}
	}
	
	public static Map<Integer,List<Integer>> adjGraph;
	public static Map<Integer,List<Node>> incGraph;
	public static Map<String,Integer> edges;
	
	//O(N*M) implementation of the Bellman-Ford algorithm
	public static boolean bellmanford(int a[][], int n, Map<Integer,List<Node>> g)
	{
		List<Node> list;
		for(int i=0;i<=n;i++)
		{
			for(int j=0;j<n;j++) a[i][j] = Integer.MAX_VALUE;
		}
		a[0][0]=0;
		for(int i=1;i<=n;i++)
		{
			for(int j=0;j<n;j++)
			{
				a[i][j]=a[i-1][j]; //best minimum so far
				list = g.get(j);
				if (list==null) continue;
				for(Node node: list)
				{
					if (a[i-1][node.id]==Integer.MAX_VALUE) continue;
					if (a[i-1][node.id]+node.weight<a[i][j])
					{
						if (i==n) return false; //graph contains a negative length cycle
						a[i][j] = a[i-1][node.id]+node.weight;
					}
				}
			}
		}
		return true; //graph contains no negative length cycle
	}
	
	public static void reweight(int a[][], int n)
	{
		Set<Entry<String,Integer>> set = edges.entrySet();
		String key;
		int id1,id2,weight;
		String str[];
		for(Entry<String,Integer> e: set)
		{
			key = e.getKey();
			str = key.split("\\|");
			if (str.length!=2)
			{
				System.out.println("Edge key not consistent!");
				System.exit(0);
			}
			id1 = Integer.parseInt(str[0]);
			id2 = Integer.parseInt(str[1]);
			weight = e.getValue();
			weight = weight + a[n][id1] - a[n][id2];
			e.setValue(weight);
		}
	}
	
	public static void dijkstra(int dist[][],int source, int n)
	{
		List<Integer> queue = new ArrayList<Integer>();
		for(int i=1;i<=n;i++)
		{
			dist[source][i] = Integer.MAX_VALUE;
			queue.add(i);
		}
		dist[source][source] = 0;
		int minindex=0,mindist,nodeid;
		List<Integer> list;
		while(queue.size()>0)
		{
			minindex=0;
			mindist=dist[source][queue.get(minindex)];
			nodeid = queue.get(minindex);
			for(int i=1;i<queue.size();i++)
			{
				if (dist[source][queue.get(i)]<mindist)
				{
					mindist = dist[source][queue.get(i)];
					minindex = i;
					nodeid = queue.get(minindex);
				}
			}
			queue.remove(minindex);
			list = adjGraph.get(nodeid);
			if (list==null) continue;
			int newlen;
			for(Integer neighbor : list)
			{
				if (dist[source][nodeid]==Integer.MAX_VALUE) continue;
				newlen = dist[source][nodeid]+edges.get(Integer.toString(nodeid)+"|"+Integer.toString(neighbor));
				if (newlen<dist[source][neighbor]) dist[source][neighbor] = newlen;
			}
		}
	}
	
	public static void main(String args[])
	{
		long time = System.currentTimeMillis();
		File file = new File("/Users/Dany/Downloads/g3.txt");
		BufferedReader reader = null;
		int n = 0,m = 0;
		try
		{
			reader = new BufferedReader(new FileReader(file));
			String line;
			String str[];
			int linecounter=0,u,v,weight;
			Node iNode;
			adjGraph = new HashMap<Integer,List<Integer>>();
			incGraph = new HashMap<Integer,List<Node>>();
			edges = new HashMap<String,Integer>();
			List<Node> iList;
			List<Integer> aList;
			boolean vertexPresent[] = null;
			//Read the input file
			while((line = reader.readLine())!=null)
			{
				linecounter++;
				str = line.split("\\s+");
				if (linecounter==1)
				{
					//Line must contain 2 integers
					if (str.length!=2)
					{
						System.out.println("First line did not contain two integers!");
						System.exit(0);
					}
					n = Integer.parseInt(str[0]);
					m = Integer.parseInt(str[1]);
					System.out.println("Graph with "+n+" vertices and "+m+" edges.");
					vertexPresent = new boolean[n];
					for(int i=0;i<n;i++) vertexPresent[i]=false;
				}
				else
				{
					//Line must contain 3 integers
					if (str.length!=3)
					{
						System.out.println("Edge must be defined by 3 integers!");
						System.exit(0);
					}
					u = Integer.parseInt(str[0]);
					v = Integer.parseInt(str[1]);
					weight = Integer.parseInt(str[2]);
					//Set vertices u and v's present flag to true
					vertexPresent[u-1]=true;
					vertexPresent[v-1]=true;
					//Put edge into adjacency graph
					aList = adjGraph.get(u);
					if (aList==null) aList = new ArrayList<Integer>();
					aList.add(v);
					adjGraph.put(u, aList);
					edges.put(String.valueOf(u)+"|"+String.valueOf(v),weight);
					//Put edge into incidence graph
					iNode = new Node(u,weight);
					iList = incGraph.get(v);
					if (iList==null) iList = new ArrayList<Node>();
					iList.add(iNode);
					incGraph.put(v, iList);
				}
			}
			//Check if all vertices are present
			for(int i=0;i<n;i++)
			{
				if (vertexPresent[i]==false)
				{
					System.out.println("Not all vertices found!");
					System.exit(0);
				}	
			}
			System.out.println("Found all "+String.valueOf(n)+" vertices in input file!");
			//Check if all edges are present
			if (m!=linecounter-1)
			{
				System.out.println("Not all edges found in input file!");
				System.exit(0);
			}
			System.out.println("All edges have been found!");
			//1.) Add source node with id 0
			// Add 0 to every incidence list
			for(int i=1;i<=n;i++)
			{
				iList = incGraph.get(i);
				if (iList==null) iList = new ArrayList<Node>();
				iList.add(new Node(0,0));
				incGraph.put(i, iList);
			}
			n++; //n=1001
			//2.) Run Bellman-Ford algorithm to detect negative length cycles
			int A[][] = new int[n+1][n];
			if (bellmanford(A,n,incGraph)) System.out.println("Successfully run Bellman-Ford!");
			else
			{
				System.out.println("Graph contains a negative length cycle!");
				System.out.println("Computation time: "+Double.toString((double)((System.currentTimeMillis()-time)/1000))+" sec");
				System.exit(0);
			}
			//3.) Reweight the graph in O(m)
			n--; //n=1000
			reweight(A,n);
			//4.) Run Dijkstra n times in O(n*m*log(n))
			int res[][] = new int[n+1][n+1];
			for(int i=1;i<=n;i++) dijkstra(res,i,n);
			//5.) Compute the correct path lengths in O(n^2)
			int min = Integer.MAX_VALUE;
			for(int i=1;i<=n;i++)
			{
				for(int j=1;j<=n;j++)
				{
					res[i][j] = res[i][j]-A[n][i]+A[n][j];
					if (res[i][j]<min) min = res[i][j];
				}
			}
			System.out.println("Minimum length of all pairs distances: "+Integer.toString(min));
			System.out.println("Computation time: "+Double.toString((double)((System.currentTimeMillis()-time)/1000))+" sec");
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Did not find the input file!");
		}
		catch(IOException e)
		{
			System.out.println("Could not open input file!");
		}
	}
}
