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
	
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		String str1 = sc.nextLine();
		String str2 = sc.nextLine();
		sc.close();
		System.out.println("String are pemut? = "+ispermutation(str1,str2));
	}
}