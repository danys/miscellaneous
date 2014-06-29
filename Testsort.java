package interview;

import java.util.Random;

public class Testsort
{
	public static void selectionsort(int a[])
	{
		for(int i=0;i<a.length-1;i++)
		{
			int minindex = i;
			for(int j=i+1;j<a.length;j++) if (a[j]<a[minindex]) minindex = j;
			if (minindex!=i)
			{
				int temp = a[minindex];
				a[minindex] = a[i];
				a[i] = temp;
			}
		}
	}
	
	public static void insertionsort(int a[])
	{
		for(int i=1;i<a.length;i++)
		{
			int temp = a[i];
			int j=i-1;
			while((j>=0) && (a[j]>temp))
			{
				a[j+1]=a[j];
				j--;
			}
			a[j+1]=temp;
		}
	}
	
	public static void shellsort(int a[], int increments[])
	{
		for(int c=0;c<increments.length;c++)
		{
			int incr = increments[c];
			for(int i=incr;i<a.length;i++)
			{
				int temp = a[i];
				int j=i-incr;
				while((j>=0) && (a[j]>temp))
				{
					a[j+incr]=a[j];
					j=j-incr;
				}
				a[j+incr]=temp;
			}
		}
	}
	
	public static void bubblesort(int a[])
	{
		boolean changed = true;
		while(changed)
		{
			changed = false;
			for(int i=0;i<a.length-1;i++)
			{
				if (a[i+1]<a[i])
				{
					int temp = a[i+1];
					a[i+1]=a[i];
					a[i]=temp;
					changed = true;
				}
			}
		}
	}
	
	public static void quicksort(int a[], int l, int r)
	{
		if (l>=r) return;
		int i=l;
		int j=r;
		int p=a[r];
		int temp;
		while(true)
		{
			while((i<=r) && (a[i]<=p)) i++;
			while((j>=l) && (a[j]>=p)) j--;
			if (i==r+1) i=r;
			if (j==l-1) j=l;
			if (i>=j) break;
			temp = a[j];
			a[j]=a[i];
			a[i]=temp;
		}
		temp=a[i];
		a[i]=a[r];
		a[r]=temp;
		quicksort(a,l,i-1);
		quicksort(a,i+1,r);
	}
	
	public static void insert(int a[], int i, int maxi)
	{
		while(2*i<=maxi)
		{
			int j = 2*i;
			if ((j+1<=maxi) && (a[j+1]>a[j])) j++;
			if (a[j]>a[i])
			{
				int temp = a[j];
				a[j] = a[i];
				a[i] = temp;
				i = j;
			}
			else i = maxi+1;
		}
	}

	public static void heapsort(int a[])
	{
		//build the heap
		for(int i=(a.length/2)-1;i>=0;i--) insert(a,i,a.length-1);
		//sort the heap
		for(int i=a.length-1;i>=1;i--)
		{
			int temp = a[i];
			a[i] = a[0];
			a[0] = temp;
			insert(a,0,i-1);
		}
	}
	
	public static void merge(int a[],int l,int r, int mid)
	{
		int tempa[] = new int[r-l+1];
		int i=l;
		int j=mid+1;
		int cc=0;
		while((i<=mid) && (j<=r))
		{
			if (a[i]<=a[j])
			{
				tempa[cc]=a[i];
				i++;
			}
			else
			{
				tempa[cc]=a[j];
				j++;
			}
			cc++;
		}
		if (i>mid)
		{
			while(j<=r)
			{
				tempa[cc]=a[j];
				j++;
				cc++;
			}
		}
		else
		{
			while(i<=mid)
			{
				tempa[cc]=a[i];
				i++;
				cc++;
			}
		}
		for(int k=0;k<r-l+1;k++) a[l+k]=tempa[k];
	}
	
	public static void mergesort(int a[],int l, int r)
	{
		if (l>=r) return;
		int mid = (l+r)/2;
		mergesort(a,l,mid);
		mergesort(a,mid+1,r);
		merge(a,l,r,mid);
	}
	
	public static void printlist(int a[],String text)
	{
		System.out.print(text+" list = ");
		for(int i=0;i<a.length;i++)
		{
			System.out.print(" "+a[i]);
		}
		System.out.println();
	}
	
	public static void main(String args[])
	{
		int len = 10;
		int maxint = 30;
		int list[] = new int[len];
		Random r = new Random();
		for(int i=0;i<len;i++) list[i]=r.nextInt(maxint+1);
		printlist(list,"Random");
		//int incr[] = new int[]{5,3,1};
		//shellsort(list,incr);
		//quicksort(list,0,len-1);
		//heapsort(list);
		mergesort(list,0,len-1);
		printlist(list,"Sorted");
	}
}