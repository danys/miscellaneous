import java.util.Arrays;
import java.util.Scanner;

public class permut
{
	
	public static String sorter(String str)
	{
		char cstr[] = str.toCharArray();
		Arrays.sort(cstr);
		return Arrays.toString(cstr);
	}
	
	public static boolean ispermutation(String str1,String str2)
	{
		if (str1.length()!=str2.length()) return false;
		return (sorter(str1).compareTo(sorter(str2))==0);
	}
	
	public static int[] charscanner(String str)
	{
		int count[] = new int[256]; //256 = size of ASCII character set
		for(int i=0;i<count.length;i++) count[i]=0;
		char c[] = str.toCharArray();
		for(int i=0;i<str.length();i++)
		{
			count[c[i]-'a']++;
		}
		return count;
	}
	
	public static boolean ispermutation2(String str1,String str2)
	{
		if (str1.length()!=str2.length()) return false;
		int c1[] = charscanner(str1);
		int c2[] = charscanner(str2);
		for(int i=0;i<str1.length();i++)
		{
			if (c1[i]!=c2[i]) return false;
		}
		return true;
	}
	
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		String str1 = sc.nextLine();
		String str2 = sc.nextLine();
		sc.close();
		System.out.println("String are pemut? = "+ispermutation2(str1,str2));
	}
}
