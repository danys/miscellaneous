public class ArrayStack
{
	Node list[];
	int maxlen;
	int stackpointer;
	
	public ArrayStack(int length)
	{
		this.list = new Node[length];
		this.maxlen = length;
		this.stackpointer = 0;
	}
	
	public Object pop()
	{
		if (stackpointer==0) return null;
		stackpointer--;
		Node o = list[stackpointer];
		list[stackpointer]=null;
		return o.data;
	}
	
	public Object peek()
	{
		if (stackpointer==0) return null;
		return list[stackpointer-1].data;
	}
	
	public void push(Object o)
	{
		if (stackpointer==maxlen)
		{
			//double the array size
			maxlen *= 2;
			Node nlist[] = new Node[maxlen];
			for(int i=0;i<maxlen/2;i++) nlist[i]=list[i];
			this.list = nlist;
		}
		Node nnode = new Node(o);
		this.list[stackpointer] = nnode;
		stackpointer++;
	}
	
	public int maxsize()
	{
		return maxlen;
	}
}