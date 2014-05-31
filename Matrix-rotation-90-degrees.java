package interview;

public class Rotation
{
	public static void printm(int m[][])
	{
		for(int i=0;i<m.length;i++)
		{
			for(int j=0;j<m.length;j++)
			{
				System.out.print("\t"+m[i][j]);
			}
			System.out.println();
		}
	}
	
	public static int[][] init(int s)
	{
		int m[][] = new int[s][s];
		for(int i=0;i<m.length;i++)
		{
			for(int j=0;j<m.length;j++)
			{
				m[i][j]=0;
			}
		}
		return m;
	}
	
	public static void rotate(int m[][])
	{
		for(int depth=0;depth<(m.length+1)/2;depth++)
		{
			for(int col=depth;col<m.length-1-depth;col++)
			{
				int temp = m[depth][col];
				int coord[] = new int[2];
				coord[0]=depth;
				coord[1]=col;
				int newcoord[];
				for(int i=1;i<=3;i++)
				{
					newcoord = coordtrans(coordtrans(coordtrans(coord,m.length),m.length),m.length);
					m[coord[0]][coord[1]] = m[newcoord[0]][newcoord[1]];
					coord[0]=newcoord[0];
					coord[1]=newcoord[1];
				}
				//assign temp
				m[coord[0]][coord[1]] = temp;
			}
		}
	}
	
	public static int[] coordtrans(int[] coord,int msize)
	{
		int[] rescoord = new int[2];
		rescoord[0]=coord[1];
		rescoord[1]=msize-1-coord[0];
		return rescoord;
	}
	
	public static void main(String args[])
	{
		int matrix[][] = init(4);
		matrix[0][0]=1;
		matrix[0][1]=2;
		matrix[0][2]=3;
		matrix[0][3]=4;
		matrix[1][0]=5;
		matrix[1][1]=6;
		matrix[1][2]=7;
		matrix[1][3]=8;
		matrix[2][0]=9;
		matrix[2][1]=10;
		matrix[2][2]=11;
		matrix[2][3]=12;
		matrix[3][0]=13;
		matrix[3][1]=14;
		matrix[3][2]=15;
		matrix[3][3]=16;
		printm(matrix);
		rotate(matrix);
		printm(matrix);
	}
}
