package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Knapsack
{
	public static int solve(int nitems,int size,int A[][],int sizes[],int values[])
	{
		//Initialize
		for(int i=0;i<=size;i++) A[0][i]=0;
		for(int i=1;i<=nitems;i++)
		{
			for(int j=0;j<=size;j++)
			{
				if (j-sizes[i-1]<0) A[i][j]=A[i-1][j];
				else A[i][j]=Math.max(A[i-1][j], A[i-1][j-sizes[i-1]]+values[i-1]);
			}
		}
		return A[nitems][size];
	}
	
	public static void main(String args[])
	{
		BufferedReader br = null;
		try
		{
			FileReader fr = new FileReader("C:\\Users\\Dany\\Downloads\\knapsack1.txt");
			br = new BufferedReader(fr);
			String line;
			String strparts[];
			line = br.readLine();
			strparts = line.split("\\s+");
			int size,nitems,linecounter;
			if ((line==null) || (line.isEmpty()) || (strparts.length!=2))
			{
				System.out.println("First line must specify the maximum weight as well as the number of items separated by a space!");
				System.exit(0);
			}
			size = Integer.parseInt(strparts[0]);
			nitems = Integer.parseInt(strparts[1]);
			int A[][] = new int[nitems+1][size+1];
			int sizes[] = new int[nitems];
			int values[] = new int[nitems];
			linecounter=0;
			line = br.readLine();
			while((line!=null) && (linecounter<nitems))
			{
				strparts = line.split("\\s+");
				if (strparts.length!=2)
				{
					System.out.println("All lines in the input file need to contain two integers!");
					System.exit(0);
				}
				values[linecounter] = Integer.parseInt(strparts[0]);
				sizes[linecounter] = Integer.parseInt(strparts[1]);
				line = br.readLine();
				linecounter++;
			}
			System.out.println("Result = "+solve(nitems,size,A,sizes,values));
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
