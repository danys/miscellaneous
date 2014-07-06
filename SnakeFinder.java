public class SnakeFinder 
{
	public static int nfinds(char board[][], String str, int k, int row, int col)
	{
		if (k<0) return 0;
		if ((row<0) || (col<0) || (row>board[0].length-1) || (col>board.length-1)) return 0;
		if (k==0)
		{
			int result = 0;
			for(int i=0;i<board[0].length;i++)
			{
				for(int j=0;j<board.length;j++)
				{
					if (board[i][j]==str.charAt(0))
					{
						if (k!=str.length()-1) result += nfinds(board,str,k+1,i,j+1)+nfinds(board,str,k+1,i,j-1)+nfinds(board,str,k+1,i+1,j)+nfinds(board,str,k+1,i-1,j);
						else result += 1;
					}
				}
			}
			return result;
		}
		else if (k==str.length()-1)
		{
			if (board[row][col]==str.charAt(k)) return 1;
			else return 0;
		}
		else
		{
			if (board[row][col]!=str.charAt(k)) return 0;
			else return nfinds(board,str,k+1,row,col+1)+nfinds(board,str,k+1,row,col-1)+nfinds(board,str,k+1,row+1,col)+nfinds(board,str,k+1,row-1,col);
		}
	}
	
	public static void main(String args[])
	{
		char board[][] = new char[5][5];
		String str = "SNAKE";
		board[0][0] = 'S';
		board[0][1] = 'E';
		board[0][2] = 'A';
		board[0][3] = 'Z';
		board[0][4] = 'I';
		board[1][0] = 'N';
		board[1][1] = 'S';
		board[1][2] = 'A';
		board[1][3] = 'N';
		board[1][4] = 'U';
		board[2][0] = 'A';
		board[2][1] = 'B';
		board[2][2] = 'U';
		board[2][3] = 'C';
		board[2][4] = 'F';
		board[3][0] = 'K';
		board[3][1] = 'Z';
		board[3][2] = 'E';
		board[3][3] = 'X';
		board[3][4] = 'S';
		board[4][0] = 'E';
		board[4][1] = 'E';
		board[4][2] = 'K';
		board[4][3] = 'A';
		board[4][4] = 'N';
		System.out.println("String "+str+" occurred "+nfinds(board,str,0,0,0)+" times!");
	}
}