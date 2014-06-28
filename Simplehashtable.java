import java.util.LinkedList;

public class Simplehashtable
{
	private LinkedList<Bucket> table[];
	private int size;
	
	public Simplehashtable(int size)
	{
		this.table = (LinkedList<Bucket>[]) new LinkedList<?>[size];
		for(int i=0;i<size;i++) this.table[i] = new LinkedList<Bucket>();
		this.size = size;
	}
	
	private int hashindex(int key)
	{
		return key%size;
	}
	
	public void put(int key, String value)
	{
		LinkedList<Bucket> l = table[hashindex(key)];
		Bucket b = new Bucket(key,value);
		l.addLast(b);
	}
	
	public String get(int key)
	{
		LinkedList<Bucket> l = table[hashindex(key)];
		int i = 0;
		Bucket temp = l.get(i);
		while((temp.getkey()!=key) && (i<l.size()))
		{
			i++;
			temp = l.get(i);
		}
		return (temp.getkey()==key) ? temp.getvalue() : "Not found";
	}
	
	public int getsize()
	{
		return size;
	}
	
	public void printtable()
	{
		for(int i=0;i<size;i++)
		{
			LinkedList<Bucket> l = table[i];
			System.out.print("Index = \t"+i);
			for(int j=0;j<l.size();j++)
			{
				Bucket t = l.get(j);
				System.out.print(" | Key = "+t.getkey()+", value = "+t.getvalue());
			}
			System.out.println("");
		}
	}
}
class Bucket
{
	private int key;
	private String value;
	
	public Bucket(int key, String value)
	{
		this.key = key;
		this.value = value;
	}
	
	public int getkey()
	{
		return this.key;
	}
	
	public String getvalue()
	{
		return this.value;
	}
}