package pa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This application solves the traveling salesman problem using a dynamic programming approach.
 * Time complexity: O(n^2*2^n)
 * The program works up to instances of 25 cities.
 * 
 * @author Dany
 *
 */
public class TSP
{
	private static float A[][];
	private static float B[][];
	private static float dist[][];
	private static List<List<Integer>> smallSets;
	private static List<List<Integer>> bigSets;
	private static int maxsets = 2704156;
	private static int maxdestinations = 25;
	
	public static class Coordinate
	{
		private float x;
		private float y;
		
		public Coordinate(float x,float y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	public static float euclidDistance(Coordinate c1, Coordinate c2)
	{
		return (float)Math.sqrt((c1.x-c2.x)*(c1.x-c2.x)+(c1.y-c2.y)*(c1.y-c2.y));
	}
	
	public static void generateSets(List<Integer> indexes,List<Integer> set,int maxsize,List<List<Integer>> sets)
	{
		if (set.size()==maxsize)
		{
			if (maxsize==0) return;
			List<Integer> l = new ArrayList<Integer>();
			for(int i=0;i<set.size();i++) l.add(set.get(i));
			sets.add(l);
			return;
		}
		Integer nextindex;
		for(int i=0;i<indexes.size();i++)
		{
			nextindex = indexes.get(i);
			if ((set.size()>0) && (nextindex<set.get(set.size()-1))) continue;
			indexes.remove(nextindex);
			set.add(nextindex);
			generateSets(indexes,set,maxsize,sets);
			set.remove(nextindex);
			indexes.add(i, nextindex);
		}
	}
	
	public static List<Integer> reducedSet(List<Integer> list, int x)
	{
		List<Integer> res = new ArrayList<Integer>();
		for(Integer i: list)	if (i.intValue()!=x) res.add(i.intValue());
		return res;
	}
	
	public static int seekSet(List<List<Integer>> sets, List<Integer> set,int l, int r)
	{
		if (set.size()==0) return 0; //case where empty list is sought: m=0
		if (l>r) return -1;
		int m=(l+r)/2;
		List<Integer> cSet;
		int x,counter=0,verdict=0;
		cSet = sets.get(m);
		for(int j=0;j<cSet.size();j++)
		{
			x = cSet.get(j);
			if (x>set.get(j).intValue())
			{
				verdict=-1;
				break;
			}
			else if (x<set.get(j).intValue())
			{
				verdict=1;
				break;
			}
			else if (x==set.get(j).intValue()) counter++;
		}
		if ((verdict==0)&&(counter==cSet.size())) return m;
		else if (verdict==-1) return seekSet(sets,set,l,m-1);
		else if (verdict==1) return seekSet(sets,set,m+1,r);
		return -1;
	}
	
	public static float solve(int ncities)
	{
		int counter=0,index,k;
		float minimum;
		for(int i=0;i<ncities;i++)
		{
			if (i==0) A[counter][i] = 0; //i ranges from 0 to ncities-1
			else A[counter][i] = Integer.MAX_VALUE;
		}
		List<Integer> list = new ArrayList<Integer>();
		List<Integer> emptylist = new ArrayList<Integer>();
		List<Integer> bigSet,rSet;
		for(int i=2;i<=ncities;i++) list.add(new Integer(i));
		for(int m=2;m<=ncities;m++)
		{
			System.out.println("m = "+m);
			generateSets(list,emptylist,m-2,smallSets); //lists do not contain 1
			System.out.println("OK generated small sets = "+smallSets.size());
			generateSets(list,emptylist,m-1,bigSets); //lists do not contain 1
			System.out.println("OK generated big sets = "+bigSets.size());
			counter=0;
			for(int s=0;s<bigSets.size();s++)
			{
				bigSet = bigSets.get(s);
				for(Integer j: bigSet)
				{
					rSet = reducedSet(bigSet,j);
					index = seekSet(smallSets,rSet,0,smallSets.size()-1);
					minimum = Float.MAX_VALUE;
					for(int kindex=0;kindex<=bigSet.size();kindex++)
					{
						if (kindex==bigSet.size()) k=1;
						else k=bigSet.get(kindex);
						if (k==j) continue;
						minimum = Math.min(minimum,A[index][k-1]+dist[k-1][j-1]);
					}
					B[counter][j-1]=minimum;
				}
				counter++;
			}
			System.out.println("Cleaning up");
			for(List<Integer> set: smallSets) set.clear();
			smallSets.clear();
			for(List<Integer> set: bigSets) set.clear();
			bigSets.clear();
			System.out.println("Copying");
			for(int i=0;i<counter;i++)
			{
				for(int j=0;j<ncities;j++)
				{
					if (B[i][j]==0) A[i][j] = Float.MAX_VALUE;
					else A[i][j]=B[i][j];
				}
			}
		}
		minimum = Float.MAX_VALUE;
		for(int i=1;i<ncities;i++)	minimum = Math.min(minimum, A[0][i]+dist[i][0]);
		return minimum;
	}
	
	public static void precompute(int ncities,Coordinate coords[])
	{
		for(int i=0;i<ncities;i++)
		{
			for(int j=0;j<ncities;j++) dist[i][j]=euclidDistance(coords[i],coords[j]);
		}
	}
	
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
					coords[linecounter-1]=new Coordinate(Float.parseFloat(posstr[0]),Float.parseFloat(posstr[1]));
				}
				linecounter++;
				if (linecounter==ncities+1) break;
			}
			dist = new float[ncities][ncities];
			precompute(ncities,coords);
			A = new float[maxsets][maxdestinations];
			B = new float[maxsets][maxdestinations];
			smallSets = new ArrayList<List<Integer>>();
			bigSets = new ArrayList<List<Integer>>();
			System.out.println("Minimum length tour has length = "+Float.toString(solve(ncities)));
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
