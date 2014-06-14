import java.util.HashMap;

public class SumUptoX
{
	//Find if two elements in array sum up to x
	//Time complexity O(N)
	public static boolean ispairsumx(int a[],int x)
	{
		HashMap<Integer,BinObj> map = new HashMap<Integer,BinObj>();
		for(int i=0;i<a.length;i++)
		{
			map.put(a[i],new BinObj(true));
		}
		for(int i=0;i<a.length;i++)
		{
			if (map.containsKey(x-a[i])) return true;
		}
		return false;
	}
	
	public static void main(String args[])
	{
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
		System.out.println("Result = " + ispairsumx(a,20));
	}
}