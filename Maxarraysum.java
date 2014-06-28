import java.util.Random;

public class Maxarraysum
{
	public static int maxsum(int a[])
	{
		int maxsum = 0;
		int cmaxsum = 0;
		for(int i=0;i<a.length;i++)
		{
			cmaxsum = cmaxsum+a[i]>0 ? cmaxsum+a[i] : 0;
			maxsum = Math.max(maxsum,cmaxsum);
		}
		return maxsum;
	}
	
	public static void main(String args[])
	{
		int len = 10;
		int maxint = 50;
		int a[] = new int[len];
		Random r = new Random();
		Random rsign = new Random();
		for(int i=0;i<len;i++)
		{
			a[i] = (rsign.nextInt(10)>=5) ? r.nextInt(maxint+1) : (-1)*r.nextInt(maxint+1);
			System.out.println("Array position "+i+" = "+a[i]);
		}
		System.out.println("Result = "+maxsum(a));
	}
}