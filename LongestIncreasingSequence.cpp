#include <iostream>
#include <stdlib.h>
#include <string.h>

using namespace std;

//Dynamic programming with recurrence relation len[i] = max(len[j])+1 with j<i && j>0
//Time complexity: O(N^2)
void longestsequence(int* a, int* longest, int* parent,int len)
{
    int j;
    for(int i=0;i<len;i++)
    {
        j=i-1;
        if (j==-1) longest[i]=1;
        else
        {
            while(j>=0)
            {
                if ((a[j]<a[i]) && (longest[i]<=longest[j]+1))
                {
                    longest[i]=longest[j]+1;
                    parent[i]=j;
                }
                j--;
            }
        }
    }
}

int main(int argc, char** argv)
{
	if (argc<2)
    {
        cout << "Enter program name followed by the integer list as arguments" << endl;
        cout << "Example: Program 2 3 5 1 9 7 6 11" << endl;
        return 0;
    }
	int len = argc-1;
	int* a = new int[len];
	int* longest = new int[len];
	int* parent = new int[len];
	for(int i=0;i<len;i++) a[i] = atoi(argv[1+i]);
	memset(longest,0,sizeof(int)*len);
	memset(parent,-1,sizeof(int)*len);
	longestsequence(a,longest,parent,len);
	int maximum=0;
	int maxnumber=0;
	for(int i=0;i<len;i++) if (longest[i]>maximum) maximum=longest[i];
	for(int i=0;i<len;i++) if (longest[i]==maximum) maxnumber++;
	int* maxseqlasti = new int[maxnumber];
	int cc=0;
	for(int i=0;i<len;i++) if (longest[i]==maximum) maxseqlasti[cc++]=i;
	int curi;
	cout << "Found " << maxnumber << " sequences with maximum length " << maximum << " :"<< endl;
	for(int i=0;i<maxnumber;i++)
    {
        cc = 0;
        curi = maxseqlasti[i];
        while(cc<maximum)
        {
            cout << a[curi] << " ";
            curi = parent[curi];
            cc++;
        }
        cout << endl;
    }
    delete [] a;
    delete [] longest;
    delete [] parent;
    delete [] maxseqlasti;
	return 0;
}
