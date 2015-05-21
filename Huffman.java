package coding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HuffmanEncoding
{
	private static List<Node> frontier;
	private static Map<Character,Integer> letter2Index;
	private static Map<String,Node> cb;
	
	private static class Node
	{
		public double value;
		public Node left;
		public Node right;
		public String label;
		private String encoding;
		
		public Node(String label, double value, Node left, Node right)
		{
			this.value = value;
			this.left = left;
			this.right = right;
			this.label = label;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof Node)) return false;
			Node node = (Node)obj;
			if (node.label==this.label) return true;
			else return false;
		}
	}
	
	//Returns the index of the minimum element of the given list except for currentMinIndex
	//This method can be used to get the minimum index of a list or the minimum index other than currentMinIndex
	private static int minIndex(List<Node> list, int currentMinIndex)
	{
		int minIndex=(currentMinIndex==0)? 1:0;
		double minVal=list.get(minIndex).value;
		Node node;
		for(int i=0;i<list.size();i++)
		{
			if (i==currentMinIndex) continue;
			node = list.get(i);
			if (node.value<=minVal)
			{
				minIndex = i;
				minVal = node.value;
			}
		}
		return minIndex;
	}
	
	//Depth-first search: Adds the leaves to the result set
	private static void dfs(Node root, String bitString, Map<String,Node> codebook)
	{
		if ((root.left==null) && (root.right==null))
		{
			root.encoding=bitString;
			codebook.put(root.label, root);
			return;
		}
		if (root.left!=null) dfs(root.left,bitString+"0",codebook);
		if (root.right!=null) dfs(root.right,bitString+"1",codebook);
	}
	
	//Initialization: Supported characters include alpha-numeric characters and several punctuation symbols
	private static void init()
	{
		frontier = new ArrayList<Node>();
		letter2Index = new HashMap<Character,Integer>();
		int currentindex=0;
		//Insert lower-case alphabet
		for(int i=0;i<26;i++)
		{
			frontier.add(new Node(String.valueOf((char)('a'+i)),0,null,null));
			letter2Index.put((char)('a'+i), i+currentindex);
		}
		currentindex += 26;
		//Insert upper-case alphabet
		for(int i=0;i<26;i++)
		{
			frontier.add(new Node(String.valueOf((char)('A'+i)),0,null,null));
			letter2Index.put((char)('A'+i), i+currentindex);
		}
		currentindex += 26;
		//Insert digits
		for(int i=0;i<10;i++)
		{
			frontier.add(new Node(String.valueOf((char)('0'+i)),0,null,null));
			letter2Index.put((char)('0'+i), i+currentindex);
		}
		currentindex += 10;
		//Insert punctuation symbols
		frontier.add(new Node(String.valueOf('.'),0,null,null));
		letter2Index.put('.', currentindex);
		currentindex++;
		frontier.add(new Node(String.valueOf(','),0,null,null));
		letter2Index.put(',', currentindex);
		currentindex++;
		frontier.add(new Node(String.valueOf('\''),0,null,null));
		letter2Index.put('\'', currentindex);
		currentindex++;
		frontier.add(new Node(String.valueOf('!'),0,null,null));
		letter2Index.put('!', currentindex);
		currentindex++;
		frontier.add(new Node(String.valueOf('-'),0,null,null));
		letter2Index.put('-', currentindex);
		currentindex++;
		frontier.add(new Node(String.valueOf('?'),0,null,null));
		letter2Index.put('?', currentindex);
		currentindex++;
		frontier.add(new Node(String.valueOf(' '),0,null,null));
		letter2Index.put(' ', currentindex);
		currentindex++;
	}
	
	//Method that analyzes the given text and produces the codebook based on the character frequencies
	public static Map<String,Node> buildCodebook(String text)
	{
		//Initialization
		init();
		int counter=0;
		char carray[] = text.toCharArray();
		Integer index;
		//Count character frequencies
		for(int i=0;i<carray.length;i++)
		{
			index = letter2Index.get(carray[i]);
			if (index!=null)
			{
				counter++;
				frontier.get(index).value++;
			}
		}
		for(int i=0;i<frontier.size();i++) frontier.get(i).value = frontier.get(i).value/counter;
		//Remove characters that are not needed
		Iterator<Node> nodeit = frontier.iterator();
		Node cnode;
		while(nodeit.hasNext())
		{
			cnode = nodeit.next();
			if (cnode.value==0.0) nodeit.remove();
		}
		int indexSmallest, indexSecondSmallest;
		Node newNode,firstNode,secondNode;
		if (frontier.size()==0) return null;
		//Perform Huffman's coding algorithm
		if (frontier.size()==1)
		{
			newNode = new Node("",0,frontier.get(0),null);
			frontier.add(newNode);
			frontier.remove(0);
		}
		else
		{
			while(frontier.size()>1)
			{
				//Seek the two nodes with the least value
				indexSmallest = minIndex(frontier,-1);
				indexSecondSmallest = minIndex(frontier,indexSmallest);
				newNode = new Node(frontier.get(indexSmallest).label+frontier.get(indexSecondSmallest).label,frontier.get(indexSmallest).value+frontier.get(indexSecondSmallest).value,frontier.get(indexSmallest),frontier.get(indexSecondSmallest));
				firstNode = frontier.get(indexSmallest);
				secondNode = frontier.get(indexSecondSmallest);
				frontier.add(newNode);
				frontier.remove(firstNode);
				frontier.remove(secondNode);
			}
		}
		//Read off the codes and store them in the codebook
		Node root = frontier.get(0);
		cb = new HashMap<String,Node>();
		dfs(root,"",cb);
		//Print the codebook
		Set<Entry<String, Node>> entries = cb.entrySet();
		String symbol;
		for(Entry<String,Node> entry: entries)
		{
			symbol = (entry.getKey().equals(" ")) ? "space" : entry.getKey();
			System.out.println("Symbol "+symbol+" has frequency \t"+entry.getValue().value+" and encoding \t"+entry.getValue().encoding);
		}
		return cb;
	}
	
	private static boolean isSetBit(byte in, int pos)
	{
		if ((pos==7) && (in<0)) return true;
		byte b = 1;
		b = (byte)(b << pos);
		b = (byte)(in & b);
		if (b>0) return true;
		else return false;
	}
	
	private static byte setBit(byte in, int pos)
	{
		byte b = 1;
		b = (byte)(b << pos);
		return (byte)(in | b);
	}
	
	//Print a byte array
	public static void printByteArray(Byte[] barray,int bytecount)
	{
		int byteindex, byteoffset;
		for(int i=0;i<bytecount*8;i++)
		{
			byteindex = i/8;
			byteoffset = i%8;
			if (isSetBit(barray[byteindex],7-byteoffset)) System.out.print("1");
			else System.out.print("0");
		}
	}
	
	/**
	 * Method that compresses the given text String using Huffman's coding algorithm. <br>
	 * If the last byte is not full it is padded with zeros. The return value provides the number of output bits.
	 * @param text the text that should be compactly coded
	 * @param barray the resulting compressed byte array
	 * @return a number that corresponds to the number of bits in the output
	 */
	public static int encode(String text, Byte barray[])
	{
		Map<String,Node> cb = buildCodebook(text);
		StringBuilder sb = new StringBuilder();
		StringBuilder presb = new StringBuilder("Encoded text = ");
		char carray[] = text.toCharArray();
		Node node;
		int bitcount=0;
		//Count the number of bits in the encoded text
		for(int i=0;i<carray.length;i++)
		{
			node = cb.get(Character.toString(carray[i]));
			if (node!=null)
			{
				sb.append(node.encoding);
				bitcount += node.encoding.length();
			}
		}
		//Encode the output in a byte array
		int bytecount = (bitcount%8==0) ? bitcount/8 : (bitcount/8)+1;
		barray = new Byte[bytecount];
		for(int i=0;i<bytecount;i++) barray[i] = new Byte((byte)0);
		int byteindex, byteoffset;
		byte b;
		for(int i=0;i<sb.length();i++)
		{
			byteindex = i/8;
			byteoffset = i%8;
			b = barray[byteindex];
			if (sb.charAt(i)=='1')	b = setBit(b,7-byteoffset);
			barray[byteindex]=b;
		}
		//printByteArray(barray,bytecount);
		System.out.println(presb.append(sb).toString());
		return bitcount;
	}
	
	public static void main(String args[])
	{
		String text = "Hello, Dany how are you?";
		//String text = "aa";
		Byte barray[]=null;
		int ncbits = encode(text,barray), tbits=text.length()*8;
		Float savings = Math.abs((float)(ncbits-tbits)/(float)(tbits))*100;
		System.out.println("Compressed output has "+ncbits+" bits. Assuming 8 bits per symbol original, input has "+tbits+" bits. Compression savings: "+savings+" %");
	}
}
