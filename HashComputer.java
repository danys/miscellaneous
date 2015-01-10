import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class can be used to easily compute hashes of Strings using hashing
 * algorithms such as MD5, SHA-1, SHA-256. The output is a String
 * conforming to the hex representation of the resulting hash.
 * 
 * @author Dany
 *
 */
public class HashComputer
{
	
	public static char convert(int x)
	{
		try
		{
			if (x<0) throw new IOException();
			if (x<10) return (char)('0'+x);
			if (x<16) return (char)('a'+x-10);
			throw new IOException();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return '0';
		}
	}
	
	public static String hash(String input, String algorithm)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(input.getBytes("UTF-8"));
			byte[] digest = md.digest();
			byte temp;
			int half;
			half=0;
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<digest.length;i++)
			{
				temp = digest[i];
				half = (temp & 0xF0) >>> 4;
				sb.append(convert(half));
				half = temp & 0x0F;
				sb.append(convert(half));
			}
			
			return sb.toString();
		}
		catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String args[])
	{
		String input = "Dany";
		String algorithm = "SHA-256";
		String output = hash(input,algorithm);
		System.out.println("The "+algorithm+" hash of "+input+" is :"+output);
	}
}
