package exercise1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SortnCount 
{
	public static long merge(int a[],int i,int m,int j)
	{
		int b[] = new int[j-i+1];
		int in1=i;
		int in2=m+1;
		int cc=0;
		long inv=0;
		while((in1<=m) && (in2<=j))
		{
			if (a[in1]<a[in2])
			{
				b[cc++]=a[in1];
				in1++;
			}
			else
			{
				b[cc++]=a[in2];
				inv += m-in1+1;
				in2++;
			}
		}
		if (in1>m) while (in2<=j) b[cc++]=a[in2++];
		else while (in1<=m) b[cc++]=a[in1++];
		for(int z=0;z<j-i+1;z++) a[z+i]=b[z];
		return inv;
	}
	
	public static long mergesort(int a[], int i, int j)
	{
		long res=0;
		if (i>=j) return 0;
		int m=(j-i)/2+i;
		res += mergesort(a,i,m);
		res += mergesort(a,m+1,j);
		res += merge(a,i,m,j);
		return res;
	}
	
	public static void main(String args[])
	{
		int a[] = {1,6,4,3,2,5};
		System.out.println("Number of inversions = "+Long.toString(mergesort(a,0,a.length-1)));
	}
}
