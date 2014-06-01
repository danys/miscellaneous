package interview;

public class Node
{
	int data;
	Node next=null;
	
	public Node(int data)
	{
		this.data = data;
	}
	
	public void append(int data)
	{
		Node current = this;
		Node newnode = new Node(data);
		while(current.next!=null) current = current.next;
		current.next = newnode;
	}
	
	public Node delete(Node head, int data)
	{
		if (head.data==data) return head.next;
		else
		{
			Node current = head;
			while(current.next!=null)
			{
				if (current.next.data==data)
				{
					current.next = current.next.next;
				}
				current = current.next;
			}
		}
		return head;
	}
	
	public void print(Node head)
	{
		while(head!=null)
		{
			System.out.print("-> "+head.data);
			head = head.next;
		}
		System.out.println();
	}
	
	public Node bubblesort(Node head)
	{
		if (head==null) return head;
		Node temp,temp2;
		Node current = head;
		boolean unchanged = false;
		while(!unchanged)
		{
			unchanged = true;
			while(current.next!=null)
			{
				if (current.next.data<current.data)
				{
					if (current==head)
					{
						head=current.next;
						temp = current.next.next;
						current.next.next = current;
						current.next = temp;
					}
					else
					{
						Node p = head;
						Node prev = null;
						while((p.next!=null) && (p.next.data!=current.data))
						{
							if (p.next.data==current.data) prev=p;
							p = p.next;
						}
						temp = current.next.next;
						temp2 = current.next;
						current.next.next = current;
						current.next = temp;
						if (prev!=null) prev.next = temp2;
					}
					unchanged=false;
				}
				current = current.next;
			}
			current = head;
		}
		return head;
	}
}
