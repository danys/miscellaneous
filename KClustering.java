package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class KClustering
{
	private static List<Edge> edges;
	private static int uf[];
	private static int rank[];
	
	public static class Edge implements Comparable<Edge>
	{
		public int id1;
		public int id2;
		public int weight;
		
		public Edge(int id1,int id2,int weight)
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
			this.weight = weight;
		}

		@Override
		public int compareTo(Edge o)
		{
			if (this.weight<o.weight) return -1;
			else if (this.weight>o.weight) return 1;
			return 0;
		}
	}
	
	public static boolean sameRoot(int node1,int node2)
	{
		return find(node1)==find(node2);
	}
	
	public static void union(int node1,int node2)
	{
		int root1 = find(node1);
		int root2 = find(node2);
		if (root1==root2) return;
		if (rank[root1]>rank[root2]) uf[root2]=root1;
		else if (rank[root1]<rank[root2]) uf[root1]=root2;
		else
		{
			uf[root2]=root1;
			rank[root1]=rank[root1]+1;
		}
	}
	
	public static int find(int nodeId)
	{
		while(uf[nodeId]!=nodeId) nodeId = uf[nodeId];
		return nodeId;
	}
	
	public static void solve(int nnodes, int minclusters)
	{
		//Sort edges by weight
		Collections.sort(edges);
		int nclusters = nnodes,edgeIndex=0;
		uf = new int[nnodes+1];
		rank = new int[nnodes+1];
		//Initialization
		for(int i=0;i<=nnodes;i++) uf[i]=i;
		for(int i=0;i<=nnodes;i++) rank[i]=0;
		Edge currentEdge;
		//Kruskal
		while(nclusters>minclusters)
		{
			currentEdge = edges.get(edgeIndex);
			if (!sameRoot(currentEdge.id1,currentEdge.id2))
			{
				union(currentEdge.id1,currentEdge.id2);
				nclusters--;
			}
			edgeIndex++;
		}
		Edge e;
		//We have a minclusters clusters
		while(sameRoot(edges.get(edgeIndex).id1,edges.get(edgeIndex).id2)) edgeIndex++;
		System.out.println("Maximim spacing of "+minclusters+"-clustering = "+edges.get(edgeIndex).weight);
	}
	
	public static void main(String args[])
	{
		BufferedReader br = null;
		try
		{
			FileReader fr = new FileReader("C:\\Users\\Dany\\Downloads\\clustering1.txt");
			br = new BufferedReader(fr);
			String line;
			String strparts[];
			line = br.readLine();
			int nnodes,node1,node2,cost,linecounter;
			nnodes = Integer.parseInt(line);
			edges = new ArrayList<Edge>();
			line = br.readLine();
			linecounter=2;
			while((line!=null) && (!line.isEmpty()))
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
				edges.add(new Edge(node1,node2,cost));
				line = br.readLine();
				linecounter++;
			}
			System.out.println("Read "+linecounter+" lines");
			solve(nnodes,4);
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
