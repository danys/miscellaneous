package assignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

//Linear time algorithm to solve the 2-SAT problem.
public class TwoSat
{
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
	
	public static class StronglyConnectedComponents
	{
		public HashMap<Integer,ArrayList<Integer>> graph;
		public HashSet<edge> edges;
		public int leader[];
		public HashMap<Integer,Integer> newname;
		public boolean visited[];
		public int t;
		public int s;
		
		public  int insertedge(int node1, int node2)
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
		
		public  void dfs(int node,boolean second)
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
		
		public  void dfsexec(int nnodes, boolean second)
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
		
		public  void init(int nnodes, boolean initall)
		{
			if (initall) newname = new HashMap<Integer,Integer>();
			leader = new int[nnodes+1];
			visited = new boolean[nnodes+1];
		}
		
		public boolean isSatisfyable()
		{
			for(int i=1;i<leader.length-2;i+=2)
			{
				if (leader[i]==leader[i+1])	return false;
			}
			return true;
		}
		
		//Edges need to conform to the following convention:
		//The vertex ids in the list of edges must be chosen such their ids have values 1+2*n if they are true and 2*n if they are false
		public  void runKosaraju(List<edge> edgeList) throws IOException
		{
			int nedges=0, nnodes=0;
			graph = new HashMap<Integer,ArrayList<Integer>>();
			edges = new HashSet<edge>();
			s = -1;
			t = -1;
			for(edge e: edgeList)
			{
				nedges++;
				nnodes += insertedge(e.node1,e.node2);
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
			if (isSatisfyable()) System.out.println("This instance is satisfiable");
			else System.out.println("This instance is not satisfiable");
		}
	}
	
	public static boolean addToMap(Integer xt, int xfile,int idcounter,Map<Integer,Integer> map)
	{
		if (xt==null)
		{
			if (xfile<0)
			{
				map.put(-xfile, idcounter);
				map.put(xfile, idcounter+1);
			}
			else
			{
				map.put(xfile, idcounter);
				map.put(-xfile, idcounter+1);
			}
			return true;
		}
		else return false;
	}
	
	public static void main(String args[])
	{
		String file = "C:\\Users\\Dany\\Downloads\\2sat6.txt",line;
		Map<Integer,Integer> map;
		BufferedReader br = null;
		int xfile,yfile,idcounter,nedges,linecount,maxlines;
		Integer xt,yt,xf,yf;
		List<edge> edges;
		edge e1,e2;
		try
		{
			br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			String str[] = null;
			map = new HashMap<Integer,Integer>();
			idcounter=1;nedges=0;
			edges = new ArrayList<edge>();
			maxlines = Integer.parseInt(line);
			line = br.readLine();
			linecount=1;
			xt=null;
			yt=null;
			xf=null;
			yf=null;
			while((line!=null) && (!line.isEmpty()) && (linecount<=maxlines))
			{
				str = line.split("\\s+");
				if (str.length!=2)
				{
					System.out.println("Every line needs to contain two integer numbers!");
					System.exit(0);
				}
				xfile = Integer.parseInt(str[0]);
				yfile = Integer.parseInt(str[1]);
				if (xfile<0)
				{
					xf = map.get(xfile);
					xt = map.get(-xfile);
				}
				if (yfile<0)
				{
					yf = map.get(yfile);
					yt = map.get(-yfile);
				}
				if (xfile>0)
				{
					xt = map.get(xfile);
					xf = map.get(-xfile);
				}
				if (yfile>0)
				{
					yt = map.get(yfile);
					yf = map.get(-yfile);
				}
				e1=null;
				e2=null;
				if (addToMap(xt, xfile,idcounter,map))
				{
					xt=idcounter;
					xf=idcounter+1;
					idcounter += 2;
				}
				if (addToMap(yt, yfile,idcounter,map))
				{
					yt=idcounter;
					yf=idcounter+1;
					idcounter += 2;
				}
				if ((xfile>0) && (yfile>0))
				{
					e1 = new edge(xf,yt);
					e2 = new edge(yf,xt);
				}
				else if ((xfile>0) && (yfile<0))
				{
					e1 = new edge(xf,yf);
					e2 = new edge(yt,xt);
				}
				else if ((xfile<0) && (yfile>0))
				{
					e1 = new edge(xt,yt);
					e2 = new edge(yf,xf);
				}
				else if ((xfile<0) && (yfile<0))
				{
					e1 = new edge(xt,yf);
					e2 = new edge(yt,xf);
				}
				edges.add(e1);
				edges.add(e2);
				nedges += 2;
				line = br.readLine();
				linecount++;
			}
			System.out.println("Read file, inserted "+new Integer(idcounter-1).toString()+" vertices and "+ nedges +" edges!");
			StronglyConnectedComponents sc = new StronglyConnectedComponents();
			sc.runKosaraju(edges);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch(NumberFormatException e)
		{
			System.out.println("Invalid number found in input file!");
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
