public class Palindrome
{
	public static Node seek(int index,Node head)
	{
		int currentindex = 0;
		while((currentindex<index) && (head!=null))
		{
			head = head.next;
			currentindex++;
		}
		return head;
	}
	
	
	//head points to linked list of length N
	//Time complexity of function is O(N^2)
	public static boolean ispalindrome(Node head)
	{
		Node start = head;
		Node end = head;
		int startindex = 0;
		int endindex = 0;
		//N-1 iterations to determine last index
		while(end.next!=null)
		{
			end = end.next;
			endindex++;
		}
		//Loop ceil(N/2) times
		//Time complexity of loop is O(N^2)
		while(startindex<=endindex)
		{
			if (end.data!=start.data) return false;
			startindex++;
			start = start.next;
			endindex--;
			//in each loop: goes to position N,N-1,N-2,...,N-floor(N/2)
			end = seek(endindex,head);
		}
		return true;
	}
	
	public static Node reverselist(Node head)
	{
		if (head==null) return null;
		Node newhead = null;
		while(head!=null)
		{
			Node n = new Node(head.data);
			n.next = newhead;
			newhead = n;
			head = head.next;
		}
		return newhead;
	}
	
	//Time complexity O(N)
	public static boolean ispalindromebetter(Node head)
	{
		//reversing done in N steps
		Node reversed = reverselist(head);
		int length=0;
		Node current = head;
		//loop executed N times
		while(current!=null)
		{
			current = current.next;
			length++;
		}
		//for loop executed N/2 times
		for(int i=0;i<length/2;i++)
		{
			if (head.data!=reversed.data) return false;
			head = head.next;
			reversed=reversed.next;
		}
		return true;
	}
	
	public static void main(String args[])
	{
		Node head = new Node(2);
		head.append(7);
		head.append(3);
		head.append(0);
		head.append(2);
		head.append(1);
		System.out.println("Linked list is palindrome = "+ispalindromebetter(head));
	}
}
