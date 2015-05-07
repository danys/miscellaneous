package pa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main
{
	public static class Coordinate
	{
		private double x;
		private double y;
		
		public Coordinate(double x,double y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	public static double euclidDistance(Coordinate c1, Coordinate c2)
	{
		return Math.sqrt((c1.x-c2.x)*(c1.x-c2.x)+(c1.y-c2.y)*(c1.y-c2.y));
	}
	
	public static void generateBigSets(int ncities)
	{
		bigSets = new ArrayList<List<Integer>>();
		boolean present[] = new boolean[ncities+1];
		List<Integer> bigSet;
		List<Integer> smallSet;
		table = new int[smallSets.size()*ncities];
		missing = new int[smallSets.size()*ncities];
		for(int index=0;index<smallSets.size();index++)
		{
			smallSet = smallSets.get(index);
			for(int i=0;i<present.length;i++) present[i]=false;
			for(Integer x: smallSet) present[x]=true;
			for(int i=1;i<present.length;i++)
			{
				if ((!present[i]) && (i>smallSet.get(smallSet.size()-1)))
				{
					bigSet = new ArrayList<Integer>();
					for(Integer x: smallSet) bigSet.add(new Integer(x.intValue()));
					bigSet.add(i);
					bigSets.add(bigSet);
					table[bigSets.size()-1]=index;
					missing[bigSets.size()-1]=i;
				}
			}
		}
	}
	
	public static void generateSets(List<Integer> indexes,List<Integer> set,int maxsize)
	{
		if (maxsize==1)
		{
			List<Integer> list1 = new ArrayList<>();
			list1.add(1);
			smallSets.add(list1);
			return;
		}
		if (set.size()==maxsize)
		{
			List<Integer> l = new ArrayList<Integer>();
			for(int i=0;i<set.size();i++) l.add(set.get(i));
			smallSets.add(l);
			return;
		}
		Integer nextindex;
		for(int i=0;i<indexes.size();i++)
		{
			nextindex = indexes.get(i);
			if ((set.size()>0) && (nextindex<set.get(set.size()-1))) continue;
			indexes.remove(nextindex);
			set.add(nextindex);
			generateSets(indexes,set,maxsize);
			set.remove(nextindex);
			indexes.add(i, nextindex);
		}
	}
	
	public static double solve(int ncities, Coordinate coords[])
	{
		double B[][];
		int counter=0;
		for(int i=1;i<=ncities;i++)
		{
			if (i==1) A[counter][i-1] = 0;
			else A[counter][i-1] = Integer.MAX_VALUE;
		}
		counter++;
		List<Integer> list = new ArrayList<Integer>(),startlist = new ArrayList<Integer>();
		List<Integer> reducedSet;
		double temp,minimum;
		int index,j,bigs=0;
		for(int i=2;i<=ncities;i++) list.add(new Integer(i));
		for(int m=2;m<=ncities;m++)
		{
			System.out.println("Generating sets with size = "+m);
			startlist = new ArrayList<Integer>();
			startlist.add(1);
			generateSets(list,startlist,m-1);
			generateBigSets(ncities);
			B = new double[bigSets.size()][ncities];
			counter=0;
			for(int i=0;i<bigSets.size();i++)
			{
				index = table[i];
				reducedSet = smallSets.get(index);
				j = missing[i];
				minimum = Double.MAX_VALUE;
				for(Integer k: reducedSet)
				{
					if (k==j) continue;
					temp = A[index][k-1]+euclidDistance(coords[k], coords[j]);
					if (temp<minimum) minimum=temp;
				}
				B[i][j-1]=minimum;
			}
			System.out.println("Trying to allocate A = "+bigSets.size());
			bigs = bigSets.size();
			for(List<Integer> l: bigSets) l.clear();
			bigSets.clear();
			for(List<Integer> l: smallSets) l.clear();
			smallSets.clear();
			A = new double[bigs][ncities+1];
			for(int i=0;i<bigs;i++)
			{
				for(int l=1;l<=ncities;l++)	A[i][l-1]=B[i][l-1];
			}
		}
		minimum = Integer.MAX_VALUE;
		for(int i=0;i<bigs;i++)
		{
			for(int z=2;z<=ncities;z++)
			{
				temp = A[i][z-1]+euclidDistance(coords[z], coords[1]);
				if (temp<minimum) minimum=temp;
			}
		}
		return minimum;
	}
	
	private static double A[][];
	private static List<List<Integer>> smallSets;
	private static List<List<Integer>> bigSets;
	private static int table[];
	private static int missing[];
	
	public static void main(String args[])
	{
		BufferedReader br = null;
		Coordinate coords[] = null;
		try
		{
			br = new BufferedReader(new FileReader("C:\\Users\\Dany\\Downloads\\tsp.txt"));
			String line;
			String posstr[];
			int linecounter=0,ncities=0;
			while((line = br.readLine())!=null)
			{
				if (linecounter==0)
				{
					ncities = Integer.parseInt(line);
					coords = new Coordinate[ncities+1];
				}
				else
				{
					posstr = line.split("\\s+");
					if (posstr.length!=2)
					{
						System.out.println("City coordinates must have an x and a y coorinate!");
						System.exit(0);
					}
					coords[linecounter]=new Coordinate(Double.parseDouble(posstr[0]),Double.parseDouble(posstr[1]));
				}
				linecounter++;
				if (linecounter==ncities+1) break;
			}
			A = new double[1][ncities];
			smallSets = new ArrayList<List<Integer>>();
			bigSets = new ArrayList<List<Integer>>();
			System.out.println("Minimum length tour has length = "+Double.toString(solve(ncities,coords)));
		}
		catch(IOException e)
		{
			System.out.println("Problem reading input file!");
		}
		catch(NumberFormatException e)
		{
			System.out.println("First line in input file does not contain an integer!");
		}
		
	}
}
