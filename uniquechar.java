public class uniquechar
{
	final static int maxchar = 26;
	static boolean charlist[] = new boolean[maxchar];
	
	public static boolean uniquecharacter(String str)
	{
		for(int i=0;i<maxchar;i++) charlist[i]=false;
		char[] c = str.toCharArray();
		for(int i=0;i<str.length();i++)
		{
			if (charlist[c[i]-'a']==true) return false;
			else charlist[c[i]-'a']=true;
		}
		return true;
	}
	
	public static void main(String args[])
	{
		String s = "abdkejf";
		System.out.println("Has unique char = "+uniquecharacter(s));
	}
}