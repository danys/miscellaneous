import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class Treenode
{
	private ArrayList<Treenode> links;
	private int value;
	
	public Treenode(int value)
	{
		this.value = value;
		this.links = new ArrayList<Treenode>();
	}
	
	public void addchild(Treenode node)
	{
		if (links!=null) links.add(node);
	}
	
	public void dfs()
	{
		System.out.println("Starting node value = " + this.value);
		Stack<Treenode> s = new Stack<Treenode>();
		s.push(this);
		while(!s.empty())
		{
			Treenode current = s.pop();
			System.out.println("Current node value = " + current.value);
			for(int i=0;i<current.links.size();i++)
			{
				Treenode next = current.links.get(i);
				s.push(next);
			}
		}
	}
	
	public void rec_dfs(Treenode root)
	{
		System.out.println("Current node value = " + root.value);
		for(int i=0;i<root.links.size();i++) rec_dfs(root.links.get(i));
	}
	
	public void bfs()
	{
		System.out.println("Starting node value = " + this.value);
		LinkedList<Treenode> s = new LinkedList<Treenode>();
		s.addFirst(this);
		while(s.size()!=0)
		{
			Treenode current = s.removeLast();
			System.out.println("Current node value = " + current.value);
			for(int i=0;i<current.links.size();i++)
			{
				Treenode next = current.links.get(i);
				s.addFirst(next);
			}
		}
	}
	
	public void rec_bfs(LinkedList<Treenode> list)
	{
		if (list.size()==0) return;
		Treenode t = list.removeLast();
		System.out.println("Current node value = " + t.value);
		for(int i=0;i<t.links.size();i++) list.addFirst(t.links.get(i));
		rec_bfs(list);
	}
	
	public static void main(String args[])
	{
		Treenode root = new Treenode(5);
		Treenode e1 = new Treenode(4);
		Treenode e2 = new Treenode(7);
		Treenode e3 = new Treenode(1);
		Treenode e4 = new Treenode(2);
		Treenode e5 = new Treenode(9);
		Treenode e6 = new Treenode(7);
		Treenode e7 = new Treenode(10);
		root.addchild(e1);
		root.addchild(e2);
		e1.addchild(e3);
		e1.addchild(e4);
		e1.addchild(e5);
		e2.addchild(e6);
		e2.addchild(e7);
		//do a depth first search
		System.out.println("Depth first search");
		root.dfs();
		//do a breadth first search
		System.out.println("Breadth first search");
		root.bfs();
		//do a recursive depth first search
		System.out.println("Recursive depth first search");
		root.rec_dfs(root);
		//do a recursive breadth first search
		System.out.println("Recursive breadth first search");
		LinkedList<Treenode> l = new LinkedList<Treenode>();
		l.addFirst(root);
		root.rec_bfs(l);
	}
}