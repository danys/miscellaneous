import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Class provides an implementation of Karger's randomized algorithm to find a minimum cut
 * in a connected graph. 
 *
 *
 */
public class MinCut
{
	public static HashMap<Integer,ArrayList<Integer>> map;
	public static HashMap<Integer,ArrayList<Integer>> tmap;
	public static ArrayList<edge> edgelist;
	
	public static class edge
	{
		public Integer n1;
		public Integer n2;
		public edge(Integer n1, Integer n2)
		{
			this.n1 = n1;
			this.n2 = n2;
		}
		
		//Method helps when removing edges with ArrayList<edge>#remove(Object obj)
		public boolean equals(Object obj)
		{
			if (obj==null) return false;
			if (obj==this) return true;
			if (!(obj instanceof edge)) return false;
			edge e = (edge)obj;
			return ((e.n1.intValue()==this.n1.intValue()) && (e.n2.intValue()==this.n2.intValue()));
		}
	}
	
	//Prints the incident nodes for the first n nodes
	public static void printmap(int n)
	{
		ArrayList<Integer> l;
		for(int i=1;i<=n;i++)
		{
			l = map.get(Integer.valueOf(i));
			if (l!=null)
			{
				System.out.print("Node "+i+" = ");
				for(int j=0;j<l.size();j++) System.out.print(l.get(j)+" ");
				System.out.println();
			}
		}
	}
	
	//Duplicates the content of tmap into a new object
	public static HashMap<Integer,ArrayList<Integer>> cloner()
	{
		HashMap<Integer,ArrayList<Integer>> map = new HashMap<Integer,ArrayList<Integer>>();
		Set<Map.Entry<Integer,ArrayList<Integer>>> s = tmap.entrySet();
		Iterator<Map.Entry<Integer,ArrayList<Integer>>> it = s.iterator();
		while(it.hasNext())
		{
			Map.Entry<Integer,ArrayList<Integer>> me = it.next();
			ArrayList<Integer> nlist = new ArrayList<Integer>();
			ArrayList<Integer> list = me.getValue();
			for(int i=0;i<list.size();i++) nlist.add(Integer.valueOf(list.get(i).intValue()));
			map.put(me.getKey(),nlist);
		}
		return map;
	}
	
	public static edge buildedge(Integer n1, Integer n2)
	{
		if (n1.intValue()<=n2.intValue()) return new edge(n1,n2);
		else return new edge(n2,n1);
	}
	
	//Put all the edges into a set and return it as an array list
	public static ArrayList<edge> edgelist()
	{
		ArrayList<edge> l = new ArrayList<edge>();
		Iterator<Map.Entry<Integer,ArrayList<Integer>>> it = map.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<Integer,ArrayList<Integer>> me = it.next();
			ArrayList<Integer> list = me.getValue();
			for(int i=0;i<list.size();i++) l.add(buildedge(me.getKey(),list.get(i)));
		}
		return l;
	}
	
	public static void removeremaining(int val)
	{
		Iterator<edge> it = edgelist.iterator();
		edge e = null;
		while(it.hasNext())
		{
			e = it.next();
			if ((e.n1.intValue()==val) || (e.n2.intValue()==val)) it.remove();
		}
	}
	
	public static void contract(edge e)
	{
		int node1 = e.n1.intValue();
		int node2 = e.n2.intValue();
		edgelist.remove(e);
		ArrayList<Integer> edgelist1 = map.get(Integer.valueOf(node1));
		ArrayList<Integer> edgelist2 = map.get(Integer.valueOf(node2));
		//remove all remaining edges between node1 and node2 
		while(edgelist1.remove(Integer.valueOf(node2)));
		while(edgelist2.remove(Integer.valueOf(node1)));
		int temp;
		for(int i=0;i<edgelist2.size();i++)
		{
			temp = edgelist2.get(i).intValue();
			//update edges in edgelist
			edgelist.add(buildedge(Integer.valueOf(node1),Integer.valueOf(temp)));
			//update node1's edges in edgelist1
			edgelist1.add(Integer.valueOf(temp));
			//update temp's edges in templist
			ArrayList<Integer> templist = map.get(Integer.valueOf(temp));
			templist.add(Integer.valueOf(node1));
			templist.remove(Integer.valueOf(node2));
		}
		map.remove(Integer.valueOf(node2));
		removeremaining(node2);
	}
	
	//Karger's algorithm
	public static int karger()
	{
		Random r = new Random();
		int redge;
		edge e = null;
		while(map.keySet().size()>2)
		{
			redge = r.nextInt(edgelist.size());
			e = edgelist.get(redge);
			contract(e);
		}
		return map.values().iterator().next().size();
	}
	
	//Method used to solve an instance of the minimum cut problem
	public static void solve(int maxiterations)
	{
		int res = Integer.MAX_VALUE;
		int currentres;
		for(int i=0;i<maxiterations;i++)
		{
			map = cloner();
			edgelist = edgelist();
			System.out.println("Iteration "+Integer.toString(i+1)+" of "+Integer.toString(maxiterations));
			currentres = karger();
			if (currentres<res)	res = currentres;
			System.out.println("Current minimum cut = "+res);
		}
		System.out.println("=> Minimum cut = "+res);
	}
	
	public static void main(String args[])
	{
		String file = "C:\\Users\\Username\\graph.txt";
		//The file should contain an adjacency list representation of a graph.
		//The first number in each line corresponds to the node number, the rest
		//of the line is the adjacency list of that node.
		String line;
		BufferedReader br = null;
		int n=0;
		try
		{
			br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			tmap = new HashMap<Integer,ArrayList<Integer>>();
			while((line!=null) && (!line.isEmpty()))
			{
				n++;
				String str[] = line.split("\\s+");
				ArrayList<Integer> list = new ArrayList<Integer>();
				for(int i=1;i<str.length;i++) list.add(Integer.parseInt(str[i]));
				tmap.put(Integer.parseInt(str[0]), list);
				line = br.readLine();
			}
			int maxiterations = n;//(n*(n-1))/2;
			long t = System.nanoTime();
			solve(maxiterations);
			System.out.format("Computation time = %.3fs%n",(System.nanoTime()-t)/(1000000000.0));
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
