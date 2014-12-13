package algos;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
* This class can be used to build an online URL shortener service.
* The class should be used together with a relational database.
* It is assumed the database table for the links has a primary key
* field named id with auto increment funcitonality.

* Usage: Insert the original link into the database and use the
* returned id to generate a short URL with the method
* converttoalphanumeric(long id, int base, int target). The returned
* String is base 62 (alphabet: [0..9a..zA..Z]). The target number
* is being used to append a two digit checksum to the returned short
* link. This ensures that an enumeration of all links less evident.
*/
public class URLShortener
{
	public static char ntochar(long n)
	{
		char c;
		if (n<=9)
		{
			c = (char) ((char)n+'0');
			return c;
		}
		else if (n<=35)
		{
			c = (char) ((char)(n-10)+'a');
			return c;
		}
		else if (n<=61)
		{
			c = (char) ((char)(n-36)+'A');
			return c;
		}
		try
		{
				throw new IOException("n out of bounds!");
		}
		catch (IOException e)
		{
				e.printStackTrace();
		}
		return '!';
	}
	
	public static long charton(char c)
	{
		if ((c>='0') && (c<='9')) return (long)c-'0';
		else if ((c>='a') && (c<='z')) return ((long)c-'a')+10L;
		else if ((c>='A') && (c<='Z')) return ((long)c-'A')+36L;
		try
		{
			throw new IOException();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return -1;
	}
	
	public static int getchecknumber(int targetsum, long x, int base)
	{
		 return targetsum-(int)(x%(base*base));
	}
	
	public static String converttoalphanumeric(long x, int base, int targetsum)
	{
		StringBuffer sb = new StringBuffer();
		targetsum = targetsum%(base*base);
		long y = (long)getchecknumber(targetsum,hash(x),base);
		if (y<0) y=-y;
		while(x>0)
		{	
			sb.append(ntochar(x%base));
			x=x/base;
		}
		int counter=0;
		if (y==0)	sb.insert(0, "00");
		else
		{
			while(y>0)
			{	
				sb.insert(counter++,ntochar(y%base));
				y=y/base;
			}
		}
		sb.reverse();
		return sb.toString();
	}
	
	public static long converttonum(String str,int posstart, int posend, int base)
	{
		char a[] = str.toCharArray();
		long sum=0;
		long exp=1;
		for(int i=posend;i>=posstart;i--)
		{
			sum += charton(a[i])*exp;
			exp *= (long)base;
		}
		return sum;
	}
	
	public static long converttoID(String shortstr, int base, int targetsum)
	{
		targetsum = targetsum%(base*base);
		long part1 = converttonum(shortstr,0,shortstr.length()-3,base);
		long part2 = converttonum(shortstr,shortstr.length()-2,shortstr.length()-1,base);
		if (((int)(hash(part1)%(base*base))+part2==targetsum) || ((int)(hash(part1)%(base*base))-part2==targetsum)) return part1;
		else return -1;
	}
	
	public static long hash(long x)
	{
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		if (md!=null)
		{
			md.update(ByteBuffer.allocate(8).putLong(x).array());
			byte[] h = md.digest();
			return h[0]+h[1]*256;
		}
		return -1;
	}
	
	public static void test(long id, int base, int target)
	{
		String shortlink = converttoalphanumeric(id,base,target);
		long idfromlink = converttoID(shortlink,base,target);
		System.out.println("ID = "+id+" --> shortlink = "+shortlink);
		System.out.println("Shortlink = "+shortlink+" --> ID = "+idfromlink);
		System.out.println();
	}
	
	public static void main(String args[])
	{
		int base = 62;
		test(104993L,base,11);
		test(1049748593L,base,1024);
		test(104996748593L,base,155200);
		test(10494596748593L,base,2111111111);
		test(593L,base,11);
	}
}
