package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HammingClustering
{
	private static byte filenodes[][];
	private static byte nodes[][];
	private static int nnodes,nbits,numbernodes;
	private static byte len1masks[][];
	private static byte len2masks[][];
	private static int table[];
	private static List<Edge> edges;
	private static Set<Edge> edgeset;
	private static int uf[];
	private static int rank[];
	private static Map<Integer,Integer> valToIndex;
	
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
		
		@Override
		public boolean equals(Object o)
		{
			if (o==null) return false;
			if (!(o instanceof Edge)) return false;
			Edge e = (Edge)o;
			return ((this.id1==e.id1) && (this.id2==e.id2));
		}
		
		@Override
		public int hashCode()
		{
		    return this.id1+this.id2;
		}
	}
	
	//Clustering methods
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
	
	public static byte extractByte(int index,String str[])
	{
		byte b=0;
		int startindex=index*8;
		for(int z=startindex;z<=startindex+7;z++)
		{
			if (str[z].compareTo("1")==0) b |= (1 << (7-(z-startindex)));
		}
		return b;
	}
	
	public static void insertbits(int id, String bits[])
	{
		for(int i=0;i<bits.length/8;i++) filenodes[id][i]=extractByte(i,bits);
	}
	
	public static byte setBit(byte b,int pos)
	{
		return (byte)(b | (1 << pos));
	}
	
	public static boolean isSetBit(byte b,int pos)
	{
		if (pos==7) return (b<0);
		byte t = (byte) (b & (1 << pos));
		return (t>0);
	}
	
	public static int computeNodeVal(int i)
	{
		int res=0;
		int base=1;
		for(int j=0;j<nbits/8;j++)
		{
			res += base*(filenodes[i][j]+128);
			base = base*256;
		}
		return res;
	}
	
	public static int computeReducedNodeVal(int i)
	{
		int res=0;
		int base=1;
		for(int j=0;j<nbits/8;j++)
		{
			res += base*(nodes[i][j]+128);
			base = base*256;
		}
		return res;
	}
	
	public static int computeComputedNodeVal(byte barray[])
	{
		int res=0;
		int base=1;
		for(int j=0;j<nbits/8;j++)
		{
			res += base*(barray[j]+128);
			base = base*256;
		}
		return res;
	}
	
	public static void generate1Masks()
	{
		len1masks = new byte[nbits][nbits/8];
		byte b = 0;
		int byteid,byteoffset;
		for(int i=0;i<nbits;i++)
		{
			byteid=i/8;
			byteoffset=i%8;
			for(int j=0;j<nbits/8;j++)
			{
				b=0;
				if (j!=byteid) b=0;
				else b=setBit(b,7-byteoffset);
				len1masks[i][j]=b;
			}
		}
		System.out.println("Generated one bit masks of size "+nbits);
	}
	
	public static void generate2Masks()
	{
		len2masks = new byte[(nbits*(nbits-1))/2][nbits/8];
		byte b = 0;
		int byteid1,byteoffset1,byteid2,byteoffset2,counter=0;
		for(int i=0;i<nbits;i++)
		{
			byteid1=i/8;
			byteoffset1=i%8;
			for(int j=0;j<nbits;j++)
			{
				if (i>=j) continue;
				byteid2=j/8;
				byteoffset2=j%8;
				for(int k=0;k<nbits/8;k++)
				{
					b=0;
					if ((k!=byteid1) && (k!=byteid2)) b=0;
					else
					{
						if (k==byteid1) b=setBit(b,7-byteoffset1);
						if (k==byteid2) b=setBit(b,7-byteoffset2);
					}
					len2masks[counter][k]=b;
				}
				counter++;
			}
		}
		System.out.println("Generated two bit masks of size "+counter);
	}
	
	public static void computeNodes()
	{
		byte computednode[] = new byte[nbits/8];
		int weight;
		edgeset = new HashSet<Edge>();
		Integer index;
		for(int i=0;i<numbernodes;i++)
		{
			for(int j=0;j<nbits+((nbits*(nbits-1))/2);j++)
			{
				if (j<=nbits-1) weight=1;
				else weight=2;
				for(int k=0;k<nbits/8;k++)
				{
					if (j<=nbits-1) computednode[k]=(byte)(nodes[i][k]^len1masks[j][k]);
					else computednode[k]=(byte)(nodes[i][k]^len2masks[j-nbits][k]);
				}
				index = valToIndex.get(computeComputedNodeVal(computednode));
				if (index!=null) edgeset.add(new Edge(i,index.intValue(),weight));
			}
		}
		edges = new ArrayList<Edge>();
		Iterator<Edge> it = edgeset.iterator();
		while(it.hasNext())	edges.add(it.next());
		System.out.println("Edge list has size = "+edges.size());
	}
	
	public static void kruskal(int nnodes)
	{
		//Sort edges by weight
		Collections.sort(edges);
		uf = new int[nnodes+1];
		rank = new int[nnodes+1];
		//Initialization
		for(int i=0;i<=nnodes;i++) uf[i]=i;
		for(int i=0;i<=nnodes;i++) rank[i]=0;
		//Edge currentEdge;
		//Kruskal
		for(Edge currentEdge: edges)
		{
			if (!sameRoot(currentEdge.id1,currentEdge.id2))
			{
				union(currentEdge.id1,currentEdge.id2);
			}
		}
	}
	
	public static int solve(int nnodes)
	{
		table = new int[16777216]; //2^24 = 16'777'216
		for(int i=0;i<16777216;i++) table[i]=-1;
		numbernodes=0;
		nodes = new byte[nnodes][nbits/8];
		valToIndex = new HashMap<Integer,Integer>();
		//Remove duplicates from filenodes
		for(int i=0;i<nnodes;i++)
		{
			if (table[computeNodeVal(i)]==-1)
			{
				for(int j=0;j<nbits/8;j++) nodes[numbernodes][j]=filenodes[i][j];
				valToIndex.put(computeNodeVal(i), numbernodes);
				numbernodes++;
				table[computeNodeVal(i)]=i;
			}
		}
		System.out.println("Reduced number of nodes = "+numbernodes);
		//Generate bit masks for Hamming length 1
		generate1Masks();
		//Generate bit masks for Hamming length 1
		generate2Masks();
		computeNodes();
		//Count number of nodes in edges
		Set<Integer> set = new HashSet<Integer>();
		for(Edge edge: edges)
		{
			set.add(edge.id1);
			set.add(edge.id2);
		}
		int nedgevertices=set.size();
		System.out.println("Number of vertices in edges = "+nedgevertices);
		//Translate the names of edge vertices
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		int index=1;
		for(Integer i: set)
		{
			map.put(i, index);
			index++;
		}
		Edge edge;
		for(int i=0;i<edges.size();i++)
		{
			edge = edges.get(i);
			edge.id1 = map.get(edge.id1);
			edge.id2 = map.get(edge.id2);
		}
		System.out.println("Finished translating node names!");
		//Run kruskal's clustering algorithm
		kruskal(nedgevertices);
		int nleaders=0;
		//Count the leaders
		for(int i=0;i<nedgevertices;i++) if (uf[i]==i) nleaders++;
		return numbernodes-nedgevertices+nleaders;
	}
	
	public static void main(String args[])
	{
		BufferedReader br = null;
		try
		{
			FileReader fr = new FileReader("C:\\Users\\Dany\\Downloads\\clustering_big.txt");
			br = new BufferedReader(fr);
			String line;
			String strparts[];
			line = br.readLine();
			int linecounter;
			strparts = line.split("\\s+");
			if (strparts.length!=2)
			{
				System.out.println("First line in file must contain two numbers: the number of nodes and the number of bits separated by a space.");
				System.exit(0);
			}
			nnodes = Integer.parseInt(strparts[0]);
			nbits = Integer.parseInt(strparts[1]);
			line = br.readLine();
			System.out.println("Nnodes = "+nnodes);
			linecounter=1;
			filenodes = new byte[nnodes][nbits];
			while((line!=null) && (linecounter<nnodes))
			{
				strparts = line.split("\\s+");
				if (strparts.length!=nbits)
				{
					System.out.println("All but the first line need to contain three space-separated integers for the ids of the edge's first node and the second node and the corresponding weight!");
					System.exit(0);
				}
				insertbits(linecounter-1,strparts);
				line = br.readLine();
				linecounter++;
			}
			System.out.println("Read "+linecounter+" lines");
			System.out.println("Solution = "+solve(nnodes));
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
