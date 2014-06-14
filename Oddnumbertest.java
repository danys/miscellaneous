import java.io.IOException;
import java.util.HashMap;

public class Oddnumbertest
{
	//The input array a contains integer numbers
	//One integer occurs an odd number of times and all other integers occur an even number of times
	//The function should return the integer that appears an odd number of times
	//Time complexity: O(N)
	public static int findodd(int a[])
	{
		try
		{
			HashMap<Integer,BinObj> map = new HashMap<Integer,BinObj>();
			for(int i=0;i<a.length;i++)
			{
				if (map.containsKey(a[i]))
				{
					map.put(a[i],new BinObj(!(map.get(a[i]).b)));
				}
				else map.put(a[i],new BinObj(true));
			}
			for(int i=0;i<a.length;i++) if (map.get(a[i]).b) return a[i];
			throw new IOException();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return -1;
	}
	
	public static void main(String args[])
	{
		//Throws an IOException because input was not as expected
		/*int a[] = new int[10];
		a[0] = 2;
		a[1] = 12;
		a[2] = 2;
		a[3] = 12;
		a[4] = 5;
		a[5] = 5;
		a[6] = 12;
		a[7] = 8;
		a[8] = 8;
		a[9] = 12;*/
		//The following input returns the correct result 12
		int a[] = new int[9];
		a[0] = 2;
		a[1] = 12;
		a[2] = 2;
		a[3] = 12;
		a[4] = 5;
		a[5] = 5;
		a[6] = 12;
		a[7] = 8;
		a[8] = 8;
		System.out.println("Result = " + findodd(a));
	}
}