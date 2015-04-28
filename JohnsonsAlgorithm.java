package assignment;

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
	
	//Reweighting all edges in O(m)
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
	
	public static class PriorityQueue
	{
		public int ids[];
		public int headPointer;
		public int dist[][];
		public int source;
		public Map<Integer,Integer> itable;
		
		public PriorityQueue(int capacity,int dist[][],int source)
		{
			ids = new int[capacity+1];
			headPointer = 1;
			this.dist = dist;
			this.source = source;
			this.itable = new HashMap<Integer,Integer>();
		}
		
		public void add(int e)
		{	
			ids[headPointer]=ids[1];
			ids[1]=e;
			headPointer++;
			itable.put(e, 1);
			itable.put(ids[headPointer-1], headPointer-1);
			heapify(1,headPointer-1);
		}
		
		public void heapify(int i, int m)
		{
			int t,j;
			while(2*i<=m)
			{
				j = 2*i;
				if (j<m)
				{
					if (dist[source][ids[j]]>dist[source][ids[j+1]]) j++;
				}
				if (dist[source][ids[i]]>dist[source][ids[j]])
				{
					t = ids[i];
					ids[i]=ids[j];
					itable.put(ids[j], i);
					ids[j]=t;
					itable.put(t,j);
					i=j;
				}
				else i=m;
			}
		}
		
		public int extractAndRemoveMin() throws Exception
		{
			if (headPointer==1) throw new Exception();
			int res = ids[1];
			headPointer--;
			ids[1]=ids[headPointer];
			itable.put(ids[headPointer],1);
			heapify(1,headPointer-1);
			itable.remove(new Integer(res));
			return res;
		}
		
		public int size()
		{
			return headPointer-1;
		}
		
		public int lookup(int x)
		{
			Integer l = itable.get(x);
			if (l==null) return -1;
			else return l.intValue();
		}
		
		public void decreaseKey(int x)
		{
			int i = lookup(x),parent,t;
			while(i/2>=1)
			{
				parent = i/2;
				if (dist[source][ids[parent]]<=dist[source][ids[i]]) break;
				else
				{
					t = ids[parent];
					ids[parent] = ids[i];
					itable.put(ids[i],parent);
					ids[i] = t;
					itable.put(t,i);
					i /= 2;
				}
			}
		}
	}
	
	//O(m*log(n)) implementation of Dijkstra's shortest path algorithm
	public static void dijkstra(int dist[][],int source, int n)
	{
		PriorityQueue queue = new PriorityQueue(n+1,dist,source);
		boolean done[] = new boolean[n+1];
		for(int i=1;i<=n;i++)
		{
			dist[source][i] = Integer.MAX_VALUE;
			done[i] = false;
			queue.add(i);
		}
		dist[source][source] = 0;
		queue.decreaseKey(source);
		int nodeid;
		List<Integer> list;
		while(queue.size()>0)
		{
			nodeid=0;
			try
			{
				nodeid = queue.extractAndRemoveMin();
			} catch (Exception e)
			{
				System.out.println("Error extracting form empty queue!");
				System.exit(0);
			}
			done[nodeid]=true;
			list = adjGraph.get(nodeid);
			if (list==null) continue;
			int newlen;
			for(Integer neighbor : list)
			{
				if (done[neighbor]) continue;
				if (dist[source][nodeid]==Integer.MAX_VALUE) continue;
				newlen = dist[source][nodeid]+edges.get(Integer.toString(nodeid)+"|"+Integer.toString(neighbor));
				if (newlen<dist[source][neighbor])
				{
					dist[source][neighbor] = newlen;
					queue.decreaseKey(neighbor);
				}
			}
		}
	}
	
	/**
	 * Method uses Johnson's algorithm for finding all-pairs shortest paths.
	 * Time complexity: O(n*m*log(n))
	 * Sparse graphs m=O(n) => time complexity: O(n^2*log(n)). Better than Floyd-Warshall's O(n^3) algorithm.
	 * Dense graphs m=O(n^2) => time complexity: O(n^3*log(n))
	 * If the graph has negative length cycles the method detects it using Bellman-Ford.
	 * @param args no arguments
	 */
	public static void main(String args[])
	{
		long time = System.currentTimeMillis();
		File file = new File("C:\\Users\\Dany\\Downloads\\graph.txt");
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
			//Johnson's algorithm for finding all-pairs shortest paths
			//1.) Add source node with id 0 in O(n)
			// Add 0 to every incidence list
			for(int i=1;i<=n;i++)
			{
				iList = incGraph.get(i);
				if (iList==null) iList = new ArrayList<Node>();
				iList.add(new Node(0,0));
				incGraph.put(i, iList);
			}
			n++; //n=1001
			//2.) Run Bellman-Ford algorithm to detect negative length cycles in O(n*m)
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
					if (res[i][j]==Integer.MAX_VALUE) continue;
					res[i][j] = res[i][j]-A[n][i]+A[n][j];
					if (res[i][j]<min) min = res[i][j];
				}
			}
			System.out.println("Minimum length of all pairs distances: "+Integer.toString(min));
			System.out.println("Computation time: "+Double.toString((double)(System.currentTimeMillis()-time)/1000.0)+" sec");
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