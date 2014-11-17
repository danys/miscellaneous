package exercise1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class computes strongly connected components in a directed graph.
 * The class implements Kosaraju's algorithm. 
 * To avoid a possible stack overflow depending on the size of the graph
 * it is necessary to ajust the VM parameters, for example:
 * -Xmx1024m -Xms1024m -Xmn256m -Xss16m.
 * In Eclipse this can be configured in:
 * Window->Preferences->Java->Installed JREs->Edit->Default VM arguments box
 *
 */
public class StronglyConnectedComponents
{
	public static HashMap<Integer,ArrayList<Integer>> graph;
	public static HashSet<edge> edges;
	public static int leader[];
	public static HashMap<Integer,Integer> newname;
	public static boolean visited[];
	public static int t;
	public static int s;
	
	public static class edge
	{
		private final Integer node1;
		private final Integer node2;
		
		public edge(Integer node1, Integer node2)
		{
			this.node1 = node1;
			this.node2 = node2;
		}
		
		@Override
		public int hashCode()
		{
			return this.node1.intValue()+this.node2.intValue();
		}
		@Override
		public boolean equals(Object obj)
		{
			if (obj==null) return false;
			if (obj==this) return true;
			if (!(obj instanceof edge)) return false;
			edge e = (edge)obj;
			return ((e.node1.intValue()==this.node1.intValue()) && (e.node2.intValue()==this.node2.intValue()));
		}
	}
	
	public static int insertedge(int node1, int node2)
	{
		int result = 0; //number of new nodes
		edge e = new edge(Integer.valueOf(node1),Integer.valueOf(node2));
		edges.add(e);
		ArrayList<Integer> l1 = graph.get(Integer.valueOf(node1));
		if (l1==null)
		{
			l1 = new ArrayList<Integer>();
			graph.put(Integer.valueOf(node1), l1);
			result++;
		}
		ArrayList<Integer> l2 = graph.get(Integer.valueOf(node2));
		if (l2==null)
		{
			l2 = new ArrayList<Integer>();
			graph.put(Integer.valueOf(node2), l2);
			result++;
		}
		l1.add(Integer.valueOf(node2));
		l2.add(Integer.valueOf(node1));
		return result;
	}
	
	public static void dfs(int node,boolean second)
	{
		visited[node]=true;
		leader[node]=s;
		ArrayList<Integer> list = graph.get(Integer.valueOf(node));
		int node2;
		edge tempedge, revtempedge;
		for(int i=0;i<list.size();i++)
		{
			node2 = list.get(i).intValue();
			tempedge = new edge(Integer.valueOf(node),Integer.valueOf(node2));
			revtempedge = new edge(Integer.valueOf(node2),Integer.valueOf(node));
			if (((!second) && (edges.contains(revtempedge))) || (((second) && (edges.contains(tempedge))))) if (!visited[node2]) dfs(node2,second);
		}
		t++;
		if (!second) newname.put(Integer.valueOf(t), Integer.valueOf(node));
	}
	
	public static void dfsexec(int nnodes, boolean second)
	{
		t=0;
		s=-1;
		int j;
		for(int i=nnodes;i>=1;i--)
		{
			j=i;
			if (second) j=newname.get(Integer.valueOf(j)).intValue();
			if (!visited[j])
			{
				s=j;
				dfs(j, second);
			}
		}
	}
	
	public static void init(int nnodes, boolean initall)
	{
		if (initall) newname = new HashMap<Integer,Integer>();
		leader = new int[nnodes+1];
		visited = new boolean[nnodes+1];
	}
	
	public static void printnbiggestcomponents(int n)
	{
		HashMap<Integer,Integer> hm = new HashMap<Integer,Integer>();
		Integer count;
		for(int i=0;i<leader.length;i++)
		{
			count = hm.get(Integer.valueOf(leader[i]));
			if (count==null) hm.put(Integer.valueOf(leader[i]), Integer.valueOf(1));
			else hm.put(Integer.valueOf(leader[i]), Integer.valueOf(count.intValue()+1));
		}
		Integer[] leadersizes = (Integer[])(hm.values().toArray(new Integer[hm.values().size()]));
		Arrays.sort(leadersizes);
		System.out.println("Sorted "+leadersizes.length+" components");
		for(int i=leadersizes.length-1;i>=leadersizes.length-n;i--)
			System.out.println("Component "+Integer.valueOf(leadersizes.length-i).toString()+" has "+Integer.valueOf(leadersizes[i].intValue()).toString()+" nodes");
	}
	
	public static void main(String[] args) throws IOException
	{
		/*
		File contains a directed edge in every line represented by the two node numbers
		which are separated by a space. The first node in every line is the tail of the
		edge and the second node is the head.
		*/
		String file = "C:\\Users\\Dany\\Downloads\\SCC.txt";
		int nbiggest = 10;
		String line;
		BufferedReader br = null;
		int nedges=0, nnodes=0, node1, node2;
		try
		{
			br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			String str[] = null;
			graph = new HashMap<Integer,ArrayList<Integer>>();
			edges = new HashSet<edge>();
			s = -1;
			t = -1;
			while((line!=null) && (!line.isEmpty()))
			{
				nedges++;
				str = line.split("\\s+");
				if (str.length!=2) throw new IOException();
				node1=Integer.parseInt(str[0]);
				node2=Integer.parseInt(str[1]);
				nnodes += insertedge(node1,node2);
				line = br.readLine();
			}
			System.out.println("Number of edges = "+String.valueOf(nedges)+" and number of nodes = "+String.valueOf(nnodes));
			init(nnodes,true);
			System.out.println("Initialized arrays");
			dfsexec(nnodes,false);
			System.out.println("dfsexec run 1 done");
			init(nnodes,false);
			System.out.println("Initialized arrays second time");
			dfsexec(nnodes,true);
			System.out.println("dfsexec run 2 done");
			printnbiggestcomponents(nbiggest);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
				e.printStackTrace();
			}
		}
	}
}