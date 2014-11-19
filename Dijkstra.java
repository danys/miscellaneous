package algos;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Dijkstra
{
	public static HashMap<Integer,ArrayList<segment>> graph;
	public static int distance[];
	public static int prevNode[];
	public static minheap heap;
	
	public static class minheap
	{
		private int list[];
		private int size;
		
		public minheap(int n)
		{
			list = new int[n+1];
			size = 0;
		}
		
		public void add(int x)
		{
			list[size+1]=x;						
			size++;
			heapify(size);
		}
		
		public int poll()
		{
			if (size>0)
			{
				int t=list[1];
				list[1]=list[size];
				size--;
				bubbledown();
				return t;
			}
			else return -1;
		}
		
		private void swap(int i,int j)
		{
			int temp=list[i];
			list[i]=list[j];
			list[j]=temp;
		}
		
		private void heapify(int start)
		{
			int current=start, parent;
			while(current/2>=1)
			{
				parent=current/2;
				if (distance[list[parent]]<=distance[list[current]]) break;
				else
				{
					swap(parent,current);
					current=parent;
				}
			}
		}
		
		private void bubbledown()
		{
			int current=1,left,right,bestindex;
			while(2*current<=size)
			{
				left=current*2;
				right=current*2+1;
				if (right>size) bestindex=left;
				else bestindex= (distance[list[left]]<distance[list[right]]) ? left:right;
				if (distance[list[bestindex]]>distance[list[current]]) break;
				else
				{
					swap(current,bestindex);
					current = bestindex;
				}
			}
		}
		
		public int size()
		{
			return size;
		}
		
		public void print()
		{
			System.out.println("Heap content");
			for(int i=1;i<=size;i++) System.out.print(" "+Integer.toString(list[i]));
			System.out.println();
		}
	}
	
	public static class segment
	{
		private int nextnode;
		private int weight;
		
		public segment(int next,int weight)
		{
			this.nextnode = next;
			this.weight = weight;
		}
		
		public segment(String s)
		{
			String tuple[] = s.split(",");
			this.nextnode=Integer.parseInt(tuple[0]);
			this.weight=Integer.parseInt(tuple[1]);
		}
		
		public int getweight()
		{
			return this.weight;
		}
		
		public int getnextnode()
		{
			return this.nextnode;
		}
	}
	
	public static void dijkstra(int nnodes, int fromNode)
	{
		distance = new int[nnodes+1];
		prevNode = new int[nnodes+1];
		Iterator<Integer> it = graph.keySet().iterator();
		Integer currentnode;
		while(it.hasNext())
		{
			currentnode = it.next();
			if (currentnode.intValue()==fromNode) distance[fromNode]=0;
			else distance[currentnode.intValue()]=Integer.MAX_VALUE;
			prevNode[currentnode.intValue()]=-1;
			heap.add(currentnode.intValue());
		}
		int current;
		ArrayList<segment> list;
		segment nextseg;
		int newval;
		while(heap.size()>0)
		{
			current = heap.poll();
			list = graph.get(Integer.valueOf(current));
			for(int i=0;i<list.size();i++)
			{
				nextseg = list.get(i);
				newval = distance[current]+nextseg.weight;
				if (newval<distance[nextseg.nextnode])
				{
					distance[nextseg.nextnode] = newval;
					heap.heapify(nextseg.nextnode); //heapify() on every distance change
					prevNode[nextseg.nextnode] = current;
				}
			}
		}
	}
	
	public static void print(int n)
	{
		System.out.print(distance[n]);
	}
	
	public static void main(String args[])
	{
		/*The file should be structured as follows:
		Every line contains information about a specific node, the first column indicates this node's number.
		Next in this line follows a space-separated list of pairs which represent a neighboring node and the distance from the current node to this node. 
		*/
		String file = "Data.txt";
		int distfromNode = 1; //compute distances from this node
		BufferedReader br = null;
		String line;
		int currentnode, nnodes=0;
		ArrayList<segment> list;
		try
		{
			br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			String str[] = null;
			graph = new HashMap<Integer,ArrayList<segment>>();
			while((line!=null) && (!line.isEmpty()))
			{
				nnodes++;
				str = line.split("\\s+");
				currentnode = Integer.parseInt(str[0]);
				for(int z=1;z<str.length;z++)
				{
					list = graph.get(Integer.valueOf(currentnode));
					if (list==null)
					{
						list = new ArrayList<segment>();
						graph.put(Integer.valueOf(currentnode), list);
					}
					list.add(new segment(str[z]));
				}
				line = br.readLine();
			}
			heap = new minheap(nnodes);
			long time = System.nanoTime();
			dijkstra(nnodes,distfromNode);
			time = System.nanoTime()-time;
			System.out.format("Computation time = %.2f ms%n", time/1000000.0);
			for(int i=1;i<=nnodes;i++)
			{
				if (i!=1) System.out.println("\n");
				System.out.print("dist["+Integer.valueOf(i).toString()+"] = ");
				print(i);
			}
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
