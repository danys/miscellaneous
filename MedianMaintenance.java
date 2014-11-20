package algos;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MedianMaintenance
{
	public static heap lowhighh;
	public static heap highlowh;
	
	//Heap class can be instantiated as a binary min-heap or as as binary max-heap
	public static class heap
	{
		private int list[];
		private int size;
		private boolean minheap;
		
		//minheap == true => build min-heap
		//minheap == false => build max-heap
		public heap(int n, boolean minheap)
		{
			list = new int[n+1];
			size = 0;
			this.minheap = minheap;
		}
		
		public void add(int x)
		{
			list[size+1]=x;						
			size++;
			heapify(size);
		}
		
		public int peek()
		{
			if (size>0) return list[1];
			else return -1; 
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
		
		//Push up the element in position start as much as possible
		private void heapify(int start)
		{
			int current=start, parent;
			while(current/2>=1)
			{
				parent=current/2;
				if (((list[parent]<=list[current]) && (minheap)) || ((list[parent]>=list[current]) && (!minheap))) break;
				else
				{
					swap(parent,current);
					current=parent;
				}
			}
		}
		
		//Let element in position 1 trickle down as much as possible
		private void bubbledown()
		{
			int current=1,left,right,bestindex;
			while(2*current<=size)
			{
				left=current*2;
				right=current*2+1;
				if (right>size) bestindex=left;
				else bestindex=(((minheap) && (list[left]<list[right])) || ((!minheap) && (list[left]>list[right]))) ? left:right;
				if ((minheap&&(list[bestindex]>=list[current])) || (!minheap&&(list[bestindex]<=list[current]))) break;
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
	}
	
	//The actual algorithm to find the current median in O(log n) time
	public static int inserter(int x, int i)
	{
		int result=0;
		if (i==0) highlowh.add(x);
		else
		{
			if (x<highlowh.peek()) highlowh.add(x);
			else
			{
				if (lowhighh.size()==0) lowhighh.add(x);
				else
				{
					if (x>=lowhighh.peek()) lowhighh.add(x);
					else highlowh.add(x);
				}
			}
		}
		//Balance the heap if necessary
		if (Math.abs(lowhighh.size()-highlowh.size())>1)
		{
			if (lowhighh.size()>highlowh.size()) highlowh.add(lowhighh.poll());
			else lowhighh.add(highlowh.poll());
		}
		if (lowhighh.size()==highlowh.size()) result = highlowh.peek();
		else result = (highlowh.size()>lowhighh.size()) ? highlowh.peek() : lowhighh.peek();
		System.out.println("Result i = "+i+" => "+result);
		return result;
	}
	
	public static void main(String args[])
	{
		String file = "median.txt";
		int nints=10000;
		BufferedReader br = null;
		String line;
		int result=0,i=0;
		lowhighh = new heap(nints/2,true);
		highlowh = new heap(nints/2,false);
		try
		{
			br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			while((line!=null) && (!line.isEmpty()))
			{
				result += inserter(Integer.parseInt(line),i);
				line = br.readLine();
				i++;
			}
			System.out.println("Total sum => "+result%10000);
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
